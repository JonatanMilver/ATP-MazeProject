package Model;


import Client.Client;
import Client.IClientStrategy;
import IO.MyDecompressorInputStream;
import Server.Server;
import Server.ServerStrategyGenerateMaze;
import Server.ServerStrategySolveSearchProblem;
import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.InetAddress;
import java.util.Observable;

/**
 * The class that holds the generating and solving logic.
 */
public class MyModel extends Observable implements IModel {

    private Maze maze;
    private Solution solution;
    private Server generateServer;
    private Server solveServer;
    private boolean mazeIsSolved;
    private static MyModel myModel;
    private int playerCurrentRow;
    private int playerCurrentCol;
    private static final Logger LOG = LogManager.getLogger();


    public void setMazeIsSolved(boolean mazeIsSolved) {
        this.mazeIsSolved = mazeIsSolved;
    }

    public int getPlayerCurrentRow() {
        return playerCurrentRow;
    }

    public int getPlayerCurrentCol() {
        return playerCurrentCol;
    }

    public boolean isMazeIsSolved() {
        return mazeIsSolved;
    }

    private MyModel(){
        mazeIsSolved = false;
        generateServer = new Server(5400, 1000, new ServerStrategyGenerateMaze());
        solveServer = new Server(5401, 1000, new ServerStrategySolveSearchProblem());
        generateServer.start();
        solveServer.start();
        Server.setConfigurations("GeneratingAlgorithm", "MyMazeGenerator");
        Server.setConfigurations("SearchingAlgorithm", "Best First Search");
    }

    /**
     * Static function, implementing Singleton.
     * @return MyModel
     */
    public static MyModel getInstance(){
        if(myModel == null)
            myModel = new MyModel();
        return myModel;
    }

