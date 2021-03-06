package edu.up.cs301.president.Players;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;

import edu.up.cs301.game.GameComputerPlayer;
import edu.up.cs301.game.infoMsg.GameInfo;
import edu.up.cs301.game.infoMsg.IllegalMoveInfo;
import edu.up.cs301.game.infoMsg.NotYourTurnInfo;
import edu.up.cs301.president.CardInfo.Card;
import edu.up.cs301.president.PresidentPassAction;
import edu.up.cs301.president.PresidentPlayAction;
import edu.up.cs301.president.PresidentState;
import edu.up.cs301.president.PresidentTradeAction;
// TODO still need to check this
/**
 * PresidentSmartAI
 * @author Hera Malik
 * @author Ben Pirkl
 * @author Kama Simon
 * @author Geryl Vinoya
 * @version April 2019
 * class that decides what the SmartAI should do
 */
public class PresidentSmartAI extends GameComputerPlayer implements Serializable {

    /* instance variable */
    PresidentState savedState;

    /**
     * constructor
     *
     * @param name the player's name (e.g., "John")
     */
    public PresidentSmartAI(String name) {
        super(name);
    }


    /**
     * receiveInfo
     * smartAI chooses to play or pass depending on the current set
     * @param info the type of GameInfo whether it be NotYourTurn, IllegalMove or PresidentState
     */
    @Override
    protected void receiveInfo(GameInfo info) {
        sleep(500); // delay CPU
        if(info == null) { // if info is null
            Log.i("PresidentDumbAI", "info is null");
        }
        else if(info instanceof NotYourTurnInfo || info instanceof IllegalMoveInfo) {

        }
        else if(info instanceof PresidentState) {

            savedState = (PresidentState) info; // current state of game

            // CPU's hand
            ArrayList<Card> temp = savedState.getPlayers().get(this.playerNum).getHand();

            // if round is over and in trading, then that should be only move that CPU does
            if(savedState.getRoundStart()) {
                ArrayList<Card> trade = toTrade(temp);
                game.sendAction(new PresidentTradeAction(this, trade));
                return;
            }

            // if current set is 0 or the current turn and previous played turn is equal,
            // then CPU should play
            if(savedState.getCurrentSet().size() == 0 || savedState.getTurn() == savedState.getPrev()) {
                if(getFourMin(temp) != null) {
                    game.sendAction(new PresidentPlayAction(this, getFourMin(temp)));
                }
                else if(getTripleMin(temp) != null) {
                    game.sendAction(new PresidentPlayAction(this, getTripleMin(temp)));
                }
                else{
                    ArrayList<Card> c = bestEmptySet(temp); // card to play
                    if(c == null) {
                        game.sendAction(new PresidentPassAction(this));
                        return;
                    }
                    temp.clear(); // clear temp hand
                    game.sendAction(new PresidentPlayAction(this, c)); // play card

                }
            }

            switch (savedState.getCurrentSet().size()) {
                // If the Size of the Current Played Set is 1,
                // Play the lowest Card possible
                case 1: // if current set is 1
                    Card t = getMin(temp);
                    if(t == null) {
                        game.sendAction(new PresidentPassAction(this));
                        return;
                    }
                    temp.clear();
                    temp.add(t);
                    if(temp.get(0).getValue() <= savedState.getCurrentSet().get(0).getValue()) {
                        game.sendAction(new PresidentPassAction(this));
                    }
                    else {
                        game.sendAction(new PresidentPlayAction(this, temp));
                    }
                    break;
                case 2: // if current set is 2
                    // Find the highest 2-of-a-kind set of cards in hand
                    ArrayList<Card> twoCard = getDoubleMin(temp);
                    if (twoCard == null || twoCard.get(0).getValue() <= savedState.getCurrentSet().get(0).getValue()) {
                        game.sendAction(new PresidentPassAction(this));
                        return;
                    }
                    game.sendAction(new PresidentPlayAction(this, twoCard));
                    break;
                case 3: // if current set is 3
                    // Find the highest 3-of-a-kind set of cards in hand
                    ArrayList<Card> threeCard = getTripleMin(temp);
                    if(threeCard == null) {
                        game.sendAction(new PresidentPassAction(this));
                        return;
                    }
                    game.sendAction(new PresidentPlayAction(this, threeCard));
                    break;
                case 4: // if current set is 4
                    // Find the highest 4-of-a-kind set of cards in hand
                    ArrayList<Card> fourCard = getFourMin(temp);
                    if(fourCard == null) {
                        game.sendAction(new PresidentPassAction(this));
                        return;
                    }
                    game.sendAction(new PresidentPlayAction(this, fourCard));
                    break;
            }
        }
    }

