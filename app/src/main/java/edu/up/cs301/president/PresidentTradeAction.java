package edu.up.cs301.president;

import java.io.Serializable;
import java.util.ArrayList;

import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.actionMsg.GameAction;
import edu.up.cs301.president.CardInfo.Card;

public class PresidentTradeAction extends GameAction implements Serializable {

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

    public ArrayList<Card> getCardsToTrade(){
        return tradeCard;
    }
}