    /**
     * Generates the maze given rows and columns.
     * Updates 'maze' field, 'playerCurrentRow' as Start index row and 'playerCurrentColumn' as start index column.
     * @param rows int
     * @param columns int
     */
    @Override
    public void generateMaze(int rows, int columns) {
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
                        LOG.info("Generating maze using " + Server.getConfigurations("GeneratingAlgorithm"));
                        LOG.info("A maze with " + rows + " rows and " + columns + " columns was created");
                        LOG.info("Starting position is at: [" + maze.getStartPosition().getRowIndex()*2 + ", " + maze.getStartPosition().getColumnIndex()*2 + "]");
                        LOG.info("Goal position is at: [" + maze.getGoalPosition().getRowIndex()*2 + ", " + maze.getGoalPosition().getColumnIndex()*2 + "]");

                    }
                    catch(IOException | ClassNotFoundException e){
                        e.printStackTrace();
                        LOG.error("Client connection failed.");
                    }
                }
            });
            client.communicateWithServer();
            LOG.info("Client" + client.getHost() + " is connected to the server at port 5400.");
        }
        catch (IOException e){
            e.printStackTrace();
            LOG.error("Generating server didn't manage to generate a maze.");
        }
        setChanged();
        notifyObservers("GENERATE");
    }

    /**
     * Solves a given maze, updates 'solution' field.
     * @param maze Maze
     */
    @Override
    public void solveMaze(Maze maze) {
        try{
           Client client = new Client(InetAddress.getLocalHost(), 5401, new IClientStrategy() {
               @Override
               public void clientStrategy(InputStream inputStream, OutputStream outputStream) {
                   try{
                       ObjectOutputStream toServer = new ObjectOutputStream(outputStream);
                       ObjectInputStream fromServer = new ObjectInputStream(inputStream);
                       toServer.flush();
                       toServer.writeObject(maze); //send maze to server
                       toServer.flush();
                       solution = (Solution) fromServer.readObject(); //read generated maze (compressed with MyCompressor) from server
                       LOG.info("Solution length is " + solution.getSolutionPath().size());
                       LOG.info("Solving algorithm is " + Server.getConfigurations("SearchingAlgorithm"));
                   } catch (Exception e) {
                       e.printStackTrace();
                       LOG.error("Client connection failed.");
                   }
               }
           });
            client.communicateWithServer();
            LOG.info("Client" + client.getHost() + " is connected to the server at port 5401.");
        }
        catch(IOException e){
            e.printStackTrace();
            LOG.error("Solving server didn't manage to give a solution.");
        }
        setChanged();
        notifyObservers("SOLVE");
    }

    @Override
    public Maze getMaze() {
        return maze;
    }

    /**
     * Sets given maze while users LOADS a maze.
     * @param maze Maze
     */
    @Override
    public void setMaze(Maze maze) {
        this.maze = maze;
        playerCurrentRow = maze.getStartPosition().getRowIndex() * 2;
        playerCurrentCol = maze.getStartPosition().getColumnIndex() * 2;
        LOG.info("Generating maze using " + Server.getConfigurations("GeneratingAlgorithm"));
        LOG.info("A maze with " + maze.getRows() + " rows and " + maze.getColumns() + " columns was created");
        LOG.info("Starting position is at: [" + maze.getStartPosition().getRowIndex() * 2 + ", " + maze.getStartPosition().getColumnIndex() * 2 + "]");
        LOG.info("Goal position is at: [" + maze.getGoalPosition().getRowIndex() * 2 + ", " + maze.getGoalPosition().getColumnIndex() * 2 + "]");
        setChanged();
        notifyObservers("LOAD");

    }

    public static Logger getLOG() {
        return LOG;
    }

    public Solution getSolution() {
        return solution;
    }


    /**
     * Checks whether a user can move to a given direction.
     * @param direction String
     * @return boolean
     */
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
        else if(direction.equals("UPLEFT")){
            return(currRow - 1 >= 0 && currCol - 1 >= 0 && maze.getMazeArr()[currRow-1][currCol-1] == 0);
        }
        else if(direction.equals("UPRIGHT")){
            return(currRow - 1 >= 0 && currCol + 1 < maze.getMazeArr()[0].length && maze.getMazeArr()[currRow -1][currCol + 1] == 0);
        }
        else if(direction.equals("DOWNLEFT")){
            return(currRow + 1 < maze.getMazeArr().length && currCol - 1 >= 0 && maze.getMazeArr()[currRow+1][currCol-1] == 0);
        }
        else if(direction.equals("DOWNRIGHT")){
            return (currRow + 1 <maze.getMazeArr().length && currCol + 1 <maze.getMazeArr()[0].length && maze.getMazeArr()[currRow+1][currCol+1] == 0);
        }
        else{
            return false;
        }
    }

    /**
     * Updates Character's location with given direction.
     * @param direction String
     */
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
            case "UPLEFT": //Up and Left
                if(canMove("UPLEFT")){
                    playerCurrentRow--;
                    playerCurrentCol--;
                }
                break;
            case "UPRIGHT": //Up and Right
                if(canMove("UPRIGHT")){
                    playerCurrentRow--;
                    playerCurrentCol++;
                }
                break;
            case "DOWNLEFT": //Down and Left
                if(canMove("DOWNLEFT")){
                    playerCurrentRow++;
                    playerCurrentCol--;
                }
                break;
            case "DOWNRIGHT": //Down and Right
                if(canMove("DOWNRIGHT")){
                    playerCurrentRow++;
                    playerCurrentCol++;
                }
                break;
        }
        if(playerCurrentRow == maze.getGoalPosition().getRowIndex() * 2 && playerCurrentCol == maze.getGoalPosition().getColumnIndex() * 2)
            mazeIsSolved = true;
        setChanged();
        notifyObservers("MOVE");
    }

    /**
     * Sets the character to it's start position.
     * This method is being called only when user solves the maze.
     */
    public void restartMaze() {
        LOG.info("Client has solved the maze.");
        mazeIsSolved = false;
        playerCurrentRow = maze.getStartPosition().getRowIndex() * 2;
        playerCurrentCol = maze.getStartPosition().getColumnIndex() * 2;
        setChanged();
        notifyObservers("GENERATE");
    }

    /**
     * Stops the servers while application is being closed.
     */
    public void stopServers(){
        generateServer.stop();
        LOG.info("GENERATING server is being closed.");
        solveServer.stop();
        LOG.info("SOLVING server is being closed.");
    }
}
