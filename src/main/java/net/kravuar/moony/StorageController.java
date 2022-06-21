package net.kravuar.moony;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Popup;
import javafx.stage.Stage;
import net.kravuar.moony.checks.Category;
import net.kravuar.moony.checks.Check;
import net.kravuar.moony.customList.CellFactory;
import net.kravuar.moony.data.Model;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

import static net.kravuar.moony.App.ExecutablePath;
import static net.kravuar.moony.Util.createHelperStage;

public class StorageController implements Initializable {
    @FXML
    private ListView<Check> list;
    @FXML
    private TextField description;
    @FXML
    private JFXButton filterButton;
    @FXML
    private JFXButton addButton;


    private final ObservableList<Category> filterCategories = FXCollections.observableArrayList();
    private final ListView<Category> filter = new ListView<>();
    private boolean filterChanged = false;
    private final Popup popup = new Popup();


    private void addToFilter() throws IOException {
        FXMLLoader loader = Util.getLoader("addCheckCategory.fxml");
        Parent parent = loader.load();
        AddCheckCategoryController controller = loader.getController();
        var pos = filterButton.localToScreen(0.0,0.0);
        pos = pos.add(new Point2D(filterButton.getWidth(),filterButton.getHeight()));
        Stage stage = createHelperStage(new Scene(parent), pos);
        stage.showAndWait();
        Category category = controller.getCategory();
        if (category != null)
            filterCategories.add(category);
    }
    private void removeFromFilter(Category category) {
        if (category != null)
            filterCategories.remove(category);
    }
    private void removeAllFromFilter() {
        filterCategories.clear();
    }
    private void processFilter(){
        String descriptionFilter = description.getText().toLowerCase();
        if (filterCategories.isEmpty())
            list.setItems(Model.checks.filtered(check -> check.getDescription().toString().toLowerCase().contains(descriptionFilter)));
        else if (filterChanged) {
            filterChanged = false;
            list.setItems(Model.checks.filtered(check -> {
                boolean descr = check.getDescription().toString().toLowerCase()
                            .contains(descriptionFilter);
                List<Category> allCategories = new ArrayList<>(check.getCategories().stream().toList());
                allCategories.add(check.getPrimaryCategory().getValue());
                boolean categories = new HashSet<>(allCategories)
                            .containsAll(filterCategories);
                return descr && categories;
            }));
        }
    }

    @FXML
    void addCheck() throws SQLException, IOException {
        FXMLLoader loader = Util.getLoader("addCheckCategory.fxml");
        Parent parent = loader.load();
        AddCheckCategoryController controller = loader.getController();
        var pos = addButton.localToScreen(0.0,0.0);
        pos = pos.add(0.0, addButton.getHeight());

        Stage stage = createHelperStage(new Scene(parent), pos);
        stage.setTitle("Choose Category");
        stage.showAndWait();

        Category category = controller.getCategory();
        if (category != null){
            Check check = new Check(new ArrayList<>(List.of(category)), null, 0, true, LocalDate.now(),"", -1);
            check.setPrimaryCategory(check.getCategories().get(0));
            Model.addCheck(check);
        }
    }

    @FXML
    void findCheck() {
        processFilter();
    }

    @FXML
    void filterPopup() {
        if(!popup.isShowing()){
            var point = filterButton.localToScreen(0.0 , 0.0);
            filter.setPrefWidth(filterButton.getWidth());
            popup.show(filterButton, point.getX(), point.getY() + filterButton.getHeight());
        }
        else {
            popup.hide();
            processFilter();
        }
    }

    @FXML
    void removeCheck() throws SQLException {
        Check delCheck = list.getSelectionModel().getSelectedItem();
        if (delCheck != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            String income = delCheck.isIncome().getValue() ? "Income" : "Expense";
            alert.setContentText("Are you sure you want to remove this check?\n"
                    + income + "  -  "
                    + delCheck.getPrimaryCategory().getValue().getName().getValue() + "  -  "
                    + delCheck.getDate().getValue().toString() + "  -  "
                    + delCheck.getAmount().getValue());
            alert.setTitle("Warning");
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image("file:" + ExecutablePath + "/assets/Icon.png"));
            Optional<ButtonType> result =  alert.showAndWait();
            if (result.get() != ButtonType.OK)
                return;

            Model.removeCheck(delCheck);
            list.getItems().removeIf(check -> check.getId() == delCheck.getId());
            Model.checks.removeIf(check -> check.getId() == delCheck.getId());
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        filterCategories.addListener((ListChangeListener.Change<? extends Category> c) -> filterChanged = true);
        list.setCellFactory(new CellFactory<Check,CheckController>("check.fxml"));
        list.setItems(Model.checks);

        filter.setCellFactory(new CellFactory<Category, CategoryController>("category.fxml"));
        filter.setMaxHeight(250);
        ContextMenu menu = new ContextMenu();
        MenuItem add = new MenuItem();
        MenuItem remove = new MenuItem();
        MenuItem removeAll = new MenuItem();
        add.setText("Add");
        add.setOnAction(event -> { try { addToFilter(); }
                                    catch (IOException e) {throw new RuntimeException(e);}
        });
        remove.setText("Remove");
        remove.setOnAction(event -> removeFromFilter(filter.getSelectionModel().getSelectedItem()));
        removeAll.setText("Remove All");
        removeAll.setOnAction(event -> removeAllFromFilter());
        menu.getItems().addAll(add,remove,removeAll);
        filter.setContextMenu(menu);
        filter.setItems(filterCategories);

        popup.getContent().add(filter);
    }


}
