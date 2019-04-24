package edu.up.cs301.president;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;

import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.LocalGame;
import edu.up.cs301.game.actionMsg.GameAction;
import edu.up.cs301.president.CardInfo.Card;

/**
 * PresidentLocalGame class
 * @author Hera Malik
 * @author Ben Pirkl
 * @author Kama Simon
 * @author Geryl Vinoya
 * @version April 2019
 * decides if the player's move is valid or invalid
 */
public class PresidentLocalGame extends LocalGame implements Serializable {

    /* instance variables */
    private static final long serialVersionUID = 2537393762469851826L;
    private PresidentState state;
    private ArrayList<Card> pTrade, vpTrade, vsTrade, sTrade;
    public PresidentLocalGame() {
        Log.i("SJLocalGame", "creating game");
        // create the state for the beginning of the game
        state = new PresidentState();
        pTrade = new ArrayList<>();
        vpTrade = new ArrayList<>();
        vsTrade = new ArrayList<>();
        sTrade = new ArrayList<>();
    }

    @Override
    protected void sendUpdatedStateTo(GamePlayer p) {
        if(p == null){
            Log.i("PLocalGame.java","GamePlayer is null");
            return;
        }
        if(state == null){
            Log.i("PLocalGame.java", "Gamestate is null");
        }

        PresidentState playerState; // Game state
        for (int i = 0; i < this.players.length; i++) {
            if (this.players[i].equals(p)) {
                playerState = new PresidentState(state,i);
                p.sendInfo(playerState); // gets update from PresidentState
                break;
            }
        }
    }

    @Override
    protected boolean canMove(int playerIdx) {
        int whoseTurn = state.getTurn();
        return playerIdx == whoseTurn;
    }

    @Override
    protected String checkIfGameOver() {
        int score = state.checkGame();
        if(score == -1) {
            return null;
        }
        else{
            return this.playerNames[score] + "is a winner!";
        }
    }

    @Override
    protected boolean makeMove(GameAction action) {
        if( action == null ){
            Log.i("PresidentLocalGame.java", "action is null");
            return false;
        }
        int playerIdx = getPlayerIdx(action.getPlayer());
        if( action instanceof PresidentPassAction && !state.getRoundStart()) {
            return pass(playerIdx); // if pass action, then go to pass method
        }
        if ( action instanceof PresidentPlayAction && !state.getRoundStart()) {
            ArrayList<Card> temp = ((PresidentPlayAction) action).getCards(); // grabs cards from PresidentPlayAction class
            return play(playerIdx, temp); // if play action, then go to play method
        }
        if ( action instanceof PresidentOrderAction){
            return order(playerIdx); // if order method, then go to order method
        }
        if ( action instanceof PresidentTradeAction && state.getRoundStart()){
            ArrayList<Card> temp = ((PresidentTradeAction) action).getCardsToTrade();
            return trade(playerIdx, temp);
        }
        return false;
    }

