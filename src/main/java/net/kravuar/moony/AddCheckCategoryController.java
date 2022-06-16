package net.kravuar.moony;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import net.kravuar.moony.checks.Category;
import net.kravuar.moony.checks.Check;
import net.kravuar.moony.customList.CellFactory;
import net.kravuar.moony.data.DB_Controller;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AddCheckCategoryController implements Initializable {
    @FXML
    private ListView<Category> list;

    Check check;
    private ListView<Category> categories;

    @FXML
    void addCategory() throws SQLException {
        Category newCategory = list.getSelectionModel().getSelectedItem();
        check.getCategories().add(newCategory);
        categories.getItems().add(newCategory);
        DB_Controller.check_upd_categories_append(DB_Controller.categoryGetId(newCategory.getName()),check.getId());
        ((Stage) list.getScene().getWindow()).close();
    }

    public void setData(Check check, ListView<Category> categories) {
        this.check = check;
        this.categories = categories;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        list.setCellFactory(new CellFactory<Category,CategoryController>("category.fxml"));
        try { list.getItems().addAll(DB_Controller.loadCategories()); }
        catch (SQLException e) { throw new RuntimeException(e); }
    }
}
