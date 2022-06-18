package net.kravuar.moony;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import net.kravuar.moony.checks.Category;

import java.sql.SQLException;

public class AddCategoryController {
    @FXML
    private ColorPicker color;
    @FXML
    private TextField name;
    @FXML

    private Category category;

    @FXML
    void add(ActionEvent event) throws SQLException {
        String cname = name.getText();
        String cColor = color.getValue().toString();
        category = new Category(cname,cColor, -1);

        ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
    }


    public Category getCategory(){
        return category;
    }
}
