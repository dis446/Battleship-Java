package com.TG_CODE;

//import java.lang.reflect.Array;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        System.out.println("Welcome to My BattleShip Game!!!");
        System.out.println("");
        while (true) {
            Game(5, 5, 3);
            String again = input("Do you want to play again?");
            if(again.charAt(0) != 'y'){
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

        /* //Old method of storing bot's ships' locations using a Map Object. Ignore.
        Map botPlacedShips = new HashMap();
        botPlacedShips.put("row1", randInt(0,4));
        botPlacedShips.put("row2", randInt(0,4));
        botPlacedShips.put("row3", randInt(0,4));
        botPlacedShips.put("col1", randInt(0,4));
        botPlacedShips.put("col2", randInt(0,4));
        botPlacedShips.put("col3", randInt(0,4));
        */

        int noPlaced = 0; //Number of ships placed. Used by both bot and player.

        //Set bot's pieces.
        ArrayList<Integer> botPlacedRows = new ArrayList<>();
        ArrayList<Integer> botPlacedCols = new ArrayList<>();

        while (noPlaced < shipCount){
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
            while (true) { //Get row
                String stringRow = input("Choose a row to place your ship: ");
                if (stringRow.length() == 1 && isInt(stringRow)) {
                    row = Integer.parseInt(stringRow);
                    if (row <= height && row > 0 ){
                        break;
                    }
                    else{
                        System.out.println("Error. Input be between range of 1 and ");
                        System.out.print(height);
                    }
                }
                else {
                    System.out.println("Error. Input must be of one ");
                }
            }
            while (true) { //Get column
                String stringCol = input("Choose a row to place your ship: ");
                if (stringCol.length() == 1 && isInt(stringCol)) {
                    col = Integer.parseInt(stringCol);
                    if (col <= width && col > 0 ) {
                        break;
                    }
                    else{
                        System.out.println("Error. Input be between range of 1 and ");
                        System.out.print(width);
                    }
                }
                else {
                    System.out.println("Error. Input must be of one ");
                }
            }

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

            int[] playerFire;
            boolean alreadyFired; //Check if player has already fired upon that location.
            //Exit if the player hasn't.
            while (true) {
                playerFire = playerFire(1, 5);
                alreadyFired = false;
                for (int a = 0; a < playerFiredRows.size(); a++) {
                    /*
                    System.out.println("Player has fired upon: ");
                    System.out.print(playerFiredRows.get(a));
                    System.out.print(playerPlacedCols.get(a));
                    */
                    if (playerFiredRows.get(a).equals(playerFire[0]) && playerFiredCols.get(a).equals(playerFire[1])) {
                        System.out.println("You've already fired on that location!");
                        alreadyFired = true;
                        break; //break out of for loop.
                    }
                }
                if (!alreadyFired) {
                    playerFiredRows.add(playerFire[0]);
                    playerFiredCols.add(playerFire[1]);
                    break; //break out of while loop.
                }
            }

            boolean hit = false;
            for (int a = 0 ; a < botPlacedRows.size(); a++) {
                if (botPlacedRows.get(a).equals(playerFire[0]) && botPlacedCols.get(a).equals(playerFire[1])) {
                    //Hit
                    System.out.println("You have hit one of the bot's ships!!!");
                    hit = true;
                    botBoard = setPiece(botBoard,playerFire[0], playerFire[1], 'X');
                    botShipsActive--;
                    System.out.println("The bot now has " + Integer.toString(botShipsActive) + " ships active.");
                    break;
                }
            }
            if (!hit){
                System.out.println("You missed!");
                botBoard = setPiece(botBoard,playerFire[0], playerFire[1], 'M');
            }

            int[] botFire;
            while (true) {
                botFire = botFire(1, 5);
                alreadyFired = false;
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
            for (int a = 0 ; a < playerPlacedRows.size(); a++) {
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
            if (!hit){
                System.out.println("The bot missed!");
                playerBoard = setPiece(playerBoard,botFire[0], botFire[1], 'M');
            }

            /* //Old method of checking if bot had hit or not, by checking if the character at botFire[0],botfire[1] was 'S'.
            //Didn't work.
            if (playerBoard[botFire[0]][botFire[1]] == ('S')) {
                //Bot has hit!!!
                System.out.println("The bot has hit one of your ships!!!");
                playerBoard = setPiece(playerBoard,botFire[0], botFire[1], 'X');
                playerShipsActive--;
                System.out.println("You now have " + Integer.toString(playerShipsActive) + " ships active.");
            }
            else{
                System.out.println("The bot missed!");
                playerBoard = setPiece(playerBoard,botFire[0], botFire[1], 'M');
            }
            */
        }
        if (botShipsActive == 0){
            System.out.println("YOU WIN!!!");
        }
        else{
            System.out.println("YOU LOSE!!!");
        }
    }


    public static boolean isInt(String s) {
        boolean isInt = false;
        try {
            Integer.parseInt(s); // s is a valid integer
            isInt = true;
        }
        catch (NumberFormatException ex) {
            // s is not an integer
        }
        return isInt;
    }

    public static int randInt(int min, int max) {
        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }

    private static String input(String message){
    System.out.println(message);
    Scanner scan = new Scanner(System.in);
    String ms = scan.nextLine();
    return ms;
    }

    private static char[][] initBoard(int height, int width){
        char [][] board = new char[height][width];
        //Arrays.fill(board, 'O');
        for (char[] row: board){
            Arrays.fill(row, 'O');
        }
        return board;
    }

    private static void printBoard(char[][] board){
        //System.out.println("printBoard called!");
        for(char[] row: board) {
            for (char piece: row){
                System.out.print(' ');
                System.out.print(piece);
            }
            System.out.println("");
        }
        System.out.println("");
    }

    private static char[][] setPiece(char[][] board, int row, int column, char piece){
        row--;
        column--;
        board[row][column] = piece;
        return board;
    }

    private static int[] botFire(int min, int max){
        int row = randInt(min, max);
        int col = randInt(min, max);
        return new int[] {row, col};
    }

    private static int[] playerFire(int min, int max) {
        int row;
        while (true) {
            String stringRow = input("Choose a row to fire upon: ");
            if ((stringRow.length() == 1) && (isInt(stringRow))) {
                row = Integer.parseInt(stringRow);
                if (row >= min && row <= max ) {
                    break;
                }
                else{
                    System.out.println("Error. Number must be between the range of ");
                    System.out.print(min);
                    System.out.print(" and ");
                    System.out.print(max);
                }
            }
            else {
                System.out.println("Error. Input must be a one digit integer.");
            }
        }
        int col;
        while (true) {
            String stringColoum = input("Choose a row to fire upon: ");
            if ((stringColoum.length() == 1) && (isInt(stringColoum))) {
                col = Integer.parseInt(stringColoum);
                if (row >= min && row <= max ) {
                    break;
                }
                else{
                    System.out.println("Error. Number must be between the range of ");
                    System.out.print(min);
                    System.out.print(" and ");
                    System.out.print(max);
                }
            }
            else {
                System.out.println("Error. Input must be a one digit integer.");
            }
        }
        //System.out.println("PlayerFire() returns: ");
        //System.out.println(row);
        //System.out.println(col);
        return new int[]{row, col};
    }
}

