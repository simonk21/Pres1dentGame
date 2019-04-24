package edu.up.cs301.president;

import org.junit.Test;

import java.util.ArrayList;

import edu.up.cs301.president.CardInfo.Card;

import static org.junit.Assert.assertEquals;

public class PlayerTrackerTest {

    @Test
    public void getScore() {
        // checking if score adds up correctly
        // if rank is invalid like '23', then it shouldn't change score
        PresidentState testState = new PresidentState();
        assertEquals(testState.getPlayers().get(0).getScore(), 0);
        testState.getPlayers().get(0).setScore(0);
        testState.getPlayers().get(1).setScore(1);
        testState.getPlayers().get(2).setScore(2);
        testState.getPlayers().get(3).setScore(3);
        assertEquals(testState.getPlayers().get(0).getScore(), 0);
        assertEquals(testState.getPlayers().get(1).getScore(), 0);
        assertEquals(testState.getPlayers().get(2).getScore(), 1);
        assertEquals(testState.getPlayers().get(3).getScore(), 2);
        testState.getPlayers().get(0).setScore(23);
        assertEquals(testState.getPlayers().get(0).getScore(), 0);
        testState.getPlayers().get(0).setScore(3);
        assertEquals(testState.getPlayers().get(0).getScore(), 2);
        testState.getPlayers().get(0).setScore(3);
        assertEquals(testState.getPlayers().get(0).getScore(), 4);
    }

    @Test
    public void setScore() {
        PresidentState testState = new PresidentState();
        testState.getPlayers().get(0).setScore(23);
        assertEquals(testState.getPlayers().get(0).getScore(), 0);
        testState.getPlayers().get(0).setScore(-23);
        assertEquals(testState.getPlayers().get(0).getScore(), 0);
        testState.getPlayers().get(0).setScore(10000000);
        assertEquals(testState.getPlayers().get(0).getScore(), 0);
    }

    @Test
    public void getStringRank() { // checking if string is correct
        PresidentState testState = new PresidentState();
        assertEquals(testState.getPlayers().get(0).getStringRank(), "none");
        testState.getPlayers().get(0).setRank(3);
        assertEquals(testState.getPlayers().get(0).getStringRank(), "President");
    }

    @Test
    public void getRank() {
        PresidentState testState = new PresidentState();
        testState.getPlayers().get(0).setRank(2);
        assertEquals(testState.getPlayers().get(0).getRank(), 2);
        testState.getPlayers().get(0).setRank(10);
        assertEquals(testState.getPlayers().get(0).getRank(), -1);
    }

    @Test
    public void setRank() {
        PresidentState testState = new PresidentState();
        testState.getPlayers().get(0).setRank(2);
        assertEquals(testState.getPlayers().get(0).getRank(), 2);
        testState.getPlayers().get(0).setRank(10);
        assertEquals(testState.getPlayers().get(0).getRank(), -1);
    }

    @Test
    public void setLeaveGame() {
        PresidentState testState = new PresidentState();
        testState.getPlayers().get(0).setLeaveGame(1);
        assertEquals(testState.getPlayers().get(0).getLeaveGame(), 1);
    }

    @Test
    public void getLeaveGame() {
    }

    @Test
    public void getHand() {
        ArrayList<Card> hand = new ArrayList<>();
        hand.add(new Card(1, "Diamonds"));
        hand.add(new Card(2, "Diamonds"));
        hand.add(new Card(3, "Diamonds"));
        PresidentState testState = new PresidentState();
        testState.getPlayers().get(0).setHand(hand);
        assert(testState.getPlayers().get(0).getHand().size() == 3);
        assert(testState.getPlayers().get(0).getHand().get(0).getValue() == 1);
        assert(testState.getPlayers().get(0).getHand().get(0).getSuit().equals("Diamonds"));
    }

    @Test
    public void addCard() {
        PresidentState testState = new PresidentState();
        testState.getPlayers().get(0).addCard(new Card(1, "Diamonds"));
        assert(testState.getPlayers().get(0).getHand().size() == 14);
    }

    @Test
    public void setHand() {
        /* shows the implementation and testing in getHand method */
    }

    @Test
    public void removeCard() {
        /** taken from addCard testing */
        PresidentState testState = new PresidentState();
        testState.getPlayers().get(0).addCard(new Card(1, "Diamonds"));
        assert(testState.getPlayers().get(0).getHand().size() == 14);
        // checking where the card we added is
        assert(testState.getPlayers().get(0).getHand().get(13).getValue() == 1);
        assert(testState.getPlayers().get(0).getHand().get(13).getSuit().equals("Diamonds"));
        testState.getPlayers().get(0).removeCard("Diamons", 1); // mispelled removal
        assert(testState.getPlayers().get(0).getHand().size() == 14);
        testState.getPlayers().get(0).removeCard("Diamonds", 1);
        assert(testState.getPlayers().get(0).getHand().size() == 13);

    }
}