package net.kravuar.moony;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AddCheckCategoryController implements Initializable {
    private Label amountRef;
    @FXML
    private TextField amount;

    @FXML
    void changeAmount(ActionEvent event) {
        String newAmount = amount.getText();
        amountRef.setText(newAmount);
        Node source = ((Node)  event.getSource());
        Stage stage  = (Stage) source.getScene().getWindow();
        stage.close();
    }

    public void setLabel(Label label){ amountRef= label; }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        amount.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                amount.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }
}
