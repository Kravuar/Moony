package net.kravuar.moony;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import net.kravuar.moony.checks.Category;
import net.kravuar.moony.customList.CellFactory;
import net.kravuar.moony.data.DB_Controller;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class SettingsController implements Initializable {
    @FXML
    private ListView<Category> list;



    @FXML
    void addCategory(ActionEvent event) {
        //becomes available immediately
    }

    @FXML
    void changeCategory(ActionEvent event) {
        //changes inure after restart
    }

    @FXML
    void removeCategory(ActionEvent event) throws SQLException {
        //changes inure after restart
        String category = (((MenuItem) event.getTarget()).getText());
        for (int id : DB_Controller.getIds())
            DB_Controller.check_upd_categories_remove(category, id);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        list.setCellFactory(new CellFactory<Category,CategoryController>("category.fxml"));
        try { list.getItems().addAll(DB_Controller.loadCategories()); }
        catch (SQLException e) { throw new RuntimeException(e); }
    }
}