    /**
     * toTrade
     * Method that initializes trade for the Dumb AI based on ranking
     * If the SmartAI is Vice-President (Case 2) then return the lowest
     * card to be traded. If the SmartAI is President (Case 3) return
     * the two lowest cards in its hand to be traded off.
     * @param temp the player's hand
     * @return the cards player should trade
     */
    private ArrayList<Card> toTrade(ArrayList<Card> temp) {
        switch (savedState.getPlayers().get(this.playerNum).getRank()) {
            case 0:
                break;
            case 1:
                break;
            case 2:
                Card min = getMin(temp);
                temp.clear();
                temp.add(min);
                break;
            case 3:
                Card min1 = getMin(temp);
                for(int i = 0; i < temp.size(); i++) {
                    if(temp.get(i).getValue() == min1.getValue()
                    && temp.get(i).getSuit().equals(min1.getSuit())) {
                        temp.remove(i);
                        break;
                    }
                }
                Card min2 = getMin(temp);
                temp.clear();
                temp.add(min1);
                temp.add(min2);
                break;
        }
        return temp;
    }

    /**
     * bestEmptySet
     * Method that finds the best card to play on an empty
     * currentSet based on what is in its hand
     * @param temp player's hand
     * @return the card(s) that player plays
     */
    private ArrayList<Card> bestEmptySet(ArrayList<Card> temp) {
        Card maxTwo = new Card(-1, "Default");
        Card nextCard = new Card(-1, "Default");
        Card minCard;
        ArrayList<Card> noTwo = new ArrayList<>();

        for(int i = 0; i < temp.size(); i++) {
            if(temp.get(i).getValue() != 13) {
                noTwo.add(new Card(temp.get(i).getValue(), temp.get(i).getSuit()));
            }
        }
        if(noTwo.size() == temp.size()) {
                minCard = getMin(noTwo);
                if(minCard == null) {
                    return null;
                }
                noTwo.clear();
                noTwo.add(minCard);
                return noTwo;
        }
        else{
            for(int i = 0; i < temp.size(); i++) {
                if(temp.get(i).getValue() == 13) {
                    maxTwo.setCardVal(temp.get(i).getValue());
                    maxTwo.setCardSuit(temp.get(i).getSuit());
                }
                else {
                    nextCard.setCardSuit(temp.get(i).getSuit());
                    nextCard.setCardVal(temp.get(i).getValue());
                }
            }
            noTwo.clear();
            noTwo.add(maxTwo);
            noTwo.add(nextCard);
            return noTwo;
        }
    }

    /**
     * getMin
     * searches for the min card in hand
     * @param temp Arraylist of Cards that holds the DumbAI's hand
     * @return the min card
     */
    private Card getMin(ArrayList<Card> temp) {
        Card c = new Card(15, "Default");
        Card curr = new Card(15, "Default");
        if(savedState.getCurrentSet().size() == 0) {
            for(int i = 0; i < temp.size(); i++) {
                if(c.getValue() > temp.get(i).getValue()) {
                    c.setCardSuit(temp.get(i).getSuit());
                    c.setCardVal(temp.get(i).getValue());
                }
            }
            return c;
        }
        else {
            for(int i = 0; i < savedState.getCurrentSet().size(); i++) {

                if(savedState.getCurrentSet().get(i).getValue() != 13) {
                    curr.setCardVal(savedState.getCurrentSet().get(i).getValue());
                    curr.setCardSuit(savedState.getCurrentSet().get(i).getSuit());
                    break;
                }
            }
            if(curr.getValue() == 15) {
                return null;
            }
            for(int i = 0; i < temp.size(); i++) {
                if(temp.get(i).getValue() > curr.getValue() &&
                        temp.get(i).getValue() < c.getValue() && temp.get(i).getValue() != 13){
                    c.setCardSuit(temp.get(i).getSuit());
                    c.setCardVal(temp.get(i).getValue());
                }
            }
            if(c.getValue() == 15) {
                return null;
            }
            else {
                return c;
            }
        }
    }

