package edu.up.cs301.president.CardInfo;

import java.io.Serializable;

/**
 * Card class
 * @author Hera Malik
 * @author Ben Pirkl
 * @author Kama Simon
 * @author Geryl Vinoya
 * @version April 2019
 * implements Serializable for Network Play
 * Holds the card value and card suit of a card
 */
public class Card implements Serializable {

    /** Descriptive variable for the card */
    /* 1-8: numbers 9: Jack, 10: Queen, 11: King, 12: A, 13: 2 */
    private int cardVal;

    /** Card suit */
    /* Types: Spades, Hearts, Diamonds, Clubs */
    private String cardSuit;

    /**
     * Card constructor
     * initializes values of card depending on parameters
     * @param cardVal descriptive integer value of card
     * @param cardSuit card's suit
     */
    public Card(int cardVal, String cardSuit) {
        this.cardVal = cardVal;
        this.cardSuit = cardSuit;
    }

    /**
     * Card copy constructor
     * @param orig original Card to copy
     */
    public Card(Card orig) {
        cardSuit = orig.cardSuit;
        cardVal = orig.cardVal;
    }

    /** getters and setters for cards */
    public void setCardVal(int cardVal) { this.cardVal = cardVal; }

    public void setCardSuit(String cardSuit) { this.cardSuit = cardSuit; }

    public int getValue() { return cardVal; }

    public String getSuit() { return cardSuit; }

    /**
     * getFace
     * returns the name of the card
     * based on its set value
     * @return String of Card Value
     */
    public String getFace() {
        int nameOfCard = this.cardVal;
        if(nameOfCard == 1){
            return "Three";
        }
        else if(nameOfCard == 2) {
            return "Four";
        }
        else if(nameOfCard == 3) {
            return "Five";
        }
        else if(nameOfCard == 4) {
            return "Six";
        }
        else if(nameOfCard == 5) {
            return "Seven";
        }
        else if(nameOfCard == 6) {
            return "Eight";
        }
        else if(nameOfCard == 7) {
            return "Nine";
        }
        else if(nameOfCard == 8) {
            return "Ten";
        }
        else if(nameOfCard == 9) {
            return "Jack";
        }
        else if(nameOfCard == 10) {
            return "Queen";
        }
        else if(nameOfCard == 11) {
            return "King";
        }
        else if(nameOfCard == 12) {
            return "Ace";
        }
        else if(nameOfCard == 13) {
            return "Two";
        }
        return null;
    }

    /** Returns card name and suit */
    public String getCardName() { return this.getFace() + "_" + this.getSuit(); }

} //Card class
