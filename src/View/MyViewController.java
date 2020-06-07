package View;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

public class MyViewController implements IView{

    public Menu exitButton;
    public MenuItem newButton;

    public void handleExitButton(){
//        System.out.println("Exit");
        Platform.exit();

    }

    @Override
    public void handleNewButton() {
        System.out.println("NEW");
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


}