    /**
     * play
     * play action will play cards that player wants to play
     * unless cards are not of higher value of current set
     * @param idx player's index/number
     * @param temp cards that player wants to play
     * @return true (if able to place) or false (if not able to)
     */
    public boolean play(int idx, ArrayList<Card> temp) { // TODO need to check this method and comment better
        if(temp == null){ // base case: check if temp is null
            return false;
        }
        int checkHandContainsCard = 0;
        // checks to see how much of the cards in temp matches the cards in player's hand
        for(int i = 0; i < state.getPlayers().get(idx).getHand().size(); i++){
            for(int j = 0; j < temp.size(); j++){
                if(temp.get(j).getValue() == state.getPlayers().get(idx).getHand().get(i).getValue() &&
                    temp.get(j).getSuit().equals(state.getPlayers().get(idx).getHand().get(i).getSuit())){
                    checkHandContainsCard++;
                }
            }
        }
        // base case: checks if cards wanting to play is not correct
        if(checkHandContainsCard != temp.size()){
            return false; // if there's at least one card that doesn't match player's hand then return
        } // this helps when human player clicks on brown parts of GUI and tries to play it


        int setValid = checkSetValid(temp);
        if (setValid == -1){ // base case: temp doesn't contain all same card values
            return false; // set is not all the same cards or has two
        }

        if(temp.size() != state.getCurrentSet().size() && state.getCurrentSet().size() != 0){
            return false; // base case: temp size doesn't equal current set size
        } // temp must be same size as current set or current set must be 0

        if(state.getCurrentSet().size() == 0){ // if current set = 0, then we can restart set
            state.getCurrentSet().clear(); // clears set
            for(int i = 0; i < temp.size(); i++) {
                state.getPlayers().get(idx).removeCard(temp.get(i).getSuit(), temp.get(i).getValue());
            } // remove cards from player's hand
            state.setCurrentSet(temp); // then set current set to temp
            state.setPrev(); // sets this player's idx to prev (to keep track of who last played)
            if(!checkNoCards()){ // checks if player contains no cards
                state.nextPlayer(); // if not, then we want to go to next player
            }
            while(state.getPlayers().get(state.getTurn()).getHand().size() < 1 || state.getPlayers().get(state.getTurn()).getLeaveGame() == 1){
                state.nextPlayer();
            }
            return true;
        }
        else{ // current set is not empty
            int currentVal = -1; // set currentVal to something
            int playerVal = -1; // set playerVal to something
            int count = 0; // counter
            for(int i = 0; i < state.getCurrentSet().size(); i++){
                if(state.getCurrentSet().get(i).getValue() != 13){
                    currentVal = state.getCurrentSet().get(i).getValue();
                    break; // found a card that isn't a two
                }
                count++;
            }
            if(count == state.getCurrentSet().size()){
                return false; // all cards in set are two's (unbeatable)
            }
            count = 0;
            for(int i = 0; i < temp.size(); i++){
                if(temp.get(i).getValue() != 13){
                    playerVal = temp.get(i).getValue();
                    break;
                } // found a card in temp that isn't a two
                count++;
            }
            if(count == temp.size() || playerVal > currentVal){
                state.getCurrentSet().clear();
                state.setCurrentSet(temp); // player's set is higher or all cards are two's
                for(int i = 0; i < temp.size(); i++){ // removes all cards from player's hand
                    state.getPlayers().get(idx).removeCard(temp.get(i).getSuit(), temp.get(i).getValue());
                }
                state.setPrev(); // sets the player who last played
                if(!checkNoCards()){
                    state.nextPlayer();
                }
                if(count == temp.size()){
                    state.getCurrentSet().clear();
                    state.setTurn(state.getPrev()); // if player plays all two's then it is automatically this persons turn
                }
                while(state.getPlayers().get(state.getTurn()).getHand().size() < 1 || state.getPlayers().get(state.getTurn()).getLeaveGame() == 1){
                    state.nextPlayer();
                }
                return true;
            }
            else{
                return false; // player val is less than currentVal
            }
        }
    }

    /**
     * pass
     * pass action will pass player's turn
     * @return true (player can pass turn) or false (player cannot pass turn)
     */
    private boolean pass(int turn){ // TODO need to check this method and add better comments
        if(state.getTurn() != turn){
            return false; // not player's turn
        }

        state.nextPlayer();


        if(state.getPlayers().get(state.getTurn()).getLeaveGame() == 1){
            state.nextPlayer();
        }
        while(state.getPlayers().get(state.getTurn()).getHand().size() < 1){
            checkNoCards();
        }
        if(state.getPrev() == state.getTurn()) {
            state.getCurrentSet().clear(); // next turn can restart
        }
        return true;
    }

    /**
     * order
     * order action will order hand from least to greatest value
     * @param idx the player's index/number
     * @return true (all the time since you should always be able to order)
     */
    private boolean order(int idx){
        for (int i = 0; i < state.getPlayers().get(idx).getHand().size(); i++) {
            for (int j = i + 1; j < state.getPlayers().get(idx).getHand().size(); j++)
            {
                if (state.getPlayers().get(idx).getHand().get(i).getValue() >
                    state.getPlayers().get(idx).getHand().get(j).getValue())
                {
                    Card temp = new Card(-1,  "Default");
                    temp.setCardVal(state.getPlayers().get(idx).getHand().get(i).getValue());
                    temp.setCardSuit(state.getPlayers().get(idx).getHand().get(i).getSuit());
                    state.getPlayers().get(idx).getHand().get(i).setCardSuit(state.getPlayers().get(idx).getHand().get(j).getSuit());
                    state.getPlayers().get(idx).getHand().get(i).setCardVal(state.getPlayers().get(idx).getHand().get(j).getValue());
                    state.getPlayers().get(idx).getHand().get(j).setCardVal(temp.getValue());
                    state.getPlayers().get(idx).getHand().get(j).setCardSuit(temp.getSuit());
                }
            }
        }
        return true;
    }
    /**
     External Citation
     Date: 12 April 2019
     Problem: Adding ordering of cards

     Resource:
     https://www.sanfoundry.com/java-program-sort-array-ascending-order/
     Solution: Used code from article
     */

    /**
     * trade
     * @param idx player's index
     * @param toTrade the cards the player wants to trade
     * @return true (able to trade), false (unable to trade)
     */
    private boolean trade(int idx, ArrayList<Card> toTrade){
        if(!state.getRoundStart()){
            return false;
        }
        else{
            switch (state.getPlayers().get(state.getTurn()).getRank()){
                case -1:
                    return false;
                case 0:
                    state.nextPlayer();
                    break;
                case 1:
                    state.nextPlayer();
                    break;
                case 2:
                    if(toTrade.size() != 1){
                        return false;
                    }
                    else{
                        removeTrade(toTrade.get(0), idx);
                        vpTrade = toTrade;
                        state.nextPlayer();
                    }
                    break;
                case 3:
                    if(toTrade.size() != 2){
                        return false;
                    }
                    else{
                        removeTrade(toTrade.get(0), idx);
                        removeTrade(toTrade.get(1), idx);
                        pTrade = toTrade;
                        state.nextPlayer();
                    }
                    break;
            }
            int count = 0;
            if(vpTrade.size() == 1 && pTrade.size() == 2){
                count++;
            }
            if(count == 1){
                trade();
                state.setRoundStart(false);
                return true;
            }
        }
        return true;
    }

