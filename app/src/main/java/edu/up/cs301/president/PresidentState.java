package edu.up.cs301.president;

import java.util.ArrayList;

import edu.up.cs301.game.infoMsg.GameState;
import edu.up.cs301.president.CardInfo.Card;
import edu.up.cs301.president.CardInfo.Deck;
// TODO check this entire class
/**
 * PresidentState
 * @author Hera Malik
 * @author Ben Pirkl
 * @author Kama Simon
 * @author Geryl Vinoya
 * @version April 2019
 */
public class PresidentState extends GameState {

    // to satisfy Serializable interface
    private static final long serialVersionUID = 7737393762469851826L;

    // 52 card deck
    private Deck deck;
    private ArrayList<Card> currentSet;
    private ArrayList<Card> tradeDeck;
    private int turn; // the current turn in game
    private int prev;

    private boolean roundStart;

    private ArrayList<PlayerTracker> players;

    public PresidentState() {
        deck = new Deck(); // initializes deck
        currentSet = new ArrayList<>(); // current set played
        tradeDeck = new ArrayList<>(); // cards to trade

        players = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            players.add(new PlayerTracker());
        }
        deck.deal(players); // deals cards (unsorted)

        turn = (int) (Math.random() * 4); // selects random player to start

        prev = -1;
        roundStart = false;
    }

    public PresidentState(PresidentState orig, int idx) {
        currentSet = new ArrayList<>();
        for(int i = 0; i < orig.currentSet.size(); i++){
            currentSet.add(new Card(orig.getCurrentSet().get(i)));
        }
        turn = orig.turn;
        prev = orig.prev;
        roundStart = orig.roundStart;
        players = new ArrayList<>();
        for(int i = 0; i < orig.players.size(); i++){
                players.add(new PlayerTracker(orig.players.get(i)));
            }
        }

    /** getters and setters */
    public void setTurn(int idx) {
        this.turn = idx;
    }
    public int getTurn() {
        return turn;
    }
    public ArrayList<Card> getCurrentSet() { return currentSet; }
    public int getPrev() { return prev; }
    public void setPrev() { prev = turn; }
    public void setCurrentSet( ArrayList<Card> in) {
        this.currentSet = in;
    }
    public ArrayList<PlayerTracker> getPlayers() { return players; }
    /** end of getters and setters */

    public int checkGame() {
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getScore() >= 11) {
                return i;
            }
        }
        return -1;
    }

    /**
     * gameWon
     *
     * Checks if player won game
     * Must have reached 11 points
     *
     * @return true (player won game) or false (game continues)
     */
    public boolean gameWon(PlayerTracker player) {
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i) == player) {
                if (player.getScore() >= 11) {
                    return true;
                }
            }
        }
        return false;
    } // TODO this doesn't make sense

    /**
     * playersWithCards
     *
     * @return number of players with cards
     */
    public int playersWithCards() {
        int count = 0;
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getHand().size() > 0) {
                count++;
            }
        }
        return count;
    }

    /**
     * setRoundStart
     *
     * Set the round start to be false
     */
    public void setRoundStart(boolean roundStart) {
        this.roundStart = roundStart;
        if(roundStart){
            deck = new Deck();
            deck.deal(players);
            if(trade()){
                for(int i = 0; i < players.size(); i++){
                    players.get(i).setRank(-1);
                }
            }

        }
    }

    /**
     * nextPlayer
     *
     * Updates turn
     */
    public void nextPlayer() {
        if (turn == players.size() - 1) {
            turn = 0;
        } else {
            turn++;
        }
    }


    /** trade functions */

    public void checkPresident(int turn) {
        if(players.get(turn).getRank() == -1){
            int count = 0;
            for(int i = 0; i < players.size(); i++){
                if(players.get(i).getRank() > players.get(turn).getRank()){
                    count++;
                }
            }
            if(count == 0){
                players.get(turn).setRank(3); // set to president
                players.get(turn).setScore(3);
                gameWon(players.get(turn));
            }
            else if(count == 1){
                players.get(turn).setRank(2); // set to vp
                players.get(turn).setScore(2);
                gameWon(players.get(turn));
            }
            else if(count == 2){
                players.get(turn).setRank(1); // set to vs
                players.get(turn).setScore(1);
            }
            else if(count == 3){
                players.get(turn).setRank(0); // set to scum
                players.get(turn).setScore(0);
            }
        }
    }

    /**
     * trade
     *
     * @return true (can trade) or false (cannot trade)
     */
    public boolean trade() {
        /**
         *  Check if the round is just starting and is not the first
         *  round of the game, if it isn't, initialize trade

         */
        if (roundStart) {
            for (int i = 0; i < players.size(); i++) {
                if (players.get(i).getRank() == 3) {
                    // Get the first smallest valued card in hand
                    Card firstMinCardInPresHand = getMinCard(players.get(i).getHand());
                    players.get(i).removeCard(firstMinCardInPresHand.getSuit(),firstMinCardInPresHand.getValue());
                    // Get the second smallest valued card in hand.
                    Card secondMinCardInPresHand = getMinCard(players.get(i).getHand());
                    players.get(i).removeCard(secondMinCardInPresHand.getSuit(), secondMinCardInPresHand.getValue());

                    for (int findScum = 0; findScum < players.size(); findScum++) {
                        if (players.get(findScum).getRank() == 0) {
                            Card firstMaxCardInScumHand = getMaxCard(players.get(findScum).getHand());
                            players.get(findScum).removeCard(firstMaxCardInScumHand.getSuit(), firstMaxCardInScumHand.getValue());

                            Card secondMaxCardInScumHand = getMaxCard(players.get(findScum).getHand());
                            players.get(findScum).removeCard(secondMaxCardInScumHand.getSuit(), secondMaxCardInScumHand.getValue());

                            players.get(findScum).getHand().add(firstMinCardInPresHand);
                            players.get(findScum).getHand().add(secondMinCardInPresHand);
                            players.get(i).getHand().add(firstMaxCardInScumHand);
                            players.get(i).getHand().add(secondMaxCardInScumHand);

                        }
                    }
                } else if (players.get(i).getRank() == 2) {

                    // Get the lowest valued card in hand
                    Card firstCardInVPHand = getMinCard(players.get(i).getHand());
                    players.get(i).removeCard(firstCardInVPHand.getSuit(), firstCardInVPHand.getValue());

                    Card firstCardInViceScumHand = null;
                    for (int findViceScum = 0; findViceScum < players.size(); findViceScum++) {
                        if (players.get(findViceScum).getRank() == 1) {
                            firstCardInViceScumHand = getMaxCard(players.get(findViceScum).getHand());
                            players.get(findViceScum).removeCard(firstCardInViceScumHand.getSuit(), firstCardInViceScumHand.getValue());

                            players.get(findViceScum).getHand().add(firstCardInVPHand);
                            players.get(i).getHand().add(firstCardInViceScumHand);
                        }
                    }

                    return true;
                }
            } return true; // Trade is a valid option.
        } else {
            /** If Round Start == False
             *  e.g. if the game is in play, trade is
             *  not available so return false
             */
            return false;
        }

    }

    /**
     * Method to find the maximum valued card in the players hand
     *
     * @param playerHand
     * @return max card in player hand
     */
    Card getMaxCard(ArrayList<Card> playerHand){
        int currentIndex = 0; // For Loop variable
        Card maxCard = new Card(-1, null);
        for(Card c : playerHand){
            if(maxCard.getValue() < playerHand.get(currentIndex).getValue()){
                maxCard.setCardVal(c.getValue());
                maxCard.setCardSuit(c.getSuit());
            }
            currentIndex++;
        }
        return maxCard;
    }

    /**
     * Method to find the maximum valued card in the players hand
     *
     * @param playerHand
     * @return
     */
    Card getMinCard(ArrayList<Card> playerHand){
        int currentIndex = 0; // For Loop variable
        Card minCard = new Card(55, null); // Arbitrary
        for(Card c : playerHand){
            if(minCard.getValue() > playerHand.get(currentIndex).getValue()){
                minCard.setCardVal(c.getValue());
                minCard.setCardSuit(c.getSuit());
            }
            currentIndex++;
        }
        return minCard;
    }

}
