package View;

import Model.MyModel;
import ViewModel.MyViewModel;
import algorithms.mazeGenerators.Maze;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.Observable;
import java.util.ResourceBundle;

public class NewPage extends AView implements Initializable {
    public Label colLabel;
    public Label rowsLabel;
    public TextField rowsText;
    public TextField colText;
    public Button generateButton;

//    public NewPage(Scene scene, Stage stage, MyViewModel viewModel) {
//        super(scene, stage, viewModel);
//    }


    public MazeDisplayer getMazeDisplayer() {
        return mazeDisplayer;
    }

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
                return;
            }
            try{
                columns = Integer.parseInt(colText.getText());
            }
            catch(NumberFormatException e){
                Alert alert = new Alert(Alert.AlertType.WARNING, "Number of columns must be a number!");
                alert.show();
                return;
            }
//Should check if rows and columns are bigger than 1.
//            hideButtons();
            backToGenerate.setVisible(true);
            bPane.setVisible(true);
            drawmaze(rows, columns);
        }
    }

    /**
     * Hide all buttons and show the canvas(the maze).
     */
    public void hideButtons(){
        bPane.prefHeightProperty().bind(mazeDisplayer.heightProperty());
        bPane.prefWidthProperty().bind(mazeDisplayer.widthProperty());
        colLabel.setVisible(false);
        rowsLabel.setVisible(false);
        rowsText.setVisible(false);
        colText.setVisible(false);
        generateButton.setVisible(false);
        backToGenerate.setVisible(true);
        bPane.setVisible(true);

    }

    public void drawmaze(int rows, int columns) {
        if(myViewModel == null){
            myViewModel = new MyViewModel();
        }
        Maze mazeToGenerate = myViewModel.generateMaze(rows,columns);
        mazeToGenerate.print();
        mazeDisplayer.drawMaze(mazeToGenerate);
    }

    public void drawLoadedMaze(Maze maze){
        if (mazeDisplayer == null){
            mazeDisplayer = new MazeDisplayer();
        }
        maze.print();
        backToGenerate.setVisible(true);
        bPane.setVisible(true);
        mazeDisplayer.drawMaze(maze);
    }

    /**
     * Sets all controllers visible to be able to generate a new maze
     * @param mouseEvent
     */
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

    /**
     * Retreives the maze from the file. Sends it to the draw function
     * @param mazeFile File.
     */
    public void showLoadedMaze(File mazeFile){
        if(myViewModel == null)
            myViewModel = new MyViewModel();
        Maze maze = myViewModel.loadMaze(mazeFile);
        drawLoadedMaze(maze);
    }

    @Override
    public void update(Observable o, Object arg) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void keyPressed(KeyEvent keyEvent) {
        int player_row_position = mazeDisplayer.getPlayerRow();
        int player_col_position = mazeDisplayer.getPlayerColumn();
        switch (keyEvent.getCode()) {
            case UP:
                if(canMove("UP")){
                    mazeDisplayer.set_player_position(player_row_position - 1, player_col_position);
                    break;
                }

            case DOWN:
                if(canMove("DOWN")) {
                    mazeDisplayer.set_player_position(player_row_position + 1, player_col_position);

                    break;
                }
            case RIGHT:
                if(canMove("RIGHT")) {
                    mazeDisplayer.set_player_position(player_row_position, player_col_position + 1);
                    break;
                }
            case LEFT:
                if(canMove("LEFT")) {
                    mazeDisplayer.set_player_position(player_row_position, player_col_position - 1);
                    break;
                }
            case W:
                mazeDisplayer.set_player_position(player_row_position - 1, player_col_position);
                break;
            case S:
                mazeDisplayer.set_player_position(player_row_position + 1, player_col_position);
                break;
            case D:
                mazeDisplayer.set_player_position(player_row_position, player_col_position + 1);
                break;
            case A:
                mazeDisplayer.set_player_position(player_row_position, player_col_position - 1);
                break;
            default:
                mazeDisplayer.set_player_position(player_row_position, player_col_position);

        }
        keyEvent.consume();
    }

    private boolean canMove(String direction) {
        return myViewModel.canMove(direction);
    }

    public void mouseClicked(MouseEvent mouseEvent) {
        mazeDisplayer.requestFocus();
    }
}
