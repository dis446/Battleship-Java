package com.TG_CODE;

//import java.lang.Thread; //Use this for Thread.sleep for stub testing infinite loops.

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        System.out.println("Welcome to My BattleShip Game!!!");
        System.out.println("");
        while (true) {
            int height = getInt("How many rows would you like to play with?", 1, 10);
            //height = Number of rows.
            int width = getInt("How many columns would you like to play with?", 1, 10);
            //width = Number of columns.
            int maxShips = height * width;

            //Ensure that the player doesn't choose to play with impossible number of ships.
            int shipCount = getInt("How many ships would you like to play with?", 1, maxShips);

            Game(height, width, shipCount);
            String again = input("Do you want to play again? (Y/N)");
            if (again.charAt(0) != 'y') {
                break;
            }
        }
    }

    private static void Game(int height, int width, int shipCount) {

        int playerShipsActive = shipCount;
        int botShipsActive = shipCount;

        //Initialize player and bot's boards.
        char[][] playerBoard = initBoard(height, width);
        System.out.println("Here's your board: ");
        printBoard(playerBoard);

        char[][] botBoard = initBoard(height, width);
        System.out.println("");
        System.out.println("Here's the bot's board: ");
        printBoard(botBoard);

        int noPlaced = 0; //Number of ships placed. Used by both bot and player.

        //Set bot's pieces.
        ArrayList<Integer> botPlacedRows = new ArrayList<>();
        ArrayList<Integer> botPlacedCols = new ArrayList<>();

        while (noPlaced < shipCount) {
            int row = randInt(1, height);
            int col = randInt(1, width);

            boolean alreadyPlaced = false;
            for (int a = 0; a < botPlacedRows.size(); a++) {
                if (botPlacedRows.get(a).equals(row) && botPlacedCols.get(a).equals(col)) {
                    alreadyPlaced = true;
                    break;
                }
            }
            if (!alreadyPlaced) {
                //Need to keep bot's ships hidden from the player. For obvious reasons.
                //botBoard = setPiece(botBoard, row, col, 'S');
                botPlacedRows.add(row);
                botPlacedCols.add(col);
                noPlaced++;
            }
        }

        //Place player's ships
        ArrayList<Integer> playerPlacedRows = new ArrayList<>();
        ArrayList<Integer> playerPlacedCols = new ArrayList<>();

        noPlaced = 0;
        while (noPlaced < shipCount) {
            int row;
            int col;
            row = getInt("Choose a row to place your ship: ", 1, height);
            col = getInt("Choose a column to place your ship: ", 1, width);

            boolean alreadyPlaced = false;
            for (int a = 0; a < playerPlacedRows.size(); a++) {
                if (playerPlacedRows.get(a).equals(row) && playerPlacedCols.get(a).equals(col)) {
                    System.out.println("You've already placed a ship there!");
                    alreadyPlaced = true;
                    break;
                }
            }
            if (!alreadyPlaced) {
                playerBoard = setPiece(playerBoard, row, col, 'S');
                playerPlacedRows.add(row);
                playerPlacedCols.add(col);
                noPlaced++;
            }
            printBoard(playerBoard);
        }

        //Use ArrayList type to store rows and columns fired upon as the size of these Array's are indefinite.
        ArrayList<Integer> botFiredRows = new ArrayList<>();
        ArrayList<Integer> botFiredCols = new ArrayList<>();
        ArrayList<Integer> playerFiredRows = new ArrayList<>();
        ArrayList<Integer> playerFiredCols = new ArrayList<>();

        while (playerShipsActive != 0 && botShipsActive != 0) { //Main Game loop.
            System.out.println("Here's the bot's board: ");
            printBoard(botBoard);
            System.out.println("");

            System.out.println("Here's your board: ");
            printBoard(playerBoard);
            System.out.println("");

            int[] playerFire; //playerfire[0] is the row. playerfire[1] is the column.

            while(true){
                playerFire = playerFire(height, width);
                boolean alreadyFired = false;
                //Thread.sleep(1000);
                for (int a = 0; a < playerFiredRows.size(); a++){
                    //Loop through playerFiredRows and playerFiredColumns to see if player has already fired there.
                    //System.out.println("Player has previously fired at");
                    //System.out.println(playerFiredRows.get(a));
                    //System.out.println(playerFiredCols.get(a));
                    //Thread.sleep(1000);
                    if (((playerFiredRows.get(a)).equals(playerFire[0])) && ((playerFiredCols.get(a)).equals(playerFire[1]))){
                        System.out.println("You've already fired upon that location");
                        alreadyFired = true;
                        break;
                    }
                }
                if(!alreadyFired){
                    playerFiredRows.add(playerFire[0]);
                    playerFiredCols.add(playerFire[1]);
                    break;
                }
            }

            boolean hit = false;
            for (int a = 0; a < botPlacedRows.size(); a++) {
                if (botPlacedRows.get(a).equals(playerFire[0]) && botPlacedCols.get(a).equals(playerFire[1])) {
                    //Hit
                    System.out.println("You have hit one of the bot's ships!!!");
                    hit = true;
                    botBoard = setPiece(botBoard, playerFire[0], playerFire[1], 'X');
                    botShipsActive--;
                    System.out.println("The bot now has " + Integer.toString(botShipsActive) + " ships active.");
                    break;
                }
            }
            if (!hit) {
                System.out.println("You missed!");
                botBoard = setPiece(botBoard, playerFire[0], playerFire[1], 'M');
            }

            int[] botFire;
            while (true) {
                botFire = botFire(height, width);
                boolean alreadyFired = false;
                for (int a = 0; a < botFiredRows.size(); a++) {
                    if (botFiredRows.get(a).equals(botFire[0]) && botFiredCols.get(a).equals(botFire[1])) {
                        System.out.println("You've already fired on that location!");
                        alreadyFired = true;
                        break; //break out of for loop and restart while loops as alreadyFired is now true.
                    }
                }
                if (!alreadyFired) {
                    botFiredRows.add(botFire[0]);
                    botFiredCols.add(botFire[1]);
                    break; //break out of while loop.
                }
            }

            hit = false;
            for (int a = 0; a < playerPlacedRows.size(); a++) {
                if (playerPlacedRows.get(a).equals(botFire[0]) && playerPlacedCols.get(a).equals(botFire[1])) {
                    //Hit
                    System.out.println("The bot has hit one of your ships!!!");
                    hit = true;
                    playerBoard = setPiece(playerBoard, botFire[0], botFire[1], 'X');
                    playerShipsActive--;
                    System.out.println("You now have " + Integer.toString(playerShipsActive) + " ships active.");
                    break;
                }
            }
            if (!hit) {
                System.out.println("The bot missed!");
                playerBoard = setPiece(playerBoard, botFire[0], botFire[1], 'M');
            }
        }
        if (botShipsActive == 0) {
            System.out.println("YOU WIN!!!");
        }
        else {
            System.out.println("YOU LOSE!!!");
        }
    }

    private static boolean isInt(String s) {
        boolean isInt = false;
        try {
            Integer.parseInt(s); // s is a valid integer
            isInt = true;
        } catch (NumberFormatException ex) {
            // s is not an integer
        }
        return isInt;
    }

    private static int randInt(int min, int max) {
        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }

    private static String input(String message) {
        System.out.println(message);
        Scanner scan = new Scanner(System.in);
        return scan.nextLine();
    }

    private static int getInt(String message, int min, int max){
        String str = input(message);
        int in;
        if (isInt(str)){
            in = Integer.parseInt(str);
        }
        else{
            System.out.println("Only integer values are accepted.");
            in = getInt(message, min, max);
            return in;
        }
        if (in >= min && in <= max){
            return in;
        }
        else{
            System.out.println("Value must be between " + Integer.toString(min) + " and " + Integer.toString(max));
            in = getInt(message, min, max);
            return in;
        }
    }

    private static char[][] initBoard(int height, int width) {
        char[][] board = new char[height][width];
        //Arrays.fill(board, 'O');
        for (char[] row : board) {
            Arrays.fill(row, 'O');
        }
        return board;
    }

    private static void printBoard(char[][] board) {
        //System.out.println("printBoard called!");
        for (char[] row : board) {
            for (char piece : row) {
                System.out.print(' ');
                System.out.print(piece);
            }
            System.out.println("");
        }
        System.out.println("");
    }

    private static char[][] setPiece(char[][] board, int row, int column, char piece) {
        row--;
        column--;
        board[row][column] = piece;
        return board;
    }

    private static int[] botFire(int rowMax, int colMax) {
        int row = randInt(1, rowMax);
        int col = randInt(1, colMax);
        return new int[]{row, col};
    }

    private static int[] playerFire(int rowMax, int colMax) {
        int row = getInt("Choose a row to fire upon: ", 1, rowMax);
        int col = getInt("Choose a column to fire upon: ", 1, colMax);
        return new int[]{row, col};
    }
}


