package com.lpl.tuan.tic_tac_toe;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//Modified from code by Jatin Thakur on codebytes.in

public class SinglePlayerLogic
{
    public Board b = new Board();
    public Random rand = new Random();
    public Point lastPCMove = null;
    public boolean isX = true;

    //Most likely empty. Use this to run any code at initialization
    public SinglePlayerLogic()
    {

    }

    public void AIStart()
    {
        Point p = new Point(rand.nextInt(3), rand.nextInt(3));
        b.placeAMove(p, 1);
        //b.displayBoard();
        lastPCMove = p;
    }

    public void run(int x, int y)
    {
        //Run one iteration
        if(!b.isGameOver())
        {
            Point userMove = new Point(x, y);
            b.placeAMove(userMove, 2); //2 for O and O is the user

            if(b.isGameOver())
                return;

            b.minimax(0, 1);
            b.placeAMove(b.computersMove, 1);
            //b.displayBoard();
            lastPCMove = b.computersMove;
        }
    }

    public void run2P(int x, int y)
    {
        if(!b.isGameOver())
        {
            Point userMove = new Point(x, y);
            if(isX)
            {
                isX = false;
                b.placeAMove(userMove, 2);
            }
            else
            {
                isX = true;
                b.placeAMove(userMove, 1);
            }
            //b.displayBoard();
        }
    }
}

class Point
{
    int x, y;

    Point(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString()
    {
        return "[" + x + ", " + y + "]";
    }
}

class Board
{

    private List<Point> availablePoints;
    //Scanner scan = new Scanner(System.in);
    private int[][] board = new int[3][3];

    boolean isGameOver()
    {
        //Game is over is someone has won, or board is full (draw)
        if(hasAIWon() || hasPlayerWon() || getAvailableStates().isEmpty())
            return true;
        return false;
    }

    boolean hasAIWon()
    {
        //Check for diagonal
        if(board[0][0] == board[1][1] && board[0][0] == board[2][2] && board[0][0] == 1)
            return true;
        if(board[0][2] == board[1][1] && board[0][2] == board[2][0] && board[0][2] == 1)
            return true;

        //Check for horizontal and vertical
        for (int i = 0; i < 3; ++i)
        {
            if(board[i][0] == board[i][1] && board[i][0] == board[i][2] && board[i][0] == 1)
                return true;
            if(board[0][i] == board[1][i] && board[0][i] == board[2][i] && board[0][i] == 1)
                return true;
        }
        return false;
    }

    boolean hasPlayerWon()
    {
        //Check for diagonal
        if(board[0][0] == board[1][1] && board[0][0] == board[2][2] && board[0][0] == 2)
            return true;
        if(board[0][2] == board[1][1] && board[0][2] == board[2][0] && board[0][2] == 2)
            return true;

        //Check for horizontal and vertical
        for (int i = 0; i < 3; ++i)
        {
            if(board[i][0] == board[i][1] && board[i][0] == board[i][2] && board[i][0] == 2)
                return true;
            if(board[0][i] == board[1][i] && board[0][i] == board[2][i] && board[0][i] == 2)
                return true;
        }

        return false;
    }

    //Check the available spots and determine their worth
    public List<Point> getAvailableStates()
    {
        availablePoints = new ArrayList<>();

        for (int i = 0; i < 3; ++i)
            for (int j = 0; j < 3; ++j)
                if (board[i][j] == 0)
                    availablePoints.add(new Point(i, j));

        return availablePoints;
    }

    void placeAMove(Point point, int player)
    {
        board[point.x][point.y] = player;   //player = 1 for X, 2 for O
    }

    //Unnecessary for android app unless troubleshooting
    void displayBoard()
    {
        System.out.println("ANOTHER TURN\n");

        for(int[] i : board)
        {
            for(int j: i)
                System.out.print(j + " ");
            System.out.println();
        }

    }

    Point computersMove;

    int minimax(int depth, int turn)
    {
        if(hasAIWon())
            return +1;
        if(hasPlayerWon())
            return -1;

        List<Point> pointsAvailable = getAvailableStates();
        if (pointsAvailable.isEmpty()) return 0;

        int min = Integer.MAX_VALUE, max = Integer.MIN_VALUE;

        for (int i = 0; i < pointsAvailable.size(); ++i)
        {
            Point point = pointsAvailable.get(i);
            if (turn == 1)
            {
                placeAMove(point, 1);
                int currentScore = minimax(depth + 1, 2);
                max = Math.max(currentScore, max);


                //if(depth == 0)System.out.println("Score for position "+(i+1)+" = "+currentScore);
                if(currentScore >= 0){ if(depth == 0) computersMove = point;}
                if(currentScore == 1){board[point.x][point.y] = 0; break;}
                if(i == pointsAvailable.size()-1 && max < 0){if(depth == 0)computersMove = point;}
            }
            else if (turn == 2)
            {
                placeAMove(point, 2);
                int currentScore = minimax(depth + 1, 1);
                min = Math.min(currentScore, min);
                if(min == -1){board[point.x][point.y] = 0; break;}
            }
            board[point.x][point.y] = 0;
        }
        return turn == 1?max:min;
    }
}
