package View;

import ViewModel.MyViewModel;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class MyViewController implements IView{

    public Label exitButton;
    public MenuItem newButton;
    private MyViewModel viewModel;

    public MyViewController() {
        this.viewModel = new MyViewModel();
//        this.mazeDisplayer = new MazeDisplayer();
    }

    public void handleExitButton(){
//        System.out.println("Exit");
        viewModel.showMaze();
        Platform.exit();

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
        Stage aboutStage = new Stage();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("AboutUs.fxml"));
            aboutStage.setTitle("About");
            aboutStage.setScene(new Scene(root, 500, 450));
            aboutStage.setMaximized(true);
            aboutStage.show();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void handlePropertiesButton() {

    }

    @Override
    public void handleHelpButton() {
        String hints = "Hints:\n" +
                "* You nead to reach the goal position\n" +
                "* You can not go through walls\n" +
                "* Whenever help is needed, press the SOLVE button in order to get the solution\n" +
                "* You can go anywhere by your possition if possible\n" +
                "* Possible steps are: Up, Down, Left, Right and Diagonally.\n" +
                "Diagonal steps are up-left, up-right, down-left, down-right only if these steps are possible."
                ;
        Alert dialogBox = new Alert(Alert.AlertType.INFORMATION, hints, ButtonType.OK);
        dialogBox.setHeaderText("TIPS AND HINTS");

        //dialogVbox.setContentText(hints);
        dialogBox.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        dialogBox.setResizable(true);
        DialogPane dialogPane = dialogBox.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("view.css").toExternalForm());
        dialogPane.getStyleClass().add("hints");


        dialogBox.show();
    }


}
