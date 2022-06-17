package net.kravuar.moony.checks;

import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.util.List;

public class Check {
    public static final class Field {
        public static final String AMOUNT = "amount";
        public static final String DESCRIPTION = "description";
        public static final String INCOME = "income";
        public static final String DATE = "date";
        public static final String PRIMARY = "primaryid";
        public static final String CATEGORIES = "categories";

        private Field() {}
    }

    private final ListProperty<Category> categories;
    private final Property<Category> primaryCategory;
    private final DoubleProperty amount;
    private final BooleanProperty income;
    private final Property<LocalDate> date;
    private final StringProperty description;
    private final IntegerProperty id;

    public Check(List<Category> categories, Category primaryCategory, double amount, boolean income, LocalDate date, String description, int id) {
        this.categories = new SimpleListProperty<>(FXCollections.observableArrayList(categories));
        this.primaryCategory = new SimpleObjectProperty<>(primaryCategory);
        this.amount = new SimpleDoubleProperty(amount);
        this.income = new SimpleBooleanProperty(income);
        this.date = new SimpleObjectProperty<>(date);
        this.description = new SimpleStringProperty(description);
        this.id = new SimpleIntegerProperty(id);
    }

    public StringProperty getDescription() {
        return description;
    }
    public BooleanProperty isIncome() {
        return income;
    }
    public DoubleProperty getAmount() {
        return amount;
    }
    public ListProperty<Category> getCategories() {
        return categories;
    }
    public Property<Category> getPrimaryCategory() {
        return primaryCategory;
    }
    public Property<LocalDate> getDate() {
        return date;
    }
    public IntegerProperty getId() {
        return id;
    }


    public void setDescription(String description) {
        this.description.setValue(description);
    }
    public void setIncome(boolean income) {
        this.income.setValue(income);
    }
    public void setAmount(double amount) {
        this.amount.setValue(amount);
    }
    public void setCategories(List<Category> categories) { this.categories.setValue(FXCollections.observableArrayList(categories)); }
    public void setPrimaryCategory(Category primaryCategory) {
        this.primaryCategory.setValue(primaryCategory);
    }
    public void setDate(LocalDate date) {
        this.date.setValue(date);
    }
    public void setId(int id) {
        this.id.setValue(id);
    }
}
