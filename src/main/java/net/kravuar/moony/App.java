package net.kravuar.moony;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import java.io.IOException;
public class App extends Application {

    private static final String DB_URL = "jdbc:postgresql://localhost:5432/moony";
    private static final String DB_PASS = "appPass";
    private static final String DB_USERNAME = "postgres";
    public static final Connection connection;
    static {
        try { connection = DriverManager.getConnection(DB_URL,DB_USERNAME,DB_PASS); }
        catch (SQLException e) { throw new RuntimeException(e); }
    }

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