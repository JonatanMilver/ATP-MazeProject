package View;

import algorithms.mazeGenerators.Maze;
import javafx.beans.property.IntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

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

    public MazeDisplayer mazeDisplayer;
    public Button backToGenerate;
    public BorderPane bPane;
    public AnchorPane anchorPane;

    public Label player_row_label = new Label();
    public Label player_col_label = new Label();
//    public int player_row_position;
//    public int player_col_position;


    public MazeDisplayer getMazeDisplayer() {
        return mazeDisplayer;
    }

//    private MyViewModel myViewModel;


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
            mouseClicked(null);
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
//        if(this.viewModel == null){
////            myViewModel = MyViewModel.getInstance();
//            System.out.println("SHHHHHHHHHHHHit!");
//        }
        Maze mazeToGenerate = viewModel.generateMaze(rows,columns);
        mazeToGenerate.print();
        mazeDisplayer.drawMaze(mazeToGenerate);
    }

    public void drawLoadedMaze(Maze maze){
//        if (mazeDisplayer == null){
//            mazeDisplayer = new MazeDisplayer();
//        }
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
//        if(myViewModel == null)
//            myViewModel = new MyViewModel();
//            myViewModel = MyViewModel
        Maze maze = viewModel.loadMaze(mazeFile);
        drawLoadedMaze(maze);
    }

    @Override
    public void update(Observable o, Object arg) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        viewModel.playerCurrentRow.bind(player_row_label.textProperty());
        viewModel.playercurrentColumn.bind(player_col_label.textProperty());
    }

    public void set_player_position(int rowPosition , int colPosition){
        mazeDisplayer.set_player_position(rowPosition , colPosition);
        player_row_label.setText(String.valueOf(rowPosition));
        player_col_label.setText(String.valueOf(colPosition));
    }

    public void keyPressed(KeyEvent keyEvent) {
//        System.out.println("getMazeDisplayer().getPlayerRow(): "+getMazeDisplayer().getPlayerRow());
//        System.out.println("getMazeDisplayer().getPlayerColumn(): "+ getMazeDisplayer().getPlayerColumn());
//        System.out.println("mazeDisplayer.getPlayerRow(): "+mazeDisplayer.getPlayerRow());
//        System.out.println("mazeDisplayer.getPlayerColumn(): "+mazeDisplayer.getPlayerColumn());
//        System.out.println("viewModel.playerCurrentRow: "+viewModel.playerCurrentRow.getValue());
//        System.out.println("viewModel.playercurrentColumn: "+viewModel.playercurrentColumn.getValue());

        int player_row_position = getMazeDisplayer().getPlayerRow();
        int player_col_position = getMazeDisplayer().getPlayerColumn();

        player_row_label.setText(String.valueOf(player_row_position));
        player_col_label.setText(String.valueOf(player_col_position));

        switch (keyEvent.getCode()) {
            case UP:
                if(canMove("UP")){
//                    mazeDisplayer.set_player_position(player_row_position - 1, player_col_position);
                    set_player_position(player_row_position - 1, player_col_position);
                }
                break;
            case DOWN:
                if(canMove("DOWN")) {
//                    mazeDisplayer.set_player_position(player_row_position + 1, player_col_position);
                    set_player_position(player_row_position + 1, player_col_position);
                }
                break;
            case RIGHT:
                if(canMove("RIGHT")) {
//                    mazeDisplayer.set_player_position(player_row_position, player_col_position + 1);
                    set_player_position(player_row_position, player_col_position + 1);
                }
                break;
            case LEFT:
                if(canMove("LEFT")) {
//                    mazeDisplayer.set_player_position(player_row_position, player_col_position - 1);
                    set_player_position(player_row_position, player_col_position - 1);
                }
                break;
//            case W:
//                mazeDisplayer.set_player_position(player_row_position - 1, player_col_position);
//                break;
//            case S:
//                mazeDisplayer.set_player_position(player_row_position + 1, player_col_position);
//                break;
//            case D:
//                mazeDisplayer.set_player_position(player_row_position, player_col_position + 1);
//                break;
//            case A:
//                mazeDisplayer.set_player_position(player_row_position, player_col_position - 1);
//                break;
            default:
//                mazeDisplayer.set_player_position(player_row_position, player_col_position);
                set_player_position(player_row_position, player_col_position);

        }
        keyEvent.consume();
        System.out.println("player_row_position: " + player_row_position);
        System.out.println("player_col_position: " + player_col_position);
    }

    private boolean canMove(String direction) {
        return viewModel.canMove(direction);
    }

    public void mouseClicked(MouseEvent mouseEvent) {
        mazeDisplayer.requestFocus();
    }
}
