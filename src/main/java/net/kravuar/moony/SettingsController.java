package net.kravuar.moony;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import net.kravuar.moony.checks.Category;
import net.kravuar.moony.customList.CellFactory;
import net.kravuar.moony.data.DB_Controller;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class SettingsController implements Initializable {
    @FXML
    private ListView<Category> list;



    @FXML
    void addCategory() throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("addCategory.fxml"));
        Parent parent = loader.load();
        AddCategoryController controller = loader.getController();
        controller.setData(list,"");
        Stage stage = new Stage(StageStyle.UNDECORATED);
        stage.setScene(new Scene(parent));
        stage.show();
    }

    @FXML
    void changeCategory() throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("addCategory.fxml"));
        Parent parent = loader.load();
        AddCategoryController controller = loader.getController();
        controller.setData(list,list.getSelectionModel().getSelectedItem().getName());
        controller.setEditMode(true);
        Stage stage = new Stage(StageStyle.UNDECORATED);
        stage.setScene(new Scene(parent));
        stage.show();
    }

    @FXML
    void removeCategory(ActionEvent event) throws SQLException {
        Category category = list.getSelectionModel().getSelectedItem();
        if (category != null) {
            String name = category.getName();
            for (int id : DB_Controller.getIds())
                DB_Controller.check_upd_categories_remove(name, id);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        list.setCellFactory(new CellFactory<Category,CategoryController>("category.fxml"));
        try { list.getItems().addAll(DB_Controller.loadCategories()); }
        catch (SQLException e) { throw new RuntimeException(e); }
    }
}
