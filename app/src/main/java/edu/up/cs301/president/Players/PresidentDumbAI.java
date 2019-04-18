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

/**
 * PresidentDumbAI class
 * @author Hera Malik
 * @author Ben Pirkl
 * @author Kama Simon
 * @author Geryl Vinoya
 * @version April 2019
 * class that decides what the Dumb AI should do
 */
public class PresidentDumbAI extends GameComputerPlayer implements Serializable {

    private PresidentState savedState;
    /**
     * constructor
     *
     * @param name the player's name (e.g., "John")
     */
    public PresidentDumbAI(String name) {
        super(name);
    }

    @Override
    protected void receiveInfo(GameInfo info) {
        sleep(500);

        if(info == null) {  // if info is null return
            Log.i("PresidentDumbAI", "info is null");
            return;
        }

        if(info instanceof NotYourTurnInfo || info instanceof IllegalMoveInfo){
            return; // if it isn't player's turn or it's an illegal move then return
        }

        if(info instanceof PresidentState) {
            savedState = (PresidentState) info;
            if(savedState.getPlayers().get(this.playerNum).getHand().size() < 1){
                game.sendAction(new PresidentPassAction(this)); // if this dumbAI has no cards, pass
                return;
            }
            // takes the player's hand and saves it to an arraylist
            // finds the max card
            ArrayList<Card> temp = savedState.getPlayers().get(this.playerNum).getHand();
            Card t = getMax(temp);

            sleep(500);

            // if the current set isn't 0, then possibly pass
            if(savedState.getCurrentSet().size() != 0 && Math.random() < 0.2){
                game.sendAction(new PresidentPassAction(this));
                return;
            }

            // checks current set size and plays as follows
            switch (savedState.getCurrentSet().size()) {
                case 0: // if current set is empty, then MUST play
                    temp.clear();
                    temp.add(t);
                    game.sendAction(new PresidentPlayAction(this, temp));
                    return;
                case 1: // if current set is 1, then play if can beat value of current set
                    temp.clear();
                    temp.add(t);
                    if(temp.get(0).getValue() <= savedState.getCurrentSet().get(0).getValue()){
                        game.sendAction(new PresidentPassAction(this));
                    }
                    else {
                        game.sendAction(new PresidentPlayAction(this, temp));
                    }
                    return;
            }
            game.sendAction(new PresidentPassAction(this)); // if all else fails, pass
        }
    }

    /**
     * getMax
     * searches for the max card in hand
     * @param temp Arraylist of Cards that holds the DumbAI's hand
     * @return the max card
     */
    private Card getMax(ArrayList<Card> temp){
        Card c = new Card(-1, "Default");
        for(int i = 0; i < temp.size(); i++){
            if(c.getValue() < temp.get(i).getValue()){
                c.setCardSuit(temp.get(i).getSuit());
                c.setCardVal(temp.get(i).getValue());
            }
        }
        return c;
    }

    /**
     * getDoubleMax
     * finds two cards that are of same value
     * then returns it
     * @param temp the player's hand
     * @return null if no doubles found, returns c if found
     */
    private ArrayList<Card> getDoubleMax(ArrayList<Card> temp){
        Card max1 = new Card(-1, "Default");
        Card max2 = new Card(-1, "Default");
        for(int i = 0; i < temp.size(); i++){
            if(max1.getValue() < temp.get(i).getValue()){
                max1.setCardSuit(temp.get(i).getSuit());
                max1.setCardVal(temp.get(i).getValue());
                for(int j = 0; j < temp.size(); j++){
                    if(max1.getValue() == temp.get(j).getValue() && i != j){
                        max2.setCardVal(temp.get(j).getValue());
                        max2.setCardSuit(temp.get(j).getSuit());
                        ArrayList<Card> c = new ArrayList<>();
                        c.add(max1);
                        c.add(max2);
                        return c;
                    }
                }
            }
        }
        return null;
    }
}
