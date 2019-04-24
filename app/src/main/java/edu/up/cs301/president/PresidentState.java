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
    private int turn; // the current turn in game
    private int prev;

    private boolean roundStart;

    private ArrayList<PlayerTracker> players;

    public PresidentState() {
        deck = new Deck(); // initializes deck
        currentSet = new ArrayList<>(); // current set played

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
    public boolean getRoundStart() { return roundStart; }
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
            for(int i = 0; i < players.size(); i++){
                if(players.get(i).getRank() == 0){
                    turn = i;
                }
            }
        }
        else{
            for(int i = 0; i < players.size(); i++){
                if(players.get(i).getRank() == 0){
                    turn = i;
                }
                players.get(i).setRank(-1);
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
            }
            else if(count == 1){
                players.get(turn).setRank(2); // set to vp
                players.get(turn).setScore(2);
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

}
