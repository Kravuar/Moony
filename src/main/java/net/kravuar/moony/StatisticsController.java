package net.kravuar.moony;

import javafx.beans.binding.ObjectBinding;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import net.kravuar.moony.checks.Category;
import net.kravuar.moony.checks.Check;
import net.kravuar.moony.customList.CellFactory;
import net.kravuar.moony.data.Model;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static net.kravuar.moony.Util.createHelperStage;

public class StatisticsController implements Initializable {

    @FXML
    private DatePicker fromDate;
    @FXML
    private DatePicker toDate;

    @FXML
    private ListView<Category> list;
    @FXML
    private PieChart expenses;
    @FXML
    private PieChart incomes;

    @FXML
    void refresh(ActionEvent event) {
        // Check if list contains deleted categories, delete them
    }


    @FXML
    void addAll() {
        list.setItems(Model.categories);
    }
    @FXML
    void addCategory() throws IOException {
        FXMLLoader loader = Util.getLoader("addCheckCategory.fxml");
        Parent parent = loader.load();
        AddCheckCategoryController controller = loader.getController();
        Stage stage = createHelperStage(new Scene(parent));
        stage.showAndWait();
        Category category = controller.getCategory();
        if (category != null)
            list.getItems().add(0, category);
    }
    @FXML
    void removeCategory() {
        Category category = list.getSelectionModel().getSelectedItem();
        if (category != null)
            list.getItems().removeIf(candidate -> candidate.equals(category));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        list.setCellFactory(new CellFactory<Category,CategoryController>("category.fxml"));
    }
}
