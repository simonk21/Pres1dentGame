package edu.up.cs301.president;

import java.io.Serializable;
import java.util.ArrayList;

import edu.up.cs301.president.CardInfo.Card;

/**
 * PlayerTracker class
 * @author Hera Malik
 * @author Ben Pirkl
 * @author Kama Simon
 * @author Geryl Vinoya
 * @version April 2019
 * keeps track of the player's information needed
 */
public class PlayerTracker implements Serializable {

    /* instance variables */
    private int score; // player's score
    private int leave; // 0 if didn't leave game, 1 if did

    private ArrayList<Card> playerHand; // player's hand array list
    private static int MAX_CARDS = 13; // max cards in hand
    /*
       rank:
       -1 : no rank
       0 : scum
       1 : vice scum
       2 : vice pres
       3 : pres
     */
    private int rank;

    /**
     * PlayerTracker constructor
     * sets defaults for player
     */
    public PlayerTracker(){
        playerHand = new ArrayList<>(MAX_CARDS);
        score = 0;
        rank = -1;
        leave = 0;
    }

    /**
     * PlayerTracker copy constructor
     * @param orig original PlayerTracker
     */
    public PlayerTracker(PlayerTracker orig){
        score = orig.score;
        rank = orig.rank;
        leave = orig.leave;
        playerHand = new ArrayList<>();
        for(int i = 0; i < orig.getHand().size(); i++){
            playerHand.add(new Card(orig.getHand().get(i)));
        }
    }

    /** setters and getters */
    public int getScore() { return score; }

    public void setScore(int rank) {
        switch (rank){
            case 0:
                break;
            case 1:
                break;
            case 2:
                this.score++; // if vice pres add one point
                break;
            case 3:
                this.score += 2; // if pres add two pts
        }
    }

    public String getStringRank(){
        String stringRank = "none";
        switch(rank){
            case 0:
                stringRank = "Scum";
                break;
            case 1:
                stringRank = "Vice Scum";
                break;
            case 2:
                stringRank = "Vice President";
                break;
            case 3:
                stringRank = "President";
                break;
        }
        return stringRank;
    }

    public int getRank() { return rank; }

    public void setRank(int rank) { this.rank = rank; }

    public void setLeaveGame(int leave) { this.leave = leave; }

    public int getLeaveGame() { return leave; }

    public ArrayList<Card> getHand() { return playerHand; }

    /**
     * addCard
     * adds card to player's hand
     * @param in card to insert into hand
     */
    public void addCard(Card in) { playerHand.add(in); }

    /**
     * setHand
     * sets the hand of player's hand
     * @param hand array list of cards to set as player's hand
     */
    public void setHand(ArrayList<Card> hand) { playerHand = hand; }

    /**
     * removeCard
     * Removes card after set is played
     * @param suit card to be removed
     * @return true (able to remove) or false (not able to remove)
     */
    public void removeCard(String suit, int val) {
        for(int i = 0; i < playerHand.size(); i++){
            if(playerHand.get(i).getValue() == val && playerHand.get(i).getSuit().equals(suit)){
                playerHand.remove(i);
            }
        }
    }
}
