package net.kravuar.moony;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import net.kravuar.moony.checks.Category;
import net.kravuar.moony.checks.Check;
import net.kravuar.moony.customList.CellFactory;
import net.kravuar.moony.customList.Settable;
import net.kravuar.moony.data.Model;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

import static net.kravuar.moony.Util.createHelperStage;

public class CheckController implements Settable<Check>, Initializable {
    @FXML
    private TextField amount;
    @FXML
    private DatePicker date;
    @FXML
    private TextArea description;
    @FXML
    private ImageView dollar;
    @FXML
    private Label primaryCategory;
    @FXML
    private ListView<Category> categories;
    @FXML
    private Rectangle primeRect;


    private Check check;

    @Override
    public void set(Check newCheck) {
        if (check != newCheck){
            check = newCheck;
            amount.textProperty().bind(check.getAmount().asString());
            date.valueProperty().bind(check.getDate());
            description.textProperty().bind(check.getDescription());
            categories.setItems(check.getCategories());

            // Updates only when list of checks is updated, need to use listeners
            primaryCategory.textProperty().bind(Bindings.createStringBinding(() -> check.getPrimaryCategory().getValue().getName().getValue(),
                                                                             check.getPrimaryCategory()));

            primeRect.fillProperty().bind(Bindings.createObjectBinding(() -> Color.valueOf(check.getPrimaryCategory().getValue().getColor().getValue()),
                                                                       check.getPrimaryCategory()));

            //
            if (check.isIncome().getValue())
                dollar.setImage(new Image("file:src/main/resources/net/kravuar/moony/assets/Income.png"));
            else
                dollar.setImage(new Image("file:src/main/resources/net/kravuar/moony/assets/Expence.png"));

        }
    }

    @FXML
    void setAsPrimary() throws SQLException {
        Category newPrimary = categories.getSelectionModel().getSelectedItem();
        check.setPrimaryCategory(newPrimary);
        Model.updateCheck(check, Check.Field.PRIMARY);
    }

    @FXML
    void changeDollar() throws SQLException {
        check.setIncome(!check.isIncome().getValue());
        Model.updateCheck(check,Check.Field.INCOME);
        if (check.isIncome().getValue())
            dollar.setImage(new Image("file:src/main/resources/net/kravuar/moony/assets/Income.png"));
        else
            dollar.setImage(new Image("file:src/main/resources/net/kravuar/moony/assets/Expence.png"));
    }

    @FXML
    void addCategory() throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("addCheckCategory.fxml"));
        Parent parent = loader.load();
        AddCheckCategoryController controller = loader.getController();
        controller.setData(check);
        Stage stage = createHelperStage(new Scene(parent));
        stage.show();
    }

    @FXML
    void removeCategory() throws SQLException {
        String name = categories.getSelectionModel().getSelectedItem().getName().getValue();
        check.getCategories().removeIf(category -> Objects.equals(category.getName().getValue(), name));
        Model.updateCheck(check, Check.Field.CATEGORIES);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        categories.setCellFactory(new CellFactory<Category, CategoryController>("category.fxml"));
        description.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                try { check.setDescription(description.getText()); Model.updateCheck(check, Check.Field.DESCRIPTION); }
                catch (SQLException e) { throw new RuntimeException(e); }
            }
            else { description.textProperty().unbind(); }

        });
        amount.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                try { check.setAmount(Double.parseDouble(amount.getText())); Model.updateCheck(check, Check.Field.AMOUNT); }
                catch (SQLException e) { throw new RuntimeException(e); }
            }
            else { amount.textProperty().unbind(); }
        });
        amount.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*[.]?\\d*")) {
                amount.setText(newValue.replaceAll("[^\\d.]", ""));
            }
        });
        date.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                try { check.setDate(date.getValue()); Model.updateCheck(check, Check.Field.DATE); }
                catch (SQLException e) { throw new RuntimeException(e); }
            }
            else { date.valueProperty().unbind(); }
        });
    }
}
