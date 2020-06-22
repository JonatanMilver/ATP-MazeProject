package View;

import algorithms.mazeGenerators.Maze;
import algorithms.search.AState;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
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
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
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

import java.io.*;
import java.net.URL;
import java.util.*;

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
    private int storyNumber = 1;
    @FXML
    private Slider volumeSlider;

    public Label player_row_label = new Label();
    public Label player_col_label = new Label();
    public StringProperty playerCurrentRow = new SimpleStringProperty();
    public StringProperty playercurrentColumn = new SimpleStringProperty();
    Iterator<String> itr;
    @FXML
    private Label currentSong;
    @FXML
    private AnchorPane menuAnchor;
    private MediaPlayer mp;


    public void handleExitButton(){
        Platform.exit();
        System.exit(0);
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

    public void zoomInAndOut() {
        bPane.setOnScroll(new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent scrollEvent) {
                double zoomFactor = 1.05;
                double deltaY = scrollEvent.getDeltaY();

                if (deltaY < 0) {
                    zoomFactor = 0.95;
                }
                bPane.setScaleX(bPane.getScaleX() * zoomFactor);
                bPane.setScaleY(bPane.getScaleY() * zoomFactor);
            }
        });
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
            if(storyNumber == 1) {
                bPane.setStyle("-fx-background-image: url('parket.jpg')");
                mazeDisplayer.chooseStorytoFillMaze(1);
            }
            else if(storyNumber == 2) {
                bPane.setStyle("-fx-background-image: url('grass.jpg')");
                mazeDisplayer.chooseStorytoFillMaze(2);
            }
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
                ButtonType WooHoo = new ButtonType("WooHoo!!");
                Alert alert = new Alert(Alert.AlertType.NONE, "",WooHoo);
                Button end = (Button)alert.getDialogPane().lookupButton(WooHoo);

                Media winningVideo = null;
                if(storyNumber == 1)
                    winningVideo= new Media(new File("resources/JordanSolve.mp4").toURI().toString());
                else if(storyNumber == 2)
                    winningVideo = new Media(new File("resources/WhiskeySolve.mp4").toURI().toString());
                MediaPlayer mpWinning = new MediaPlayer(winningVideo);

                MediaView mv = new MediaView(mpWinning);
                VBox vBox = new VBox(0.001, mv);
                vBox.getChildren().add(end);
                end.setAlignment(Pos.CENTER_LEFT);
                end.setOnAction(e->{
                    mpWinning.stop();
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
                alert.getDialogPane().getScene().getWindow().setOnCloseRequest(event ->{
                    mpWinning.stop();
                    alert.close();
                });
                mpWinning.play();
                alert.showAndWait();
                restartMaze();
            }
        }
        else if(operation.equals("LOAD")){
            bPane.setVisible(true);
            mazeDisplayer.setVisible(true);
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
            bPane.setMinWidth(anchorPane.getWidth());
            if(viewModel.getMaze() != null)
                mazeDisplayer.drawMaze(viewModel.getMaze());
        });
        anchorPane.heightProperty().addListener((obs, oldVal, newVal) -> {
            menuAnchor.setMinHeight(anchorPane.getHeight());
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

        zoomInAndOut();
        listenToResize();
        volumeSlider.setMin(0);
        volumeSlider.setMax(1);
        volumeSlider.setValue(0.2);

        ArrayList<String> musicList = new ArrayList<>();
        musicList.add(new File("resources/_91nova-Hotbox.mp3").toURI().toString());
        musicList.add(new File("resources/Glimlip&Leave-Lockdown.mp3").toURI().toString());
        musicList.add(new File("resources/Peyruis-Oracle.mp3").toURI().toString());
        musicList.add(new File("resources/Timothy_Infinite-Espera.mp3").toURI().toString());
        itr = musicList.iterator();
        play(itr.next(),musicList);

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
            storyNumber = 1;
        }
        else if(actionEvent.getSource() == whiskey){
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





    private void play(String mediaFile,ArrayList<String> musicList ){
        Media media = new Media(mediaFile);
        mp = new MediaPlayer(media);
        currentSong.setText(mediaFile.substring(mediaFile.lastIndexOf('/')+1));
        musicMV.setMediaPlayer(mp);
        mp.play();
        mp.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                mp.stop();
                if(!itr.hasNext()) {
                    itr = musicList.iterator();
                }
                play(itr.next(), musicList);
                return;
            }
        });
    }

    public void listenToMusicVolume(MouseEvent mouseEvent) {
        mp.setVolume(volumeSlider.getValue());
    }
}
