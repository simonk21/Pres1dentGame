package edu.up.cs301.president;

import java.io.Serializable;

import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.actionMsg.GameAction;

public class PresidentTradeAction extends GameAction implements Serializable {
    /**
     * constructor for GameAction
     *
     * @param player the player who created the action
     */
    public PresidentTradeAction(GamePlayer player) {
        super(player);
    }
}
