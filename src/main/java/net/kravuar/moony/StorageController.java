package net.kravuar.moony;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import net.kravuar.moony.checks.Check;
import net.kravuar.moony.customList.CellFactory;
import net.kravuar.moony.data.Model;

import java.net.URL;
import java.sql.SQLException;
import java.util.*;

public class StorageController implements Initializable {
    @FXML
    private ListView<Check> list;

    @FXML
    private TextField description;


    @FXML
    void addCheck() throws SQLException {

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
