package net.kravuar.moony.categories;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import net.kravuar.moony.checks.Check;

public class CategoryCellFactory implements Callback<ListView<Category>, ListCell<Category>> {
    @Override
    public ListCell<Category> call(ListView<Category> param) {
        return new CategoryCell();
    }
}