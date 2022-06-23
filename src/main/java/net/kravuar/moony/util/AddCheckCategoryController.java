package net.kravuar.moony.util;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import net.kravuar.moony.checks.Category;
import net.kravuar.moony.customList.CellFactory;
import net.kravuar.moony.data.Model;
import net.kravuar.moony.objects.CategoryController;

import java.net.URL;
import java.util.ResourceBundle;

public class AddCheckCategoryController implements Initializable {
    @FXML
    private ListView<Category> list;

    Category category;

    private void addCategory() {
        category = list.getSelectionModel().getSelectedItem();
        ((Stage) list.getScene().getWindow()).close();
    }

    public Category getCategory() {
        return category;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        list.setCellFactory(new CellFactory<>("category.fxml", CategoryController.class));
        list.setItems(Model.categories);
        list.setOnMouseClicked(mouseEvent -> {
            if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                if(mouseEvent.getClickCount() == 2){
                    addCategory();
                }
            }
        });
    }
}
