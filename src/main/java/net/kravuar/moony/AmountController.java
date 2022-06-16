package net.kravuar.moony;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import net.kravuar.moony.checks.Check;
import net.kravuar.moony.data.DB_Controller;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AmountController implements Initializable {
    @FXML
    private TextField amount;

    private Check checkRef;
    private Label amountRef;
    @FXML
    void changeAmount() throws SQLException {
        double newAmount = Double.parseDouble(amount.getText());
        checkRef.setAmount(newAmount);
        amountRef.setText(Double.toString(newAmount));
        DB_Controller.check_upd_amount(newAmount, checkRef.getId());
        ((Stage) amount.getScene().getWindow()).close();
    }

    public void setData(Label amount,Check check){
        amountRef = amount;
        checkRef = check;
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
