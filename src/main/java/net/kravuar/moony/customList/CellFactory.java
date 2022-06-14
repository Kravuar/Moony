package net.kravuar.moony.customList;


import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class CellFactory<T,C extends Settable<T>> implements Callback<ListView<T>, ListCell<T>> {
    private final String fxml;
    public CellFactory(String fxml) {
        this.fxml = fxml;
    }
    @Override
    public ListCell<T> call(ListView<T> param) {
        return new Cell<T,C>(fxml);
    }
}
