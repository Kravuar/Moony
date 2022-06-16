package net.kravuar.moony;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import net.kravuar.moony.checks.Category;
import net.kravuar.moony.checks.Check;
import net.kravuar.moony.customList.CellFactory;
import net.kravuar.moony.data.DB_Controller;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class StorageController implements Initializable {
    @FXML
    private ListView<Check> list;

    @FXML
    private TextField description;

    private ObservableList<Check> data;

    @FXML
    void addCheck() throws SQLException {
        Check check = new Check(new ArrayList<>(), Category.placeholder,0,true,LocalDate.now(),"", -1);
        DB_Controller.check_upd_add(check);
        check.setId(DB_Controller.getLastId());
        list.getItems().add(0,check);
        data.add(0,check);
    }

    @FXML
    void findCheck() {
        String toFind = description.getText();
        // filter data with that description
        // set it to list

    }

    @FXML
    void removeCheck() throws SQLException {
        Check delCheck = list.getSelectionModel().getSelectedItem();
        DB_Controller.check_upd_remove(delCheck.getId());
        list.getItems().removeIf(check -> check.getId() == delCheck.getId());
        data.removeIf(check -> check.getId() == delCheck.getId());
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        list.setCellFactory(new CellFactory<Check,CheckController>("check.fxml"));
        data = FXCollections.observableArrayList();
        try { data.addAll(DB_Controller.loadChecks()); } catch (SQLException e) { throw new RuntimeException(e); }
        list.setItems(data);
    }
}
