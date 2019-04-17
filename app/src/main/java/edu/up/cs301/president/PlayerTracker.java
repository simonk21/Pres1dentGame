package edu.up.cs301.president;

import java.io.Serializable;
import java.util.ArrayList;

import edu.up.cs301.president.CardInfo.Card;

public class PlayerTracker implements Serializable {

    private int score; // player's score
    private int pass; // 0 if didn't pass, 1 if passed

    private ArrayList<Card> playerHand;
    private static int MAX_CARDS = 13;
    /*
       rank:
       -1 : no rank
       0 : scum
       1 : vice scum
       2 : vice pres
       3 : pres
     */
    private int rank;

    public PlayerTracker(){
        playerHand = new ArrayList<>(MAX_CARDS);
        score = 0;
        pass = 0;
        rank = -1;
    }

    public PlayerTracker(PlayerTracker orig){
        score = orig.score;
        pass = orig.pass;
        rank = orig.rank;
        playerHand = new ArrayList<>();
        for(int i = 0; i < orig.getHand().size(); i++){
            playerHand.add(new Card(orig.getHand().get(i)));
        }
    }

    /* player's score */
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

    /* player's rank */
    public int getRank() { return rank; }
    public void setRank(int rank) { this.rank = rank; }

    public ArrayList<Card> getHand() { return playerHand; }
    public void addCard(Card in) { playerHand.add(in); }

    public void setHand(ArrayList<Card> hand) { playerHand = hand; }

    /* player's pass */
    public int getPass() { return pass; }
    public void setPass() { pass = 1; }
    public void resetPass() { pass = 0; }

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
