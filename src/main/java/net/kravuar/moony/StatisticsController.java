package net.kravuar.moony;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import net.kravuar.moony.checks.Category;
import net.kravuar.moony.checks.Check;
import net.kravuar.moony.customList.CellFactory;
import net.kravuar.moony.data.Model;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
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
    private Label totalExpensesLabel;
    @FXML
    private Label totalIncomesLabel;

    private final Label info = new Label("");

    @FXML
    void refresh() {
        List<Category> removed = list.getItems().stream().filter(category -> !Model.categories.contains(category)).toList();
        list.getItems().removeAll(removed);
        LocalDate from = fromDate.getValue(); LocalDate to = toDate.getValue();
        if (from == null)
            from = LocalDate.MIN;
        if (to == null)
            to = LocalDate.MAX;

        LocalDate finalFrom = from;
        LocalDate finalTo = to;
        List<Check> data = Model.checks.stream().filter(check -> {
            LocalDate checkDate = check.getDate().getValue();
            return checkDate.isAfter(finalFrom)
                    && checkDate.isBefore(finalTo)
                    && list.getItems().stream().anyMatch(category -> check.getCategories().contains(category));
        }).toList();

        Map<Category, Double> incomesData = list.getItems().stream()
                .collect(toMap(Function.identity(), c -> 0.0));
        Map<Category, Double> expensesData = list.getItems().stream()
                .collect(toMap(Function.identity(), c -> 0.0));

        data.forEach(check -> {
            if (check.isIncome().getValue())
                check.getCategories().forEach(category -> incomesData.merge(category, check.getAmount().getValue(), Double::sum));
            else
                check.getCategories().forEach(category -> expensesData.merge(category, check.getAmount().getValue(), Double::sum));
        });
        Double totalIncomes = incomesData.values().stream().reduce(0.0, Double::sum);
        Double totalExpenses = expensesData.values().stream().reduce(0.0, Double::sum);
        totalExpensesLabel.setText("Total: " + totalExpenses);
        totalIncomesLabel.setText("Total: " + totalIncomes);

        incomes.getData().setAll(incomesData.entrySet().stream().map(entry -> new PieChart.Data(entry.getKey().getName().getValue(),entry.getValue())).toList());
        expenses.getData().setAll(expensesData.entrySet().stream().map(entry -> new PieChart.Data(entry.getKey().getName().getValue(),entry.getValue())).toList());


        AtomicInteger i = new AtomicInteger(0);
        incomes.getData().forEach(nodeData -> {
            String color = Color.web(list.getItems().get(i.getAndIncrement())
                            .getColor()
                            .getValue())
                            .toString()
                            .replace("0x","#");
            nodeData.getNode().setStyle(
                "-fx-pie-color: " + color.substring(0,color.length() - 2) + ";");
        });
        i.set(0);
        expenses.getData().forEach(nodeData -> {
            String color = Color.web(list.getItems().get(i.getAndIncrement())
                            .getColor()
                            .getValue())
                            .toString()
                            .replace("0x","#");
            nodeData.getNode().setStyle(
                    "-fx-pie-color: " + color.substring(0,color.length() - 2) + ";");
        });

        for (final PieChart.Data nodeData : incomes.getData()) {
            nodeData.getNode().addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
                info.setTranslateX(e.getSceneX());
                info.setTranslateY(e.getSceneY());

                info.setText(String.valueOf(nodeData.getPieValue()));
            });
        }
        for (final PieChart.Data nodeData : expenses.getData()) {
            nodeData.getNode().addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
                info.setTranslateX(e.getSceneX());
                info.setTranslateY(e.getSceneY());

                info.setText(String.valueOf(nodeData.getPieValue()));
            });
        }
        // add label to stage
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
