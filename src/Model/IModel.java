package Model;


import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;

public interface IModel {

    Maze generateMaze(int rows, int columns);
    Solution solveMaze(Maze maze);

}
