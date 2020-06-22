package View;

import ViewModel.MyViewModel;
import algorithms.mazeGenerators.Maze;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
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
    public Stage openNewStage(String fxmlFileName, String stageTitle, double v, double v1){
        Stage newStage = new Stage();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("../" + fxmlFileName));
            newStage.setTitle(stageTitle);
            newStage.setScene(new Scene(root, v, v1));


        }
        catch(IOException e){
            e.printStackTrace();
        }
        return newStage;
    }

    @Override
    public void handleExitButton() {

    }

    @Override
    public void handleNewButton() {

    }

    @Override
    public void handleLoadButton() {

    }

    protected static void configureFileChooser(final FileChooser fileChooser){
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
        if(mazeToSave == null){
            Alert noMaze = new Alert(Alert.AlertType.ERROR, "There is no maze to save!");
            noMaze.show();
            return;
        }

        File file = fileChooser.showSaveDialog(stage);

        //need to check if person closes (X) the file chooser!!!
        if(file != null) {
            writeMazeToFile(mazeToSave, file.getPath());
        }
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
        Stage aboutStage = openNewStage("View/AboutUs.fxml","About",500,405);
        try {
            aboutStage.getIcons().add(new javafx.scene.image.Image(new FileInputStream("resources/puzzle.png")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        aboutStage.show();
    }

    @Override
    public void handlePropertiesButton() {
        Stage propertiesStage = openNewStage("View/Properties.fxml", "Properties", 535,299);
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
                        "* Whenever help is needed, press the SOLVE button in order to get the solution\n" +
                        "* You can go anywhere by your possition if possible\n" +
                        "* Possible steps are: Up, Down, Left, Right and Diagonally.\n" +
                        "* Diagonal steps are up-left, up-right, down-left, down-right only if these steps are possible."
                ;
        Alert dialogBox = new Alert(Alert.AlertType.INFORMATION, hints, ButtonType.OK);
        dialogBox.setHeaderText("TIPS AND HINTS");

        dialogBox.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        dialogBox.setResizable(true);
        dialogBox.setTitle("Help");
        DialogPane dialogPane = dialogBox.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("../view.css").toExternalForm());
        dialogPane.getStyleClass().add("hints");


        dialogBox.show();
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
