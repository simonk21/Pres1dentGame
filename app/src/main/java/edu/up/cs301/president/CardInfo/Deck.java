package edu.up.cs301.president.CardInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

import edu.up.cs301.president.PlayerTracker;

/**
 * Deck class
 * @author Hera Malik
 * @author Ben Pirkl
 * @author Kama Simon
 * @author Geryl Vinoya
 * @version April 2019
 * Implements Serializable for Network Play
 * Creates a deck that holds all the cards
 */
public class Deck implements Serializable {

    /* instance variable */
    private ArrayList<Card> deck;

    /**
     * Deck Constructor
     * creates the cards to be in the deck
     * shuffles the deck
     */
    public Deck(){
        deck = new ArrayList<>();
        String suit = "Default";
        for(int i = 0; i < 4; i++){
            if(i == 0){
                suit = "Hearts";
            }
            else if(i == 1){
                suit = "Clubs";
            }
            else if(i == 2){
                suit = "Spades";
            }
            else if(i == 3){
                suit = "Diamonds";
            }
            for(int j = 1; j < 14; j++){
                Card card = new Card(j, suit);
                deck.add(card);
            }
        }
        Collections.shuffle(deck);
    }

    /**
     * deal
     * deals the deck to all players
     * @param players Arraylist of players in game
     */
    public void deal(ArrayList<PlayerTracker> players){
        for(int i = 0; i < players.size(); i++){
            for(int j = 0; j < 13; j++){
                Card temp = deck.remove(0); // removes card from deck
                players.get(i).addCard(temp); // adds card to players hand
            }
        }
    }

} // Deck class
