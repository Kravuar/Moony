package net.kravuar.moony.customList;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ListCell;
import net.kravuar.moony.util.Util;

import java.io.IOException;

public class Cell<T,C extends Settable<T>> extends ListCell<T> {
    private final Parent pane;
    private final C controller;

    public Cell(String path, Class<C> type){
        try {
            FXMLLoader loader = Util.getLoader(path, type);
            pane = loader.load();
            controller = loader.getController();
        } catch (IOException exc) { throw new RuntimeException(exc); }
    }

    @Override
    public void updateItem(T obj, boolean empty) {
        super.updateItem(obj, empty);
        if (empty){
            setGraphic(null);
        } else {
            controller.set(obj);
            setGraphic(pane);
        }
    }
}
