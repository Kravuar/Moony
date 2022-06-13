module net.moony.moony {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.jfoenix;


    opens net.kravuar.moony to javafx.fxml;
    exports net.kravuar.moony;
}