    /**
     * getDoubleMax
     * finds two cards that are of same value
     * then returns it
     * @param temp the player's hand
     * @return null if no doubles found, returns c if found
     */
    private ArrayList<Card> getDoubleMin(ArrayList<Card> temp) {
        Card firstMin = getMin(temp);
        if(firstMin == null) {
            return null;
        }
        Card secondMin = new Card(15, "Default");
        Card curr = new Card(15, "Default");
        ArrayList<Card> c = new ArrayList<>();
        for(int i = 0; i < savedState.getCurrentSet().size(); i++) {
            if(savedState.getCurrentSet().get(i).getValue() != 13) {
                curr.setCardSuit(savedState.getCurrentSet().get(i).getSuit());
                curr.setCardVal(savedState.getCurrentSet().get(i).getValue());
                break;
            }
        } // get current val of current set
        for(int i = 0; i < temp.size(); i++) {
            if(temp.get(i).getValue() == firstMin.getValue() &&
                !temp.get(i).getSuit().equals(firstMin.getSuit())) {
                secondMin.setCardSuit(temp.get(i).getSuit());
                secondMin.setCardVal(temp.get(i).getValue());
                c.add(firstMin);
                c.add(secondMin);
                return c;
            }
        } // gets two min cards that are greater than set
        for(int i = 0; i < temp.size(); i++) {
            if(temp.get(i).getValue() == 13) {
                secondMin.setCardVal(temp.get(i).getValue());
                secondMin.setCardSuit(temp.get(i).getSuit());
                c.add(firstMin);
                c.add(secondMin);
                return c;
            }
        } // if player has a two, then use that and min card
        return null; // if unable then return null
    }

    /**
     * getTripleMin
     * finds three cards that are of same value
     * then returns them
     * @param temp the player's hand
     * @return null if no triples found, returns c if found
     */
    private ArrayList<Card> getTripleMin(ArrayList<Card> temp){
        ArrayList<Card> TripleMin = getDoubleMin(temp);
        if(TripleMin == null) {
            return null;
        }
        Card thirdMin = new Card(15, "Default");
        Card curr = new Card(15, "Default");
        for(int i = 0; i < savedState.getCurrentSet().size(); i++) {
            if(savedState.getCurrentSet().get(i).getValue() != 13) {
                curr.setCardSuit(savedState.getCurrentSet().get(i).getSuit());
                curr.setCardVal(savedState.getCurrentSet().get(i).getValue());
                break;
            }
        } // get current val of current set
        for(int i = 0; i < temp.size(); i++) {
            if(TripleMin.get(1).getValue() == 13) {
                if (temp.get(i).getValue() == TripleMin.get(0).getValue() &&
                        !temp.get(i).getSuit().equals(TripleMin.get(0).getSuit())) {
                    thirdMin.setCardVal(temp.get(i).getValue());
                    thirdMin.setCardSuit(temp.get(i).getSuit());
                    TripleMin.add(thirdMin);
                    return TripleMin;
                }
            }
            else if(TripleMin.get(0).getValue() == 13) {
                if (temp.get(i).getValue() == TripleMin.get(1).getValue() &&
                        !temp.get(i).getSuit().equals(TripleMin.get(1).getSuit())) {
                    thirdMin.setCardVal(temp.get(i).getValue());
                    thirdMin.setCardSuit(temp.get(i).getSuit());
                    TripleMin.add(thirdMin);
                    return TripleMin;
                }
            }
            else{
                if(TripleMin.get(0).getValue() == temp.get(i).getValue() &&
                    !TripleMin.get(0).getSuit().equals(temp.get(i).getSuit()) &&
                    !TripleMin.get(1).getSuit().equals(temp.get(i).getSuit())) {
                    thirdMin.setCardVal(temp.get(i).getValue());
                    thirdMin.setCardSuit(temp.get(i).getSuit());
                    TripleMin.add(thirdMin);
                    return TripleMin;
                }
                else if(temp.get(i).getValue() == 13) {
                    thirdMin.setCardVal(temp.get(i).getValue());
                    thirdMin.setCardSuit(temp.get(i).getSuit());
                    TripleMin.add(thirdMin);
                    return TripleMin;
                }
            }
        }
        return null;
    }

