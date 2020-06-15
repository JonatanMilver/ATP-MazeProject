package Model;


import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;

public interface IModel {

    void generateMaze(int rows, int columns);
    void solveMaze(Maze maze);

}
