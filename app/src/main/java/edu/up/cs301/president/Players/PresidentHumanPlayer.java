package edu.up.cs301.president.Players;

import android.graphics.PorterDuff;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;

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
import edu.up.cs301.president.PresidentTradeAction;
// TODO switch highlight needs to be changed!
/**
 * A GUI of a Human Player. The GUI displays the player's hand, score and rank
 * of all players, the current set and allows the human player to press on their
 * cards, the pass, play, order, and leave game buttons in order to send moves
 * to the game
 *
 * @author Kama Simon
 * @author Geryl Vinoya
 * @author Ben Pirkl
 * @author Hera Malik
 * @version April 2019
 */
public class PresidentHumanPlayer extends GameHumanPlayer implements View.OnClickListener, Serializable {

    /* instance variables */

    // the most recent game state, as given to us by the PresidentLocalGame
    private PresidentState state;

    // the android activity that we are running
    private GameMainActivity myActivity;

    // textview of player's name
    private TextView youText, player1Text, player2Text, player3Text; // TODO: attach name from the config menu

    // textview of player's names on scoreboard
    private TextView youName, p1Name, p2Name, p3Name;

    // textview of player's score
    private TextView youScore, player1Score, player2Score, player3Score;

    // textview of player's rank
    private TextView youRank, player1Rank, player2Rank, player3Rank;

    // textview of the number of cards in each of the other player's hands
    private TextView cards_1, cards_2, cards_3; // shows rem. cards // TODO: might take this out?

    // buttons in GUI (except for pause button)
    private Button playButton, passButton, orderButton, leaveGameButton,
            rulesButton, returnRulesButton, tradeButton; // TODO: add in functionality of order and leaveGameButton
    private TextView tradeResponse;
    // ImageButton array of all the human player's cards
    private ImageButton[] playersCards = new ImageButton[13];

    // ImageButton of the current set (will display one card)
    private ImageButton[] currentSet = new ImageButton[4]; // TODO: need to add functionality of multiple cards

    // ImageButton of card(s) that player selects
    private ImageButton[] selectedCard; // TODO: need to add functionality of multiple cards

    // human players number (turn)
    private int turn; //TODO when I reach 5 cards left unable to select cards anymore ?? Apr 16 3:22
    /**
     * constructor
     *
     * @param name the player's name
     */
    public PresidentHumanPlayer(String name) {
        super(name);
    } // TODO we can use this to update name

    /**
     * Returns the GUI's top view object
     *
     * @return the top object in the GUI's view heirarchy
     */
    public View getTopView() {
        return myActivity.findViewById(R.id.in_game_gui);
    }

