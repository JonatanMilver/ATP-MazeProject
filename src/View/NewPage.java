package View;

import algorithms.mazeGenerators.Maze;
import algorithms.search.AState;
import algorithms.search.Solution;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
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
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Observable;
import java.util.ResourceBundle;

public class NewPage extends AView implements Initializable {
    public Label colLabel;
    public Label rowsLabel;
    public TextField rowsText;
    public TextField colText;
    public Button generateButton;
    public Button showSolution;

    public MazeDisplayer mazeDisplayer;
    public BorderPane bPane;
    public AnchorPane anchorPane;

    public Label player_row_label = new Label();
    public Label player_col_label = new Label();
    public Button backToMain;
    public StringProperty playerCurrentRow = new SimpleStringProperty();
    public StringProperty playercurrentColumn = new SimpleStringProperty();
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
//            backToGenerate.setVisible(true);
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
//        backToGenerate.setVisible(true);
        bPane.setVisible(true);

    }


    public void drawmaze(int rows, int columns) {
//        if(this.viewModel == null){
////            myViewModel = MyViewModel.getInstance();
//            System.out.println("SHHHHHHHHHHHHit!");
//        }
        viewModel.generateMaze(rows,columns);
        viewModel.getMaze().print();
        mazeDisplayer.drawMaze(viewModel.getMaze());
    }

    public void drawLoadedMaze(){
//        if (mazeDisplayer == null){
//            mazeDisplayer = new MazeDisplayer();
//        }
        if(viewModel.getMaze() == null) {
            Alert mazeNotLoaded = new Alert(Alert.AlertType.WARNING, "Maze not found!");
            mazeNotLoaded.show();
            return;
        }
        viewModel.getMaze().print();
//        backToGenerate.setVisible(true);
        bPane.setVisible(true);
        mazeDisplayer.drawMaze(viewModel.getMaze());
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
//        backToGenerate.setVisible(false);
        bPane.setVisible(false);
        mazeDisplayer.getGraphicsContext2D().clearRect(0,0, mazeDisplayer.getWidth(),mazeDisplayer.getHeight());
        mazeDisplayer.toBack();
    }

    /**
     * Retreives the maze from the file. Sends it to the draw function
     * @param mazeFile File.
     */
    public void showLoadedMaze(File mazeFile){
        viewModel.loadMaze(mazeFile);

    }

    @Override
    public void update(Observable o, Object arg) {
        String operation = (String) arg;
        if(operation.equals("GENERATE")){
            int rows = viewModel.getMaze().getRows();
            int columns = viewModel.getMaze().getColumns();
            mazeDisplayer.drawMaze(viewModel.getMaze());
            showSolution.setVisible(true);
        }
        else if(operation.equals("SOLVE")){
            drawSolution(viewModel.getSolution().getSolutionPath());
        }
        else if(operation.equals("MOVE") || operation.equals("MOVEANDSOLVED")){
            int rowChanged = viewModel.getCharacterRow();
            int colChanged = viewModel.getCharacterCol();
            set_player_position(rowChanged,colChanged);
            if(operation.equals("MOVEANDSOLVED")){
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "It's A-MAZE-ING!");
                alert.show();
//                backToMain(null);
                restartMaze();
            }
        }
        else if(operation.equals("LOAD")){
            drawLoadedMaze();
        }
    }

    private void restartMaze() {
        viewModel.restartMaze();
    }

    private void drawSolution(ArrayList<AState> solutionPath) {
        mazeDisplayer.drawSolution(solutionPath);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        player_row_label.setText(String.valueOf(viewModel.getCharacterRow()));
        player_col_label.setText(String.valueOf(viewModel.getCharacterCol()));
        playerCurrentRow.bind(player_row_label.textProperty());
        playercurrentColumn.bind(player_col_label.textProperty());
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

        viewModel.moveCharacter(keyEvent);
        keyEvent.consume();
//        System.out.println("player_row_position: " + player_row_label.getText());
//        System.out.println("player_col_position: " + player_col_label.getText());
//        System.out.println();
    }

//    private boolean canMove(String direction) {
//        return viewModel.canMove(direction);
//    }

    public void mouseClicked(MouseEvent mouseEvent) {
        mazeDisplayer.requestFocus();
    }

    public void backToMain(MouseEvent mouseEvent) {
        Parent root = null;
        try{
            root = FXMLLoader.load(getClass().getResource("MyView.fxml"));
            Stage stage = (Stage) backToMain.getScene().getWindow();
            stage.setScene(new Scene(root, 501,402));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showSolution(MouseEvent mouseEvent) {
        viewModel.solveMaze();
    }
}
