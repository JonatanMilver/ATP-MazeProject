package View;

import Model.MyModel;
import algorithms.mazeGenerators.Maze;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.*;
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
    @FXML
    private Button backFromAbout;
    private NewPage newPage;
    @FXML
    private MediaView mv;
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../NewPage.fxml"));
        Parent root = null;
        try {
            root = loader.load();

            newPage = loader.getController();
            viewModel.addObserver(newPage);
//            getStage();
            Stage newStage = new Stage();
            newStage.setOnCloseRequest(e->{
                MyModel model = MyModel.getInstance();
                model.stopServers();
            });
            Rectangle2D screenBounds = Screen.getPrimary().getBounds();
            newStage.setScene(new Scene(root, screenBounds.getWidth(), screenBounds.getHeight()-100));
            newStage.setTitle("Let's Maze");
            newStage.getIcons().add(new javafx.scene.image.Image(new FileInputStream("resources/puzzle.png")));
//            newStage.setFullScreen(true);
            newStage.show();
            Stage stageToClose = ((Stage)newButton.getScene().getWindow());
//            stageToClose.fireEvent(new WindowEvent(stageToClose, WindowEvent.WINDOW_CLOSE_REQUEST));
            stageToClose.close();



//            stage.setScene(scene);

//            stage.setMaximized(true);
//            stage.setFullScreen(true);
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../NewPage.fxml"));
            try {
                root = loader.load();
                getStage();
                newPage = loader.getController();
                viewModel.addObserver(newPage);
                Stage newStage = new Stage();
                Rectangle2D screenBounds = Screen.getPrimary().getBounds();
                newStage.setScene(new Scene(root, screenBounds.getWidth(), screenBounds.getHeight()-100));
                newStage.setTitle("Let's Maze");
                newStage.getIcons().add(new javafx.scene.image.Image(new FileInputStream("resources/puzzle.png")));
                newStage.show();
                Stage stageToClose = ((Stage)newButton.getScene().getWindow());
                stageToClose.close();

                newPage.showLoadedMaze(file);
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



    @Override
    public void handleSaveButton() {
        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("maze files (*.maze)", "*.maze");
        fileChooser.getExtensionFilters().add(extFilter);

        Maze mazeToSave = viewModel.getMaze();

        File file = fileChooser.showSaveDialog(stage);

        writeMazeToFile(mazeToSave , file.getPath());
    }

    private void writeMazeToFile(Maze mazeFromClient , String path) {
        try {
            FileOutputStream file = new FileOutputStream(path);
            ObjectOutputStream oos = new ObjectOutputStream(file);
            oos.writeObject(mazeFromClient);
            oos.flush();
            oos.close();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleAboutButton() {
        Stage aboutStage = openNewStage("AboutUs.fxml","About",500,405);
        try {
            aboutStage.getIcons().add(new javafx.scene.image.Image(new FileInputStream("resources/puzzle.png")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        aboutStage.show();
    }

    @Override
    public void handlePropertiesButton() {
        Stage propertiesStage = openNewStage("Properties.fxml", "Properties", 350,200);
        try {
            propertiesStage.getIcons().add(new javafx.scene.image.Image(new FileInputStream("resources/puzzle.png")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        propertiesStage.show();
    }

    @Override
    public void handleHelpButton() {
        String hints =
                "* You nead to reach the goal position\n" +
                        "* You can not go through walls\n" +
                        "* Whenever help is needed, press the 'SHOW SOLUTION' button in order to get the solution\n" +
                        "The 'SHOW SOLUTION' button is active only when you are in the middle of a game\n"+
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
            Image logo = null;
            try {
                logo = new Image(new FileInputStream("resources/logo1.png"));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            if(logoImage != null) {
                logoImage.setImage(logo);
                logoImage.setX(300);
                logoImage.setFitWidth(303);
            }

        }



    public void quitFromAbout(MouseEvent mouseEvent) {
        if(mouseEvent.getSource() == backFromAbout){
            Stage aboutStage = (Stage)backFromAbout.getScene().getWindow();
            aboutStage.close();
        }
    }


}
