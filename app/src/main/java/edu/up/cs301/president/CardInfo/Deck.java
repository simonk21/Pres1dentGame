package edu.up.cs301.president.CardInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

import edu.up.cs301.president.PlayerTracker;

public class Deck implements Serializable {

    private ArrayList<Card> deck;
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

    public void deal(ArrayList<PlayerTracker> players){
        for(int i = 0; i < players.size(); i++){
            for(int j = 0; j < 13; j++){
                Card temp = deck.remove(0);
                players.get(i).addCard(temp);
            }
        }
    }

}
