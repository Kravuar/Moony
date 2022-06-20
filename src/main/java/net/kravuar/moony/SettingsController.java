package net.kravuar.moony;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import net.kravuar.moony.checks.Category;
import net.kravuar.moony.customList.CellFactory;
import net.kravuar.moony.data.Model;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

import static net.kravuar.moony.App.ExecutablePath;
import static net.kravuar.moony.Util.createHelperStage;

public class SettingsController implements Initializable {
    @FXML
    private ListView<Category> list;



    private void processChange(boolean isEditMode) throws IOException, SQLException {
        FXMLLoader loader = Util.getLoader("addCategory.fxml");
        Parent parent = loader.load();
        AddCategoryController controller = loader.getController();
        var pos = list.localToScreen(0.0,0.0);
        pos = pos.add(new Point2D(300,0));
        Stage stage = createHelperStage(new Scene(parent), pos);
        stage.showAndWait();
        Category category = controller.getCategory();
        if (category == null)
            return;

        if (!isEditMode){
            if (Model.categories.stream().anyMatch(candidate -> candidate.getName().getValue().equals(category.getName().getValue()))){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Category with such name already exists.");
                Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
                alertStage.getIcons().add(new Image("file:" + ExecutablePath + "/assets/Icon.png"));
                alertStage.show();
                return;
            }
            Model.addCategory(category);
        }
        else {
            Category toChange = list.getSelectionModel().getSelectedItem();
            toChange.setName(category.getName().getValue());
            toChange.setColor(category.getColor().getValue());
            Model.updateCategory(category,Category.Field.NAME);
            Model.updateCategory(category,Category.Field.COLOR);
            list.getSelectionModel().clearSelection();
        }

    }

    @FXML
    void addCategory() throws IOException, SQLException {
        processChange(false);
    }

    @FXML
    void changeCategory() throws IOException, SQLException {
        Category selected = list.getSelectionModel().getSelectedItem();
        if (selected != null)
            processChange(true);
    }

    @FXML
    void removeCategory() throws SQLException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("All check with such primary category will be deleted.");
        alert.setTitle("Warning");
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("file:" + ExecutablePath + "/assets/Icon.png"));
        Optional<ButtonType> result =  alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            Category category = list.getSelectionModel().getSelectedItem();
            if (category != null) {
                Model.removeCategory(category);
            }
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        list.setCellFactory(new CellFactory<Category,CategoryController>("category.fxml"));
        list.setItems(Model.categories);
    }
}
