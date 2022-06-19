package net.kravuar.moony;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
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
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Function;

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

    @FXML
    void refresh() {
        List<Category> removed = list.getItems().stream().filter(category -> !Model.categories.contains(category)).toList();
        list.getItems().removeAll(removed);
        List<Category> categories = list.getItems().stream().toList();
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
                    && categories.stream().anyMatch(category -> check.getPrimaryCategory().getValue().equals(category));
        }).toList();

        Map<Category, Double> incomesData = categories.stream()
                .collect(toMap(Function.identity(), c -> 0.0));
        Map<Category, Double> expensesData = categories.stream()
                .collect(toMap(Function.identity(), c -> 0.0));


        AtomicReference<Double> totalIncomes = new AtomicReference<>(0.0);
        AtomicReference<Double> totalExpenses = new AtomicReference<>(0.0);
        data.forEach(check -> {
            if (check.isIncome().getValue()){
                totalIncomes.updateAndGet(v -> v + check.getAmount().getValue());
                incomesData.merge(check.getPrimaryCategory().getValue(),  check.getAmount().getValue(), Double::sum);
            }
            else {
                totalExpenses.updateAndGet(v -> v + check.getAmount().getValue());
                expensesData.merge(check.getPrimaryCategory().getValue(),  check.getAmount().getValue(), Double::sum);
            }
        });

        totalExpensesLabel.setText("Total: " + totalExpenses.get());
        totalIncomesLabel.setText("Total: " + totalIncomes.get());

        incomes.getData().setAll(incomesData.entrySet().stream()
                .filter(entry -> entry.getValue() > 0.0)
                .map(entry -> new PieChart.Data(entry.getKey().getName().getValue(),entry.getValue()))
                .toList());
        expenses.getData().setAll(expensesData.entrySet().stream()
                .filter(entry -> entry.getValue() > 0.0)
                .map(entry -> new PieChart.Data(entry.getKey().getName().getValue(),entry.getValue()))
                .toList());


        Consumer<PieChart.Data> applyGraphics = nodeData -> {
            String color = Color.web(categories.stream()
                            .filter(category -> category.getName().getValue().equals(nodeData.getName()))
                            .findFirst().get()
                            .getColor()
                            .getValue())
                    .toString()
                    .replace("0x","#");
            nodeData.getNode().setStyle(
                    "-fx-pie-color: " + color.substring(0,color.length() - 2) + ";");
            nodeData.setName(nodeData.getName() + " " + nodeData.getPieValue());
        };
        incomes.getData().forEach(applyGraphics);
        expenses.getData().forEach(applyGraphics);
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
