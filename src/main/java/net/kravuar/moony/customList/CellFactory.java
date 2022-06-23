package net.kravuar.moony.customList;


import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class CellFactory<T,C extends Settable<T>> implements Callback<ListView<T>, ListCell<T>> {
    private final String fxml;
    private final Class<C> type;
    public CellFactory(String fxml, Class<C> type) {
        this.fxml = fxml;
        this.type = type;
    }
    @Override
    public ListCell<T> call(ListView<T> param) {
        return new Cell<>(fxml, type);
    }
}
