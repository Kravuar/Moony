package net.kravuar.moony;

import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
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

import static net.kravuar.moony.Util.createHelperStage;

public class StorageController implements Initializable {
    @FXML
    private ListView<Check> list;
    @FXML
    private TextField description;


    @FXML
    void addCheck() throws SQLException, IOException {
        Check check = new Check(new ArrayList<>(), null, 0, true, LocalDate.now(),"", -1);
        FXMLLoader loader = Util.getLoader("addCheckCategory.fxml");
        Parent parent = loader.load();
        AddCheckCategoryController controller = loader.getController();
        controller.setData(check);
        Stage stage = createHelperStage(new Scene(parent));
        stage.setTitle("Choose Category");
        stage.showAndWait();
        if (!check.getCategories().isEmpty()){
            check.setPrimaryCategory(check.getCategories().get(0));
            Model.addCheck(check);
        }
    }

    @FXML
    void findCheck() {
        String toFind = description.getText().toLowerCase();
        list.setItems(App.data.filtered(check -> check.getDescription().toString().toLowerCase().contains(toFind)));
    }

    @FXML
    void removeCheck() throws SQLException {
        Check delCheck = list.getSelectionModel().getSelectedItem();
        if (delCheck != null) {
            Model.removeCheck(delCheck);
            list.getItems().removeIf(check -> check.getId() == delCheck.getId());
            App.data.removeIf(check -> check.getId() == delCheck.getId());
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        list.setCellFactory(new CellFactory<Check,CheckController>("check.fxml"));
        list.setItems(Model.checks);
    }


}
