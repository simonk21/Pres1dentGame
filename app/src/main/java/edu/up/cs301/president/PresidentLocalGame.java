package edu.up.cs301.president;

import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.LocalGame;
import edu.up.cs301.game.actionMsg.GameAction;
import edu.up.cs301.president.CardInfo.Card;

public class PresidentLocalGame extends LocalGame {

    private PresidentState state;

    public PresidentLocalGame() {
        Log.i("SJLocalGame", "creating game");
        // create the state for the beginning of the game
        state = new PresidentState();
    }

    @Override
    protected void sendUpdatedStateTo(GamePlayer p) {
        if(p == null){
            Log.i("PLocalGame.java","GamePlayer is null");
            return;
        }
        if(state == null){
            Log.i("PLocalGame.java", "Gamestate is null");
        }

        PresidentState playerState;
        for (int i = 0; i < this.players.length; i++) {
            if (this.players[i].equals(p)) {
                playerState = new PresidentState(state,i);
                p.sendInfo(playerState);
                break;
            }
        }
    }

    @Override
    protected boolean canMove(int playerIdx) {
        int whoseTurn = state.getTurn();
        return playerIdx == whoseTurn;
    }

    @Override
    protected String checkIfGameOver() {
        int score = state.checkGame();
        if(score == -1) {
            return null;
        }
        return null; // TODO: need to print message for winner
    } // TODO need to change the checkIfGameOver

    @Override
    protected boolean makeMove(GameAction action) {
        if( action == null ){
            Log.i("PresidentLocalGame.java", "action is null");
            return false;
        }
        int playerIdx = getPlayerIdx(action.getPlayer());
        if( action instanceof PresidentPassAction ) {
            return pass(playerIdx);
        }
        if ( action instanceof PresidentPlayAction ) {
            ArrayList<Card> temp = ((PresidentPlayAction) action).getCards(); // grabs cards from PresidentPlayAction class
            return play(playerIdx, temp);
        }
        if ( action instanceof PresidentOrderAction ){
            return order(playerIdx);
        }
        return false;
    }

    public boolean play(int idx, ArrayList<Card> temp) {
        if(state.getCurrentSet().size() != 0) {
            if (temp.get(0).getValue() > state.getCurrentSet().get(0).getValue()){
                state.getCurrentSet().clear();
                state.setCurrentSet(temp);
                for(int i = 0; i < state.getPlayers().get(idx).getHand().size();i++) {
                    if(state.getPlayers().get(idx).getHand().get(i).getValue() ==
                            temp.get(0).getValue() &&
                            state.getPlayers().get(idx).getHand().get(i).getSuit().equals(temp.get(0).getSuit())){
                        int val = temp.get(0).getValue();
                        String suit = temp.get(0).getSuit();
                        state.getPlayers().get(idx).removeCard(suit,val);
                    }
                }
                state.getPlayers().get(idx).resetPass();
                if(!checkNoCards()) {
                    state.nextPlayer();
                }
                return true;
            }
            return false;
        }
        state.setCurrentSet(temp);
        for(int i = 0; i < state.getPlayers().get(idx).getHand().size();i++) {
            if(state.getPlayers().get(idx).getHand().get(i).getValue() ==
                    temp.get(0).getValue() &&
                    state.getPlayers().get(idx).getHand().get(i).getSuit().equals(temp.get(0).getSuit())){
                int val = temp.get(0).getValue();
                String suit = temp.get(0).getSuit();
                state.getPlayers().get(idx).removeCard(suit,val);
            }
        }
        state.getPlayers().get(idx).resetPass();
        state.nextPlayer();
        checkNoCards();
        return true;
    }

    /**
     * pass
     *
     * @return true (player can pass turn) or false (player cannot pass turn)
     */
    private boolean pass(int turn){
        if(state.getTurn() != turn){
            return false;
        }
        state.getPlayers().get(turn).setPass();
        if(state.checkPass()) {
            state.getCurrentSet().clear();

            for(int i = 0; i < state.getPlayers().size(); i++) {
                if (state.getPlayers().get(i).getPass() == 1) {
                } else {
                    state.setTurn(i);
                }
            }

            for(int i =  0; i < state.getPlayers().size();i++){
               state.getPlayers().get(i).resetPass();
            }
        }
        state.nextPlayer();
        checkNoCards();
        return true;
    }

    private boolean order(int idx){
        for (int i = 0; i < state.getPlayers().get(idx).getHand().size(); i++) {
            for (int j = i + 1; j < state.getPlayers().get(idx).getHand().size(); j++)
            {
                if (state.getPlayers().get(idx).getHand().get(i).getValue() >
                    state.getPlayers().get(idx).getHand().get(j).getValue())
                {
                    Card temp = new Card(-1,  "Default");
                    temp.setCardVal(state.getPlayers().get(idx).getHand().get(i).getValue());
                    temp.setCardSuit(state.getPlayers().get(idx).getHand().get(i).getSuit());
                    state.getPlayers().get(idx).getHand().get(i).setCardSuit(state.getPlayers().get(idx).getHand().get(j).getSuit());
                    state.getPlayers().get(idx).getHand().get(i).setCardVal(state.getPlayers().get(idx).getHand().get(j).getValue());
                    state.getPlayers().get(idx).getHand().get(j).setCardVal(temp.getValue());
                    state.getPlayers().get(idx).getHand().get(j).setCardSuit(temp.getSuit());
                }
            }
        }
        return true;
    }
    private boolean checkNoCards(){
        int count = 0;
        if(state.getPlayers().get(state.getTurn()).getHand().size() < 1) {
            while (state.getPlayers().get(state.getTurn()).getHand().size() < 1) {
                count++;
                state.checkPresident(state.getTurn());
                if (count == 4) {
                    state.setRoundStart(true);
                    return true;
                }
                state.nextPlayer();
            }
            return true;
        }
        return false;
    }
}
