package net.kravuar.moony;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private JFXDrawer drawer;
    @FXML
    private JFXHamburger hamburger;
    @FXML
    private AnchorPane pane;


    public void changeScene(String sceneName) throws IOException {
        Pane root = FXMLLoader.load(getClass().getResource(sceneName + ".fxml"));
        pane.getChildren().removeAll();
        pane.getChildren().setAll(root);
        root.prefWidthProperty().bind(pane.widthProperty());
        root.prefHeightProperty().bind(pane.heightProperty());
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        VBox box;
        try {
            FXMLLoader menuLoader = new FXMLLoader(App.class.getResource("menu.fxml"));
            box = menuLoader.load();
            drawer.setSidePane(box);
            drawer.addEventFilter(MouseDragEvent.MOUSE_DRAGGED, Event::consume);
            MenuController menuController = menuLoader.getController();
            menuController.setMainController(this);
        } catch (IOException e) { throw new RuntimeException(e); }


        HamburgerSlideCloseTransition transition = new HamburgerSlideCloseTransition(hamburger);
        transition.setRate(-1);
        hamburger.addEventHandler(MouseEvent.MOUSE_PRESSED, mouseEvent -> {
            if (drawer.isClosing() || drawer.isOpening())
                return;
            transition.setRate(transition.getRate() * -1);
            transition.play();


            if (drawer.isOpened())
                drawer.close();
            else
                drawer.open();
        });
    }
}