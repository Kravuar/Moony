package net.kravuar.moony;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import net.kravuar.moony.data.DB_Controller;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AmountController implements Initializable {
    @FXML
    private TextField amount;

    private int checkId;
    private Label amountRef;

    @FXML
    void changeAmount(ActionEvent event) throws SQLException {
        double newAmount = Double.parseDouble(amount.getText());
        amountRef.setText(Double.toString(newAmount));
        ((Stage) ((Node)  event.getSource()).getScene().getWindow()).close();
        DB_Controller.check_upd_amount(newAmount, checkId);
    }

    public void setData(Label label, int checkId){
        amountRef= label;
        this.checkId = checkId;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        amount.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*[.]?\\d*")) {
                amount.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }
}
