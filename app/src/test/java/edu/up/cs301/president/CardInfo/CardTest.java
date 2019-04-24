package edu.up.cs301.president.CardInfo;

import org.junit.Test;

import static org.junit.Assert.*;

public class CardTest {

    @Test
    public void setCardVal() {
        Card testCard = new Card(0, "test");
        testCard.setCardVal(1);
        assertEquals(testCard.getValue(), 1);
    }

    @Test
    public void setCardSuit() {
        Card testCard = new Card(0, "test");
        testCard.setCardSuit("SuitTest");
        assertEquals(testCard.getSuit(), "SuitTest");
    }

    @Test
    public void getValue() {
        Card testCard = new Card(0, "test");
        assertEquals(testCard.getValue(), 0);
    }

    @Test
    public void getSuit() {
        Card testCard = new Card(0, "test");
        assertEquals(testCard.getSuit(), "test");
    }

    @Test
    public void getFace() {
        Card testCard = new Card(2, "test");
        assertEquals(testCard.getFace(), "Four");
    }

    @Test
    public void getCardName() {
        Card testCard = new Card(1, "Hearts");
        assertEquals(testCard.getCardName(), "Three_Hearts");
    }
}