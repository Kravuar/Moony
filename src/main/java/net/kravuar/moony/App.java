package net.kravuar.moony;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import net.kravuar.moony.checks.Check;
import net.kravuar.moony.data.DB_Controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import java.io.IOException;
public class App extends Application {

    private static final String DB_URL = "jdbc:postgresql://localhost:5432/moony";
    private static final String DB_PASS = "appPass";
    private static final String DB_USERNAME = "postgres";

    public static final Connection connection;
    static { try { connection = DriverManager.getConnection(DB_URL,DB_USERNAME,DB_PASS); } catch (SQLException e) { throw new RuntimeException(e); } }

    public static final ObservableList<Check> data = FXCollections.observableArrayList();
    static { try { data.addAll(DB_Controller.loadChecks()); } catch (SQLException e) { throw new RuntimeException(e); } }

    @Override
    public void start(Stage stage) throws IOException {


        FXMLLoader mainLoader = Util.getLoader("main.fxml");
        Scene scene = new Scene(mainLoader.load());
        MainController mainController = mainLoader.getController();


        mainController.changeScene("storage");
        stage.getIcons().add(new Image("file:src/main/resources/net/kravuar/moony/assets/Icon.png"));
        stage.setTitle("Moony");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}