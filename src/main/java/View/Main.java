package View;
import Model.MyModel;
import ViewModel.MyViewModel;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;



import java.io.FileInputStream;



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
        primaryStage.setOnCloseRequest(e->{
            model.stopServers();
        });



        Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        primaryStage.setTitle("Let's Maze");
        primaryStage.setScene(new Scene(root, 501, 404));
        primaryStage.setResizable(true);
        Platform.setImplicitExit(true);
        primaryStage.getIcons().add(new javafx.scene.image.Image(new FileInputStream("resources/Pictures/puzzle.png")));


        primaryStage.show();


    }



    public static void main(String[] args) {
        launch(args);
    }
}
