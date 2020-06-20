package View;

import Model.MyModel;
import ViewModel.MyViewModel;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.net.URI;


public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MyView.fxml"));
        Parent root = loader.load();
        MyModel model = MyModel.getInstance();
        MyViewModel viewModel = MyViewModel.getInstance();
        MyViewController view = loader.getController();
        model.addObserver(viewModel);
        viewModel.addObserver(view);

        Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        primaryStage.setTitle("Let's Maze");
        primaryStage.setScene(new Scene(root, screenBounds.getWidth(), screenBounds.getHeight()-100));
        primaryStage.setResizable(true);
        Platform.setImplicitExit(true);
        primaryStage.getIcons().add(new javafx.scene.image.Image(new FileInputStream("resources/puzzle.png")));


        primaryStage.show();


    }



    public static void main(String[] args) {
        launch(args);
    }
}
