package ca.bcit.tictactoegame;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity
        extends AppCompatActivity implements View.OnClickListener
{

    private Button[][] button_array = new Button[3][3];
    private boolean is_player1_turn = true;
    private int num_of_round;
    private int p1_pts;
    private int p2_pts;
    private static final int TIE = 9;
    private TextView p1_textview;
    private TextView p2_textview;
    private Button reset;
    private static final int ZERO = 0;
    private static final int ONE = 1;
    private static final int TWO = 2;

    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        p1_textview = findViewById(R.id.player1_score);
        p2_textview = findViewById(R.id.player2_score);

        reset = findViewById(R.id.reset);

        //reset all elements of the game when reset button is pressed
        //PRE: none
        //POST: call rest_game method, which reset the game_board and player scores
        //RETURN none
        reset.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v)
            {
                reset_game();
            }
        });

        //Getting all button ids from activity_main.xml and store them into a 2D button array
        for(int i=0; i < button_array.length; ++i)
        {
            for(int j=0; j < button_array.length; ++j)
            {
                String button_id = "button_" + i + j;
                int id = getResources().getIdentifier(button_id, "id", getPackageName());
                button_array[i][j] = findViewById(id);
                button_array[i][j].setOnClickListener(this);
            }
        }
    }

    //Draw X or O depending on player's turn. Apply rules and keep checking if there's a winner yet
    //PRE: none
    //POST: Player1 is X, player2 is O. Calls checker method to see if there's a winner after current move
    //RETURN: none
    @Override
    public void onClick(View v) {
        if (!((Button) v).getText().toString().equals("")) {return;}

        if(is_player1_turn)
        {
            ((Button)v).setText("X");
            ((Button)v).setTextColor(Color.parseColor("#1605ff"));
        } else {
            ((Button)v).setText("O");
            ((Button)v).setTextColor(Color.parseColor("#ff0576"));
        }

        num_of_round++;

        if(checker())
        {
            if(is_player1_turn){
                player_wins(ONE);
            } else {
                player_wins(TWO);
            }

        } else if(num_of_round == TIE){
            draw();
        } else {
            is_player1_turn = !is_player1_turn;
        }
    }

    //Apply tic tac toe rules to see if there's a strike
    //PRE:none
    //POST: check check strike horizontally, vertically, cross (topleft->bottomright, topright ->bottomleft)
    //RETURN boolean. (true is there's winner)
    private boolean checker() {
        String[][] field = new String[3][3];

        //checking horizontally and be sure they are not empty
        for (int i = ZERO; i < field.length; ++i) {
            for (int j = ZERO; j < field.length; ++j) {
                field[i][j] = button_array[i][j].getText().toString();
            }
        }

        //checking horizontally and be sure they are not empty
        for (int i = ZERO; i < field.length; ++i) {

            if (field[i][ZERO].equals(field[i][ONE])
                    && field[i][ZERO].equals(field[i][TWO])
                    && !field[i][ZERO].equals(""))
                return true;

        }

        //checking vertically and be sure they are not empty
        for (int i = ZERO; i < field.length; ++i) {
            if (field[ZERO][i].equals(field[ONE][i])
                    && field[ZERO][i].equals(field[TWO][i])
                    && !field[ZERO][i].equals(""))
                return true;
        }

        //checking cross -top left to bottom right
        if (field[ZERO][ZERO].equals(field[ONE][ONE])
                && field[ZERO][ZERO].equals(field[TWO][TWO])
                && !field[ZERO][ZERO].equals(""))
                return true;

        //checking cross -top right to bottom left
        if (field[ZERO][TWO].equals(field[ONE][ONE])
                && field[ZERO][TWO].equals(field[TWO][ZERO])
                && !field[ZERO][TWO].equals(""))
            return true;

        return false;
    }

    //display winner player to user
    //PRE:none
    //POST:use Toast to display which player won the game
    //RETURN none
    private void player_wins(int player)
    {
        if(player == ONE){
            p1_pts++;
            Toast.makeText(this, "PLAYER 1 WINS!", Toast.LENGTH_SHORT).show();
            update_pts_text(ONE);
            reset_board();
        }

        if(player == TWO){
            p2_pts++;
            Toast.makeText(this, "PLAYER 2 WINS!", Toast.LENGTH_SHORT).show();
            update_pts_text(TWO);
            reset_board();
        }
    }

    //display a tie message to user
    //RRE:none
    //POST:display a tie message to both players after 9 moves
    //RETURN none
    private void draw()
    {
        Toast.makeText(this, "NO WINNER!!", Toast.LENGTH_SHORT).show();
        reset_game();
    }

    //updates user's new score
    //PRE:none
    //POST update player's score depending on which player in the parameter
    //RETURN none
    private void update_pts_text(int player)
    {
        if(player == ONE){
            p1_textview.setText("Score: " + p1_pts + "\n");
        }

        if(player == TWO){
            p2_textview.setText("Score: " + p2_pts + "\n");
        }
    }

    //reset the board when a game wins/ties
    //PRE: none
    //POST: reset the board when a game wins/ties but keep both players' score
    //RETURN none
    private void reset_board()
    {
        for(int i=ZERO; i < button_array.length; ++i){
            for(int j=ZERO; j < button_array.length; ++j){
                button_array[i][j].setText("");
            }
        }
        num_of_round = 0;
        is_player1_turn = true;
    }

    //reset the whole game
    //PRE:none
    //POST: reset everything including the board and players' score
    //RETURN none
    private void reset_game()
    {
        p1_pts = 0;
        p2_pts = 0;
        update_pts_text(ONE);
        update_pts_text(TWO);
        reset_board();
    }
}
