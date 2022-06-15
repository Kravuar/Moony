package net.kravuar.moony;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import net.kravuar.moony.checks.Category;
import net.kravuar.moony.data.DB_Controller;

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
        Category category = new Category(cname,cColor);
        Node source = ((Node)  event.getSource());
        Stage stage  = (Stage) source.getScene().getWindow();
        if (!isEditMode){
            DB_Controller.categories_upd_add(category);
            list.getItems().add(category);
        }
        else {
            Category toChange = list.getSelectionModel().getSelectedItem();
            if (toChange != null){
                DB_Controller.categories_upd_color(cColor, DB_Controller.getId(toChange.getName()));
                DB_Controller.categories_upd_name(cname, DB_Controller.getId(toChange.getName()));
                toChange.setName(cname);
                toChange.setColor(cColor);
                list.getSelectionModel().clearSelection();
            }
            isEditMode = false;
        }
        stage.close();
    }

    public void setData(ListView<Category> list, String cname) {
        this.list = list;
        name.setText(cname);
    }
    public void setEditMode(boolean condition) {isEditMode = condition;}
}
