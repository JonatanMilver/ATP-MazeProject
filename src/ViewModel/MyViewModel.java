package ViewModel;

import Model.IModel;
import Model.MyModel;
import algorithms.mazeGenerators.Maze;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.awt.*;

public class MyViewModel extends Canvas {
    private IModel model;

    public MyViewModel() {
        this.model = new MyModel();
    }

    public void showMaze(){
        Maze maze =model.generateMaze(5,10);
        maze.print();
    }

}
