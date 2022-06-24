package net.kravuar.moony;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import net.kravuar.moony.main.MainController;
import net.kravuar.moony.util.Util;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class App extends Application {
    public static final String ExecutablePath;
    static {
        try {
            URL url = AppLauncher.class.getProtectionDomain().getCodeSource().getLocation();
            File myFile = new File(url.toURI());

            //ExecutablePath = myFile.getParentFile().getPath();
            ExecutablePath = "E:\\Balovstvo\\moony";
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
    // for IDE change ExecutablePath to folder with DB and assets(unnecessary)
    private static final String DB_URL = "jdbc:h2:" + ExecutablePath + "\\moony";
    private static final String DB_PASS = "appPass";
    private static final String DB_USERNAME = "moonyUser";



    public static final Connection connection;
    static { try { connection = DriverManager.getConnection(DB_URL,DB_USERNAME,DB_PASS); } catch (SQLException e) { throw new RuntimeException(e); } }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader mainLoader = Util.getLoader("main.fxml", MainController.class);
        Scene scene = new Scene(mainLoader.load());
        MainController mainController = mainLoader.getController();


        mainController.changeScene("storage");
        stage.getIcons().add(new Image("file:" + ExecutablePath + "/assets/Icon.png"));
        stage.setTitle("Moony");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}