package View;

import ViewModel.MyViewModel;
import algorithms.mazeGenerators.Maze;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class NewPage {
    public Label colLabel;
    public Label rowsLabel;
    public TextField rowsText;
    public TextField colText;
    public Button generateButton;
    public MazeDisplayer mazeDisplayer;
    public Button backToGenerate;
    public BorderPane bPane;
    public AnchorPane anchorPane;


    private MyViewModel myViewModel;


    public void changeToMazeScene(MouseEvent mouseEvent) {
        if (mouseEvent.getSource() == generateButton){
            int rows=0;
            int columns = 0;
            try {
                rows = Integer.parseInt(rowsText.getText());
            }
            catch(NumberFormatException e){
                Alert alert = new Alert(Alert.AlertType.WARNING, "Number of rows must be a number!");
                alert.show();
            }
            try{
                columns = Integer.parseInt(colText.getText());
            }
            catch(NumberFormatException e){
                Alert alert = new Alert(Alert.AlertType.WARNING, "Number of columns must be a number!");
                alert.show();
            }
//Should check if rows and columns are bigger than 1.
//            mazeDisplayer.prefWidth(bPane.getWidth());
//            mazeDisplayer.prefHeight(bPane.getHeight());
            bPane.prefHeightProperty().bind(mazeDisplayer.heightProperty());
            bPane.prefWidthProperty().bind(mazeDisplayer.widthProperty());
            colLabel.setVisible(false);
            rowsLabel.setVisible(false);
            rowsText.setVisible(false);
            colText.setVisible(false);
            generateButton.setVisible(false);
            backToGenerate.setVisible(true);
            bPane.setVisible(true);
            drawmaze(rows, columns);
        }
    }

    public void drawmaze(int rows, int columns) {
        if (mazeDisplayer == null){
            System.out.println("!!!!!!!");
        }
        if(myViewModel == null){
            myViewModel = new MyViewModel();
        }
//        int[][] maze = {{1,0,1,0,1,0,0,1,0,0,1,0,0} ,{0,0,0,0,1,1,1,1,1,1,1,0,0}};
        Maze mazeToGenerate = myViewModel.generateMaze(rows,columns);
        mazeToGenerate.print();
        mazeDisplayer.drawMaze(mazeToGenerate);
        //mazeDisplayer.toFront();
    }

    public void goBackToGenerate(MouseEvent mouseEvent) {
        colLabel.setVisible(true);
        rowsLabel.setVisible(true);
        rowsText.setVisible(true);
        colText.setVisible(true);
        generateButton.setVisible(true);
        backToGenerate.setVisible(false);
        bPane.setVisible(false);
        mazeDisplayer.getGraphicsContext2D().clearRect(0,0, mazeDisplayer.getWidth(),mazeDisplayer.getHeight());
        mazeDisplayer.toBack();
    }
}
