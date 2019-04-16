package edu.up.cs301.president.Players;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import edu.up.cs301.animation.AnimationSurface;
import edu.up.cs301.game.GameHumanPlayer;
import edu.up.cs301.game.GameMainActivity;
import edu.up.cs301.game.R;
import edu.up.cs301.game.actionMsg.GameAction;
import edu.up.cs301.game.infoMsg.GameInfo;
import edu.up.cs301.game.infoMsg.IllegalMoveInfo;
import edu.up.cs301.game.infoMsg.NotYourTurnInfo;
import edu.up.cs301.president.CardInfo.Card;
import edu.up.cs301.president.PresidentOrderAction;
import edu.up.cs301.president.PresidentPassAction;
import edu.up.cs301.president.PresidentPlayAction;
import edu.up.cs301.president.PresidentState;

/**
 * A GUI of a counter-player. The GUI displays the current value of the counter,
 * and allows the human player to press the '+' and '-' buttons in order to
 * send moves to the game.
 *
 * Just for fun, the GUI is implemented so that if the player presses either button
 * when the counter-value is zero, the screen flashes briefly, with the flash-color
 * being dependent on whether the player is player 0 or player 1.
 *
 * @author Steven R. Vegdahl
 * @author Andrew M. Nuxoll
 * @version July 2013
 */
public class PresidentHumanPlayer extends GameHumanPlayer implements View.OnClickListener {

    /* instance variables */

    // the most recent game state, as given to us by the CounterLocalGame
    private PresidentState state;

    // the android activity that we are running
    private GameMainActivity myActivity;

    // textview in GUI
    private TextView player1Text, player2Text, player3Text, player4Text;
    private TextView cards_1, cards_2, cards_3; // shows rem. cards

    // buttons in GUI
    private Button playButton, passButton, orderButton;

    // card ImageButton
    private ImageButton[] playersCards = new ImageButton[13];

    private int turn;

    private ImageButton currentSet;

    private ImageButton selectedCard;
    private Card cardToPlay;


    /**
     * constructor
     *
     * @param name the player's name
     */
    public PresidentHumanPlayer(String name) {
        super(name);
    }

    /**
     * Returns the GUI's top view object
     *
     * @return the top object in the GUI's view heirarchy
     */
    public View getTopView() {
        return myActivity.findViewById(R.id.in_game_gui);
    }

    /**
     * sets the counter value in the text view
     */
    protected void updateDisplay() {
        switch (this.state.getTurn()) {
            case 0:
                switchHighlight(0);
                break;
            case 1:
                switchHighlight(1);
                break;
            case 2:
                switchHighlight(2);
                break;
            case 3:
                switchHighlight(3);
                break;
        }
        if(selectedCard != null){
            selectedCard.setBackgroundResource(R.drawable.scoreboard);
        }
    }

    /**
     * this method gets called when the user clicks the '+' or '-' button. It
     * creates a new CounterMoveAction to return to the parent activity.
     *
     * @param button the button that was clicked
     */
    public void onClick(View button) {
        // if we are not yet connected to a game, ignore
        if (game == null) return;

        GameAction action = null;
        if (button.getId() == R.id.playButton) {
            // play button: player will put down cards
            if(selectedCard == null){
                Toast.makeText(this.myActivity, "No Card Selected", Toast.LENGTH_SHORT).show();
                return;
            }
            ArrayList<Card> temp = new ArrayList<Card>();
            temp.add(getGUICard());
            selectedCard.getBackground().clearColorFilter();
            action = new PresidentPlayAction(this, temp);
        } else if (button.getId() == R.id.passButton) {
            selectedCard = null;
            action = new PresidentPassAction(this);

        } else if (button.getId() == R.id.orderButton) {

            action = new PresidentOrderAction(this);

        } else {
            // something else was pressed: ignore
            return;
        }

        game.sendAction(action); // send action to the game
        receiveInfo(state);
    }// onClick