    public boolean trade(){
        for(int i = 0; i < state.getPlayers().size(); i++){
            if(state.getPlayers().get(i).getRank() == 0){
                Card max1 = getMax(i);
                removeTrade(max1, i);
                Card max2 = getMax(i);
                removeTrade(max2, i);
                sTrade.add(max1);
                sTrade.add(max2);
            }
            else if(state.getPlayers().get(i).getRank() == 1){
                Card max1 = getMax(i);
                removeTrade(max1, i);
                vsTrade.add(max1);
            }
        }
        int count = 0;
        while(count != 4){
            switch(state.getPlayers().get(state.getTurn()).getRank()){
                case 0:
                    state.getPlayers().get(state.getTurn()).getHand().add(pTrade.get(0));
                    state.getPlayers().get(state.getTurn()).getHand().add(pTrade.get(1));
                    pTrade.clear();
                    break;
                case 1:
                    state.getPlayers().get(state.getTurn()).getHand().add(vpTrade.get(0));
                    vpTrade.clear();
                    break;
                case 2:
                    state.getPlayers().get(state.getTurn()).getHand().add(vsTrade.get(0));
                    vsTrade.clear();
                    break;
                case 3:
                    state.getPlayers().get(state.getTurn()).getHand().add(sTrade.get(0));
                    state.getPlayers().get(state.getTurn()).getHand().add(sTrade.get(1));
                    sTrade.clear();
                    break;
            }
            state.nextPlayer();
            count++;
        }
        return true;
    }

    public Card getMax(int idx){
        Card max = new Card(-1, "Default");
        ArrayList<Card> hand = state.getPlayers().get(idx).getHand();
        for(int i = 0; i < hand.size(); i++){
            if(max.getValue() < hand.get(i).getValue()){
                max.setCardVal(hand.get(i).getValue());
                max.setCardSuit(hand.get(i).getSuit());
            }
        }
        return max;
    }

    public void removeTrade(Card removeCard_1, int idx){
        if(removeCard_1 != null){
            for(int i = 0; i < state.getPlayers().get(idx).getHand().size(); i++){
                if(state.getPlayers().get(idx).getHand().get(i).getValue() == removeCard_1.getValue()
                && state.getPlayers().get(idx).getHand().get(i).getSuit().equals(removeCard_1.getSuit())){
                    state.getPlayers().get(idx).removeCard(removeCard_1.getSuit(), removeCard_1.getValue());
                }
            }
        }
    }
    /**
     * checkNoCards
     * after a pass or play it checks if anyone got rid of their cards and if they can be ranked
     * also checks if a new round can start
     * @return true is player can
     */
    private boolean checkNoCards(){ // TODO need to check this method and add comments
        if(state.getPlayers().get(state.getTurn()).getHand().size() < 1){
            state.checkPresident(state.getTurn());
            if(state.getPrev() == state.getTurn()){
                state.getCurrentSet().clear();
            }
            if(state.getPlayers().get(state.getTurn()).getRank() == 1){
                for(int i = 0; i < state.getPlayers().size(); i++){
                    if(state.getPlayers().get(i).getHand().size() > 0){
                        state.getPlayers().get(i).getHand().clear();
                        state.checkPresident(i);
                    }
                }
                state.setRoundStart(true);
                return true;
            }
            state.nextPlayer();
            return true;
        }
        return false;
    }

    /**
     * checkSetValid
     * check if set is valid
     * @param temp card set of player
     * @return 0 if
     */
    private int checkSetValid(ArrayList<Card> temp){ // TODO check this method and comment better
        Card c = new Card(-1, "Default");
        int count = 0;
        for(int i = 0; i < temp.size(); i++){
            if(temp.get(i).getValue() != 13){ // since two is a wild
                c.setCardSuit(temp.get(i).getSuit());
                c.setCardVal(temp.get(i).getValue());
                break;
            }
            count++;
        }
        if(c.getValue() == -1 && count == temp.size()){
            return 0; // all cards are twos
        }
        for(int i = 0; i < temp.size(); i++){
            if(c.getValue() != temp.get(i).getValue()){ // if value doesn't match this cards value then return
                if(temp.get(i).getValue() == 13) {
                    return 0;
                }
                else{
                    return -1;
                }
            }
        }
        return 0;
    }


}
