package View;

import javafx.stage.Stage;

public interface IView {

    void handleExitButton();
    void handleNewButton();
    void handleLoadButton();
    void handleSaveButton();
    void handleAboutButton();
    void handlePropertiesButton();
    void handleHelpButton();
    Stage newStage(String path , String title, int width , int height);
}
