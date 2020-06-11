package ViewModel;

import Model.IModel;
import Model.MyModel;
import algorithms.mazeGenerators.Maze;
import algorithms.search.AState;
import algorithms.search.Solution;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.awt.*;

public class MyViewModel extends Canvas {
    private IModel model;


    public MyViewModel() {
        this.model = new MyModel();
    }

    public void showMaze(int rows, int columns){
        Maze maze =model.generateMaze(rows,columns);
        maze.print();
        showSolution(maze);
    }
    public void showSolution(Maze maze){

        Solution sol = model.solveMaze(maze);
        for(int i = 0; i < sol.getSolutionPath().size(); i++){
            System.out.println(String.format("%s. %s", i, sol.getSolutionPath().get(i).toString()));
        }
    }

}
