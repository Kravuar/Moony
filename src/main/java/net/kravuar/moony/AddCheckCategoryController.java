package net.kravuar.moony;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import net.kravuar.moony.checks.Category;
import net.kravuar.moony.customList.CellFactory;
import net.kravuar.moony.data.DB_Controller;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AddCheckCategoryController implements Initializable {
    @FXML
    private ListView<Category> list;

    Category category;

    @FXML
    void addCategory() {
        category = list.getSelectionModel().getSelectedItem();
        ((Stage) list.getScene().getWindow()).close();
    }

    public Category getCategory() {
        return category;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        list.setCellFactory(new CellFactory<Category,CategoryController>("category.fxml"));
        try { list.getItems().addAll(DB_Controller.loadCategories()); }
        catch (SQLException e) { throw new RuntimeException(e); }
    }
}
