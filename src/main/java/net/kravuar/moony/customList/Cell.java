package net.kravuar.moony.customList;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.layout.Pane;
import net.kravuar.moony.App;

import java.io.IOException;

public class Cell<T,C extends Settable<T>> extends ListCell<T> {
    private final Pane pane;
    private final C controller;

    public Cell(String path){
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource(path));
            pane = loader.load();
            controller = loader.getController();
        } catch (IOException exc) { throw new RuntimeException(exc); }
    }

    @Override
    public void updateItem(T obj, boolean empty) {
        super.updateItem(obj, empty);
        if (obj != null && !empty){
            controller.set(obj);
            setGraphic(pane);
        } else {
            setGraphic(null);
        }
    }
}
