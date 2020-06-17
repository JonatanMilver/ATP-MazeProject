package Model;


import Client.Client;
import IO.MyDecompressorInputStream;
import Server.Server;
import algorithms.mazeGenerators.Maze;
import Server.ServerStrategyGenerateMaze;
import Client.IClientStrategy;
import Server.ServerStrategySolveSearchProblem;
import algorithms.search.*;
import javafx.beans.property.IntegerProperty;

import java.io.*;
import java.net.InetAddress;
import java.util.Observable;


public class MyModel extends Observable implements IModel {

    private Maze maze;
    private Solution solution;

    public void setMazeIsSolved(boolean mazeIsSolved) {
        this.mazeIsSolved = mazeIsSolved;
    }

    private boolean mazeIsSolved;
    private static MyModel myModel;
    private int playerCurrentRow;
    private int playerCurrentCol;

    public int getPlayerCurrentRow() {
        return playerCurrentRow;
    }

    public void setPlayerCurrentRow(int playerCurrentRow) {
        this.playerCurrentRow = playerCurrentRow;
    }

    public int getPlayerCurrentCol() {
        return playerCurrentCol;
    }

    public void setPlayerCurrentCol(int playerCurrentCol) {
        this.playerCurrentCol = playerCurrentCol;
    }



    public boolean isMazeIsSolved() {
        return mazeIsSolved;
    }



    private MyModel(){
        mazeIsSolved = false;
    }

    public static MyModel getInstance(){
        if(myModel == null)
            myModel = new MyModel();
        return myModel;
    }

    @Override
    public void generateMaze(int rows, int columns) {
        Server server = new Server(5400, 1000, new ServerStrategyGenerateMaze());
        server.start();
        try{
            Client client = new Client(InetAddress.getLocalHost(), 5400, new IClientStrategy() {
                public void clientStrategy(InputStream inputStream, OutputStream outputStream){
                    try{
                        ObjectOutputStream toServer = new ObjectOutputStream(outputStream);
                        ObjectInputStream fromServer = new ObjectInputStream(inputStream);
                        toServer.flush();
                        int[] mazeDimensions = new int[]{rows, columns};
                        toServer.writeObject(mazeDimensions);
                        toServer.flush();
                        byte[] compressedMaze = (byte[]) fromServer.readObject(); //read generated maze (compressed with MyCompressor) from server
                        InputStream is = new MyDecompressorInputStream(new ByteArrayInputStream(compressedMaze));
                        byte[] decompressedMaze = new byte[998013 /*CHANGE SIZE ACCORDING TO YOU MAZE SIZE*/]; //allocating byte[] for the decompressed maze -
                        is.read(decompressedMaze); //Fill decompressedMaze with bytes
                        maze = new Maze(decompressedMaze);
                        playerCurrentRow = maze.getStartPosition().getRowIndex() * 2;
                        playerCurrentCol = maze.getStartPosition().getColumnIndex() * 2;
                       // maze.print();

                    }
                    catch(IOException | ClassNotFoundException e){
                        e.printStackTrace();
                    }
                }
            });
            client.communicateWithServer();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        setChanged();
        notifyObservers("GENERATE");
        server.stop();


    }

    @Override
    public void solveMaze(Maze maze) {
        Server server = new Server(5401, 1000, new ServerStrategySolveSearchProblem());
        server.start();
        final Solution[] theSolution = new Solution[1];
        try{
           Client client = new Client(InetAddress.getLocalHost(), 5401, new IClientStrategy() {
               @Override
               public void clientStrategy(InputStream inputStream, OutputStream outputStream) {
                   try{
                       ObjectOutputStream toServer = new ObjectOutputStream(outputStream);
                       ObjectInputStream fromServer = new ObjectInputStream(inputStream);
                       toServer.flush();
//                       MyMazeGenerator mg = new MyMazeGenerator();
//                       Maze maze = mg.generate(50, 50);
//                       maze.print();
                       toServer.writeObject(maze); //send maze to server
                       toServer.flush();
                       solution = (Solution) fromServer.readObject(); //read generated maze (compressed with MyCompressor) from server
                   } catch (Exception e) {
                       e.printStackTrace();
                   }
               }
           });
            client.communicateWithServer();
        }
        catch(IOException e){
            e.printStackTrace();
        }
        setChanged();
        notifyObservers("SOLVE");
        server.stop();
    }

    @Override
    public Maze getMaze() {
        return maze;
    }
    @Override
    public void setMaze(Maze maze) {
        this.maze = maze;
        playerCurrentRow = maze.getStartPosition().getRowIndex() * 2;
        playerCurrentCol = maze.getStartPosition().getColumnIndex() * 2;
        setChanged();
        notifyObservers("LOAD");
    }

    public Solution getSolution() {
        return solution;
    }


    private boolean canMove(String direction) {
        int currRow = playerCurrentRow;
        int currCol = playerCurrentCol;
        if (direction.equals("UP")) {
            return (currRow - 1 >= 0 && maze.getMazeArr()[currRow - 1][currCol] == 0);
        }
        else if(direction.equals("DOWN")){
            return(currRow + 1 < maze.getMazeArr().length && maze.getMazeArr()[currRow+1][currCol] == 0);
        }
        else if(direction.equals("RIGHT")){
            return(currCol + 1 < maze.getMazeArr()[0].length && maze.getMazeArr()[currRow][currCol+1] == 0);
        }
        else if(direction.equals("LEFT")){
            return(currCol - 1 >= 0 && maze.getMazeArr()[currRow][currCol-1] == 0);
        }
        else{
            return false;
        }
    }
    @Override
    public void updateCharacterLocation(String direction)
    {
        switch(direction)
        {
            case "UP": //Up
                if(canMove("UP")) {
                    playerCurrentRow--;
                }
                break;

            case "DOWN": //Down
                if(canMove("DOWN"))
                    playerCurrentRow++;
                break;
            case "LEFT": //Left
                if(canMove("LEFT"))
                    playerCurrentCol--;
                break;
            case "RIGHT": //Right
                if(canMove("RIGHT"))
                    playerCurrentCol++;
                break;
        }
        if(playerCurrentRow == maze.getGoalPosition().getRowIndex() * 2 && playerCurrentCol == maze.getGoalPosition().getColumnIndex() * 2)
            mazeIsSolved = true;
        setChanged();
        notifyObservers("MOVE");
    }

    public void restartMaze() {
        mazeIsSolved = false;
        playerCurrentRow = maze.getStartPosition().getRowIndex() * 2;
        playerCurrentCol = maze.getStartPosition().getColumnIndex() * 2;
        setChanged();
        notifyObservers("GENERATE");
    }
}
