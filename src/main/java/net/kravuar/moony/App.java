package net.kravuar.moony;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader mainLoader = new FXMLLoader(App.class.getResource("main.fxml"));
        Scene scene = new Scene(mainLoader.load());
        MainController mainController = mainLoader.getController();


        mainController.changeScene("storage");
        stage.getIcons().add(new Image("file:src/main/resources/net/kravuar/moony/assets/Icon.png"));
        stage.setTitle("Moony");
        stage.setScene(scene);stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}