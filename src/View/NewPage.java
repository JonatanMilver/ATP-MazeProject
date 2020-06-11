package View;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class NewPage {
    public Label colLabel;
    public Label rowsLabel;
    public TextField rowsText;
    public TextField colText;
    public Button generateButton;
    public MazeDisplayer mazeDisplayer;


    public void changeToMazeScene(MouseEvent mouseEvent) {
        if (mouseEvent.getSource() == generateButton){
//            Parent root = null;
//            {
//                try {
//                    root = FXMLLoader.load(getClass().getResource("MazePage.fxml"));
//                } catch (IOException e) {
//                    System.out.println("no such file: MazePage.fxml - NewPageController line 24");
//                }
//            }
//            Stage s =(Stage) generateButton.getScene().getWindow();
//            assert root != null;
//            s.setTitle("It's time to Maze!");
//            s.setScene(new Scene(root , 500 , 450));
            colLabel.setVisible(false);
            rowsLabel.setVisible(false);
            rowsText.setVisible(false);
            colText.setVisible(false);
            generateButton.setVisible(false);
            drawmaze(mouseEvent);
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!11233");
        }
    }

    public void drawmaze(MouseEvent mouseEvent) {
        if (mazeDisplayer == null){
            System.out.println("!!!!!!!");
        }
        int[][] maze = {{1,0,1,0,1,0,0,1,0,0,1,0,0} ,{0,0,0,0,1,1,1,1,1,1,1,0,0}};
        mazeDisplayer.drawMaze(maze);
    }
}
