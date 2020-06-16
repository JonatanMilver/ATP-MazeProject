package View;

import ViewModel.MyViewModel;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

public abstract class AView implements IView, Observer {
    protected Scene scene;
    protected Stage stage;
//    protected MyViewModel viewModel = new MyViewModel();
    protected MyViewModel viewModel = MyViewModel.getInstance();


//    public AView(Scene scene, Stage stage, MyViewModel viewModel) {
//        this.scene = scene;
//        this.stage = stage;
//        this.viewModel = viewModel;
//    }


    @Override
    public void handleExitButton() {

    }

    @Override
    public void handleNewButton() {

    }

    @Override
    public void handleLoadButton() {

    }

    @Override
    public void handleSaveButton() {

    }

    @Override
    public void handleAboutButton() {

    }

    @Override
    public void handlePropertiesButton() {

    }

    @Override
    public void handleHelpButton() {

    }

    public Stage newStage(String path , String title, int width , int height){
        Stage new_stage = new Stage();
        try {
            Parent root = FXMLLoader.load(getClass().getResource(path));
            new_stage.setTitle(title);
            new_stage.setScene(new Scene(root , width , height));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new_stage;
    }


    @Override
    public void update(Observable o, Object arg) {

    }
}
