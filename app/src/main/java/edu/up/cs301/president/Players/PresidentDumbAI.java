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


    /**
     * Method to receive various game-states
     * from the GameState and send them to
     * the DumbAI throughout the game
     * @param info
     */
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

            // If the round just started,
            // Initialize the trade function based
            // on the DumbAI's rank.
            if(savedState.getRoundStart()){
                ArrayList<Card> trade = toTrade(temp);
                game.sendAction(new PresidentTradeAction(this, trade));
                return;
            }
            Card t = getMax(temp);

            sleep(500);

            // if the current set isn't 0, then possibly pass:
            // 20% chance for the Dumb AI to randomly Pass the turn.
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
                     // Check if the card to be played is a legal play. If it isnt,
                     // pass the turn. If it is, play it.
                    if(temp.get(0).getValue() <= savedState.getCurrentSet().get(0).getValue()){
                        game.sendAction(new PresidentPassAction(this));
                    }
                    else {
                        game.sendAction(new PresidentPlayAction(this, temp));
                    }
                    return;
            }
            // A 'Catch-All' case to ensure that the DumbAI doesn't freeze up.
            game.sendAction(new PresidentPassAction(this)); // If all else fails, pass
        }
    }

    /**
     * toTrade
     * Method that initializes trade for the Dumb AI based on ranking
     * If the DumbAI is Vice-President (Case 2) then return the lowest
     * card to be traded. If the DumbAI is President (Case 3) return
     * the two lowest cards in its hand to be traded off.
     * @param temp the CPU's hand
     * @return the cards CPU should trade
     */
    private ArrayList<Card> toTrade(ArrayList<Card> temp){
        switch (savedState.getPlayers().get(this.playerNum).getRank()){
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
                for(int i = 0; i < temp.size(); i++){
                    if(temp.get(i).getValue() == min1.getValue()
                            && temp.get(i).getSuit().equals(min1.getSuit())){
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
     * getMin
     * searches for the min card in hand
     * used for "if" CPU is President or Vice President
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
            if(curr.getValue() == 15){
                return null;
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
     * getMax
     * searches for the max card in hand
     * Used during Play
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
} // PresidentDumbAI class
