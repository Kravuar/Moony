package net.kravuar.moony;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import net.kravuar.moony.checks.Check;
import net.kravuar.moony.customList.Settable;

public class CheckController implements Settable<Check> {
    @FXML
    private Label amount;
    @FXML
    private Label date;
    @FXML
    private Label description;
    @FXML
    private ImageView dollar;
    @FXML
    private Label primaryCategory;


    @Override
    public void set(Check check){
        amount.setText(check.getAmount().toString());
        date.setText(check.getDate().toString());
        description.setText(check.getDescription());
        if (check.isIncome())
            dollar.setImage(new Image("file:src/main/resources/net/kravuar/moony/assets/Income.png"));
        else
            dollar.setImage(new Image("file:src/main/resources/net/kravuar/moony/assets/Expence.png"));
        primaryCategory.setText(check.getPrimaryCategory().toString());
    }
    //changers
}
