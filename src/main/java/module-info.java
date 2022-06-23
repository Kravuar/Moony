module net.moony.moony {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.jfoenix;
    requires java.sql;

    exports net.kravuar.moony;
    exports net.kravuar.moony.customList;
    opens net.kravuar.moony.customList to javafx.fxml;
    exports net.kravuar.moony.scenes;
    opens net.kravuar.moony.scenes to javafx.fxml;
    exports net.kravuar.moony.objects;
    opens net.kravuar.moony.objects to javafx.fxml;
    exports net.kravuar.moony.main;
    opens net.kravuar.moony.main to javafx.fxml;
    exports net.kravuar.moony.util;
    opens net.kravuar.moony.util to javafx.fxml;
}