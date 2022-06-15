package net.kravuar.moony;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import net.kravuar.moony.checks.Check;
import net.kravuar.moony.customList.CellFactory;

import java.net.URL;
import java.util.*;

public class StorageController implements Initializable {

    @FXML
    private ListView<Check> list;

    @FXML
    void addCheck(ActionEvent event) {

    }

    @FXML
    void findCheck(ActionEvent event) {

    }

    @FXML
    void removeCheck(ActionEvent event) {
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        list.setCellFactory(new CellFactory<Check,CheckController>("check.fxml"));
    }
}
