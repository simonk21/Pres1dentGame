package edu.up.cs301.president;

import java.util.ArrayList;

import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.actionMsg.GameAction;
import edu.up.cs301.president.CardInfo.Card;

public class PresidentPlayAction extends GameAction {

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

    public ArrayList<Card> getCards() { return cards; }
}
