package net.kravuar.moony.categories;

import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class CategoryCell extends ListCell<Category> {
    private final Text name;
    private final Rectangle rect;
    private final HBox box;


    public CategoryCell(){
        name = new Text();
        rect = new Rectangle();
        box = new HBox(name,rect);
    }

    @Override
    public void updateItem(Category category, boolean empty) {
        super.updateItem(category, empty);
        if (category != null && !empty){
            setText(null);
            //Gathering data
            name.setText(category.getName());
            rect.setHeight(5);
            rect.setWidth(5);
            rect.setFill(Color.valueOf(category.getColor()));

            setGraphic(box);
        } else {
            setGraphic(null);
        }
    }
}
