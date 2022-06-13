package net.kravuar.moony;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;

public class MenuController {
    private MainController mainController;
    private String lastScene;


    @FXML
    void exit() {
        System.exit(0);
    }

    @FXML
    void changeScene(ActionEvent event) throws IOException {
        JFXButton button = (JFXButton) event.getSource();
        String scene = button.getText().toLowerCase();
        if (!scene.equals(lastScene)) {
            mainController.changeScene(scene);
            lastScene = scene;
        }
    }

    @FXML
    void settings(){} //most significant - custom categories

    public void setMainController(MainController controller){ this.mainController = controller;}
}
