package edu.up.cs301.president;

import android.util.Log;

import java.util.ArrayList;

import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.LocalGame;
import edu.up.cs301.game.actionMsg.GameAction;
import edu.up.cs301.president.CardInfo.Card;

public class PresidentLocalGame extends LocalGame {

    private PresidentState state;

    public PresidentLocalGame() {
        Log.i("SJLocalGame", "creating game");
        // create the state for the beginning of the game
        state = new PresidentState();
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

        PresidentState playerState;
        for (int i = 0; i < this.players.length; i++) {
            if (this.players[i].equals(p)) {
                playerState = new PresidentState(state,i);
                p.sendInfo(playerState);
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
        return null; // TODO: need to print message for winner
    } // TODO need to change the checkIfGameOver

    @Override
    protected boolean makeMove(GameAction action) {
        if( action == null ){
            Log.i("PresidentLocalGame.java", "action is null");
            return false;
        }
        int playerIdx = getPlayerIdx(action.getPlayer());
        if( action instanceof PresidentPassAction ) {
            return pass(playerIdx);
        }
        if ( action instanceof PresidentPlayAction ) {
            ArrayList<Card> temp = ((PresidentPlayAction) action).getCards(); // grabs cards from PresidentPlayAction class
            return play(playerIdx, temp);
        }
        if ( action instanceof PresidentOrderAction ){
            return order(playerIdx);
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
    public boolean play(int idx, ArrayList<Card> temp) {
        if(temp == null){
            return false;
        }
        int checkHandContainsCard = 0;
        for(int i = 0; i < state.getPlayers().get(idx).getHand().size(); i++){
            for(int j = 0; j < temp.size(); j++){
                if(temp.get(j).getValue() == state.getPlayers().get(idx).getHand().get(i).getValue() &&
                    temp.get(j).getSuit().equals(state.getPlayers().get(idx).getHand().get(i).getSuit())){
                    checkHandContainsCard++;
                }
            }
        }
        if(checkHandContainsCard != temp.size()){
            return false;
        }
        int setValid = checkSetValid(temp);
        if (setValid == -1){
            return false; // set is not all the same cards or has two
        }
        if(temp.size() != state.getCurrentSet().size() && state.getCurrentSet().size() != 0){
            return false;
        } // temp must be same size as current set or current set must be 0
        if(state.getCurrentSet().size() == 0){
            state.getCurrentSet().clear();
            for(int i = 0; i < temp.size(); i++) {
                state.getPlayers().get(idx).removeCard(temp.get(i).getSuit(), temp.get(i).getValue());
            }
            state.setCurrentSet(temp); // then set current set to temp
            state.setPrev(); // sets player who last played
            if(!checkNoCards()){
                state.nextPlayer();
            }
            return true;
        }
        else{
            int currentVal = -1; // set currentVal to something
            int playerVal = -1; // set playerVal to something
            int count = 0;
            for(int i = 0; i < state.getCurrentSet().size(); i++){
                if(state.getCurrentSet().get(i).getValue() != 2){
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
                if(temp.get(i).getValue() != 2){
                    playerVal = temp.get(i).getValue();
                    break;
                }
                count++;
            }
            if(count == temp.size() || playerVal > currentVal){
                state.getCurrentSet().clear();
                state.setCurrentSet(temp); // player's set is all two's or players set is higher
                for(int i = 0; i < temp.size(); i++){ // removes all cards from player's hand
                    state.getPlayers().get(idx).removeCard(temp.get(i).getSuit(), temp.get(i).getValue());
                }
                state.getPlayers().get(idx).resetPass(); // TODO might be able to remove this method
                state.setPrev(); // sets the player who last played
                if(!checkNoCards()){
                    state.nextPlayer();
                }
                return true;
            }
            else{
                return false;
            }
        }
    }

    /**
     * pass
     *
     * @return true (player can pass turn) or false (player cannot pass turn)
     */
    private boolean pass(int turn){
        if(state.getTurn() != turn){
            return false; // not player's turn
        }
        state.getPlayers().get(turn).setPass();
        state.nextPlayer();
        if(state.getPrev() == state.getTurn()) {
            state.getCurrentSet().clear();
            for(int i =  0; i < state.getPlayers().size();i++){
                state.getPlayers().get(i).resetPass();
            }
        }
        checkNoCards();
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
     * https://www.sanfoundry.com/java-program-sort-array-ascending-order/ //TODO need to add citation
     */

    /**
     * checkNoCards
     * after a pass or play it checks if anyone got rid of their cards and if they can be ranked
     * also checks if a new round can start
     * @return
     */
    private boolean checkNoCards(){
        int count = 0;
        if(state.getPlayers().get(state.getTurn()).getSetFinish() == 1){
            state.nextPlayer();
            return true;
        }
        else{
            while (state.getPlayers().get(state.getTurn()).getHand().size() < 1) {
                count++;
                state.checkPresident(state.getTurn());
                if (count == 4) {
                    state.setRoundStart(true);
                    return true;
                }
                state.nextPlayer();
            }
            return false;
        }
    } // TODO need to change this !!!!! started it but logic doesn't make sense yet

    /**
     *
     * @param temp
     * @return
     */
    private int checkSetValid(ArrayList<Card> temp){
        Card c = new Card(-1, "Default");
        int count = 0;
        for(int i = 0; i < temp.size(); i++){
            if(temp.get(i).getValue() != 2){ // since two is a wild
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
