package net.kravuar.moony;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
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
    void addCheck() throws SQLException {
        Check check = new Check(new ArrayList<>(), new Category("",""),0,true,LocalDate.now(),"", -1);
        DB_Controller.check_upd_add(check);
        check.setId(DB_Controller.getLastId());
        list.getItems().add(check);
    }

    @FXML
    void findCheck(ActionEvent event) {

    }

    @FXML
    void removeCheck() throws SQLException {
        Check delCheck = list.getSelectionModel().getSelectedItem();
        DB_Controller.check_upd_remove(delCheck.getId());
        list.getItems().removeIf(check -> check.getId() == delCheck.getId());
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        list.setCellFactory(new CellFactory<Check,CheckController>("check.fxml"));
        try { list.getItems().addAll(DB_Controller.loadChecks()); } catch (SQLException e) { throw new RuntimeException(e); }
    }
}
