package net.kravuar.moony;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import net.kravuar.moony.checks.Category;
import net.kravuar.moony.checks.Check;
import net.kravuar.moony.customList.CellFactory;

import java.net.URL;
import java.time.LocalDate;
import java.util.*;

public class StorageController implements Initializable {

    @FXML
    private ListView<Check> list;


    @FXML
    void addCheck(ActionEvent event) {
        list.getItems().addAll(
                new Check(
                        new ArrayList(Arrays.asList(new Category("kek","#58BA67"), new Category("geg","#C2F82E"))),
                        new Category("gavno","#58BA67"),
                        50,
                        true,
                        LocalDate.of(2021,5,21),
                        "a check description"
                ),
                new Check(
                        new ArrayList(Arrays.asList(new Category("bebr","#AB68AF"), new Category("koop","#589BAF"))),
                        new Category("bebr","#AB68AF"),
                        190,
                        false,
                        LocalDate.of(2021,8,15),
                        "another description"
                ));
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
