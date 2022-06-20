package net.kravuar.moony;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Util {
    public static Stage createHelperStage(Scene scene, Point2D at){
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.getIcons().add(new Image("file:src/main/resources/net/kravuar/moony/assets/Icon.png"));
        stage.setX(at.getX());
        stage.setY(at.getY());
        stage.setScene(scene);
        return stage;
    }
    public static FXMLLoader getLoader(String fxml) {
        return new FXMLLoader(App.class.getResource(fxml));
    }
}
