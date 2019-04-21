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

    @Override
    protected void receiveInfo(GameInfo info) {
        sleep(1000);
        if(info == null) {
            Log.i("PresidentDumbAI", "info is null");
        }
        if(info instanceof NotYourTurnInfo || info instanceof IllegalMoveInfo){
            return;
        }
        if(info instanceof PresidentState) { //TODO updated DumbAi for 0-2 card current set
            savedState = (PresidentState) info;
            ArrayList<Card> temp = savedState.getPlayers().get(this.playerNum).getHand();

            if(savedState.getCurrentSet().size() == 0 || savedState.getTurn() == savedState.getPrev()){
                ArrayList<Card> c = bestEmptySet(temp);
                if(c == null){
                    game.sendAction(new PresidentPassAction(this));
                    return;
                }
                temp.clear();
                game.sendAction(new PresidentPlayAction(this, c));
                return;
            }
            switch (savedState.getCurrentSet().size()) {
                case 0:
                    ArrayList<Card> c = bestEmptySet(temp);
                    if(c == null){
                        game.sendAction(new PresidentPassAction(this));
                        return;
                    }
                    temp.clear();
                    game.sendAction(new PresidentPlayAction(this, c));
                    break;
                case 1:
                    Card t = getMin(temp);
                    if(t == null){
                        game.sendAction(new PresidentPassAction(this));
                        return;
                    }
                    temp.clear();
                    temp.add(t);
                    if(temp.get(0).getValue() <= savedState.getCurrentSet().get(0).getValue()){
                        game.sendAction(new PresidentPassAction(this));
                    }
                    else {
                        game.sendAction(new PresidentPlayAction(this, temp));
                    }
                    break;
                case 2:
                    ArrayList<Card> twoCard = getDoubleMax(temp);
                    if (twoCard == null || twoCard.get(0).getValue() <= savedState.getCurrentSet().get(0).getValue()) {
                        game.sendAction(new PresidentPassAction(this));
                        return;
                    }
                    game.sendAction(new PresidentPlayAction(this, twoCard));
                    break;
                case 3:
                    ArrayList<Card> threeCard = getTripleMax(temp);
                    if(threeCard == null || threeCard.get(0).getValue() <= savedState.getCurrentSet().get(0).getValue()){
                        game.sendAction(new PresidentPassAction(this));
                        return;
                    }
                    game.sendAction(new PresidentPlayAction(this, threeCard));
                    break;
                case 4:
                    ArrayList<Card> fourCard = getFourMax(temp);
                    if(fourCard == null || fourCard.get(0).getValue() <= savedState.getCurrentSet().get(0).getValue()){
                        game.sendAction(new PresidentPassAction(this));
                        return;
                    }
                    game.sendAction(new PresidentPlayAction(this, fourCard));
                    break;
            }
        }
    }

    private ArrayList<Card> bestEmptySet(ArrayList<Card> temp){
        Card maxTwo = new Card(-1, "Default");
        Card nextCard = new Card(-1, "Default");
        Card minCard;
        ArrayList<Card> noTwo = new ArrayList<>();
        for(int i = 0; i < temp.size(); i++){
            if(temp.get(i).getValue() != 13){
                noTwo.add(new Card(temp.get(i).getValue(), temp.get(i).getSuit()));
            }
        }
        if(noTwo.size() == temp.size()) {
                minCard = getMin(noTwo);
                if(minCard == null){
                    return null;
                }
                noTwo.clear();
                noTwo.add(minCard);
                return noTwo;
        }
        else{
            for(int i = 0; i < temp.size(); i++){
                if(temp.get(i).getValue() == 13){
                    maxTwo.setCardVal(temp.get(i).getValue());
                    maxTwo.setCardSuit(temp.get(i).getSuit());
                }
                else{
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
     * getMax
     * searches for the min card in hand
     * @param temp Arraylist of Cards that holds the DumbAI's hand
     * @return the min card
     */
    private Card getMin(ArrayList<Card> temp){
        Card c = new Card(15, "Default");
        Card curr = new Card(15, "Default");
        if(savedState.getCurrentSet().size() == 0){
            for(int i = 0; i < temp.size(); i++){
                if(c.getValue() > temp.get(i).getValue()){
                    c.setCardSuit(temp.get(i).getSuit());
                    c.setCardVal(temp.get(i).getValue());
                }
            }
            return c;
        }
        else{
            for(int i = 0; i < savedState.getCurrentSet().size(); i++){

                if(savedState.getCurrentSet().get(i).getValue() != 13){
                    curr.setCardVal(savedState.getCurrentSet().get(i).getValue());
                    curr.setCardSuit(savedState.getCurrentSet().get(i).getSuit());
                    break;
                }
            }
            for(int i = 0; i < temp.size(); i++){
                if(temp.get(i).getValue() > curr.getValue() &&
                        temp.get(i).getValue() < c.getValue() && temp.get(i).getValue() != 13){
                    c.setCardSuit(temp.get(i).getSuit());
                    c.setCardVal(temp.get(i).getValue());
                }
            }
            if(c.getValue() == 15){
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
    private ArrayList<Card> getDoubleMax(ArrayList<Card> temp){
        Card firstMin = getMin(temp);
        if(firstMin == null){
            return null;
        }
        Card secondMin = new Card(15, "Default");
        Card curr = new Card(15, "Default");
        ArrayList<Card> c = new ArrayList<>();
        for(int i = 0; i < savedState.getCurrentSet().size(); i++){
            if(savedState.getCurrentSet().get(i).getValue() != 13){
                curr.setCardSuit(savedState.getCurrentSet().get(i).getSuit());
                curr.setCardVal(savedState.getCurrentSet().get(i).getValue());
                break;
            }
        } // get current val of current set
        for(int i = 0; i < temp.size(); i++){
            if(temp.get(i).getValue() == firstMin.getValue() &&
                !temp.get(i).getSuit().equals(firstMin.getSuit())){
                secondMin.setCardSuit(temp.get(i).getSuit());
                secondMin.setCardVal(temp.get(i).getValue());
                c.add(firstMin);
                c.add(secondMin);
                return c;
            }
        } // gets two min cards that are greater than set
        for(int i = 0; i < temp.size(); i++){
            if(temp.get(i).getValue() == 13){
                secondMin.setCardVal(temp.get(i).getValue());
                secondMin.setCardSuit(temp.get(i).getSuit());
                c.add(firstMin);
                c.add(secondMin);
                return c;
            }
        } // if player has a two, then use that and min card
        return null; // if unable then return null
    }

    private ArrayList<Card> getTripleMax(ArrayList<Card> temp){ // TODO I want to fix this method
        Card max1 = new Card(-1, "Default");
        Card max2 = new Card(-1, "Default");
        Card max3 = new Card(-1, "Default");
        for(int i = 0; i < temp.size(); i++){
            if(max1.getValue() < temp.get(i).getValue()){
                max1.setCardVal(temp.get(i).getValue());
                max2.setCardSuit(temp.get(i).getSuit());
                for(int j = 0; j <  temp.size(); j++){
                    if(max1.getValue() == temp.get(j).getValue() && i !=  j){
                        max2.setCardVal(temp.get(j).getValue());
                        max2.setCardSuit(temp.get(j).getSuit());
                        for(int k = 0; k < temp.size(); k++){
                            if(max1.getValue() == temp.get(k).getValue() && i != k && j != k){
                                max3.setCardSuit(temp.get(k).getSuit());
                                max3.setCardVal(temp.get(k).getValue());
                                ArrayList<Card> c = new ArrayList<>();
                                c.add(max1);
                                c.add(max2);
                                c.add(max3);
                                return c;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    private ArrayList<Card> getFourMax(ArrayList<Card> temp){ // TODO I want to fix this method
        Card max1 = new Card(-1, "Default");
        Card max2 = new Card(-1, "Default");
        Card max3 = new Card(-1, "Default");
        Card max4 = new Card(-1, "Default");
        for(int i = 0; i < temp.size(); i++){
            if(max1.getValue() < temp.get(i).getValue()){
                max1.setCardVal(temp.get(i).getValue());
                max2.setCardSuit(temp.get(i).getSuit());
                for(int j = 0; j <  temp.size(); j++){
                    if(max1.getValue() == temp.get(j).getValue() && i !=  j){
                        max2.setCardVal(temp.get(j).getValue());
                        max2.setCardSuit(temp.get(j).getSuit());
                        for(int k = 0; k < temp.size(); k++){
                            if(max1.getValue() == temp.get(k).getValue() && i != k && j != k){
                                max3.setCardSuit(temp.get(k).getSuit());
                                max3.setCardVal(temp.get(k).getValue());
                                for(int l = 0; l < temp.size(); l++){
                                    if(max1.getValue() == temp.get(l).getValue() &&
                                            i != l && j != l && k != l){
                                        max4.setCardVal(temp.get(l).getValue());
                                        max4.setCardSuit(temp.get(l).getSuit());
                                        ArrayList<Card> c = new ArrayList<>();
                                        c.add(max1);
                                        c.add(max2);
                                        c.add(max3);
                                        c.add(max4);
                                        return c;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }
}
