package edu.up.cs301.president.Players;

import android.util.Log;

import edu.up.cs301.game.GameComputerPlayer;
import edu.up.cs301.game.infoMsg.GameInfo;
import edu.up.cs301.game.infoMsg.NotYourTurnInfo;
import edu.up.cs301.president.PresidentPassAction;
import edu.up.cs301.president.PresidentPlayAction;
import edu.up.cs301.president.PresidentState;

public class PresidentSmartAI extends GameComputerPlayer {
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
        if(info instanceof NotYourTurnInfo){
            return;
        }
        if(info instanceof PresidentState){
            sleep(1000);

            if(Math.random() < 0.5){
                game.sendAction(new PresidentPassAction(this));
            }
            else{
                game.sendAction(new PresidentPassAction(this));
            } //TODO: identical to dumb ai (need to modify)
        }
    }
}
