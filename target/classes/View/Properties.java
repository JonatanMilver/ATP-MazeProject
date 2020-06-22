package View;

import Server.Server;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

import java.awt.event.MouseEvent;

public class Properties extends AView {
    @FXML
    private MenuButton mazeAlgorithm;
    @FXML
    private MenuButton solvingAlgorithm;
    @FXML
    private MenuItem MyMazeGenerator;
    @FXML
    private MenuItem SimpleMazeGenerator;
    @FXML
    private MenuItem EmptyMazeGenerator;
    @FXML
    private MenuItem Best;
    @FXML
    private MenuItem BFS;
    @FXML
    private MenuItem DFS;
    @FXML
    private Button cancel;
    @FXML
    private Button apply;


    public void mouseClicked(ActionEvent actionEvent) {
        if(actionEvent.getSource() == MyMazeGenerator || actionEvent.getSource() == SimpleMazeGenerator || actionEvent.getSource() == EmptyMazeGenerator){
            mazeAlgorithm.setText(((MenuItem)actionEvent.getSource()).getText());
        }
        if(actionEvent.getSource() == Best || actionEvent.getSource() == BFS || actionEvent.getSource() == DFS ){
            solvingAlgorithm.setText(((MenuItem)actionEvent.getSource()).getText());
        }
        if(actionEvent.getSource() == cancel){
            Stage stage = (Stage) cancel.getScene().getWindow();
            stage.close();
            System.out.println("Pressed CANCEL");
        }
        if(actionEvent.getSource() == apply){
            Server.setConfigurations("GeneratingAlgorithm",mazeAlgorithm.getText());
            Server.setConfigurations("SearchingAlgorithm", solvingAlgorithm.getText());
            ((Stage)apply.getScene().getWindow()).close();
            System.out.println("Pressed APPLY");
        }

    }

}
