package ViewModel;

import Model.MyModel;
import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Observable;
import java.util.Observer;


public class MyViewModel extends Observable implements Observer {
    private static MyViewModel vm = null;
    private MyModel model;
    public StringProperty playerCurrentRow = new SimpleStringProperty();
    public StringProperty playercurrentColumn = new SimpleStringProperty();


    private MyViewModel() {
        this.model = new MyModel();
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
     * @return Maze
     */
    public Maze generateMaze(int rows, int columns){
        model.generateMaze(rows,columns);
        return model.getMaze();

    }

    /**
     * Wrapper for the model logic, solves a given maze
     * @return Solution
     */
    public Solution solveMaze(){
        this.model.solveMaze(model.getMaze());
        return model.getSolution();
    }

    /**
     * Reads a maze from given file
     * @param file File
     * @return Maze
     */
    public Maze loadMaze(File file){
        Maze mazeFromFile = null;
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
            mazeFromFile = (Maze)ois.readObject();

        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        finally {
            if(mazeFromFile != null){
                return mazeFromFile;
            }
            return null;
        }
    }

    @Override
    public void update(java.util.Observable o, Object arg) {

}

    public boolean canMove(String direction) {
//        System.out.println("Row: " + playerCurrentRow.getValue() + " Column: "+ playercurrentColumn.getValue());
//        System.out.println("--------------------");
        return (model.canMove(direction, playerCurrentRow.getValue(), playercurrentColumn.getValue()));
    }

    public Maze getmaze() {
        return model.getMaze();
    }
}
