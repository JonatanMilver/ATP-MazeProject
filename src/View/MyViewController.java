package View;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Observable;
import java.util.ResourceBundle;

public class MyViewController extends AView implements Initializable {


    @FXML
    public Label exitButton;
    @FXML
    public Button exitButton1;
    @FXML
    public Button mainWindowExitButton;
    @FXML
    public MenuItem newGameButton;
    @FXML
    public BorderPane menuBorderPane;
    @FXML
    public MenuItem loadGameButton;
    @FXML
    private Button backFromAbout;
    @FXML
    public MediaView mainMedia;
    private Media me;
    private MediaPlayer mp;


    public void handleExitButton(){
    Platform.exit();
}

    private void getStage(){
        if(exitButton != null){
            stage =(Stage) exitButton.getScene().getWindow();
        }
        else if(mainWindowExitButton != null){
            stage =(Stage) mainWindowExitButton.getScene().getWindow();
        }
    }
    @Override
    public void handleNewButton() {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("NewPage.fxml"));
            getStage();
            stage.setScene(new Scene(root , 500, 450));
            stage.setMaximized(true);

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
        if (file != null) {
            String filePath = file.getPath();
            String fileExtension = filePath.substring(filePath.lastIndexOf('.'), filePath.length());
            if(!fileExtension.contains(".maze")){
                Alert alert = new Alert(Alert.AlertType.WARNING, "Chosen file is not a maze!");
                alert.show();
                return;
            }
            Parent root = null;
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("NewPage.fxml"));
                root = loader.load();
//                root = FXMLLoader.load(getClass().getResource("NewPage.fxml"));
                getStage();
                stage.setScene(new Scene(root , 500, 450));
                stage.setMaximized(true);
                NewPage newPageControls = loader.getController();
                newPageControls.showLoadedMaze(file);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    private static void configureFileChooser(final FileChooser fileChooser){
        fileChooser.setTitle("Choose a maze");
        fileChooser.setInitialDirectory(
                new File(System.getProperty("java.io.tmpdir"))
        );
    }

    private void openFile(File file) {
//        try {
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
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
        String hints =
                "* You nead to reach the goal position\n" +
                        "* You can not go through walls\n" +
                        "* Whenever help is needed, press the SOLVE button in order to get the solution\n" +
                        "* You can go anywhere by your possition if possible\n" +
                        "* Possible steps are: Up, Down, Left, Right and Diagonally.\n" +
                        "* Diagonal steps are up-left, up-right, down-left, down-right only if these steps are possible."
                ;
        Alert dialogBox = new Alert(Alert.AlertType.INFORMATION, hints, ButtonType.OK);
        dialogBox.setHeaderText("TIPS AND HINTS");

        //dialogVbox.setContentText(hints);
        dialogBox.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        dialogBox.setResizable(true);
        dialogBox.setTitle("Help");
        DialogPane dialogPane = dialogBox.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("view.css").toExternalForm());
        dialogPane.getStyleClass().add("hints");


        dialogBox.show();
    }


    @Override
    public void update(Observable o, Object arg) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String mediaPath = new File("src/View/Resources/background.mp4").getAbsolutePath();
        me = new Media(new File(mediaPath).toURI().toString());
        mp = new MediaPlayer(me);
        mainMedia = new MediaView();
        mainMedia.setMediaPlayer(mp);
        mp.setAutoPlay(true);
        mainMedia.setFitHeight(400);
        mainMedia.setFitWidth(400);
    }

    public void quitFromAbout(MouseEvent mouseEvent) {
        if(mouseEvent.getSource() == backFromAbout){
            Stage aboutStage = (Stage)backFromAbout.getScene().getWindow();
            aboutStage.close();
        }
    }
}
