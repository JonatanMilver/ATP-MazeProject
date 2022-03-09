package ViewModel;

import Model.MyModel;
import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import javafx.scene.input.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Observable;
import java.util.Observer;


public class MyViewModel extends Observable implements Observer {
    private static MyViewModel vm = null;
    private MyModel model;
    private Maze maze;
    private Solution solution;

    public int getCharacterRow() {
        return characterRow;
    }

    public int getCharacterCol() {
        return characterCol;
    }

    private int characterRow;
    private int characterCol;

    public Maze getMaze() {
        return maze;
    }

    public Solution getSolution() {
        return solution;
    }

    public void setSolution(Solution solution) {
        this.solution = solution;
    }




    private MyViewModel() {
        this.model = MyModel.getInstance();
    }

    /**
     * Static function, implementing Singleton.
     * @return MyViewModel
     */
    public static MyViewModel getInstance() {
        if (vm == null){
            vm = new MyViewModel();
        }
        return vm;
    }

    /**
     * Wrapper for the model logic, generates a new maze given rows and columns.
     * @param rows int
     * @param columns int
     *
     */
    public void generateMaze(String rows, String columns){
        if(checkValidInput(rows, columns)) {
            int rowsCheck  = Integer.parseInt(rows);
            int columnsCheck = Integer.parseInt(columns);
            model.generateMaze(rowsCheck, columnsCheck);
        }
        else{
            setChanged();
            notifyObservers("INVALIDINPUT");
        }
    }

    /**
     * Checks whether inserted input of rows and columns is valid.
     * We do approve mazes of sizes bigger than 1x1 and smaller than 500x500.
     * @param rows String
     * @param columns String
     * @return boolean
     */
    public boolean checkValidInput(String rows, String columns){
        int rowsCheck = 0;
        int columnsCheck = 0;
        try {
           rowsCheck  = Integer.parseInt(rows);
           columnsCheck = Integer.parseInt(columns);
        }
        catch (NumberFormatException e){
            return false;
        }
        if(rowsCheck <= 1 || columnsCheck <= 1)
            return false;
        if(rowsCheck > 500 || columnsCheck > 500)
            return false;

        return true;

    }

    /**
     * Wrapper for the model logic, solves a given maze
     *
     */
    public void solveMaze(){
        this.model.solveMaze(model.getMaze());
    }

    /**
     * Reads a maze from given file
     * @param file File
     * @return Maze
     */
    public void loadMaze(File file) {
        Maze mazeFromFile = null;
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
            mazeFromFile = (Maze) ois.readObject();
            if (mazeFromFile != null) {
                setMaze(mazeFromFile);
            }
        }
        catch(ClassNotFoundException | IOException e){
            MyModel.getLOG().error("Client tried to load a maze but failed.");
            setChanged();
            notifyObservers("NULLMAZE");
        }

    }


    /**
     * Receives updates from MyModel and updates the relevant fields - this method is Overridden from the Observer interface.
     * Notifies NewPage about the updates.
     * @param o Observable
     * @param arg (String)
     */
    @Override
    public void update(java.util.Observable o, Object arg) {
        String operation = (String) arg;
        if(operation.equals("GENERATE")){
            maze = model.getMaze();
            characterRow = maze.getStartPosition().getRowIndex() * 2;
            characterCol = maze.getStartPosition().getColumnIndex() * 2;
        }
        else if(operation.equals("SOLVE")){
            solution = model.getSolution();
        }
        else if(operation.equals("MOVE")){
            characterRow = model.getPlayerCurrentRow();
            characterCol = model.getPlayerCurrentCol();
            if(model.isMazeIsSolved()){
                operation = "MOVEANDSOLVED";
                model.setMazeIsSolved(false);
            }
        }
        else if(operation.equals("LOAD")){
            maze = model.getMaze();
            characterRow = maze.getStartPosition().getRowIndex() * 2;
            characterCol = maze.getStartPosition().getColumnIndex() * 2;

        }

        setChanged();
        notifyObservers(operation);
    }


    /**
     * Calls MyModel's setMaze method.
     * @param maze Maze
     */
    public void setMaze(Maze maze){
        model.setMaze(maze);
    }

    /**
     * gets the event's press and passes it through to MyModel class.
     * @param keyEvent KeyEvent
     */
    public void moveCharacter(KeyEvent keyEvent) {
        String direction = "";
        switch (keyEvent.getCode()) {
            case UP: case NUMPAD8:
                direction = "UP";
                break;
            case DOWN: case NUMPAD2:
                direction = "DOWN";
                break;
            case RIGHT: case NUMPAD6:
                direction = "RIGHT";
                break;
            case LEFT: case NUMPAD4:
                direction = "LEFT";
                break;
            case NUMPAD1:
                direction = "DOWNLEFT";
                break;
            case NUMPAD3:
                direction = "DOWNRIGHT";
                break;
            case NUMPAD7:
                direction = "UPLEFT";
                break;
            case NUMPAD9:
                direction = "UPRIGHT";
        }
        model.updateCharacterLocation(direction);
    }

    /**
     * Calls MyModel's restartMaze method.
     */
    public void restartMaze() {
        model.restartMaze();
    }
}
