package View;

import algorithms.mazeGenerators.Maze;
import algorithms.search.AState;
import algorithms.search.MazeState;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class MazeDisplayer extends Canvas {
    Maze maze;
    int[][] mazeArr;

    private int playerRow;
    private int playerColumn;

    private String endImagePath = "src/View/Resources/shapelined-qawemGipVPk-unsplash.jpg";
    //"C:\\Users\\yonym\\Documents\\GitHub\\ATP-MazeProject\\src\\View\\Resources\\shapelined-qawemGipVPk-unsplash.jpg"

    private String playerImagePath = "src/View/Resources/JonatanMilver.png";
    //"C:\\Users\\yonym\\Documents\\GitHub\\ATP-MazeProject\\src\\View\\Resources\\JonatanMilver.png"

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
                goalImage = new Image(new FileInputStream(endImagePath));
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
                playerImage = new Image(new FileInputStream(playerImagePath));
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


    public void drawSolution(ArrayList<AState> solutionPath) {
        double canvasHeight = getHeight();
        double canvasWidth = getWidth();
        int row = mazeArr.length;
        int col = mazeArr[0].length;
        double cellHeight = canvasHeight/row;
        double cellWidth = canvasWidth/col;
        /*PRINT SOLUTION PATH FOR TESTING!*/
        for(AState state : solutionPath){
            System.out.println(state.getName());
        }
        GraphicsContext graphicsContext = getGraphicsContext2D();
        graphicsContext.setFill(Color.YELLOW);
        double w,h;
        h=((MazeState)solutionPath.get(0)).getCurrent_position().getRowIndex() * 2 * cellHeight;
        w = ((MazeState)solutionPath.get(0)).getCurrent_position().getColumnIndex() * 2 * cellWidth;
        int prevRowIndex = ((MazeState)solutionPath.get(0)).getCurrent_position().getRowIndex() * 2;
        int prevColIndex = ((MazeState)solutionPath.get(0)).getCurrent_position().getColumnIndex() * 2;
        graphicsContext.fillRect(w,h, cellWidth, cellHeight);
        for(int i=1;i<solutionPath.size();i++){

            h=((MazeState)solutionPath.get(i)).getCurrent_position().getRowIndex() * 2 * cellHeight;
            w = ((MazeState)solutionPath.get(i)).getCurrent_position().getColumnIndex() * 2 * cellWidth;
            graphicsContext.fillRect(w,h, cellWidth, cellHeight);

            int currRowIndex = ((MazeState)solutionPath.get(i)).getCurrent_position().getRowIndex() * 2;
            int currColIndex = ((MazeState)solutionPath.get(i)).getCurrent_position().getColumnIndex() * 2;
            fillEmptyRects(prevRowIndex, prevColIndex, currRowIndex, currColIndex, graphicsContext, cellHeight, cellWidth);
            prevRowIndex = currRowIndex;
            prevColIndex = currColIndex;

        }
    }


    /**
     * Fills the solution path.
     * @param prevRowIndex
     * @param prevColIndex
     * @param currRowIndex
     * @param currColIndex
     * @param graphicsContext
     * @param cellHeight
     * @param cellWidth
     */
    private void fillEmptyRects(int prevRowIndex, int prevColIndex, int currRowIndex, int currColIndex, GraphicsContext graphicsContext, double cellHeight, double cellWidth) {
        double w,h;
        /*Same row but the columns are different*/
        if(prevRowIndex == currRowIndex){
            if(prevColIndex < currColIndex){
                w = (prevColIndex + 1) * cellHeight;
            }
            else{
                w = (currColIndex + 1) * cellHeight;
            }
            h = prevRowIndex * cellWidth;
            graphicsContext.fillRect(w,h, cellWidth, cellHeight);
        }
        /*Same column but the rows are different*/
        if(prevColIndex == currColIndex){
            if(prevRowIndex < currRowIndex){
                w = (prevColIndex) * cellHeight;
                h = (prevRowIndex+1) * cellWidth;
            }
            else{
                w = (currColIndex ) * cellHeight;
                h = (currRowIndex+1) * cellWidth;
            }
            graphicsContext.fillRect(w,h, cellWidth, cellHeight);
        }
        /*Both row and column of previous state at the path are smaller than current's row and column*/
        if(prevRowIndex < currRowIndex && prevColIndex < currColIndex){
            /*The wall is underneath previous state*/
            if(mazeArr[prevRowIndex+1][prevColIndex] == 1 || mazeArr[currRowIndex][currColIndex - 1] == 1){
                int colIndexToFill = prevColIndex;
                while(colIndexToFill <= currColIndex){
                    w = colIndexToFill * cellHeight;
                    h = prevRowIndex * cellWidth;
                    graphicsContext.fillRect(w,h, cellWidth, cellHeight);
                    colIndexToFill++;
                }
                int rowIndexToFill = prevRowIndex+1;
                colIndexToFill--;
                w= colIndexToFill * cellHeight;
                h = rowIndexToFill * cellWidth;
                graphicsContext.fillRect(w,h, cellWidth, cellHeight);
            }
            /*The wall is at the right side of previous state*/
            else{
                int rowIndextoFill = prevRowIndex;
                while(rowIndextoFill<= currRowIndex){
                    w = prevColIndex * cellHeight;
                    h = rowIndextoFill * cellWidth;
                    graphicsContext.fillRect(w,h, cellWidth, cellHeight);
                    rowIndextoFill++;
                }
                int colIndexToFill = prevColIndex + 1;
                rowIndextoFill--;
                w = colIndexToFill * cellHeight;
                h = rowIndextoFill * cellWidth;
                graphicsContext.fillRect(w,h, cellWidth, cellHeight);

            }
        }
        /*Both row and column of previous state at the path are bigger than current's row and column*/
        if(prevRowIndex > currRowIndex && prevColIndex > currColIndex){
            /*The wall is underneath current state*/
            if(mazeArr[prevRowIndex][prevColIndex-1] == 1 || mazeArr[currRowIndex+1][currColIndex] == 1){
                int colIndexToFill = currColIndex;
                while(colIndexToFill <= prevColIndex){
                    w = colIndexToFill * cellHeight;
                    h = currRowIndex * cellWidth;
                    graphicsContext.fillRect(w,h, cellWidth, cellHeight);
                    colIndexToFill++;
                }
                int rowIndexToFill = currRowIndex+1;
                colIndexToFill--;
                w= colIndexToFill * cellHeight;
                h = rowIndexToFill * cellWidth;
                graphicsContext.fillRect(w,h, cellWidth, cellHeight);
            }
            /*The wall is at the right side of current state*/
            else{
                int rowIndextoFill = currRowIndex;
                while(rowIndextoFill<= prevRowIndex){
                    w = currColIndex * cellHeight;
                    h = rowIndextoFill * cellWidth;
                    graphicsContext.fillRect(w,h, cellWidth, cellHeight);
                    rowIndextoFill++;
                }
                int colIndexToFill = currColIndex + 1;
                rowIndextoFill--;
                w = colIndexToFill * cellHeight;
                h = rowIndextoFill * cellWidth;
                graphicsContext.fillRect(w,h, cellWidth, cellHeight);

            }
        }
        /*Prevous' row is smaller than curren't row but previous' column is bigger than current's column*/
        if(prevRowIndex < currRowIndex && prevColIndex > currColIndex){
            if(mazeArr[prevRowIndex+1][prevColIndex] == 1 || mazeArr[currRowIndex][currColIndex+1] == 1){
                int colIndexToFill = prevColIndex;
                while(colIndexToFill >= currColIndex){
                    w = colIndexToFill * cellHeight;
                    h = prevRowIndex * cellWidth;
                    graphicsContext.fillRect(w,h, cellWidth, cellHeight);
                    colIndexToFill--;
                }
                int rowIndexToFill = prevRowIndex + 1;
                colIndexToFill++;
                w= colIndexToFill * cellHeight;
                h= rowIndexToFill * cellWidth;
                graphicsContext.fillRect(w,h, cellWidth, cellHeight);
            }
            else{
                int rowIndexToFill = prevRowIndex;
                while(rowIndexToFill <= currRowIndex){
                    w = prevColIndex * cellHeight;
                    h = rowIndexToFill * cellWidth;
                    graphicsContext.fillRect(w,h,cellWidth,cellHeight);
                    rowIndexToFill++;
                }
                int colIndexToFill = currColIndex + 1;
                rowIndexToFill--;
                w= colIndexToFill * cellHeight;
                h = rowIndexToFill * cellWidth;
                graphicsContext.fillRect(w,h,cellWidth,cellHeight);
            }
        }
        /*Prevous' row is bigger than current's row but previous' column is smaller than current's column*/
        if(prevRowIndex > currRowIndex && prevColIndex < currColIndex){
            if(mazeArr[prevRowIndex][prevColIndex+1] == 1 || mazeArr[currRowIndex+1][currColIndex] == 1){
                int colIndexToFill = currColIndex;
                while(colIndexToFill >= prevColIndex){
                    w = colIndexToFill * cellHeight;
                    h = currRowIndex * cellWidth;
                    graphicsContext.fillRect(w,h, cellWidth, cellHeight);
                    colIndexToFill--;
                }
                int rowIndexToFill = currRowIndex + 1;
                colIndexToFill++;
                w= colIndexToFill * cellHeight;
                h= rowIndexToFill * cellWidth;
                graphicsContext.fillRect(w,h, cellWidth, cellHeight);
            }
            else{
                int rowIndexToFill = currRowIndex;
                while(rowIndexToFill <= prevRowIndex){
                    w = currColIndex * cellHeight;
                    h= rowIndexToFill * cellWidth;
                    graphicsContext.fillRect(w,h,cellWidth,cellHeight);
                    rowIndexToFill++;
                }
                int colIndexToFill = prevColIndex + 1;
                rowIndexToFill--;
                w= colIndexToFill * cellHeight;
                h= rowIndexToFill * cellWidth;
                graphicsContext.fillRect(w,h,cellWidth,cellHeight);

            }
        }
    }


}
