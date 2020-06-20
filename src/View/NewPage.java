package View;

import algorithms.mazeGenerators.Maze;
import algorithms.search.AState;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Observable;
import java.util.ResourceBundle;

public class NewPage extends AView implements Initializable {
    public Label colLabel;
    public Label rowsLabel;
    public TextField rowsText;
    public TextField colText;
    public Button generateButton;
    public Button showSolution;

    public MazeDisplayer mazeDisplayer;
    public AnchorPane bPane;
    public AnchorPane anchorPane;
    public MediaView musicMV;
    public MediaView mv;
    public Label moveCheckLabel;
    @FXML
    private MenuButton story;
    @FXML
    private MenuItem michaelJordan;
    @FXML
    private MenuItem whiskey;
    private int storyNumber = 0;

    public Label player_row_label = new Label();
    public Label player_col_label = new Label();
    public StringProperty playerCurrentRow = new SimpleStringProperty();
    public StringProperty playercurrentColumn = new SimpleStringProperty();



    public void handleExitButton(){
        Platform.exit();
    }

    @Override
    public void handleNewButton() {
        bPane.setVisible(false);
        showSolution.setVisible(false);
    }

    public void loadGame(ActionEvent actionEven) {
        FileChooser fileChooser = new FileChooser();
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

            showLoadedMaze(file);

        }
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
        Stage aboutStage = openNewStage("AboutUs.fxml","About",500,405);
        aboutStage.show();
    }

    @Override
    public void handlePropertiesButton() {
        Stage propertiesStage = openNewStage("Properties.fxml", "Properties", 350,200);
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
        dialogPane.getStylesheets().add(getClass().getResource("view.css").toExternalForm());
        dialogPane.getStyleClass().add("hints");


        dialogBox.show();
    }

    public void changeToMazeScene(MouseEvent mouseEvent) {
        if (mouseEvent.getSource() == generateButton){
            String rows = rowsText.getText();
            String columns =colText.getText();
            drawmaze(rows, columns);

        }
    }



    public void drawmaze(String rows, String columns) {
        viewModel.generateMaze(rows,columns);
    }


    public void drawLoadedMaze(){
        if(viewModel.getMaze() == null) {
            Alert mazeNotLoaded = new Alert(Alert.AlertType.WARNING, "Maze not found!");
            mazeNotLoaded.show();
            return;
        }
        viewModel.getMaze().print();
        bPane.setVisible(true);
        if(storyNumber == 1)
            bPane.setStyle("-fx-background-image: url('parket.jpg')");
        else if(storyNumber == 2)
            bPane.setStyle("-fx-background-image: url('grass.jpg')");
        mazeDisplayer.setWidth(bPane.getWidth());
        mazeDisplayer.setHeight(bPane.getHeight());
        mazeDisplayer.drawMaze(viewModel.getMaze());
    }


    /**
     * Retreives the maze from the file. Sends it to the draw function
     * @param mazeFile File.
     */
    public void showLoadedMaze(File mazeFile){
        viewModel.loadMaze(mazeFile);

    }

    @Override
    public void update(Observable o, Object arg) {
        String operation = (String) arg;
        if(operation.equals("GENERATE")){
            int rows = viewModel.getMaze().getRows();
            int columns = viewModel.getMaze().getColumns();
            bPane.setVisible(true);
            if(storyNumber == 1)
                bPane.setStyle("-fx-background-image: url('parket.jpg')");
            else if(storyNumber == 2)
                bPane.setStyle("-fx-background-image: url('grass.jpg')");
            mouseClicked(null);
            mazeDisplayer.drawMaze(viewModel.getMaze());
            mazeDisplayer.setDrawSolution(false);
            showSolution.setText("Show Solution");
            showSolution.setVisible(true);
        }
        else if(operation.equals("SOLVE")){
            if(!mazeDisplayer.isAskedToShowSolution()){
                mazeDisplayer.setDrawSolution(true);
                showSolution.setText("Hide Solution");
            }
            else {
                mazeDisplayer.setDrawSolution(false);
                showSolution.setText("Show Solution");
            }
            drawSolution(viewModel.getSolution().getSolutionPath());
        }
        else if(operation.equals("MOVE") || operation.equals("MOVEANDSOLVED")){
            int rowChanged = viewModel.getCharacterRow();
            int colChanged = viewModel.getCharacterCol();
            set_player_position(rowChanged,colChanged);
            if(mazeDisplayer.isAskedToShowSolution())
                drawSolution(viewModel.getSolution().getSolutionPath());
            if(operation.equals("MOVEANDSOLVED")){
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "It's A-MAZE-ING!");
                alert.showAndWait();
                restartMaze();
            }
        }
        else if(operation.equals("LOAD")){
            drawLoadedMaze();
            showSolution.setVisible(true);
        }
        else if(operation.equals("INVALIDINPUT")){
            Alert invalidInput = new Alert(Alert.AlertType.ERROR, "One or more of the inputs were invalid.");
            invalidInput.show();
        }
        else if(operation.equals("NULLMAZE")){
            Alert nullMaze = new Alert(Alert.AlertType.ERROR, "File doesn't contain a maze, please try again.");
            nullMaze.show();
        }
    }

    private void restartMaze() {
        viewModel.restartMaze();
    }

    private void listenToResize(){
        anchorPane.widthProperty().addListener((obs, oldVal, newVal) -> {
//            mv.setFitWidth(anchorPane.getWidth());
//            mv.setFitHeight(anchorPane.getHeight());
            bPane.setMinWidth(anchorPane.getWidth());
            if(viewModel.getMaze() != null)
                mazeDisplayer.drawMaze(viewModel.getMaze());
        });
        anchorPane.heightProperty().addListener((obs, oldVal, newVal) -> {
//            mv.setFitWidth(anchorPane.getWidth());
//            mv.setFitHeight(anchorPane.getHeight());

            bPane.setMinHeight(anchorPane.getHeight());
            if(viewModel.getMaze() != null)
                mazeDisplayer.drawMaze(viewModel.getMaze());
        });
        bPane.widthProperty().addListener((obs, oldVal, newVal) -> {
            mazeDisplayer.setWidth(bPane.getWidth());
            if(viewModel.getMaze() != null)
                mazeDisplayer.drawMaze(viewModel.getMaze());
        });
        bPane.heightProperty().addListener((obs, oldVal, newVal)-> {
            mazeDisplayer.setHeight(bPane.getHeight());
            if(viewModel.getMaze() != null)
                mazeDisplayer.drawMaze(viewModel.getMaze());
        });
    }



    private void drawSolution(ArrayList<AState> solutionPath) {
        mazeDisplayer.askedToShowTheSolution(solutionPath);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        player_row_label.setText(String.valueOf(viewModel.getCharacterRow()));
        player_col_label.setText(String.valueOf(viewModel.getCharacterCol()));
        playerCurrentRow.bind(player_row_label.textProperty());
        playercurrentColumn.bind(player_col_label.textProperty());
        bPane.setPrefWidth(Screen.getPrimary().getBounds().getWidth()-154);
        bPane.setPrefHeight(Screen.getPrimary().getBounds().getHeight()-30-100);
        mazeDisplayer.setWidth(Screen.getPrimary().getBounds().getWidth()-154);
        mazeDisplayer.setHeight(Screen.getPrimary().getBounds().getHeight()-30-100);

        listenToResize();


        File newVi = new File("resources/background.mp4");
        URI u = newVi.toURI();
        Media video = new Media(u.toString());
        MediaPlayer mpVideo = new MediaPlayer(video);
//        mv.setPreserveRatio(true);
//        mv.setMediaPlayer(mpVideo);
        mpVideo.setAutoPlay(true);
        mpVideo.setCycleCount(MediaPlayer.INDEFINITE);
//        mv.setFitWidth(anchorPane.getWidth());
//        mv.setFitHeight(anchorPane.getHeight());

        File musicBackground = new File("resources/backgroundSong.mp3");
        Media music = new Media(musicBackground.toURI().toString());
        MediaPlayer mpMusic = new MediaPlayer(music);
        musicMV.setMediaPlayer(mpMusic);
        mpMusic.setAutoPlay(true);
        mpMusic.setVolume(0.02);
        moveCheckLabel.setText(story.getText());

        Path path = new Path();
        path.getElements().add(new MoveTo(0,0));
        path.getElements().add(new CubicCurveTo(240, 0, 400, 0, 620, 0));
//        path.getElements().add(new CubicCurveTo(120, 0, 240, 0, 240, 0));
        PathTransition pathTransition = new PathTransition();
        pathTransition.setDuration(Duration.millis(4000));
        pathTransition.setPath(path);
        pathTransition.setNode(moveCheckLabel);
        pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
        pathTransition.setCycleCount(Timeline.INDEFINITE);
        pathTransition.setAutoReverse(true);
        pathTransition.play();
    }

    public void set_player_position(int rowPosition , int colPosition){
        mazeDisplayer.set_player_position(rowPosition , colPosition);
        player_row_label.setText(String.valueOf(rowPosition));
        player_col_label.setText(String.valueOf(colPosition));
    }

    public void choseStory(ActionEvent actionEvent){
        if(actionEvent.getSource() == michaelJordan || actionEvent.getSource() == whiskey){
            story.setText(((MenuItem)actionEvent.getSource()).getText());
        }
        if(actionEvent.getSource() == michaelJordan){
            mazeDisplayer.chooseStorytoFillMaze(1);
            storyNumber = 1;
        }
        else if(actionEvent.getSource() == whiskey){
            mazeDisplayer.chooseStorytoFillMaze(2);
            storyNumber = 2;

        }
        moveCheckLabel.setText(story.getText());
    }

    public void keyPressed(KeyEvent keyEvent) {
        viewModel.moveCharacter(keyEvent);
        keyEvent.consume();
    }

    public void mouseClicked(MouseEvent mouseEvent) {
        mazeDisplayer.requestFocus();
    }

    public void showSolution(MouseEvent mouseEvent) {
        viewModel.solveMaze();
    }

    private static void configureFileChooser(final FileChooser fileChooser){
        fileChooser.setTitle("Choose a maze");
        fileChooser.setInitialDirectory(
                new File(System.getProperty("java.io.tmpdir"))
        );
    }

}
