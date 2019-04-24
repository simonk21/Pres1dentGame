package edu.up.cs301.president;

import java.io.Serializable;
import java.util.ArrayList;

import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.actionMsg.GameAction;
import edu.up.cs301.president.CardInfo.Card;

/**
 * PresidentPlayAction
 * @author Hera Malik
 * @author Ben Pirkl
 * @author Kama Simon
 * @author Geryl Vinoya
 * @version April 2019
 * sends an instance of play action
 */
public class PresidentPlayAction extends GameAction implements Serializable {

    private ArrayList<Card> cards;
    /**
     * constructor for GameAction
     *
     * @param player the player who created the action
     */
    public PresidentPlayAction(GamePlayer player, ArrayList<Card> cards) {

        super(player);
        this.cards = cards;

    }

    /** getter method */
    public ArrayList<Card> getCards() { return cards; }
} // PresidentPlayAction class
