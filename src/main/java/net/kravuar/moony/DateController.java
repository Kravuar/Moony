package net.kravuar.moony;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import net.kravuar.moony.checks.Check;
import net.kravuar.moony.data.DB_Controller;

import java.sql.SQLException;
import java.time.LocalDate;

public class DateController {
    @FXML
    private DatePicker datePicker;

    private Check checkRef;
    private Label dateRef;
    @FXML
    void changeDate(ActionEvent event) throws SQLException {
        LocalDate newDate = datePicker.getValue();
        checkRef.setDate(newDate);
        dateRef.setText(newDate.toString());
        ((Stage) ((Node)  event.getSource()).getScene().getWindow()).close();
        DB_Controller.check_upd_date(newDate,checkRef.getId());
    }

    public void setData(Label date, Check check) {
        checkRef = check;
        dateRef = date;
    }
}
