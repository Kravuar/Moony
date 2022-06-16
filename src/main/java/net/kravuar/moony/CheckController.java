package net.kravuar.moony;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import net.kravuar.moony.checks.Category;
import net.kravuar.moony.checks.Check;
import net.kravuar.moony.customList.CellFactory;
import net.kravuar.moony.customList.Settable;
import net.kravuar.moony.data.DB_Controller;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

import static net.kravuar.moony.Util.createHelperStage;

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
    public void set(Check newCheck) {
        check = newCheck;
        amount.setText(newCheck.getAmount().toString());
        date.setText(newCheck.getDate().toString());
        description.setText(newCheck.getDescription());
        if (newCheck.isIncome())
            dollar.setImage(new Image("file:src/main/resources/net/kravuar/moony/assets/Income.png"));
        else
            dollar.setImage(new Image("file:src/main/resources/net/kravuar/moony/assets/Expence.png"));
        primaryCategory.setText(newCheck.getPrimaryCategory().toString());
        categories.setCellFactory(new CellFactory<Category, CategoryController>("category.fxml"));
        categories.getItems().clear();
        for (Category category : newCheck.getCategories()) {
            ObservableList<Category> list = categories.getItems();
            if (!list.contains(category))
                list.add(category);
        }
    }

    @FXML
    void updateAmount() throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("updateAmount.fxml"));
        Parent parent = loader.load();
        AmountController controller = loader.getController();
        controller.setData(amount, check);
        Stage stage = createHelperStage(new Scene(parent));
        stage.show();
    }

    @FXML
    void updateDate() throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("updateDate.fxml"));
        Parent parent = loader.load();
        DateController controller = loader.getController();
        controller.setData(date, check);
        Stage stage = createHelperStage(new Scene(parent));
        stage.show();
    }

    @FXML
    void setAsPrimary() throws SQLException {
        Category newPrimary = categories.getSelectionModel().getSelectedItem();
        if (!newPrimary.equals(Category.placeholder)){
            primaryCategory.setText(newPrimary.getName());
            DB_Controller.check_upd_primary(DB_Controller.categoryGetId(primaryCategory.getText()), check.getId());
        }
    }

    @FXML
    void changeDollar() throws SQLException {
        check.setIncome(!check.isIncome());
        if (check.isIncome())
            dollar.setImage(new Image("file:src/main/resources/net/kravuar/moony/assets/Income.png"));
        else
            dollar.setImage(new Image("file:src/main/resources/net/kravuar/moony/assets/Expence.png"));
        DB_Controller.check_upd_income(check.isIncome(), check.getId());
    }

    @FXML
    void addCategory() throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("addCheckCategory.fxml"));
        Parent parent = loader.load();
        AddCheckCategoryController controller = loader.getController();
        controller.setData(check, categories);
        Stage stage = createHelperStage(new Scene(parent));
        stage.show();
    }

    @FXML
    void removeCategory() throws SQLException {
        String name = categories.getSelectionModel().getSelectedItem().getName();
        categories.getItems().removeIf(category -> Objects.equals(category.getName(), name));
        DB_Controller.check_upd_categories_remove(DB_Controller.categoryGetId(name), check.getId());
    }

    private void changeDescription() throws SQLException {
        check.setDescription(description.getText());
        DB_Controller.check_upd_descr(check.getDescription(), check.getId());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        description.focusedProperty().addListener((obs, oldp, newp) -> {
            if (!newp) {
                try {
                    changeDescription();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
