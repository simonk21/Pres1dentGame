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

// TODO sometimes DUMB AI doesn't play, pauses for too long
// TODO it probably has something to do with the cases.
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
        if(info == null) {
            Log.i("PresidentDumbAI", "info is null");
        }
        if(info instanceof NotYourTurnInfo || info instanceof IllegalMoveInfo){
            return;
        }
        if(info instanceof PresidentState) { //TODO updated DumbAi for 0-2 card current set
            savedState = (PresidentState) info;
            if(savedState.getPlayers().get(this.playerNum).getHand().size() < 1){
                savedState.nextPlayer();
                return;
            }
            ArrayList<Card> temp = savedState.getPlayers().get(this.playerNum).getHand();
            Card t = getMax(temp);
            sleep(500);
            if(savedState.getCurrentSet().size() != 0 && Math.random() < 0.2){
                game.sendAction(new PresidentPassAction(this));
                return;
            }
            switch (savedState.getCurrentSet().size()) {
                case 0:
                    temp.clear();
                    temp.add(t);
                    game.sendAction(new PresidentPlayAction(this, temp));
                    return;
                case 1:
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
            game.sendAction(new PresidentPassAction(this));
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
