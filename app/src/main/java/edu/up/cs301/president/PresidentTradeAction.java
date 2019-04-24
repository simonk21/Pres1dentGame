package edu.up.cs301.president;

import java.io.Serializable;
import java.util.ArrayList;

import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.actionMsg.GameAction;
import edu.up.cs301.president.CardInfo.Card;

/**
 * PresidentTradeAction class
 * @author Hera Malik
 * @author Ben Pirkl
 * @author Kama Simon
 * @author Geryl Vinoya
 * @version April 2019
 * sends an instance of trade action
 */
public class PresidentTradeAction extends GameAction implements Serializable {

    /* instance variable */
    private ArrayList<Card> tradeCard;

    /**
     * constructor for GameAction
     *
     * @param player the player who created the action
     */
    public PresidentTradeAction(GamePlayer player, ArrayList<Card> tradeCard) {
        super(player);
        this.tradeCard = tradeCard;
    }

    /** getter method */
    public ArrayList<Card> getCardsToTrade(){
        return tradeCard;
    }
} // PresidentTradeAction class