    /**
     * callback method when we get a message (e.g., from the game)
     *
     * @param info the message
     */
    @Override
    public void receiveInfo(GameInfo info) {
        //updateDisplay(); // TODO updates the GUI to show change of turn, human still can play tho
        if (info instanceof PresidentState) {
            // we do not want to update if it is the same state
            if (state != null) {
                if (state.equals(info)) {
                    return;
                }
            }
            state = (PresidentState) info;
            if(state.getCurrentSet().size() != 0 ){
                int id = getImageId(state.getCurrentSet().get(0));
                currentSet.setTag(Integer.valueOf(id));
                currentSet.setBackgroundResource(id);
            }
            else{
                currentSet.setBackgroundResource(R.drawable.card_back);
            }
            updatePlayerGui();
            updateDisplay();
        } else if (info instanceof NotYourTurnInfo) {
            // if we had an out-of-turn or illegal move, flash the screen
            Toast.makeText(this.myActivity, "Not your turn", Toast.LENGTH_SHORT).show();
        }
        else if (info instanceof IllegalMoveInfo){
            Toast.makeText(this.myActivity, "Illegal Move", Toast.LENGTH_SHORT).show();

        }
    }

    /**
     * callback method--our game has been chosen/rechosen to be the GUI,
     * called from the GUI thread
     *
     * @param activity the activity under which we are running
     */
    public void setAsGui(GameMainActivity activity) {

        // remember the activity
        myActivity = activity;

        // Load the layout resource for our GUI
        activity.setContentView(R.layout.in_game_layout);

        playersCards[0] = activity.findViewById(R.id.card0);
        playersCards[1] = activity.findViewById(R.id.card1);
        playersCards[2] = activity.findViewById(R.id.card2);
        playersCards[3] = activity.findViewById(R.id.card3);
        playersCards[4] = activity.findViewById(R.id.card4);
        playersCards[5] = activity.findViewById(R.id.card5);
        playersCards[6] = activity.findViewById(R.id.card6);
        playersCards[7] = activity.findViewById(R.id.card7);
        playersCards[8] = activity.findViewById(R.id.card8);
        playersCards[9] = activity.findViewById(R.id.card9);
        playersCards[10] = activity.findViewById(R.id.card10);
        playersCards[11] = activity.findViewById(R.id.card11);
        playersCards[12] = activity.findViewById(R.id.card12);
        currentSet = activity.findViewById(R.id.currentPlay);
        for (int i = 0; i < 13; i++) {
            playersCards[i].setOnClickListener(new CardClickListener());
        }
        // player's name
        player1Text = activity.findViewById(R.id.player1Text);
        player2Text = activity.findViewById(R.id.player2Text);
        player3Text = activity.findViewById(R.id.Player3Text);
        player4Text = activity.findViewById(R.id.userPlayer);

        // player's rem. cards except for human player
        cards_1 = activity.findViewById(R.id.p1);
        cards_2 = activity.findViewById(R.id.p2);
        cards_3 = activity.findViewById(R.id.p3);

        // button listener
        playButton = activity.findViewById(R.id.playButton);
        playButton.setOnClickListener(this);
        passButton = activity.findViewById(R.id.passButton);
        passButton.setOnClickListener(this);
        orderButton = activity.findViewById(R.id.orderButton);
        orderButton.setOnClickListener(this);
        // if we have a game state, "simulate" that we have just received
        // the state from the game so that the GUI values are updated
        if (state != null) {
            receiveInfo(state);
        }

        if (state == null) {
            Log.i("PresidentHumanPlayer", "state is null");
            // this should never happen
        }

    }

