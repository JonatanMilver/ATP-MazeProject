package View;

import Model.MyModel;
import ViewModel.MyViewModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.*;


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


        primaryStage.setTitle("Let's Maze");
        primaryStage.setScene(new Scene(root, 501, 402));
//        primaryStage.setMaximized(true);
        primaryStage.setFullScreen(true);

        primaryStage.show();


    }



    public static void main(String[] args) {
        launch(args);
    }
}
