package edu.up.cs301.president;

import org.junit.Test;

public class PresidentStateTest {

    @Test
    public void setTurn() {
        PresidentState test = new PresidentState();
        test.setTurn(0);
        assert(test.getTurn() == 0);
        test.setTurn(-1); // this would never happen though because user will never setTurn
        assert(test.getTurn() == -1);
    }

    @Test
    public void getTurn() {
        PresidentState test = new PresidentState();
        test.setTurn(0);
        assert(test.getTurn() == 0);
    }

    @Test
    public void getCurrentSet() {
        PresidentState test = new PresidentState();
        assert(test.getCurrentSet().size() == 0); // initially current set should be zero
    }

    @Test
    public void getPrev() {
    }

    @Test
    public void setPrev() {
    }

    @Test
    public void setCurrentSet() {
    }

    @Test
    public void getPlayers() {
    }

    @Test
    public void getRoundStart() {
        PresidentState test = new PresidentState();
        assert(!test.getRoundStart());
    }

    @Test
    public void checkGame() {
        PresidentState test = new PresidentState();
        assert(test.checkGame() == -1);
    }

    @Test
    public void playersWithCards() {
        PresidentState test = new PresidentState();
    }

    @Test
    public void setRoundStart() {
        PresidentState test = new PresidentState();
        test.setRoundStart(true);
        assert(test.getRoundStart());
    }

    @Test
    public void nextPlayer() {
        PresidentState test = new PresidentState();
        test.setTurn(2);
        test.nextPlayer();
        assert(test.getTurn() == 3);
        test.nextPlayer();
        assert(test.getTurn() == 0);
    }

    @Test
    public void checkPresident() {
    }
}