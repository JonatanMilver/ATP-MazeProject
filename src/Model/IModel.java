package Model;


import algorithms.mazeGenerators.Maze;

public interface IModel {

    Maze generateMaze(int rows, int columns);
    void solveMaze(Maze maze);

}
