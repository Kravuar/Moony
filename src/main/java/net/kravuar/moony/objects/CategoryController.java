package net.kravuar.moony.objects;


import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import net.kravuar.moony.checks.Category;
import net.kravuar.moony.customList.Settable;

public class CategoryController implements Settable<Category> {
    @FXML
    private Label name;
    @FXML
    private Rectangle rect;

    @Override
    public void set(Category category){
        name.textProperty().bind(category.getName());
        rect.fillProperty().bind(Bindings.createObjectBinding(() -> Color.valueOf(category.getColor().getValue()), category.getColor()));
    }
}
