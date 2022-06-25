package net.kravuar.moony.scenes;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.stage.Popup;
import javafx.stage.Stage;
import net.kravuar.moony.checks.Category;
import net.kravuar.moony.checks.Check;
import net.kravuar.moony.customList.CellFactory;
import net.kravuar.moony.data.Model;
import net.kravuar.moony.objects.CheckController;
import net.kravuar.moony.util.AddCheckCategoryController;
import net.kravuar.moony.util.CheckFilter;
import net.kravuar.moony.util.CheckFilterController;
import net.kravuar.moony.util.Util;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import static net.kravuar.moony.App.ExecutablePath;
import static net.kravuar.moony.util.Util.createHelperStage;

public class StorageController implements Initializable {
    @FXML
    private ListView<Check> list;
    @FXML
    private JFXButton filterButton;
    @FXML
    private JFXButton addButton;

    private final Popup popup = new Popup();
    CheckFilterController filterController;

    private void processFilter(){
        String descriptionFilter = filterController.getDescription().toLowerCase();
        List<Category> categories = filterController.getCategories();
        LocalDate from = filterController.getDateFrom();
        LocalDate to = filterController.getDateTo();

        if (descriptionFilter.equals("") && categories.isEmpty() && from == null && to == null)
            list.setItems(Model.checks);



        Util.Filter<Check> filter = new CheckFilter();
        if (from != null || to != null) {
            if (from == null)
                from = LocalDate.MIN;
            if (to == null)
                to = LocalDate.MAX;
            filter = new CheckFilter.byDate(filter,from, to);
        }
        if (!descriptionFilter.equals(""))
            filter = new CheckFilter.byDescription(filter,descriptionFilter);
        if (!categories.isEmpty())
            filter = new CheckFilter.byCategories(filter,categories);

        list.setItems(Model.checks.filtered(filter::processFilter));
    }

    @FXML
    void addCheck() throws SQLException, IOException {
        FXMLLoader loader = Util.getLoader("addCheckCategory.fxml", AddCheckCategoryController.class);
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
    void filterPopup() {
        if(!popup.isShowing()){
            var point = filterButton.localToScreen(0.0 , 0.0);
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
            if (result.isPresent() && result.get() != ButtonType.OK)
                return;

            Model.removeCheck(delCheck);
            list.getItems().removeIf(check -> check.getId() == delCheck.getId());
            Model.checks.removeIf(check -> check.getId() == delCheck.getId());
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        list.setCellFactory(new CellFactory<>("check.fxml", CheckController.class));
        list.setItems(Model.checks);

        FXMLLoader loader = Util.getLoader("checkFilter.fxml", CheckFilterController.class);
        try { popup.getContent().add(loader.load()); }
        catch (IOException e) { throw new RuntimeException(e); }
        filterController = loader.getController();
    }


}
