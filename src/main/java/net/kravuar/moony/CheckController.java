package net.kravuar.moony;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import net.kravuar.moony.checks.Category;
import net.kravuar.moony.checks.Check;
import net.kravuar.moony.customList.CellFactory;
import net.kravuar.moony.customList.Settable;

import java.net.URL;
import java.util.ResourceBundle;

public class CheckController implements Settable<Check>, Initializable {
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

    private Check check;

    @Override
    public void set(Check newCheck){
        if (check == null || !check.equals(newCheck)){
            check = newCheck;
            amount.setText(newCheck.getAmount().toString());
            date.setText(newCheck.getDate().toString());
            description.setText(newCheck.getDescription());
            if (newCheck.isIncome())
                dollar.setImage(new Image("file:src/main/resources/net/kravuar/moony/assets/Income.png"));
            else
                dollar.setImage(new Image("file:src/main/resources/net/kravuar/moony/assets/Expence.png"));
            primaryCategory.setText(newCheck.getPrimaryCategory().toString());
            categories.setCellFactory(new CellFactory<Category,CategoryController>("category.fxml"));
            for (Category category: newCheck.getCategories()) {
                ObservableList<Category> list = categories.getItems();
                if (!list.contains(category))
                    list.add(category);
            }
        }
    }

    @FXML
    void updateAmount() {

    }
    @FXML
    void updateDate() {

    }
    @FXML
    void changeDollar() {
        check.setIncome(!check.isIncome());
        if (check.isIncome())
            dollar.setImage(new Image("file:src/main/resources/net/kravuar/moony/assets/Income.png"));
        else
            dollar.setImage(new Image("file:src/main/resources/net/kravuar/moony/assets/Expence.png"));
    }
    @FXML
    void addCategory() {

    }
    @FXML
    void removeCategory(ActionEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        description.focusedProperty().addListener((obs, oldp, newp) -> {
            if (!newp)
                check.setDescription(description.toString());
        });
    }
}
