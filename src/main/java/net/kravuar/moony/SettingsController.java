package net.kravuar.moony;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import net.kravuar.moony.checks.Category;
import net.kravuar.moony.customList.CellFactory;
import net.kravuar.moony.data.DB_Controller;
import net.kravuar.moony.data.Model;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static net.kravuar.moony.Util.createHelperStage;

public class SettingsController implements Initializable {
    @FXML
    private ListView<Category> list;




    @FXML
    void addCategory() throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("addCategory.fxml"));
        Parent parent = loader.load();
        AddCategoryController controller = loader.getController();
        controller.setData(list,"",false);
        Stage stage = createHelperStage(new Scene(parent));
        stage.show();
    }

    @FXML
    void changeCategory() throws IOException {
        Category selected = list.getSelectionModel().getSelectedItem();
        if (selected != null){
            FXMLLoader loader = new FXMLLoader(App.class.getResource("addCategory.fxml"));
            Parent parent = loader.load();
            AddCategoryController controller = loader.getController();
            controller.setData(list, selected.getName().getValue(),true);
            Stage stage = createHelperStage(new Scene(parent));
            stage.show();
        }
    }

    @FXML
    void removeCategory() throws SQLException {
        Category category = list.getSelectionModel().getSelectedItem();
        if (category != null) {
            Model.removeCategory(category);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        list.setCellFactory(new CellFactory<Category,CategoryController>("category.fxml"));
        list.setItems(Model.categories);
    }
}
