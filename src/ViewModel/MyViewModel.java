package ViewModel;

import Model.IModel;
import Model.MyModel;
import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;


public class MyViewModel {
    private IModel model;


    public MyViewModel() {
        this.model = new MyModel();
    }

    public Maze generateMaze(int rows, int columns){
        return this.model.generateMaze(rows, columns);

    }
    public Solution solveMaze(Maze maze){
        return this.model.solveMaze(maze);
    }

}
