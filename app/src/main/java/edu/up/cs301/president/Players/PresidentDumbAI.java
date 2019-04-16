package edu.up.cs301.president.Players;

import android.util.Log;

import java.util.ArrayList;

import edu.up.cs301.game.GameComputerPlayer;
import edu.up.cs301.game.infoMsg.GameInfo;
import edu.up.cs301.game.infoMsg.IllegalMoveInfo;
import edu.up.cs301.game.infoMsg.NotYourTurnInfo;
import edu.up.cs301.president.CardInfo.Card;
import edu.up.cs301.president.PresidentPassAction;
import edu.up.cs301.president.PresidentPlayAction;
import edu.up.cs301.president.PresidentState;

public class PresidentDumbAI extends GameComputerPlayer {

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
        sleep(1000);
        if(info == null) {
            Log.i("PresidentDumbAI", "info is null");
        }
        if(info instanceof NotYourTurnInfo || info instanceof IllegalMoveInfo){
            return;
        }
        if(info instanceof PresidentState){
            boolean mustMove = false;
            savedState = (PresidentState) info;
            ArrayList<Card> temp = savedState.getPlayers().get(this.playerNum).getHand();
            Card t = getMax(temp);
            temp.clear();
            temp.add(t);
            if(savedState.getCurrentSet().size() == 0){
                sleep(1000);
                game.sendAction(new PresidentPlayAction(this, temp));
            }
            else if(Math.random() < 0.25){
                sleep(1000);
                game.sendAction(new PresidentPassAction(this));
            }
            else{
                if(savedState.getCurrentSet().get(0).getValue() >= temp.get(0).getValue()){
                    game.sendAction(new PresidentPassAction(this));
                }
                else{
                    sleep(1000);
                    game.sendAction(new PresidentPlayAction(this, temp));
                }
            }
//            if(savedState.checkPass() && savedState.getPrev() == this.playerNum){
//                savedState.setPrev();
//                game.sendAction(new PresidentPlayAction(this,temp));
//                savedState.getCurrentSet().clear();
//            }
//            else if(Math.random() < 0.5){
//                sleep(1000);
//                if(savedState.getCurrentSet().size() != 0){
//                    game.sendAction(new PresidentPassAction(this));
//                }
//                else{
//                    mustMove = true;
//                }
//            }
//            else{
//                if(savedState.getCurrentSet().size() != 0) {
//                    if (savedState.getCurrentSet().get(0).getValue() >= temp.get(0).getValue()) {
//                        game.sendAction(new PresidentPassAction(this));
//                    }
//                    else {
//                        savedState.setPrev();
//                        game.sendAction(new PresidentPlayAction(this, temp));
//                    }
//                }
//                savedState.setPrev();
//                game.sendAction(new PresidentPlayAction(this,temp));
//
//            } //TODO: adding a card doesn't work or accessing the state?
//            if(mustMove){
//                savedState.setPrev();
//                game.sendAction(new PresidentPlayAction(this, temp));
//            }
        }
    }

    public Card getMax(ArrayList<Card> temp){
        Card c = new Card(-1, "Default");
        for(int i = 0; i < temp.size(); i++){
            if(c.getValue() < temp.get(i).getValue()){
                c.setCardSuit(temp.get(i).getSuit());
                c.setCardVal(temp.get(i).getValue());
            }
        }
        return c;
    }
}
