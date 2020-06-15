package View;

import algorithms.mazeGenerators.Maze;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class MazeDisplayer extends Canvas {
    Maze maze;
    int[][] mazeArr;

    public int getPlayerRow() {
        return playerRow;
    }

    public void setPlayerRow(int playerRow) {
        this.playerRow = playerRow;
    }

    public int getPlayerColumn() {
        return playerColumn;
    }

    public void setPlayerColumn(int playerColumn) {
        this.playerColumn = playerColumn;
    }

    private int playerRow;
    private int playerColumn;

    public Maze getMaze() {
        return maze;
    }

    public void setMaze(Maze maze1) {
        this.maze = maze1;
    }

    //    public void drawMaze(int [][] maze)
//    {
//        this.maze = maze;
//        draw();
//    }
    public void drawMaze(Maze maze)
    {
        this.maze = maze;
        playerRow = this.maze.getStartPosition().getRowIndex() * 2;
        playerColumn = this.maze.getStartPosition().getColumnIndex() * 2;
        draw();
    }

    public void draw()
    {
        mazeArr = maze.getMazeArr();
        if( maze!=null)
        {

            double canvasHeight = getHeight();
            double canvasWidth = getWidth();
            int row = mazeArr.length;
            int col = mazeArr[0].length;
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
                    if(mazeArr[i][j] == 1) // Wall
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
                    else if(i == maze.getGoalPosition().getRowIndex()*2 && j == maze.getGoalPosition().getColumnIndex()*2){
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

            double h_player = this.playerRow * cellHeight;
            double w_player = this.playerColumn * cellWidth;
            Image playerImage = null;
            try {
                playerImage = new Image(new FileInputStream("C:\\Users\\yonym\\Documents\\GitHub\\ATP-MazeProject\\src\\View\\Resources\\JonatanMilver.png"));
            } catch (FileNotFoundException e) {
                System.out.println("There is no Image player....");
            }
            graphicsContext.drawImage(playerImage,w_player,h_player,cellWidth,cellHeight);

        }
    }

    public void set_player_position(int row, int col){
        this.playerRow = row;
        this.playerColumn = col;

        draw();

    }


}
