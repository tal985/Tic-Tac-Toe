package com.lpl.tuan.tic_tac_toe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    private boolean modeMode = true, isX = true, end = false;
    private Button display, display2;
    private int count = 1;
    private String firstMove, winner;
    private LinearLayout top, mid, bot;
    private String[][] ttt = new String[3][3];

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initObjects();
    }


    @Override
    public void onClick(View v)
    {
        int tempID = v.getId();

        //Singleplayer
        if(tempID == R.id.Display && modeMode)
        {
            modeMode = false;

            top.setVisibility(View.VISIBLE);
            mid.setVisibility(View.VISIBLE);
            bot.setVisibility(View.VISIBLE);
            display2.setVisibility(View.GONE);

            if(goFirst())
            {
                display.setText("Player goes First as X!");
            }
            else
            {
                display.setText("Player goes Second as O!");
            }
        }
        //Multiplayer
        if(tempID == R.id.Display2 && modeMode)
        {
            modeMode = false;

            display2.setVisibility(View.GONE);
            top.setVisibility(View.VISIBLE);
            mid.setVisibility(View.VISIBLE);
            bot.setVisibility(View.VISIBLE);
            display.setText("X's turn.");
        }
        //Buttons
        else if(!end)
            makeMove(tempID);
        else if(end && tempID == R.id.Display)
        {
            //Reset the game
            end = false;
            ttt = new String[3][3];
            count = 1;
            winner = null;
            clearButtonGrid();
            isX = true;
            display.setText("X's turn.");
        }
    }

    private void clearButtonGrid()
    {
        Button g11 = findViewById(R.id.g11);
        Button g12 = findViewById(R.id.g12);
        Button g13 = findViewById(R.id.g13);
        Button g21 = findViewById(R.id.g21);
        Button g22 = findViewById(R.id.g22);
        Button g23 = findViewById(R.id.g23);
        Button g31 = findViewById(R.id.g31);
        Button g32 = findViewById(R.id.g32);
        Button g33 = findViewById(R.id.g33);

        g11.setText("");
        g12.setText("");
        g13.setText("");
        g21.setText("");
        g22.setText("");
        g23.setText("");
        g31.setText("");
        g32.setText("");
        g33.setText("");
    }

    private void makeMove(int id)
    {
        Button temp = findViewById(id);

        if (temp.getText().equals(""))
        {
            if (isX)
            {
                isX = false;
                display.setText("O's turn.");
                temp.setText("X");
                handleGamestate("X", temp.getTag().toString());
            } else {
                isX = true;
                display.setText("X's turn.");
                temp.setText("O");
                handleGamestate("O", temp.getTag().toString());
            }
        }

    }

    private boolean goFirst()
    {
        if((int) (Math.random() * 2) == 1)
            return true;
        return false;
    }

    private void initObjects()
    {
        top = findViewById(R.id.Top);
        mid = findViewById(R.id.Mid);
        bot = findViewById(R.id.Bot);

        display = findViewById(R.id.Display);
        display2 = findViewById(R.id.Display2);

        Button g11 = findViewById(R.id.g11);
        Button g12 = findViewById(R.id.g12);
        Button g13 = findViewById(R.id.g13);
        Button g21 = findViewById(R.id.g21);
        Button g22 = findViewById(R.id.g22);
        Button g23 = findViewById(R.id.g23);
        Button g31 = findViewById(R.id.g31);
        Button g32 = findViewById(R.id.g32);
        Button g33 = findViewById(R.id.g33);

        display.setOnClickListener(this);
        display2.setOnClickListener(this);
        g11.setOnClickListener(this);
        g12.setOnClickListener(this);
        g13.setOnClickListener(this);
        g21.setOnClickListener(this);
        g22.setOnClickListener(this);
        g23.setOnClickListener(this);
        g31.setOnClickListener(this);
        g32.setOnClickListener(this);
        g33.setOnClickListener(this);
    }

    //Add "X" or "O" and checks for win
    private void handleGamestate(String letter, String pos) {
        ttt[pos.charAt(0) - '1'][pos.charAt(1) - '1'] = letter;

        //Check to see if there's a winner
        if (winner == null) {
            //Left to right down diagonal
            if (ttt[0][0] != null && ttt[0][0].equals(ttt[1][1]) && ttt[0][0].equals(ttt[2][2]))
                winner = ttt[0][0];
            //Left to right up diagonal
            if (ttt[2][0] != null && ttt[2][0].equals(ttt[1][1]) && ttt[2][0].equals(ttt[0][2]))
                winner = ttt[2][0];

            //straight line, horizontal and vertical
            for (int i = 0; i < 3; i++) {
                if (ttt[i][0] != null && ttt[i][0].equals(ttt[i][1]) && ttt[i][0].equals(ttt[i][2])) {
                    winner = ttt[i][0];
                    break;
                } else if (ttt[0][i] != null && ttt[0][i].equals(ttt[1][i]) && ttt[0][i].equals(ttt[2][i])) {
                    winner = ttt[0][i];
                    break;
                }
            }
        }

        //If there's already a winner, exit this code
        if (winner != null)
        {
            end = true;
            winner += " has won!\nClick here to reset.";
            display.setText(winner);
            return;
        }

        //Check to see if there are any valid spaces to move left
        boolean noSpace = true;
        /*
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (ttt[i][j] == null)
                    noSpace = false;
        */

        for(String[] s : ttt)
            for(String t: s)
                if(t == null)
                    noSpace = false;

        //If no space, end the game.
        if (noSpace)
        {
            end = true;
            display.setText("It's a draw!\nClick here to reset.");
        }
    }
}
