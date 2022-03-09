package View;

import algorithms.search.AState;
import com.jfoenix.controls.JFXTextArea;
import javafx.animation.PathTransition;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.util.Duration;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
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
    public Label moveCheckLabel;
    @FXML
    private MenuButton story;
    @FXML
    private MenuItem michaelJordan;
    @FXML
    private MenuItem whiskey;
    private int storyNumber = 1;
    @FXML
    private Slider volumeSlider;
    @FXML
    private Label chooseStoryLabel;
    @FXML
    private Label currentlyPlayingLabel;
    @FXML
    private Label musicVolumeLabel;
    public Label player_row_label = new Label();
    public Label player_col_label = new Label();
    public StringProperty playerCurrentRow = new SimpleStringProperty();
    public StringProperty playercurrentColumn = new SimpleStringProperty();
    private Iterator<String> itrMusic;
    private Iterator<String> itrHints;
    @FXML
    private JFXTextArea hintsTextArea;
    @FXML
    private Label currentSong;
    @FXML
    private AnchorPane menuAnchor;
    @FXML
    private MediaPlayer mp;
    private boolean switchedStory = false;



    /**
     * Being used while pressing new Game button from NEW PAGE.
     */
    @Override
    public void handleNewButton() {
        bPane.setVisible(false);
        showSolution.setVisible(false);
    }

    /**
     * Being used while pressing LOAD button from NEW PAGE.
     */
    public void loadGame() {
        FileChooser fileChooser = new FileChooser();
        configureFileChooser(fileChooser);
        File file = fileChooser.showOpenDialog(stage);
        if (checkIfFileIsMaze(file)){
            showLoadedMaze(file);
        }
    }

    /**
     * Zooms the maze itself in and out using the scroll of the mouse.
     * While scrolling, CTRL button should be DOWN.
     */
    public void zoomInAndOut() {
        bPane.setOnScroll(new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent scrollEvent) {
                if (scrollEvent.isControlDown()) {
                    double zoomFactor = 1.05;
                    double deltaY = scrollEvent.getDeltaY();

                    if (deltaY < 0) {
                        zoomFactor = 0.95;
                    }
                    bPane.setScaleX(bPane.getScaleX() * zoomFactor);
                    bPane.setScaleY(bPane.getScaleY() * zoomFactor);

                }
            }
        });
    }


    /**
     * This method is being called while pressing the Generate Button.
     * It updates the viewModel(with drawmaze) that the user asked to generate a maze.
     * @param mouseEvent MouseEvent
     */
    public void changeToMazeScene(MouseEvent mouseEvent) {
        if (mouseEvent.getSource() == generateButton){
            String rows = rowsText.getText();
            String columns =colText.getText();
            drawmaze(rows, columns);

        }
    }

    /**
     * Updates the view model that a user asked to generate a maze with rowsXcolumns.
     * @param rows String
     * @param columns String
     */
    public void drawmaze(String rows, String columns) {
        viewModel.generateMaze(rows,columns);
    }


    public void drawLoadedMaze(){
        if(viewModel.getMaze() == null) {
            showAlert(Alert.AlertType.WARNING, "Maze not found!");
            return;
        }
        mazeDisplayer.setWidth(bPane.getWidth());
        mazeDisplayer.setHeight(bPane.getHeight());
        mazeDisplayer.drawMaze(viewModel.getMaze());
    }


    /**
     * Receives the maze from the file. Sends it to the draw function
     * @param mazeFile File.
     */
    public void showLoadedMaze(File mazeFile){
        viewModel.loadMaze(mazeFile);

    }

    /**
     * Sets background and images according to chosen story.
     */
    private void setGameAccordingToStory(){
        if(storyNumber == 1) {
            bPane.setStyle("-fx-background-image: url('Pictures/parket.jpg')");
            mazeDisplayer.chooseStorytoFillMaze(1);
            switchedStory = true;
        }
        else if(storyNumber == 2) {
            bPane.setStyle("-fx-background-image: url('Pictures/grass.jpg')");
            mazeDisplayer.chooseStorytoFillMaze(2);
            switchedStory = true;
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        String operation = (String) arg;
        switch (operation) {
            case "GENERATE":
                int rows = viewModel.getMaze().getRows();
                int columns = viewModel.getMaze().getColumns();
                bPane.setVisible(true);
                setGameAccordingToStory();
                mouseClicked();
                mazeDisplayer.drawMaze(viewModel.getMaze());
                mazeDisplayer.setDrawSolution(false);
                showSolution.setText("Show Solution");
                showSolution.setVisible(true);
                switchedStory = false;
                break;
            case "SOLVE":
                if (!mazeDisplayer.isAskedToShowSolution()) {
                    mazeDisplayer.setDrawSolution(true);
                    showSolution.setText("Hide Solution");
                } else {
                    mazeDisplayer.setDrawSolution(false);
                    showSolution.setText("Show Solution");
                }
                drawSolution(viewModel.getSolution().getSolutionPath());
                break;
            case "MOVE":
            case "MOVEANDSOLVED":
                int rowChanged = viewModel.getCharacterRow();
                int colChanged = viewModel.getCharacterCol();
                set_player_position(rowChanged, colChanged);
                if (mazeDisplayer.isAskedToShowSolution())
                    drawSolution(viewModel.getSolution().getSolutionPath());
                if (operation.equals("MOVEANDSOLVED")) {
                    ButtonType WooHoo = new ButtonType("WooHoo!!");
                    Alert alert = new Alert(Alert.AlertType.NONE, "", WooHoo);
                    Button end = (Button) alert.getDialogPane().lookupButton(WooHoo);

                    Media winningVideo = null;
                    if (storyNumber == 1 ) {
                        if(!switchedStory){
                            winningVideo = new Media(new File("resources/Videos/JordanSolve.mp4").toURI().toString());
                        }
                        else
                            winningVideo = new Media(new File("resources/Videos/WhiskeySolve.mp4").toURI().toString());
                    }
                    else if (storyNumber == 2) {
                        if(!switchedStory)
                            winningVideo = new Media(new File("resources/Videos/WhiskeySolve.mp4").toURI().toString());
                        else
                            winningVideo = new Media(new File("resources/Videos/JordanSolve.mp4").toURI().toString());

                    }
                    MediaPlayer mpWinning = new MediaPlayer(winningVideo);

                    MediaView mv = new MediaView(mpWinning);
                    VBox vBox = new VBox(0.001, mv);
                    vBox.getChildren().add(end);
                    end.setAlignment(Pos.CENTER_LEFT);
                    end.setOnAction(e -> {
                        mpWinning.stop();
                        mp.setVolume(0.04);
                        alert.close();
                    });
                    mpWinning.setOnEndOfMedia(new Runnable() {
                        @Override
                        public void run() {
                            mpWinning.seek(Duration.ZERO);
                        }
                    });
                    vBox.setAlignment(Pos.CENTER_LEFT);
                    alert.getDialogPane().setContent(vBox);
                    alert.getDialogPane().setMinHeight(600);
                    alert.getDialogPane().setMinWidth(700);
                    alert.setResizable(true);
                    alert.getDialogPane().getScene().getWindow().setOnCloseRequest(event -> {
                        mpWinning.stop();
                        alert.close();
                    });
                    mpWinning.play();
                    mp.setVolume(0);
                    alert.showAndWait();
                    switchedStory = false;
                    restartMaze();
                }
                break;
            case "LOAD":
                bPane.setVisible(true);
                setGameAccordingToStory();
                mazeDisplayer.setVisible(true);
                drawLoadedMaze();
                switchedStory = false;
                showSolution.setVisible(true);
                break;
            case "INVALIDINPUT":
                showAlert(Alert.AlertType.ERROR, "One or more of the inputs were invalid.");
                break;
            case "NULLMAZE":
                showAlert(Alert.AlertType.ERROR, "File doesn't contain a maze, please try again.");
                break;
        }
    }

    /**
     * Updates the viewModel to restart a maze.
     */
    private void restartMaze() {
        viewModel.restartMaze();
    }

    /**
     * Sets Listeners while resizing the window.
     */
    private void listenToResize(){
        anchorPane.widthProperty().addListener((obs, oldVal, newVal) -> {
            bPane.setMinWidth(anchorPane.getWidth());
            if(viewModel.getMaze() != null)
                mazeDisplayer.drawMaze(viewModel.getMaze());
            if(showSolution.getText().equals("Hide Solution"))
                mazeDisplayer.drawSolution(viewModel.getSolution().getSolutionPath());
        });
        anchorPane.heightProperty().addListener((obs, oldVal, newVal) -> {

            bPane.setMinHeight(anchorPane.getHeight());
            menuAnchor.setMinHeight(anchorPane.getHeight());
            if(viewModel.getMaze() != null)
                mazeDisplayer.drawMaze(viewModel.getMaze());
            if(showSolution.getText().equals("Hide Solution"))
                mazeDisplayer.drawSolution(viewModel.getSolution().getSolutionPath());
        });
        bPane.widthProperty().addListener((obs, oldVal, newVal) -> {
            mazeDisplayer.setWidth(bPane.getWidth());
            if(viewModel.getMaze() != null)
                mazeDisplayer.drawMaze(viewModel.getMaze());
            if(showSolution.getText().equals("Hide Solution"))
                mazeDisplayer.drawSolution(viewModel.getSolution().getSolutionPath());
        });
        bPane.heightProperty().addListener((obs, oldVal, newVal)-> {
            mazeDisplayer.setHeight(bPane.getHeight());
            if(viewModel.getMaze() != null)
                mazeDisplayer.drawMaze(viewModel.getMaze());
            if(showSolution.getText().equals("Hide Solution"))
                mazeDisplayer.drawSolution(viewModel.getSolution().getSolutionPath());
        });
        menuAnchor.heightProperty().addListener((obs,oldVal, newVal)->{
            if(newVal.doubleValue() < oldVal.doubleValue()) {
                if(generateButton.getLayoutY() > 120) {
                    generateButton.setLayoutY(generateButton.getLayoutY() - 5);
                    chooseStoryLabel.setLayoutY(chooseStoryLabel.getLayoutY() - 5);
                    showSolution.setLayoutY(showSolution.getLayoutY() - 5);
                    story.setLayoutY(story.getLayoutY() - 5);
                    musicVolumeLabel.setLayoutY(musicVolumeLabel.getLayoutY() - 5);
                    volumeSlider.setLayoutY(volumeSlider.getLayoutY() - 5);
                    currentlyPlayingLabel.setLayoutY(currentlyPlayingLabel.getLayoutY() - 5);
                    currentSong.setLayoutY(currentSong.getLayoutY() - 5);
                }
            }
            else if(newVal.doubleValue() > oldVal.doubleValue()){
                if(story.getLayoutY() < 260) {
                    generateButton.setLayoutY(generateButton.getLayoutY() + 5);
                    chooseStoryLabel.setLayoutY(chooseStoryLabel.getLayoutY() + 5);
                    showSolution.setLayoutY(showSolution.getLayoutY() + 5);
                    story.setLayoutY(story.getLayoutY() + 5);
                    musicVolumeLabel.setLayoutY(musicVolumeLabel.getLayoutY() + 5);
                    volumeSlider.setLayoutY(volumeSlider.getLayoutY() + 5);
                    currentlyPlayingLabel.setLayoutY(currentlyPlayingLabel.getLayoutY() + 5);
                    currentSong.setLayoutY(currentSong.getLayoutY() + 5);
                }
            }
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
        zoomInAndOut();
        listenToResize();
        bPane.toBack();
        volumeSlider.setMin(0);
        volumeSlider.setMax(1);
        volumeSlider.setValue(0.05);


        ArrayList<String> musicList = new ArrayList<>();
        musicList.add(new File("resources/Music/_91nova-Hotbox.mp3").toURI().toString());
        musicList.add(new File("resources/Music/Glimlip&Leave-Lockdown.mp3").toURI().toString());
        musicList.add(new File("resources/Music/Peyruis-Oracle.mp3").toURI().toString());
        musicList.add(new File("resources/Music/Timothy_Infinite-Espera.mp3").toURI().toString());
        itrMusic = musicList.iterator();
        play(itrMusic.next(),musicList);

        ArrayList<String> hintsList = new ArrayList<>();
        hintsList.add("To start a new game, enter the amount of rows and columns and then press the 'Generate' button.");
        hintsList.add("Whenever help is needed, press the Show Solution button in order to get the solution.");
        hintsList.add("Choose a story you want to be a part of by pressing the bar under 'Choose a story'.");
        hintsList.add("It is worth reaching the goal - there is a cool video waiting for you!");
        hintsList.add("Choose a story you want to be a part of by pressing the bar under 'Choose a story'.");
        hintsList.add("If you have a maze that you want to continue solving, press File->Load and there you go!.");
        hintsList.add("You can save the maze you've just started at any time and reload it whenever you want.");
        hintsList.add("By saving a maze, your current position would not be save, only the maze itself.");
        itrHints = hintsList.iterator();
        runHints(itrHints.next(), hintsList);

        moveCheckLabel.setText(story.getText());

        Path path = new Path();
        path.getElements().add(new MoveTo(0,0));
        path.getElements().add(new CubicCurveTo(240, 0, 400, 0, 620, 0));
        PathTransition pathTransition = new PathTransition();
        pathTransition.setDuration(Duration.millis(4000));
        pathTransition.setPath(path);
        pathTransition.setNode(moveCheckLabel);
        pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
        pathTransition.setCycleCount(Timeline.INDEFINITE);
        pathTransition.setAutoReverse(true);
        pathTransition.play();
    }

    /**
     * Switches between hints shown on the window.
     * @param next String
     * @param hintsList ArrayList<String>
     */
    private void runHints(String next, ArrayList<String> hintsList) {
        hintsTextArea.setText(next);
        PauseTransition pause = new PauseTransition(Duration.seconds(10));
        pause.setOnFinished(actionEvent -> {
            if(!itrHints.hasNext()){
                itrHints = hintsList.iterator();
            }
            runHints(itrHints.next(), hintsList);
            return;
        });
        pause.play();
    }

    /**
     * Updates the displayer that a player has changed his position.
     * @param rowPosition int
     * @param colPosition int
     */
    public void set_player_position(int rowPosition , int colPosition){
        mazeDisplayer.set_player_position(rowPosition , colPosition);
        player_row_label.setText(String.valueOf(rowPosition));
        player_col_label.setText(String.valueOf(colPosition));
    }

    /**
     * Gets the story that the player have chosen.
     * @param actionEvent ActionEvent
     */
    public void choseStory(ActionEvent actionEvent){
        if(actionEvent.getSource() == michaelJordan || actionEvent.getSource() == whiskey){
            story.setText(((MenuItem)actionEvent.getSource()).getText());
        }
        if(actionEvent.getSource() == michaelJordan){
            storyNumber = 1;
            switchedStory = true;
        }
        else if(actionEvent.getSource() == whiskey){
            storyNumber = 2;
            switchedStory = true;

        }
        moveCheckLabel.setText(story.getText());
    }

    /**
     * Updates viewModel that player has tried to move at the maze.
     * @param keyEvent KeyEvent
     */
    public void keyPressed(KeyEvent keyEvent) {
        viewModel.moveCharacter(keyEvent);
        keyEvent.consume();
    }

    public void mouseClicked() {
        mazeDisplayer.requestFocus();
    }

    /**
     * Updates ViewModel that a player has asked to see a solution.
     */
    public void showSolution() {
        viewModel.solveMaze();
    }


    /**
     * Plays the background music.
     * @param mediaFile String
     * @param musicList ArrayList of String
     */
    private void play(String mediaFile,ArrayList<String> musicList ){
        Media media = new Media(mediaFile);
        mp = new MediaPlayer(media);
        mp.setVolume(0.05);
        volumeSlider.setValue(mp.getVolume());
        currentSong.setText(mediaFile.substring(mediaFile.lastIndexOf('/')+1));
        musicMV.setMediaPlayer(mp);
        mp.play();
        mp.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                mp.stop();
                if(!itrMusic.hasNext()) {
                    itrMusic = musicList.iterator();
                }
                play(itrMusic.next(), musicList);
            }
        });
    }

    /**
     * Sets music volume according to the slider.
     * @param mouseEvent MouseEvent
     */
    public void listenToMusicVolume(MouseEvent mouseEvent) {
        mp.setVolume(volumeSlider.getValue());
    }
}