    public class CardClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            // removes Color Filter from all card Image Buttons
            for (int i = 0; i < 13; i++) {
                playersCards[i].getBackground().clearColorFilter();
                v.invalidate();
            }
            // selected card will have color filter
            selectedCard = (ImageButton) v;
            selectedCard.getBackground().setColorFilter(0x77000000,
                    PorterDuff.Mode.SRC_ATOP);
            v.invalidate(); // TODO: all cards are getting selected ?

        }
    }

    /**
     * updateCardGui
     * <p>
     * update the GUI of a card given which card to update
     *
     * @return void
     */
    public void updateCardGui(int i) {
        Card theCard = state.getPlayers().get(0).getHand().get(i);
        int imageId = getImageId(theCard);
        playersCards[i].setTag(imageId);
        playersCards[i].setBackgroundResource(imageId);
    } // TODO: need to change to not just 0


    /**
     * updatePlayerGui
     * <p>
     * Update's the user's GUI
     *
     * @return void
     */
    public void updatePlayerGui() { // updates the player's hand
        int i = 0;
        for (int j = 0; j < 13; j++) {
            playersCards[j].setBackgroundResource(R.drawable.scoreboard);
        }
        for (Card c : state.getPlayers().get(0).getHand()) {
            updateCardGui(i);
            playersCards[i].getBackground().setAlpha(255);
            playersCards[i].invalidate();
            i++;
        }
    }

    /**
     * getImageId
     * <p>
     * Cases to find which image ID to use for a player's set of cards
     *
     * @return the ID of the card image
     */
    public int getImageId(Card theCard) { // grabs Image Button ID
        int imageId = 0;
        if (theCard.getSuit().equals("Spades")) {
            switch (theCard.getValue()) {
                case 1:
                    imageId = R.drawable.three_spades;
                    break;
                case 2:
                    imageId = R.drawable.four_spades;
                    break;
                case 3:
                    imageId = R.drawable.five_spades;
                    break;
                case 4:
                    imageId = R.drawable.six_spades;
                    break;
                case 5:
                    imageId = R.drawable.seven_spades;
                    break;
                case 6:
                    imageId = R.drawable.eight_spades;
                    break;
                case 7:
                    imageId = R.drawable.nine_spades;
                    break;
                case 8:
                    imageId = R.drawable.ten_spades;
                    break;
                case 9:
                    imageId = R.drawable.jack_spades;
                    break;
                case 10:
                    imageId = R.drawable.queen_spades;
                    break;
                case 11:
                    imageId = R.drawable.king_spades;
                    break;
                case 12:
                    imageId = R.drawable.ace_spades;
                    break;
                case 13:
                    imageId = R.drawable.two_spades;
                    break;
            }
        } else if (theCard.getSuit().equals("Hearts")) {
            switch (theCard.getValue()) {
                case 1:
                    imageId = R.drawable.three_hearts;
                    break;
                case 2:
                    imageId = R.drawable.four_hearts;
                    break;
                case 3:
                    imageId = R.drawable.five_hearts;
                    break;
                case 4:
                    imageId = R.drawable.six_hearts;
                    break;
                case 5:
                    imageId = R.drawable.seven_hearts;
                    break;
                case 6:
                    imageId = R.drawable.eight_hearts;
                    break;
                case 7:
                    imageId = R.drawable.nine_hearts;
                    break;
                case 8:
                    imageId = R.drawable.ten_hearts;
                    break;
                case 9:
                    imageId = R.drawable.jack_hearts;
                    break;
                case 10:
                    imageId = R.drawable.queen_hearts;
                    break;
                case 11:
                    imageId = R.drawable.king_hearts;
                    break;
                case 12:
                    imageId = R.drawable.ace_hearts;
                    break;
                case 13:
                    imageId = R.drawable.two_hearts;
                    break;
            }
        } else if (theCard.getSuit().equals("Diamonds")) {
            switch (theCard.getValue()) {
                case 1:
                    imageId = R.drawable.three_diamonds;
                    break;
                case 2:
                    imageId = R.drawable.four_diamonds;
                    break;
                case 3:
                    imageId = R.drawable.five_diamonds;
                    break;
                case 4:
                    imageId = R.drawable.six_diamonds;
                    break;
                case 5:
                    imageId = R.drawable.seven_diamonds;
                    break;
                case 6:
                    imageId = R.drawable.eight_diamonds;
                    break;
                case 7:
                    imageId = R.drawable.nine_diamonds;
                    break;
                case 8:
                    imageId = R.drawable.ten_diamonds;
                    break;
                case 9:
                    imageId = R.drawable.jack_diamonds;
                    break;
                case 10:
                    imageId = R.drawable.queen_diamonds;
                    break;
                case 11:
                    imageId = R.drawable.king_diamonds;
                    break;
                case 12:
                    imageId = R.drawable.ace_diamonds;
                    break;
                case 13:
                    imageId = R.drawable.two_diamonds;
                    break;
            }
        } else if (theCard.getSuit().equals("Clubs")) {
            switch (theCard.getValue()) {
                case 1:
                    imageId = R.drawable.three_clubs;
                    break;
                case 2:
                    imageId = R.drawable.four_clubs;
                    break;
                case 3:
                    imageId = R.drawable.five_clubs;
                    break;
                case 4:
                    imageId = R.drawable.six_clubs;
                    break;
                case 5:
                    imageId = R.drawable.seven_clubs;
                    break;
                case 6:
                    imageId = R.drawable.eight_clubs;
                    break;
                case 7:
                    imageId = R.drawable.nine_clubs;
                    break;
                case 8:
                    imageId = R.drawable.ten_clubs;
                    break;
                case 9:
                    imageId = R.drawable.jack_clubs;
                    break;
                case 10:
                    imageId = R.drawable.queen_clubs;
                    break;
                case 11:
                    imageId = R.drawable.king_clubs;
                    break;
                case 12:
                    imageId = R.drawable.ace_clubs;
                    break;
                case 13:
                    imageId = R.drawable.two_clubs;
                    break;
            }
        }
        return imageId;
    }

    /**
     * checkSetFinish
     * <p>
     * Checks if a player is out of cards
     *
     * @return true if a player is out of cards
     */
    public boolean checkSetFinish(int idx) {
        if (state.setFinish()) { // checks if player is out of cards
            return true;
        }
        return false;
    }

    /**
     * switchHighlight
     * <p>
     * highlights a player on the GUI if it is their turn
     *
     * @return void
     */
    public void switchHighlight(int idx) {
        switch (idx) {
            case 0:
                player4Text.setBackgroundResource(R.color.yellow);
                player4Text.setTextColor(myActivity.getResources().getColor(R.color.black));
                player1Text.setBackgroundResource(R.color.black);
                player2Text.setBackgroundResource(R.color.black);
                player3Text.setBackgroundResource(R.color.black);
                player1Text.setTextColor(myActivity.getResources().getColor(R.color.white));
                player2Text.setTextColor(myActivity.getResources().getColor(R.color.white));
                player3Text.setTextColor(myActivity.getResources().getColor(R.color.white));
                break;
            case 1:
                player1Text.setBackgroundResource(R.color.yellow);
                player1Text.setTextColor(myActivity.getResources().getColor(R.color.black));
                player4Text.setBackgroundResource(R.color.black);
                player2Text.setBackgroundResource(R.color.black);
                player3Text.setBackgroundResource(R.color.black);
                player4Text.setTextColor(myActivity.getResources().getColor(R.color.white));
                player2Text.setTextColor(myActivity.getResources().getColor(R.color.white));
                player3Text.setTextColor(myActivity.getResources().getColor(R.color.white));
                break;
            case 2:
                player2Text.setBackgroundResource(R.color.yellow);
                player2Text.setTextColor(myActivity.getResources().getColor(R.color.black));
                player1Text.setBackgroundResource(R.color.black);
                player4Text.setBackgroundResource(R.color.black);
                player3Text.setBackgroundResource(R.color.black);
                player1Text.setTextColor(myActivity.getResources().getColor(R.color.white));
                player4Text.setTextColor(myActivity.getResources().getColor(R.color.white));
                player3Text.setTextColor(myActivity.getResources().getColor(R.color.white));
                break;
            case 3:
                player3Text.setBackgroundResource(R.color.yellow);
                player3Text.setTextColor(myActivity.getResources().getColor(R.color.black));
                player1Text.setBackgroundResource(R.color.black);
                player2Text.setBackgroundResource(R.color.black);
                player4Text.setBackgroundResource(R.color.black);
                player1Text.setTextColor(myActivity.getResources().getColor(R.color.white));
                player2Text.setTextColor(myActivity.getResources().getColor(R.color.white));
                player4Text.setTextColor(myActivity.getResources().getColor(R.color.white));
                break;
        }
        player4Text.invalidate();
        player1Text.invalidate();
        player2Text.invalidate();
        player3Text.invalidate();
    }

    public Card getGUICard() {
        /**
         * Obtains the the tag value of a given card
         * sets the card value and suit depending on which drawable was used
         */
        Card toAdd = new Card(-1, "Default");
        int tagValue = (Integer) selectedCard.getTag();
        switch (tagValue) {
            case 0: //need to fix this case
                Toast.makeText(myActivity.getApplication().getApplicationContext(), "No card selected!",
                        Toast.LENGTH_SHORT).show();
                break;

            case R.drawable.two_clubs:
                toAdd.setCardVal(13);
                toAdd.setCardSuit("Clubs");
                break;

            case R.drawable.three_clubs:
                toAdd.setCardVal(1);
                toAdd.setCardSuit("Clubs");
                break;

            case R.drawable.four_clubs:
                toAdd.setCardVal(2);
                toAdd.setCardSuit("Clubs");
                break;

            case R.drawable.five_clubs:
                toAdd.setCardVal(3);
                toAdd.setCardSuit("Clubs");
                break;

            case R.drawable.six_clubs:
                toAdd.setCardVal(4);
                toAdd.setCardSuit("Clubs");
                break;

            case R.drawable.seven_clubs:
                toAdd.setCardVal(5);
                toAdd.setCardSuit("Clubs");
                break;

            case R.drawable.eight_clubs:
                toAdd.setCardVal(6);
                toAdd.setCardSuit("Clubs");
                break;

            case R.drawable.nine_clubs:
                toAdd.setCardVal(7);
                toAdd.setCardSuit("Clubs");
                break;

            case R.drawable.ten_clubs:
                toAdd.setCardVal(8);
                toAdd.setCardSuit("Clubs");
                break;

            case R.drawable.jack_clubs:
                toAdd.setCardVal(9);
                toAdd.setCardSuit("Clubs");
                break;

            case R.drawable.queen_clubs:
                toAdd.setCardVal(10);
                toAdd.setCardSuit("Clubs");

                break;

            case R.drawable.king_clubs:
                toAdd.setCardVal(11);
                toAdd.setCardSuit("Clubs");
                break;

            case R.drawable.ace_clubs:
                toAdd.setCardVal(12);
                toAdd.setCardSuit("Clubs");
                break;

            case R.drawable.two_spades:
                toAdd.setCardVal(13);
                toAdd.setCardSuit("Spades");
                break;

            case R.drawable.three_spades:
                toAdd.setCardVal(1);
                toAdd.setCardSuit("Spades");
                break;

            case R.drawable.four_spades:
                toAdd.setCardVal(2);
                toAdd.setCardSuit("Spades");
                break;

            case R.drawable.five_spades:
                toAdd.setCardVal(3);
                toAdd.setCardSuit("Spades");
                break;

            case R.drawable.six_spades:
                toAdd.setCardVal(4);
                toAdd.setCardSuit("Spades");
                break;

            case R.drawable.seven_spades:
                toAdd.setCardVal(5);
                toAdd.setCardSuit("Spades");
                break;

            case R.drawable.eight_spades:
                toAdd.setCardVal(6);
                toAdd.setCardSuit("Spades");
                break;

            case R.drawable.nine_spades:
                toAdd.setCardVal(7);
                toAdd.setCardSuit("Spades");
                break;

            case R.drawable.ten_spades:
                toAdd.setCardVal(8);
                toAdd.setCardSuit("Spades");
                break;

            case R.drawable.jack_spades:
                toAdd.setCardVal(9);
                toAdd.setCardSuit("Spades");
                break;

            case R.drawable.queen_spades:
                toAdd.setCardVal(10);
                toAdd.setCardSuit("Spades");
                break;

            case R.drawable.king_spades:
                toAdd.setCardVal(11);
                toAdd.setCardSuit("Spades");
                break;

            case R.drawable.ace_spades:
                toAdd.setCardVal(12);
                toAdd.setCardSuit("Spades");
                break;

            case R.drawable.two_diamonds:
                toAdd.setCardVal(13);
                toAdd.setCardSuit("Diamonds");
                break;

            case R.drawable.three_diamonds:
                toAdd.setCardVal(1);
                toAdd.setCardSuit("Diamonds");
                break;

            case R.drawable.four_diamonds:
                toAdd.setCardVal(2);
                toAdd.setCardSuit("Diamonds");
                break;

            case R.drawable.five_diamonds:
                toAdd.setCardVal(3);
                toAdd.setCardSuit("Diamonds");
                break;

            case R.drawable.six_diamonds:
                toAdd.setCardVal(4);
                toAdd.setCardSuit("Diamonds");
                break;

            case R.drawable.seven_diamonds:
                toAdd.setCardVal(5);
                toAdd.setCardSuit("Diamonds");
                break;

            case R.drawable.eight_diamonds:
                toAdd.setCardVal(6);
                toAdd.setCardSuit("Diamonds");
                break;

            case R.drawable.nine_diamonds:
                toAdd.setCardVal(7);
                toAdd.setCardSuit("Diamonds");
                break;

            case R.drawable.ten_diamonds:
                toAdd.setCardVal(8);
                toAdd.setCardSuit("Diamonds");
                break;

            case R.drawable.jack_diamonds:
                toAdd.setCardVal(9);
                toAdd.setCardSuit("Diamonds");
                break;

            case R.drawable.queen_diamonds:
                toAdd.setCardVal(10);
                toAdd.setCardSuit("Diamonds");
                break;

            case R.drawable.king_diamonds:
                toAdd.setCardVal(11);
                toAdd.setCardSuit("Diamonds");
                break;

            case R.drawable.ace_diamonds:
                toAdd.setCardVal(12);
                toAdd.setCardSuit("Diamonds");
                break;

            case R.drawable.two_hearts:
                toAdd.setCardVal(13);
                toAdd.setCardSuit("Hearts");
                break;

            case R.drawable.three_hearts:
                toAdd.setCardVal(1);
                toAdd.setCardSuit("Hearts");
                break;

            case R.drawable.four_hearts:
                toAdd.setCardVal(2);
                toAdd.setCardSuit("Hearts");
                break;

            case R.drawable.five_hearts:
                toAdd.setCardVal(3);
                toAdd.setCardSuit("Hearts");
                break;

            case R.drawable.six_hearts:
                toAdd.setCardVal(4);
                toAdd.setCardSuit("Hearts");
                break;

            case R.drawable.seven_hearts:
                toAdd.setCardVal(5);
                toAdd.setCardSuit("Hearts");
                break;

            case R.drawable.eight_hearts:
                toAdd.setCardVal(6);
                toAdd.setCardSuit("Hearts");
                break;

            case R.drawable.nine_hearts:
                toAdd.setCardVal(7);
                toAdd.setCardSuit("Hearts");
                break;

            case R.drawable.ten_hearts:
                toAdd.setCardVal(8);
                toAdd.setCardSuit("Hearts");
                break;

            case R.drawable.jack_hearts:
                toAdd.setCardVal(9);
                toAdd.setCardSuit("Hearts");
                break;

            case R.drawable.queen_hearts:
                toAdd.setCardVal(10);
                toAdd.setCardSuit("Hearts");
                break;

            case R.drawable.king_hearts:
                toAdd.setCardVal(11);
                toAdd.setCardSuit("Hearts");
                break;

            case R.drawable.ace_hearts:
                toAdd.setCardVal(12);
                toAdd.setCardSuit("Hearts");
                break;
        }
        return toAdd;
    }
}// class CounterHumanPlayer
