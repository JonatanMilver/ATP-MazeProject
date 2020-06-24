package View;

import ViewModel.MyViewModel;
import algorithms.mazeGenerators.Maze;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.swing.filechooser.FileSystemView;
import java.io.*;
import java.util.Observable;
import java.util.Observer;

public abstract class AView implements IView, Observer {
    protected Scene scene;
    protected Stage stage;
    protected MyViewModel viewModel = MyViewModel.getInstance();


    /**
     * Opens a new stage of sizes vXv1 with the title of stateTitle.
     * Loads the fxmlFileName using FXMLLoader.
     * @param fxmlFileName String
     * @param stageTitle String
     * @param v double
     * @param v1 double
     * @return
     */
    public Stage openNewStage(String fxmlFileName, String stageTitle, double v, double v1){
        Stage newStage = new Stage();
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlFileName));
            newStage.setTitle(stageTitle);
            newStage.setScene(new Scene(root, v, v1));


        }
        catch(IOException e){
            e.printStackTrace();
        }
        return newStage;
    }

    /**
    * Being used while quitting the app.
    */
    public void handleExitButton(){
        Platform.exit();
        System.exit(0);
    }

    @Override
    public void handleNewButton() {

    }

    @Override
    public void handleLoadButton() {

    }

    /**
     * @param fileChooser FileChooser
     */
    protected static void configureFileChooser(final FileChooser fileChooser){
        fileChooser.setTitle("Choose a maze");
        fileChooser.setInitialDirectory(
                new File(FileSystemView.getFileSystemView().getHomeDirectory().getAbsolutePath())
        );
    }

    /**
     * Checks whether a given file contains a maze.
     * @param file File
     * @return boolean
     */
    public boolean checkIfFileIsMaze(File file){
        if (file == null){
            showAlert(Alert.AlertType.WARNING , "file is Null!");
            return false;
        }
        String filePath = file.getPath();
        String fileExtension = filePath.substring(filePath.lastIndexOf('.'));
        if(!fileExtension.contains(".maze")) {
            showAlert(Alert.AlertType.WARNING,"Chosen file is not a maze!");
            return false;
        }
        return true;
    }

    /**
     * Opens an alert and shows it.
     * @param alertType Alert.AlertType
     * @param s String
     */
    public void showAlert(Alert.AlertType alertType , String s){
        Alert alert = new Alert(alertType, s);
        alert.show();
    }

    /**
     *  Handles save logic while pressing "SAVE".
     */
    @Override
    public void handleSaveButton() {
        FileChooser fileChooser = new FileChooser();
        configureFileChooser(fileChooser);
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("maze files (*.maze)", "*.maze");
        fileChooser.getExtensionFilters().add(extFilter);

        Maze mazeToSave = viewModel.getMaze();
        if(mazeToSave == null){
            Alert noMaze = new Alert(Alert.AlertType.ERROR, "There is no maze to save!");
            noMaze.show();
            return;
        }

        File file = fileChooser.showSaveDialog(stage);

        if(file != null) {
            writeMazeToFile(mazeToSave, file.getPath());
        }
    }

    /**
     * Saves given maze to a file "FILENAME.maze"
     * @param mazeFromClient Maze
     * @param path String
     */
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

    /**
     * Handles "ABOUT US" logic.
     */
    @Override
    public void handleAboutButton() {
        Stage aboutStage = openNewStage("AboutUs1.fxml","About",707,619);
        try {
            aboutStage.getIcons().add(new javafx.scene.image.Image(new FileInputStream("resources/Pictures/puzzle.png")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        aboutStage.show();
    }

    /**
     * handle "Properties" logic.
     */
    @Override
    public void handlePropertiesButton() {
        Stage propertiesStage = openNewStage("Properties.fxml", "Properties", 535,299);
        try {
            propertiesStage.getIcons().add(new javafx.scene.image.Image(new FileInputStream("resources/Pictures/puzzle.png")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        propertiesStage.show();
    }

    /**
     * Handles "Help" logic.
     */
    @Override
    public void handleHelpButton() {
        String hints =
                        "* To start a new game, enter the amount of rows and columns and then press the 'Generate' button.\n" +
                        "* You can save the maze you've just started at any time and reload it whenever you want. \n"+
                        "* By saving a maze, your current position would not be save, only the maze itself.\n"+
                        "* In order to save a maze press File->Save and choose your maze's name.\n"+
                        "* If you have a maze that you want to continue solving, press File->Load and there you go!.\n "+
                        "* You need to reach the goal position.\n" +
                        "* You can not go through walls.\n" +
                        "* Whenever help is needed, press the Show Solution button in order to get the solution.\n" +
                        "* You can go anywhere by your position if possible.\n" +
                        "* Possible steps are: Up, Down, Left, Right and Diagonally.\n" +
                        "* Diagonal steps are up-left, up-right, down-left, down-right - only if these steps are possible.\n"+
                        "* Choose a story you want to be a part of by pressing the bar under 'Choose a story'.\n "+
                        "* It is worth reaching the goal - there is a cool video waiting for you!\n"
                ;
        Alert dialogBox = new Alert(Alert.AlertType.INFORMATION, hints, ButtonType.OK);
        dialogBox.setHeaderText("TIPS AND HINTS");

        dialogBox.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        dialogBox.setResizable(true);
        dialogBox.setTitle("Help");
        DialogPane dialogPane = dialogBox.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("view.css").toExternalForm());
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
