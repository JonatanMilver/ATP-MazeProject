package ViewModel;

import Model.MyModel;
import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.input.KeyEvent;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
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
    public void generateMaze(int rows, int columns){
        model.generateMaze(rows,columns);
        //maze = model.getMaze();

    }

    /**
     * Wrapper for the model logic, solves a given maze
     *
     */
    public void solveMaze(){
        this.model.solveMaze(model.getMaze());
        //solution =  model.getSolution();
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
            System.out.println("Thrown exception");
            }

    }


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


    public void setMaze(Maze maze){
        model.setMaze(maze);
    }

    public void moveCharacter(KeyEvent keyEvent) {
        String direction = "";
        switch (keyEvent.getCode()) {
            case UP:
                direction = "UP";
                break;
            case DOWN:
                direction = "DOWN";
                break;
            case RIGHT:
                direction = "RIGHT";
                break;
            case LEFT:
                direction = "LEFT";
                break;
//            case W:
//                mazeDisplayer.set_player_position(player_row_position - 1, player_col_position);
//                break;
//            case S:
//                mazeDisplayer.set_player_position(player_row_position + 1, player_col_position);
//                break;
//            case D:
//                mazeDisplayer.set_player_position(player_row_position, player_col_position + 1);
//                break;
//            case A:
//                mazeDisplayer.set_player_position(player_row_position, player_col_position - 1);
//                break;

        }
        model.updateCharacterLocation(direction);
    }

    public void restartMaze() {
        model.restartMaze();
    }
}
