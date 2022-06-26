package net.kravuar.moony.util;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXToggleButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Popup;
import javafx.stage.Stage;
import net.kravuar.moony.checks.Category;
import net.kravuar.moony.customList.CellFactory;
import net.kravuar.moony.objects.CategoryController;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

import static net.kravuar.moony.util.Util.createHelperStage;

public class CheckFilterController implements Initializable {
    @FXML
    private JFXButton FilterCategories;
    @FXML
    private TextField FilterDescription;
    @FXML
    private DatePicker FilterFrom;
    @FXML
    private DatePicker FilterTo;
    @FXML
    private JFXToggleButton FilterIncome;
    @FXML
    private JFXToggleButton FilterExpense;
    @FXML
    private TextField maxAmount;
    @FXML
    private TextField minAmount;

    @FXML
    private ToggleGroup OperationType;

    private final ObservableList<Category> categories = FXCollections.observableArrayList();
    private final ListView<Category> categoriesList = new ListView<>();
    private final Popup popup = new Popup();

    @FXML
    void categoriesPopup() {
        if(!popup.isShowing()){
            var pos = FilterCategories.localToScreen(0.0, 0.0);
            categoriesList.setPrefWidth(FilterCategories.getWidth());
            popup.show(FilterCategories, pos.getX(), pos.getY() + FilterCategories.getHeight());
        }
        else
            popup.hide();
    }
    private void addCategory() throws IOException {
        FXMLLoader loader = Util.getLoader("addCheckCategory.fxml", AddCheckCategoryController.class);
        Parent parent = loader.load();
        AddCheckCategoryController controller = loader.getController();
        var pos = FilterCategories.localToScreen(0.0,0.0);
        pos = pos.add(FilterCategories.getWidth(),FilterCategories.getHeight());
        Stage stage = createHelperStage(new Scene(parent), pos);
        stage.showAndWait();
        Category category = controller.getCategory();
        if (category != null)
            categories.add(category);
    }
    private void removeCategory(Category category) {
        if (category != null)
            categories.remove(category);
    }
    private void removeAllCategories() {
        categories.clear();
    }


    public List<Category> getCategories(){ return categories;}
    public String getDescription(){ return FilterDescription.getText();}
    public LocalDate getDateFrom(){ return FilterFrom.getValue();}
    public LocalDate getDateTo(){ return FilterTo.getValue();}
    public boolean getIncome(){ return FilterIncome.isSelected();}
    public boolean getExpense(){ return FilterExpense.isSelected();}
    public double getMinAmount(){
        String amount = minAmount.getText();
        if (!amount.isEmpty())
            return Double.parseDouble(amount);
        else
            return -1;
    }
    public double getMaxAmount(){
        String amount = maxAmount.getText();
        if (!amount.isEmpty())
            return Double.parseDouble(amount);
        else
            return -1;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        minAmount.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*[.]?\\d*")) {
                minAmount.setText(newValue.replaceAll("[^\\d.]", ""));
            }
        });
        maxAmount.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*[.]?\\d*")) {
                maxAmount.setText(newValue.replaceAll("[^\\d.]", ""));
            }
        });

        categoriesList.setCellFactory(new CellFactory<>("category.fxml", CategoryController.class));
        categoriesList.setMaxHeight(250);
        categoriesList.setStyle("-fx-background-color: #f5f5f5;");
        ContextMenu menu = new ContextMenu();
        MenuItem add = new MenuItem();
        MenuItem remove = new MenuItem();
        MenuItem removeAll = new MenuItem();

        add.setText("Add");
        add.setOnAction(event -> { try { addCategory(); }
                                    catch (IOException e) {throw new RuntimeException(e);}
        });
        remove.setText("Remove");
        remove.setOnAction(event -> removeCategory(categoriesList.getSelectionModel().getSelectedItem()));
        removeAll.setText("Remove All");
        removeAll.setOnAction(event -> removeAllCategories());

        menu.getItems().addAll(add,remove,removeAll);
        categoriesList.setContextMenu(menu);
        categoriesList.setItems(categories);

        popup.getContent().add(categoriesList);
    }
}
