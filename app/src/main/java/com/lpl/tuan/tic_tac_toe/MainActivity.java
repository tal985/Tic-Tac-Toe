package com.lpl.tuan.tic_tac_toe;

import android.content.DialogInterface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.Toolbar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    private boolean modeMode = true, isX = true, end = false;
    private Button display, display2;
    private Button[][] buttons = new Button[3][3];
    //stage 0 = singleplayer, stage 2 = multiplayer
    private int stage = 0;
    private LinearLayout top, mid, bot;
    private SinglePlayerLogic spl = new SinglePlayerLogic();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        centerTitle();
        initObjects();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.Reset:
                resetGame();
                return true;
            case R.id.About:
                new AlertDialog.Builder(this).setMessage("The only winning move is not to play").show();
                return true;
            case R.id.Quit:
                finishAndRemoveTask();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v)
    {
        int tempID = v.getId();

        //Singleplayer
        if(tempID == R.id.Display && modeMode)
        {
            modeMode = false;

            goIntoGame();
            setUpFirstTurn();
        }
        //Multiplayer
        if(tempID == R.id.Display2 && modeMode)
        {
            modeMode = false;
            stage = 1;

            goIntoGame();
            display.setText("X's turn.");
        }
        //Grid buttons
        else if(!end)
            makeMove(tempID);
        //Reset
        else if(end && tempID == R.id.Display)
            resetSession();

    }

    //Centers title on action bar
    private void centerTitle()
    {
        ArrayList<View> textViews = new ArrayList<>();

        getWindow().getDecorView().findViewsWithText(textViews, getTitle(), View.FIND_VIEWS_WITH_TEXT);

        if(textViews.size() > 0)
        {
            AppCompatTextView appCompatTextView = null;
            if(textViews.size() == 1)
                appCompatTextView = (AppCompatTextView) textViews.get(0);
            else
            {
                for(View v : textViews)
                    if (v.getParent() instanceof Toolbar)
                    {
                        appCompatTextView = (AppCompatTextView) v;
                        break;
                    }
            }

            if(appCompatTextView != null)
            {
                ViewGroup.LayoutParams params = appCompatTextView.getLayoutParams();
                params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                appCompatTextView.setLayoutParams(params);
                appCompatTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            }
        }
    }

    //Hides display 2 and brings out the grid
    private void goIntoGame()
    {
        display2.setVisibility(View.GONE);
        top.setVisibility(View.VISIBLE);
        mid.setVisibility(View.VISIBLE);
        bot.setVisibility(View.VISIBLE);
    }

    //Sets up the overall first turn in single player
    private void setUpFirstTurn()
    {
        if(goFirst())
            display.setText("Player goes First as X!");
        else
        {
            display.setText("Player goes Second as O!");
            isX = false;
            spl.AIStart();
            //System.out.println(spl.lastPCMove);
            updateAISquares(spl.lastPCMove);
        }
    }

    //Make a move
    private void makeMove(int id)
    {
        Button temp = findViewById(id);

        if (temp.getText().equals(""))
        {
            //Singleplayer
            if(stage == 0)
            {
                //isX should never change states since the single player will always be X or O
                if(isX)
                {
                    display.setText("X's turn.");
                    temp.setText("X");
                }
                else
                {
                    display.setText("O's turn.");
                    temp.setText("O");
                }
                String tag = temp.getTag().toString();
                spl.run(tag.charAt(0) - '1', tag.charAt(1) - '1');
                updateAISquares(spl.lastPCMove);
                //System.out.println(spl.lastPCMove);

            }
            //Multiplayer
            else if(stage == 1)
            {
                if(isX)
                {
                    isX = false;
                    display.setText("O's turn.");
                    temp.setText("X");
                    //handleMP("X", temp.getTag().toString());
                }
                else
                {
                    isX = true;
                    display.setText("X's turn.");
                    temp.setText("O");
                    //handleMP("O", temp.getTag().toString());
                }
                String tag = temp.getTag().toString();
                spl.run2P(tag.charAt(0) - '1', tag.charAt(1) - '1');
            }
        }

        //Checks to see if the game is over
        if(spl.b.isGameOver() && stage == 0)
        {
            end = true;
            //System.out.println("THE END");

            if(spl.b.hasAIWon())
                display.setText("Computer Wins!\nClick here to reset");
            else if(spl.b.hasPlayerWon())
                display.setText("How did you even get this?\nClick here to reset");
            else
                display.setText("It's a draw!\nClick here to reset.");
        }
        else if(spl.b.isGameOver() && stage == 1)
        {
            end = true;
            //System.out.println("THE END");

            //AI = O and Player = X in multiplayer
            if(spl.b.hasAIWon())
                display.setText("O wins!\nClick here to reset.");
            else if(spl.b.hasPlayerWon())
                display.setText("X wins!\nClick here to reset");
            else
                display.setText("It's a draw!\nClick here to reset.");
        }
    }

    //Reset session, but not stage
    private void resetSession()
    {
        spl = new SinglePlayerLogic();
        isX = true;
        end = false;
        clearButtonGrid();

        if(stage == 0)
            setUpFirstTurn();
        else
            display.setText("X's turn.");
    }

    //Reset entire game
    private void resetGame()
    {
        spl = new SinglePlayerLogic();
        isX = true;
        end = false;
        stage = 0;
        modeMode = true;

        clearButtonGrid();
        display.setText("unbeatable singleplayer");
        display2.setVisibility(View.VISIBLE);
        top.setVisibility(View.GONE);
        mid.setVisibility(View.GONE);
        bot.setVisibility(View.GONE);
    }

    //Update the grid after the AI's moves
    private void updateAISquares(Point pos)
    {
        //If player is "X" then the computer has to be "O"
        if(isX)
            buttons[pos.x][pos.y].setText("O");
        else
            buttons[pos.x][pos.y].setText("X");
    }

    //Clear the grid
    private void clearButtonGrid()
    {
        buttons[0][0].setText("");
        buttons[0][1].setText("");
        buttons[0][2].setText("");
        buttons[1][0].setText("");
        buttons[1][1].setText("");
        buttons[1][2].setText("");
        buttons[2][0].setText("");
        buttons[2][1].setText("");
        buttons[2][2].setText("");
    }

    //Determine if player goes first or not
    private boolean goFirst()
    {
        if((int) (Math.random() * 2) == 1)
            return true;
        return false;
    }

    //Initialize views
    private void initObjects()
    {
        top = findViewById(R.id.Top);
        mid = findViewById(R.id.Mid);
        bot = findViewById(R.id.Bot);

        display = findViewById(R.id.Display);
        display2 = findViewById(R.id.Display2);

        buttons[0][0] = findViewById(R.id.g11);
        buttons[0][1] = findViewById(R.id.g12);
        buttons[0][2] = findViewById(R.id.g13);
        buttons[1][0] = findViewById(R.id.g21);
        buttons[1][1] = findViewById(R.id.g22);
        buttons[1][2] = findViewById(R.id.g23);
        buttons[2][0] = findViewById(R.id.g31);
        buttons[2][1] = findViewById(R.id.g32);
        buttons[2][2] = findViewById(R.id.g33);

        display.setOnClickListener(this);
        display2.setOnClickListener(this);

        buttons[0][0].setOnClickListener(this);
        buttons[0][1].setOnClickListener(this);
        buttons[0][2].setOnClickListener(this);
        buttons[1][0].setOnClickListener(this);
        buttons[1][1].setOnClickListener(this);
        buttons[1][2].setOnClickListener(this);
        buttons[2][0].setOnClickListener(this);
        buttons[2][1].setOnClickListener(this);
        buttons[2][2].setOnClickListener(this);
    }
}
