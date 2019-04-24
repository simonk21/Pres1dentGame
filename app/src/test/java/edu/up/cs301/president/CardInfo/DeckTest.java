package edu.up.cs301.president.CardInfo;

import org.junit.Test;

import java.util.ArrayList;

import edu.up.cs301.president.PlayerTracker;

import static org.junit.Assert.assertEquals;

public class DeckTest {

    @Test
    public void deal() {
        Deck test = new Deck();
        ArrayList<PlayerTracker> p = new ArrayList<>();
        for(int i = 0; i < 4; i++){
            p.add(new PlayerTracker());
        }
        assertEquals(p.get(0).getHand().size(), 0);
        test.deal(p);
        assertEquals(p.get(0).getHand().size(), 13);
        int count = 0;
        ArrayList<Card> hand1 = p.get(1).getHand();
        for(int i = 0; i < hand1.size() - 1; i++){
            for(int j = 1; j < hand1.size(); j++){
                if(hand1.get(i).getValue() == hand1.get(j).getValue() &&
                    hand1.get(i).getSuit().equals(hand1.get(j).getSuit()) &&
                    i != j){
                    count++;
                }
            }
        }
        assert(count == 0);  // checking if all cards are unique for p1
        ArrayList<Card> hand2 = p.get(2).getHand();
        for(int i = 0; i < hand2.size() - 1; i++){
            for(int j = 1; j < hand2.size(); j++){
                if(hand2.get(i).getValue() == hand2.get(j).getValue() &&
                        hand2.get(i).getSuit().equals(hand2.get(j).getSuit()) &&
                        i != j){
                    count++;
                }
            }
        }
        assert(count == 0);  // checking if all cards are unique for p2
        ArrayList<Card> hand3 = p.get(3).getHand();
        for(int i = 0; i < hand3.size() - 1; i++){
            for(int j = 1; j < hand3.size(); j++){
                if(hand3.get(i).getValue() == hand3.get(j).getValue() &&
                        hand3.get(i).getSuit().equals(hand3.get(j).getSuit()) &&
                        i != j){
                    count++;
                }
            }
        }
        assert(count == 0);  // checking if all cards are unique for p3
    }
}