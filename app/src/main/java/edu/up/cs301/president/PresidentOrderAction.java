package edu.up.cs301.president;

import java.io.Serializable;

import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.actionMsg.GameAction;

/**
 * PresidentOrderAction
 * @author Hera Malik
 * @author Ben Pirkl
 * @author Kama Simon
 * @author Geryl Vinoya
 * @version April 2019
 * sends an instance of order action
 */
public class PresidentOrderAction extends GameAction implements Serializable {
    /**
     * constructor for GameAction
     *
     * @param player the player who created the action
     */
    public PresidentOrderAction(GamePlayer player) {
        super(player);
    }
} // PresidentOrderAction class