    /**
     * sets whose turn it is
     * changes selected card to scoreboard
     */
    private void updateDisplay() { // TODO: we should put all gui updates in here or have this method call other methods
        switch(this.playerNum){
            case 0:
                youText.setText(allPlayerNames[0]);
                player1Text.setText(allPlayerNames[1]);
                player2Text.setText(allPlayerNames[2]);
                player3Text.setText(allPlayerNames[3]);
                youName.setText(allPlayerNames[0]);
                p1Name.setText(allPlayerNames[1]);
                p2Name.setText(allPlayerNames[2]);
                p3Name.setText(allPlayerNames[3]);
                cards_1.setText("" + state.getPlayers().get(1).getHand().size());
                cards_2.setText("" + state.getPlayers().get(2).getHand().size());
                cards_3.setText("" + state.getPlayers().get(3).getHand().size());
                youRank.setText("" + state.getPlayers().get(0).getStringRank());
                player1Rank.setText("" + state.getPlayers().get(1).getStringRank());
                player2Rank.setText("" + state.getPlayers().get(2).getStringRank());
                player3Rank.setText("" + state.getPlayers().get(3).getStringRank());
                youScore.setText("" + state.getPlayers().get(0).getScore());
                player1Score.setText("" + state.getPlayers().get(1).getScore());
                player2Score.setText("" + state.getPlayers().get(2).getScore());
                player3Score.setText("" + state.getPlayers().get(3).getScore());
                break;
            case 1:
                youText.setText(allPlayerNames[1]);
                player1Text.setText(allPlayerNames[2]);
                player2Text.setText(allPlayerNames[3]);
                player3Text.setText(allPlayerNames[0]);
                youName.setText(allPlayerNames[1]);
                p1Name.setText(allPlayerNames[2]);
                p2Name.setText(allPlayerNames[3]);
                p3Name.setText(allPlayerNames[0]);
                cards_1.setText("" + state.getPlayers().get(2).getHand().size());
                cards_2.setText("" + state.getPlayers().get(3).getHand().size());
                cards_3.setText("" + state.getPlayers().get(0).getHand().size());
                youRank.setText("" + state.getPlayers().get(1).getStringRank());
                player1Rank.setText("" + state.getPlayers().get(2).getStringRank());
                player2Rank.setText("" + state.getPlayers().get(3).getStringRank());
                player3Rank.setText("" + state.getPlayers().get(0).getStringRank());
                youScore.setText("" + state.getPlayers().get(1).getScore());
                player1Score.setText("" + state.getPlayers().get(2).getScore());
                player2Score.setText("" + state.getPlayers().get(3).getScore());
                player3Score.setText("" + state.getPlayers().get(0).getScore());
                break;
            case 2:
                youText.setText(allPlayerNames[2]);
                player1Text.setText(allPlayerNames[3]);
                player2Text.setText(allPlayerNames[0]);
                player3Text.setText(allPlayerNames[1]);
                youName.setText(allPlayerNames[2]);
                p1Name.setText(allPlayerNames[3]);
                p2Name.setText(allPlayerNames[0]);
                p3Name.setText(allPlayerNames[1]);
                cards_1.setText("" + state.getPlayers().get(3).getHand().size());
                cards_2.setText("" + state.getPlayers().get(0).getHand().size());
                cards_3.setText("" + state.getPlayers().get(1).getHand().size());
                youRank.setText("" + state.getPlayers().get(2).getStringRank());
                player1Rank.setText("" + state.getPlayers().get(3).getStringRank());
                player2Rank.setText("" + state.getPlayers().get(0).getStringRank());
                player3Rank.setText("" + state.getPlayers().get(1).getStringRank());
                youScore.setText("" + state.getPlayers().get(2).getScore());
                player1Score.setText("" + state.getPlayers().get(3).getScore());
                player2Score.setText("" + state.getPlayers().get(0).getScore());
                player3Score.setText("" + state.getPlayers().get(1).getScore());
                break;
            case 3:
                youText.setText(allPlayerNames[3]);
                player1Text.setText(allPlayerNames[0]);
                player2Text.setText(allPlayerNames[1]);
                player3Text.setText(allPlayerNames[2]);
                youName.setText(allPlayerNames[3]);
                p1Name.setText(allPlayerNames[0]);
                p2Name.setText(allPlayerNames[1]);
                p3Name.setText(allPlayerNames[2]);
                cards_1.setText("" + state.getPlayers().get(0).getHand().size());
                cards_2.setText("" + state.getPlayers().get(1).getHand().size());
                cards_3.setText("" + state.getPlayers().get(2).getHand().size());
                youRank.setText("" + state.getPlayers().get(3).getStringRank());
                player1Rank.setText("" + state.getPlayers().get(0).getStringRank());
                player2Rank.setText("" + state.getPlayers().get(1).getStringRank());
                player3Rank.setText("" + state.getPlayers().get(2).getStringRank());
                youScore.setText("" + state.getPlayers().get(3).getScore());
                player1Score.setText("" + state.getPlayers().get(0).getScore());
                player2Score.setText("" + state.getPlayers().get(1).getScore());
                player3Score.setText("" + state.getPlayers().get(2).getScore());
                break;
        }
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
        if(state.getCurrentSet().size() == 0){
            for(int i = 0; i < 4; i++) {
                currentSet[i].setBackgroundResource(0);
            }
        }
        if(state.getCurrentSet().size() != 0 ){
            for(int i = 0; i < state.getCurrentSet().size(); i++) {
                int id = getImageId(state.getCurrentSet().get(i));
                currentSet[i].setTag(id); // TODO: It was recommended I change this, so I did. Don't know about functionality
                currentSet[i].setBackgroundResource(id);
            }
        }
        if(!state.getRoundStart()){
            tradeResponse.setText("");
            tradeResponse.setBackgroundResource(0);
        }
        else{
            tradeResponse.setText("Trading In Progress");
            tradeResponse.setTextColor(myActivity.getResources().getColor(R.color.black));
            tradeResponse.setBackgroundResource(R.color.white);
        }
    }

