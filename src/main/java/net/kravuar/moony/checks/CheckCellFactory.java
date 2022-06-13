package net.kravuar.moony.checks;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class CheckCellFactory implements Callback<ListView<Check>, ListCell<Check>> {
    @Override
    public ListCell<Check> call(ListView<Check> param) {
        return new CheckCell();
    }
}