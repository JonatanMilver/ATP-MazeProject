package View;

import algorithms.mazeGenerators.Maze;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class MazeDisplayer extends Canvas {
    Maze maze1;
    int[][] maze;

//    public void drawMaze(int [][] maze)
//    {
//        this.maze = maze;
//        draw();
//    }
    public void drawMaze(Maze maze)
    {
        this.maze1 = maze;
        draw();
    }

    public void draw()
    {
        maze = maze1.getMazeArr();
        if( maze!=null)
        {

            double canvasHeight = getHeight();
            double canvasWidth = getWidth();
            int row = maze.length;
            int col = maze[0].length;
            double cellHeight = canvasHeight/row;
            double cellWidth = canvasWidth/col;
            GraphicsContext graphicsContext = getGraphicsContext2D();
            graphicsContext.clearRect(0,0,canvasWidth,canvasHeight);
            graphicsContext.setFill(Color.RED);
            double w,h;
            //Draw Maze

//            Image wallImage = null;
//            try {
//                wallImage = new Image(new FileInputStream(getImageFileNameWall()));
//            } catch (FileNotFoundException e) {
//                System.out.println("There is no file....");
//            }
            Image goalImage = null;
            try {
                goalImage = new Image(new FileInputStream("C:\\Users\\yonym\\Documents\\GitHub\\ATP-MazeProject\\src\\View\\Resources\\shapelined-qawemGipVPk-unsplash.jpg"));
            } catch (FileNotFoundException e) {
                System.out.println("There is no file....");
            }

            for(int i=0;i<row;i++)
            {
                for(int j=0;j<col;j++)
                {
                    if(maze[i][j] == 1) // Wall
                    {
                        h = i * cellHeight;
                        w = j * cellWidth;
                        Image wallImage = null;//!!!!!!!!!!!!!!!!!!!!!!!
                        if (wallImage == null){
                            graphicsContext.fillRect(w,h,cellWidth,cellHeight);
                        }else{
                            graphicsContext.drawImage(wallImage,w,h,cellWidth,cellHeight);
                        }
                    }
                    else if(i == maze1.getGoalPosition().getRowIndex()*2 && j == maze1.getGoalPosition().getColumnIndex()*2){
                        double height = i * cellHeight;
                        double width = j * cellWidth;
//                        System.out.println(i + " first is i " + j);
//                        System.out.println(height);
//                        System.out.println(width);
//                        System.out.println(cellWidth);
//                        System.out.println(cellHeight);
//                        System.out.println(maze1.getGoalPosition().getRowIndex());
//                        System.out.println(maze1.getGoalPosition().getColumnIndex());
                        graphicsContext.drawImage(goalImage,width,height,cellWidth,cellHeight);
                    }

                }
            }

//            double h_player = getRow_player() * cellHeight;
//            double w_player = getCol_player() * cellWidth;
//            Image playerImage = null;
//            try {
//                playerImage = new Image(new FileInputStream(getImageFileNamePlayer()));
//            } catch (FileNotFoundException e) {
//                System.out.println("There is no Image player....");
//            }
//            graphicsContext.drawImage(playerImage,w_player,h_player,cellWidth,cellHeight);

        }
    }

}
