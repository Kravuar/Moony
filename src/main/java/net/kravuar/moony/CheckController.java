package net.kravuar.moony;

import javafx.beans.binding.Bindings;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
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

import static net.kravuar.moony.App.ExecutablePath;
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

            primaryCategory.textProperty().bind(Bindings.createStringBinding(() -> check.getPrimaryCategory().getValue().getName().getValue(),
                                                                             check.getPrimaryCategory().getValue().getName()));
            primeRect.fillProperty().bind(Bindings.createObjectBinding(() -> Color.valueOf(check.getPrimaryCategory().getValue().getColor().getValue()),
                                                                       check.getPrimaryCategory().getValue().getColor()));

            if (check.isIncome().getValue())
                dollar.setImage(new Image("file:" + ExecutablePath + "/assets/Income.png"));
            else
                dollar.setImage(new Image("file:" + ExecutablePath + "/assets/Expense.png"));

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
            dollar.setImage(new Image("file:" + ExecutablePath + "/assets/Income.png"));
        else
            dollar.setImage(new Image("file:" + ExecutablePath + "/assets/Expense.png"));
    }

    @FXML
    void addCategory() throws IOException, SQLException {
        FXMLLoader loader = Util.getLoader("addCheckCategory.fxml");
        Parent parent = loader.load();
        AddCheckCategoryController controller = loader.getController();
        var pos = categories.localToScreen(0.0,0.0);
        pos = pos.add(new Point2D(categories.getWidth(),0.0));
        Stage stage = createHelperStage(new Scene(parent), pos);
        stage.showAndWait();
        Category category = controller.getCategory();
        if (category != null){
            check.getCategories().add(category);
            Model.updateCheck(check,Check.Field.CATEGORIES);
        }
    }

    @FXML
    void removeCategory() throws SQLException {
        Category category = categories.getSelectionModel().getSelectedItem();
        if (category != null) {
            check.getCategories().removeIf(candidate -> candidate.equals(category));
            Model.updateCheck(check, Check.Field.CATEGORIES);
        }
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
