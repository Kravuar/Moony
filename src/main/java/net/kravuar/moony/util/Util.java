package net.kravuar.moony.util;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import static net.kravuar.moony.App.ExecutablePath;

public class Util {
    public static Stage createHelperStage(Scene scene, Point2D at){
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.getIcons().add(new Image("file:" + ExecutablePath + "/assets/Icon.png"));
        stage.setX(at.getX());
        stage.setY(at.getY());
        stage.setScene(scene);
        stage.focusedProperty().addListener((ov, onHidden, onShown) -> {
            if (onHidden)
                stage.close();
        });
        return stage;
    }
    public static FXMLLoader getLoader(String fxml, Class controller) {
        return new FXMLLoader(controller.getResource(fxml));
    }
}
