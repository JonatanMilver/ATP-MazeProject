package View;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
//        MyViewController viewController = new MyViewController();
        Parent root = FXMLLoader.load(getClass().getResource("MyView.fxml"));
        primaryStage.setTitle("Let's Maze");
        primaryStage.setScene(new Scene(root, 500, 450));
        primaryStage.show();
    }

    public void changeScene(String fxml, Stage primaryStage) throws IOException {
        Parent pane = FXMLLoader.load(getClass().getResource(fxml));

        primaryStage.getScene().setRoot(pane);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
