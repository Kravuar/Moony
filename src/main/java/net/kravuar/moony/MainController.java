package net.kravuar.moony;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private JFXDrawer drawer;
    @FXML
    private JFXHamburger hamburger;
    @FXML
    private AnchorPane pane;

    private HashMap<String,Pane> scenes;


    public void changeScene(String sceneName) {
        Pane root = scenes.get(sceneName);
        if (root != null) {
        pane.getChildren().removeAll();
        pane.getChildren().setAll(root);
        root.prefWidthProperty().bind(pane.widthProperty());
        root.prefHeightProperty().bind(pane.heightProperty());
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Le init
        scenes = new HashMap<>();
        VBox box;
        try {
            scenes.put("storage", Util.getLoader("storage.fxml").load());
            scenes.put("statistics", Util.getLoader("statistics.fxml").load());
            scenes.put("settings", Util.getLoader("settings.fxml").load());
            FXMLLoader menuLoader = Util.getLoader("menu.fxml");
            box = menuLoader.load();
            MenuController menuController = menuLoader.getController();
            menuController.setMainController(this);
        } catch (IOException e) { throw new RuntimeException(e); }
        drawer.setSidePane(box);
        drawer.addEventFilter(MouseDragEvent.MOUSE_DRAGGED, Event::consume);
        drawer.onDrawerClosedProperty().set(event -> drawer.toBack());
        drawer.toBack();


        //Drawer animation
        HamburgerSlideCloseTransition transition = new HamburgerSlideCloseTransition(hamburger);
        transition.setRate(-1);
        hamburger.addEventHandler(MouseEvent.MOUSE_PRESSED, mouseEvent -> {
            if (drawer.isHiding() || drawer.isShowing())
                return;
            transition.setRate(transition.getRate() * -1);
            transition.play();

            if (drawer.isShown())
                drawer.close();
            else{
                drawer.toFront();
                hamburger.toFront();
                drawer.open();
            }
        });
    }
}