    /**
     * this method gets called when the user clicks the pass, play, or order button. It
     * creates a new PresidentOrderAction, PresidentPassAction or PresidentPlayAction
     * to return to the parent activity.
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
            for(int i = 0; i < selectedCard.length; i++) {
                if(selectedCard[i] != null) {
                    temp.add(getGUICard(i));
                    selectedCard[i].getBackground().clearColorFilter();
                    selectedCard[i] = null;
                }
            }
                action = new PresidentPlayAction(this, temp);
        } else if(button.getId() == R.id.tradeButton) {
            // play button: player will put down cards
            if(selectedCard == null){
                Toast.makeText(this.myActivity, "No Card Selected", Toast.LENGTH_SHORT).show();
                return;
            }
            ArrayList<Card> temp = new ArrayList<Card>();
            for(int i = 0; i < selectedCard.length; i++) {
                if(selectedCard[i] != null) {
                    temp.add(getGUICard(i));
                    selectedCard[i].getBackground().clearColorFilter();
                    selectedCard[i] = null;
                }
            }
            action = new PresidentTradeAction(this, temp);

        } else if (button.getId() == R.id.passButton) {
            selectedCard = null;
            action = new PresidentPassAction(this);

        } else if (button.getId() == R.id.orderButton) {

            action = new PresidentOrderAction(this);

        } else if (button.getId() == R.id.leaveGame){
            state.getPlayers().get(turn).setLeaveGame(1);
            myActivity.finish();
            System.exit(0);

            /**
             * External Citation
             * Date: April 17 2019
             * Problem: Did not know how to exit out of game
             * Resource: https://stackoverflow.com/questions/17719634/how-to-exit-an-android-app-programmatically
             * Solution: Example code from post
             */
        } else if(button.getId() == R.id.returnRulesButton){
            this.myActivity.setContentView(R.layout.in_game_layout);
            this.setAsGui(myActivity);

        } else if(button.getId() == R.id.rulesButton){

            this.myActivity.setContentView(R.layout.rules_tab);
            this.returnRulesButton = myActivity.findViewById(R.id.returnRulesButton);
            returnRulesButton.setOnClickListener(this);

        }
        else {
            // something else was pressed: ignore
            return;
        }
        if(action != null){
            game.sendAction(action); // send action to the game
        }
    }// onClick

    /**
     * callback method when we get a message (e.g., from the game)
     *
     * @param info the message
     */
    @Override
    public void receiveInfo(GameInfo info) {
        if (info instanceof PresidentState) {
            // we do not want to update if it is the same state
            if (state != null) {
                if (state.equals(info)) {
                    return;
                }
            }
            state = (PresidentState) info;
            updateDisplay();
            updatePlayerGui(); // TODO possibly add this to updateDisplay() instead of receiveInfo
        } else if (info instanceof NotYourTurnInfo) {
            // if we had an out-of-turn or illegal move, flash the screen
            Toast.makeText(this.myActivity, "Not your turn!", Toast.LENGTH_SHORT).show();
        }
        else if (info instanceof IllegalMoveInfo){
            Toast.makeText(this.myActivity, "Illegal Move!", Toast.LENGTH_SHORT).show();

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

        turn = this.playerNum;
        // Load the layout resource for our GUI
        activity.setContentView(R.layout.in_game_layout);

        // adding the ImageButton to array
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
        currentSet[0] = activity.findViewById(R.id.current0);
        currentSet[1] = activity.findViewById(R.id.current1);
        currentSet[2] = activity.findViewById(R.id.current2);
        currentSet[3] = activity.findViewById(R.id.current3);
        for(int i = 0; i < 4; i++){
            currentSet[i].setBackgroundResource(0);
        }
        // create a Card Click Listener
        for (int i = 0; i < 13; i++) {
            playersCards[i].setBackgroundResource(0);
            playersCards[i].setOnClickListener(new CardClickListener());
        }
        // player's name
        youText = activity.findViewById(R.id.userPlayer);
        player1Text = activity.findViewById(R.id.player1Text);
        player2Text = activity.findViewById(R.id.player2Text);
        player3Text = activity.findViewById(R.id.Player3Text);
        youName = activity.findViewById(R.id.youText);
        p1Name = activity.findViewById(R.id.player1NameText);
        p2Name = activity.findViewById(R.id.player2NameText);
        p3Name = activity.findViewById(R.id.player3NameText);
        selectedCard = new ImageButton[4];

        // player's score
        youScore = activity.findViewById(R.id.youScore);
        player1Score = activity.findViewById(R.id.player1Score);
        player2Score = activity.findViewById(R.id.player2Score);
        player3Score = activity.findViewById(R.id.player3Score);

        // player's rank
        youRank = activity.findViewById(R.id.youRank);
        player1Rank = activity.findViewById(R.id.player1Rank);
        player2Rank = activity.findViewById(R.id.player2Rank);
        player3Rank = activity.findViewById(R.id.player3Rank);

        // player's remaining cards except for human player
        cards_1 = activity.findViewById(R.id.p1);
        cards_2 = activity.findViewById(R.id.p2);
        cards_3 = activity.findViewById(R.id.p3);

        // button listener
        playButton = activity.findViewById(R.id.playButton);
        playButton.setOnClickListener(this);
        tradeButton = activity.findViewById(R.id.tradeButton);
        tradeButton.setOnClickListener(this);
        passButton = activity.findViewById(R.id.passButton);
        passButton.setOnClickListener(this);
        orderButton = activity.findViewById(R.id.orderButton);
        orderButton.setOnClickListener(this);
        leaveGameButton = activity.findViewById(R.id.leaveGame);
        leaveGameButton.setOnClickListener(this);
        tradeResponse = activity.findViewById(R.id.tradeResponse);
        rulesButton = activity.findViewById(R.id.rulesButton);
        rulesButton.setOnClickListener(this);

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
    /**
     External Citation:
     Date:    1 April 2019
     Problem: Forgot how to add an OnClickListener for Multiple Buttons

     Resource:
     https://www.youtube.com/watch?v=GtxVILjLcw8
     Solution: I used the example code from the video and previous recollection
     from past assignments

     */

    /**
     External Citation:
     Date:    1 April 2019
     Problem: Difficulty changing background of Textview (wanted to show whose
     turn it was)
     Resource: https://stackoverflow.com/questions/1466788/android-textview-
     setting-the-background-color-dynamically-doesnt-work
     Solution: I used the example code from this post
     https://stackoverflow.com/questions/5327553/android-highlight-an-imagebutton-when-clicked
     */

    public class CardClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            // selected card will have color filter
            int count = 0;
            if(selectedCard != null) {
                for (int i = 0; i < 4; i++) {
                    if (selectedCard[i] == null) { // TODO null pointer array error ?
                        count = i;
                        break;
                    } else if (selectedCard[i].getId() == v.getId()) {
                        selectedCard[i].getBackground().clearColorFilter();
                        selectedCard[i] = null;
                        return;
                    }
                }
            }
            else{
                selectedCard = new ImageButton[4];
                selectedCard[count] = (ImageButton) v;
                selectedCard[count].getBackground().setColorFilter(0x77000000,
                        PorterDuff.Mode.SRC_ATOP);
                v.invalidate();
                return;
            }
            selectedCard[count] = (ImageButton) v;
            selectedCard[count].getBackground().setColorFilter(0x77000000,
                    PorterDuff.Mode.SRC_ATOP);
            v.invalidate();

        }
    }
    /**
     External Citation:
     Date:    1 April 2019
     Problem: Needed to set Color Filter for cards when player selects

     Resource:
     https://stackoverflow.com/questions/5327553/android-highlight-an
     -imagebutton-when-clicked
     Solution: Used part of the code from this post
     */

    /**
     External Citation:
     Date:  17 April 2019
     Problem: Null pointer bug

     Resource:
     https://stackoverflow.com/questions/36953702/java-lang-nullpoin
     terexception-attempt-to-read-from-null-array
     Solution: accessing selectedCard array without it being not null
     Checked if selectedCard array is null before accessing array
     */

    /**
     * updateCardGui
     * update the GUI of a card given which card to update
     *
     * @return void
     */
    private void updateCardGui(int i) {
        Card theCard = state.getPlayers().get(this.playerNum).getHand().get(i);
        int imageId = getImageId(theCard);
        playersCards[i].setTag(imageId);
        playersCards[i].setBackgroundResource(imageId);
    }

    /**
     * updatePlayerGui
     * Update's the user's GUI
     *
     * @return void
     */
    private void updatePlayerGui() { // updates the player's hand
        int i = 0;
        for (int j = 0; j < 13; j++) {
            playersCards[j].setBackgroundResource(R.drawable.scoreboard);
        }
        for (Card c : state.getPlayers().get(this.playerNum).getHand()) {
            updateCardGui(i);
            playersCards[i].getBackground().setAlpha(255);
            playersCards[i].invalidate();
            i++;
        }
    }

    /**
     * getImageId
     * Cases to find which image ID to use for a player's set of cards
     *
     * @return the ID of the card image
     */
    private int getImageId(Card theCard) { // grabs Image Button ID
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
     * switchHighlight
     * <p>
     * highlights a player on the GUI if it is their turn
     *
     * @return void
     */
    private void switchHighlight(int idx) {
        player3Text.setTextColor(myActivity.getResources().getColor(R.color.white));
        player3Text.setBackgroundResource(R.color.black);
        youText.setBackgroundResource(R.color.black);
        youText.setTextColor(myActivity.getResources().getColor(R.color.white));
        player1Text.setBackgroundResource(R.color.black);
        player1Text.setTextColor(myActivity.getResources().getColor(R.color.white));
        player2Text.setBackgroundResource(R.color.black);
        player2Text.setTextColor(myActivity.getResources().getColor(R.color.white));
        switch (this.playerNum) {
            case 0:
                switch (idx){
                    case 0: // switch from 3 to 0
                        youText.setBackgroundResource(R.color.yellow);
                        youText.setTextColor(myActivity.getResources().getColor(R.color.black));
                        break;
                    case 1: // switch from idx 0 to 1
                        player1Text.setBackgroundResource(R.color.yellow);
                        player1Text.setTextColor(myActivity.getResources().getColor(R.color.black));
                        break;
                    case 2: // switch from idx 1 to 2 turn
                        player2Text.setBackgroundResource(R.color.yellow);
                        player2Text.setTextColor(myActivity.getResources().getColor(R.color.black));
                        break;
                    case 3: // switch from idx 2 to 3 turn
                        player3Text.setBackgroundResource(R.color.yellow);
                        player3Text.setTextColor(myActivity.getResources().getColor(R.color.black));
                        break;

                }
                break;
            case 1:
                switch (idx){
                    case 0: // switch from idx 3 to 0
                        player3Text.setBackgroundResource(R.color.yellow);
                        player3Text.setTextColor(myActivity.getResources().getColor(R.color.black));
                        break;
                    case 1: // switch from idx 0 to 1
                        youText.setBackgroundResource(R.color.yellow);
                        youText.setTextColor(myActivity.getResources().getColor(R.color.black));
                        break;
                    case 2: // switch from idx 1 to 2 turn
                        player1Text.setBackgroundResource(R.color.yellow);
                        player1Text.setTextColor(myActivity.getResources().getColor(R.color.black));
                        break;
                    case 3: // switch from idx 2 to 3 turn
                        player2Text.setBackgroundResource(R.color.yellow);
                        player2Text.setTextColor(myActivity.getResources().getColor(R.color.black));
                        break;

                }
                break;
            case 2:
                switch (idx){
                    case 0: // switch from 3 to 0
                        player2Text.setBackgroundResource(R.color.yellow);
                        player2Text.setTextColor(myActivity.getResources().getColor(R.color.black));
                        break;
                    case 1: // switch from idx 0 to 1
                        player3Text.setBackgroundResource(R.color.yellow);
                        player3Text.setTextColor(myActivity.getResources().getColor(R.color.black));
                        break;
                    case 2: // switch from idx 1 to 2 turn
                        youText.setBackgroundResource(R.color.yellow);
                        youText.setTextColor(myActivity.getResources().getColor(R.color.black));
                        break;
                    case 3: // switch from idx 2 to 3 turn
                        player1Text.setBackgroundResource(R.color.yellow);
                        player1Text.setTextColor(myActivity.getResources().getColor(R.color.black));
                        break;

                }
                break;
            case 3:
                switch (idx){
                    case 0: // switch from 3 to 0
                        player1Text.setBackgroundResource(R.color.yellow);
                        player1Text.setTextColor(myActivity.getResources().getColor(R.color.black));
                        break;
                    case 1: // switch from idx 0 to 1
                        player2Text.setBackgroundResource(R.color.yellow);
                        player2Text.setTextColor(myActivity.getResources().getColor(R.color.black));
                        break;
                    case 2: // switch from idx 1 to 2 turn
                        player3Text.setBackgroundResource(R.color.yellow);
                        player3Text.setTextColor(myActivity.getResources().getColor(R.color.black));
                        break;
                    case 3: // switch from idx 2 to 3 turn
                        youText.setBackgroundResource(R.color.yellow);
                        youText.setTextColor(myActivity.getResources().getColor(R.color.black));
                        break;

                }
                break;
        }
        player3Text.invalidate();
        youText.invalidate();
        player1Text.invalidate();
        player2Text.invalidate();
    }
    /**
     * getGUICard
     * <p>
     * Cases to find which ImageButton was selected and what it represents
     *
     * @return the Card that represents the ImageID
     */
    private Card getGUICard(int idx) {
        /*
         * Obtains the the tag value of a given card
         * sets the card value and suit depending on which drawable was used
         */
        Card toAdd = new Card(-1, "Default");
        int tagValue = (Integer) selectedCard[idx].getTag();
        switch (tagValue) {
            case 0: //TODO need to fix this case (this was here, don't know if we need to fix it or we could remove)
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
}// class PresidentHumanPlayer
