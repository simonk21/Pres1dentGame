package edu.up.cs301.president.Players;

import org.junit.Test;

import java.util.ArrayList;

import edu.up.cs301.president.CardInfo.Card;
import edu.up.cs301.president.PresidentState;

import static org.junit.Assert.*;

public class PresidentSmartAITest {

    @Test
    public void receiveInfo() {
        PresidentState testState = new PresidentState();
        ArrayList<Card> testCurrent = new ArrayList<>();
        testCurrent.add(new Card(13, "Test"));
        testState.setCurrentSet(testCurrent);
        PresidentSmartAI testAI = new PresidentSmartAI("Smart");
        testAI.receiveInfo(testState);

    }
}