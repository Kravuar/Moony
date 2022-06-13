package net.kravuar.moony.checks;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import net.kravuar.moony.categories.Category;
import net.kravuar.moony.categories.CategoryCellFactory;

public class CheckCell extends ListCell<Check> {
    private final BorderPane pane;
    private final ListView<Category> categories;
    private final ImageView dollar;
    private final Text primaryCategory;

    private final Text amount;
    private final Text date;
    private final Text description;

    public CheckCell(){
        amount = new Text();
        date = new Text();
        description = new Text();
        VBox data = new VBox(amount, date, description);
        data.setFillWidth(true);

        dollar = new ImageView();
        categories = new ListView<>();
        categories.setCellFactory(new CategoryCellFactory());
        pane = new BorderPane();
        primaryCategory = new Text();

        pane.setTop(primaryCategory);
        pane.setLeft(categories);
        pane.setCenter(dollar);
        pane.setRight(data);
    }

    @Override
    public void updateItem(Check check, boolean empty) {
        super.updateItem(check, empty);
        if (check != null && !empty){
            setText(null);
            //Gathering data
            amount.setText(check.getAmount().toString());
            date.setText(check.getDate().toString());
            description.setText(check.getDescription());
            if (check.isIncome())
                dollar.setImage(new Image("file:src/main/resources/net/kravuar/moony/assets/Income.png"));
            else
                dollar.setImage(new Image("file:src/main/resources/net/kravuar/moony/assets/Expence.png"));
            primaryCategory.setText(check.getPrimaryCategory().toString());
            for(Category category : check.getCategories())
                categories.getItems().add(category);


            setGraphic(pane);
        } else {
            setGraphic(null);
        }
    }
}
