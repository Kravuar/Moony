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
        Category category = new Category(name.getText(),color.getValue().toString());
        DB_Controller.categories_upd_add(category);
        Node source = ((Node)  event.getSource());
        Stage stage  = (Stage) source.getScene().getWindow();
        list.getItems().add(category);
        stage.close();
    }

    public void setList(ListView<Category> list) {this.list = list;}
    public void setEditMode(boolean condition) {isEditMode = condition;}
}
