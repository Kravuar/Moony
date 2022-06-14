package net.kravuar.moony;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import net.kravuar.moony.checks.Category;
import net.kravuar.moony.checks.Check;
import net.kravuar.moony.customList.Settable;

public class CheckController implements Settable<Check> {
    @FXML
    private Label amount;
    @FXML
    private Label date;
    @FXML
    private TextArea description;
    @FXML
    private ImageView dollar;
    @FXML
    private Label primaryCategory;
    @FXML
    private ListView<Category> categories;

    @Override
    public void set(Check check){
        amount.setText(check.getAmount().toString());
        date.setText(check.getDate().toString());
        description.setText(check.getDescription());
        if (check.isIncome())
            dollar.setImage(new Image("file:src/main/resources/net/kravuar/moony/assets/Income.png"));
        else
            dollar.setImage(new Image("file:src/main/resources/net/kravuar/moony/assets/Expence.png"));
        primaryCategory.setText(check.getPrimaryCategory().toString());
        for (Category category: check.getCategories())
            categories.getItems().add(category);
    }
    //changers
}
