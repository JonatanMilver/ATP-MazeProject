package View;

import Model.MyModel;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Observable;
import java.util.ResourceBundle;

public class MyViewController extends AView implements Initializable {
    @FXML
    public Button exitButton1;
    @FXML
    public Button mainWindowExitButton;
    public Button newButton;
    public BorderPane menuBarBorderPane;
    public AnchorPane menuBarAnchorPane;
    private NewPage newPage;
    @FXML
    private ImageView logoImage;


    public void handleExitButton(){
        Platform.exit();
        System.exit(0);
    }

    private void getStage(){
        if(exitButton1 != null){
            stage =(Stage) exitButton1.getScene().getWindow();
        }
        else if(mainWindowExitButton != null){
            stage =(Stage) mainWindowExitButton.getScene().getWindow();
        }
    }
    @Override
    public void handleNewButton() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("NewPage.fxml"));
        Parent root = null;
        try {
            root = loader.load();
            newPage = loader.getController();
            viewModel.addObserver(newPage);
            Stage newStage = new Stage();
            newStage.setOnCloseRequest(e->{
                MyModel model = MyModel.getInstance();
                model.stopServers();
            });
            Rectangle2D screenBounds = Screen.getPrimary().getBounds();
            newStage.setScene(new Scene(root, screenBounds.getWidth(), screenBounds.getHeight()-100));
            newStage.setTitle("Let's Maze");
            newStage.getIcons().add(new javafx.scene.image.Image(new FileInputStream("resources/Pictures/puzzle.png")));
            newStage.show();
            Stage stageToClose = ((Stage)newButton.getScene().getWindow());
            stageToClose.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void handleLoadButton() {
        FileChooser fileChooser = new FileChooser();
        getStage();
        configureFileChooser(fileChooser);
        File file = fileChooser.showOpenDialog(stage);
        if (checkIfFileIsMaze(file)){
            Parent root = null;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("NewPage.fxml"));
            try {
                root = loader.load();
                getStage();
                newPage = loader.getController();
                viewModel.addObserver(newPage);
                Stage newStage = new Stage();
                Rectangle2D screenBounds = Screen.getPrimary().getBounds();
                newStage.setScene(new Scene(root, screenBounds.getWidth(), screenBounds.getHeight()-100));
                newStage.setTitle("Let's Maze");
                newStage.setOnCloseRequest(e->{
                    MyModel model = MyModel.getInstance();
                    model.stopServers();
                });
                newStage.getIcons().add(new javafx.scene.image.Image(new FileInputStream("resources/Pictures/puzzle.png")));
                newStage.show();
                Stage stageToClose = ((Stage)newButton.getScene().getWindow());
                stageToClose.close();
                newPage.showLoadedMaze(file);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
            Image logo = null;
            try {
                logo = new Image(new FileInputStream("resources/Pictures/logo1.png"));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            if(logoImage != null) {
                logoImage.setImage(logo);
                logoImage.maxWidth(303);
                logoImage.maxHeight(140);
            }
        }
}
