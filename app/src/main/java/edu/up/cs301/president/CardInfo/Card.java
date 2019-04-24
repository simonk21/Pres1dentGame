package edu.up.cs301.president.CardInfo;

import java.io.Serializable;

public class Card implements Serializable {

    /** Descriptive variable for the card */
    /* 0-7: numbers 8: Jack, 9: Queen, 10: King, 11: A, 12: 2 */
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
     * initializes values of card depending on parameters
     * @param orig the original card to copy
     */
    public Card(Card orig) {
        cardSuit = orig.cardSuit;
        cardVal = orig.cardVal;
    }

    /** Setter / Getter for Card value specifically for
     *  the trade function
     */
    public void setCardVal(int cardVal) {
        this.cardVal = cardVal;
    }

    public void setCardSuit(String cardSuit) {
        this.cardSuit = cardSuit;
    }

    /** Returns the card's value */
    public int getValue() { return cardVal; }

    /** Returns the card's suit */
    public String getSuit() { return cardSuit; }




    /**
     * getFace
     * returns the string of card value
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

}