    /**
     * getFourMax
     * finds four cards that are of same value
     * then returns them
     * @param temp the player's hand
     * @return null if no four-of-a-kind's found, returns c if found
     */
    private ArrayList<Card> getFourMin(ArrayList<Card> temp) {
        ArrayList<Card> FourMin = getTripleMin(temp);
        if(FourMin == null) {
            return null;
        }
        Card FourthMin = new Card(15, "Default");
        Card curr = new Card(15, "Default");
        for(int i = 0; i < savedState.getCurrentSet().size(); i++) {
            if(savedState.getCurrentSet().get(i).getValue() != 13) {
                curr.setCardSuit(savedState.getCurrentSet().get(i).getSuit());
                curr.setCardVal(savedState.getCurrentSet().get(i).getValue());
                break;
            }
        } // get current val of current set
        int count = 0;
        Card select = new Card(-1, "Default");
        Card select_2 = new Card(-1, "Default");
        for(int i = 0; i < FourMin.size(); i++) {
            if(FourMin.get(i).getValue() == 13) {
                count++;
            }
            else{
                if(select.getValue() == -1) {
                    select.setCardVal(FourMin.get(i).getValue());
                    select.setCardSuit(FourMin.get(i).getSuit());
                }
                else{
                    select_2.setCardVal(FourMin.get(i).getValue());
                    select.setCardSuit(FourMin.get(i).getSuit());
                }
            }
        }
        switch (count){ // number of two's in set
            case 0:
                for(int i = 0; i < temp.size(); i++) { // look for a two
                    if(temp.get(i).getValue() == 13) {
                        FourthMin.setCardSuit(temp.get(i).getSuit());
                        FourthMin.setCardVal(temp.get(i).getValue());
                        FourMin.add(FourthMin);
                        return FourMin;
                    }
                }
                break;
            case 1: // one two
                for(int i = 0; i < temp.size(); i++) {
                    if(temp.get(i).getValue() == select.getValue() &&
                        temp.get(i).getValue() == select_2.getValue() &&
                        !temp.get(i).getSuit().equals(select.getSuit()) &&
                        !temp.get(i).getSuit().equals(select_2.getSuit())) {
                        FourthMin.setCardSuit(temp.get(i).getSuit());
                        FourthMin.setCardVal(temp.get(i).getValue());
                        FourMin.add(FourthMin);
                        return FourMin;
                    }
                }
                break;
            case 2: // two two's
                for(int i = 0; i < temp.size(); i++) { // add card if it is equal to selected card
                    if(temp.get(i).getValue() == select.getValue() &&
                        !temp.get(i).getSuit().equals(select.getSuit())) {
                        FourthMin.setCardSuit(temp.get(i).getSuit());
                        FourthMin.setCardVal(temp.get(i).getValue());
                        FourMin.add(FourthMin);
                        return FourMin;
                    }
                }
                break;
            case 3: // all two's
                for(int i = 0; i < temp.size(); i++) {
                    if(temp.get(i).getValue() != 13) {
                        FourthMin.setCardSuit(temp.get(i).getSuit());
                        FourthMin.setCardVal(temp.get(i).getValue());
                        FourMin.add(FourthMin);
                        return FourMin;
                    }
                }
                break;
        }
        return null;
    }
} // PresidentSmartAI class
