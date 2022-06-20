package net.kravuar.moony;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

import static net.kravuar.moony.App.ExecutablePath;

public class MenuController implements Initializable {
    @FXML
    private ImageView image;


    private MainController mainController;
    private String lastScene;


    @FXML
    void exit() {
        System.exit(0);
    }

    @FXML
    void changeScene(ActionEvent event) {
        JFXButton button = (JFXButton) event.getSource();
        String scene = button.getText().toLowerCase();
        if (!scene.equals(lastScene)) {
            mainController.changeScene(scene);
            lastScene = scene;
        }
    }

    public void setMainController(MainController controller){ this.mainController = controller;}

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        image.setImage(new Image("file:" + ExecutablePath + "/assets/Theme.jpg"));
    }
}
