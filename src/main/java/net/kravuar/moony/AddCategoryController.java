package net.kravuar.moony;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import net.kravuar.moony.checks.Category;
import net.kravuar.moony.data.Model;

import java.sql.SQLException;

public class AddCategoryController {
    @FXML
    private ColorPicker color;
    @FXML
    private TextField name;
    @FXML

    private ListView<Category> list;
    private boolean isEditMode = false;

    @FXML
    void add(ActionEvent event) throws SQLException {
        String cname = name.getText();
        String cColor = color.getValue().toString();
        Category category;

        if (!isEditMode){
            category = new Category(cname,cColor);
            Model.addCategory(category);
        }
        else {
            category = list.getSelectionModel().getSelectedItem();
            category.setName(cname);
            category.setColor(cColor);
            Model.updateCategory(category,Category.Field.NAME);
            Model.updateCategory(category,Category.Field.COLOR);
            list.getSelectionModel().clearSelection();
        }
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
    }

    public void setData(ListView<Category> list, String selected, boolean editMode) {
        this.list = list;
        name.setText(selected);
        isEditMode = editMode;
    }
}
