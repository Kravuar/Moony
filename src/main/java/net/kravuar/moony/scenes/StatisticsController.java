package net.kravuar.moony.scenes;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import net.kravuar.moony.util.AddCheckCategoryController;
import net.kravuar.moony.objects.CategoryController;
import net.kravuar.moony.util.Util;
import net.kravuar.moony.checks.Category;
import net.kravuar.moony.checks.Check;
import net.kravuar.moony.customList.CellFactory;
import net.kravuar.moony.data.Model;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;
import static net.kravuar.moony.App.ExecutablePath;
import static net.kravuar.moony.util.Util.createHelperStage;

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
    private TextField fileName;

    @FXML
    void refresh() {
        List<Category> removed = list.getItems().stream().filter(category -> !Model.categories.contains(category)).toList();
        list.getItems().removeAll(removed);
        List<Category> categories = list.getItems().stream().toList();
        if (categories.isEmpty())
            return;
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

        BiConsumer<Double,PieChart.Data> applyGraphics = (total,nodeData) -> {
            String color = Color.web(categories.stream()
                            .filter(category -> category.getName().getValue().equals(nodeData.getName()))
                            .findFirst().get()
                            .getColor()
                            .getValue())
                    .toString()
                    .replace("0x","#");
            nodeData.getNode().setStyle(
                    "-fx-pie-color: " + color.substring(0,color.length() - 2) + ";");
            nodeData.setName(nodeData.getName() + " " + nodeData.getPieValue() + "  | " + String.format("%.2f", nodeData.getPieValue() / total * 100) + "%");
        };
        incomes.getData().forEach(nodeData -> applyGraphics.accept(totalIncomes.get(),nodeData));   // Ugly
        expenses.getData().forEach(nodeData -> applyGraphics.accept(totalExpenses.get(),nodeData));
    }

    @FXML
    void createReport() throws IOException {
        String name = fileName.getText();
        if (name != null && !name.equals("")) {
            String path = ExecutablePath + "/" + name + ".txt";
            if (new File(path).exists()){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("File with such name already exists.");
                Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
                alertStage.getIcons().add(new Image("file:" + ExecutablePath + "/assets/Icon.png"));
                alertStage.show();
                return;
            }
            String dateFrom = fromDate.getValue() == null ? "No bottom bound." : fromDate.getValue().toString();
            String dateTo = toDate.getValue() == null ? "No upper bound." : toDate.getValue().toString();
            PrintWriter writer = new PrintWriter(path, StandardCharsets.UTF_8);
            writer.println("///////////////// STATISTICS /////////////////\n\n");
            writer.println(dateFrom + "   ---   " + dateTo + "\n\n\n");
            writer.println("Total Expenses = " + totalExpensesLabel.getText());
            for (PieChart.Data data : expenses.getData())
                writer.println("\t" + data.getName());
            writer.println("\n\n\n");
            writer.println("Total Incomes = " + totalIncomesLabel.getText());
            for (PieChart.Data data : incomes.getData())
                writer.println("\t" + data.getName());
            writer.close();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Report successfully created in app folder.");
            Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
            alertStage.getIcons().add(new Image("file:" + ExecutablePath + "/assets/Icon.png"));
            alertStage.show();
        }
    }
    @FXML
    void addOrRemoveAll() {
        if (list.getItems().isEmpty())
            list.getItems().addAll(Model.categories);
        else
            list.getItems().clear();
    }
    @FXML
    void addCategory() throws IOException {
        FXMLLoader loader = Util.getLoader("addCheckCategory.fxml", AddCheckCategoryController.class);
        Parent parent = loader.load();
        AddCheckCategoryController controller = loader.getController();
        var pos = list.localToScreen(0.0,0.0);
        pos = pos.add(new Point2D(list.getWidth(),0.0));
        Stage stage = createHelperStage(new Scene(parent),pos);
        stage.showAndWait();
        Category category = controller.getCategory();
        if (category != null && !list.getItems().contains(category))
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
        list.setCellFactory(new CellFactory<>("category.fxml", CategoryController.class));
        fileName.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[-_ A-Za-z\\d]+")) {
                fileName.setText(newValue.replaceAll("[^[-_ A-Za-z\\d]]", ""));
            }
        });
    }
}
