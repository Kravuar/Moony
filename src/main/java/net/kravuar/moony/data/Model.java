package net.kravuar.moony.data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import net.kravuar.moony.checks.Category;
import net.kravuar.moony.checks.Check;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Model {
    public static final ObservableList<Check> checks = FXCollections.observableArrayList();
    public static final ObservableList<Category> categories = FXCollections.observableArrayList();

    static {
        try {
            categories.addAll(DB_Controller.loadCategories());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            checks.addAll(DB_Controller.loadChecks());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public static void updateCheck(Check check, String field) throws SQLException {
        int id = check.getId().getValue();
        switch (field) {
            case Check.Field.AMOUNT -> DB_Controller.check_upd_amount(check.getAmount().getValue(), id);
            case Check.Field.DESCRIPTION -> DB_Controller.check_upd_descr(check.getDescription().getValue(), id);
            case Check.Field.INCOME -> DB_Controller.check_upd_income(check.isIncome().getValue(), id);
            case Check.Field.DATE -> DB_Controller.check_upd_date(check.getDate().getValue(), id);
            case Check.Field.PRIMARY -> DB_Controller.check_upd_primary(check.getPrimaryCategory().getValue(), id);
            case Check.Field.CATEGORIES -> DB_Controller.check_upd_categories(check.getCategories().getValue(), id);
            default -> throw new RuntimeException("No such field");
        }
    }
    public static void addCheck(Check check) throws SQLException {
        checks.add(0,check);
        check.setId(DB_Controller.check_upd_add(check));
    }
    public static void removeCheck(Check check) throws SQLException {
        checks.remove(check);
        DB_Controller.check_upd_remove(check);
    }


    public static void updateCategory(Category category, String field) throws SQLException {
        switch (field) {
            case Category.Field.NAME ->
                    DB_Controller.categories_upd_name(category.getName().getValue(), category.getId().getValue());
            case Category.Field.COLOR ->
                    DB_Controller.categories_upd_color(category.getColor().getValue(), category.getId().getValue());
            default -> throw new RuntimeException("No such field");
        }
    }
    public static void addCategory(Category category) throws SQLException {
        categories.add(category);
        category.setId(DB_Controller.categories_upd_add(category));
    }
    public static void removeCategory(Category category) throws SQLException {
        categories.remove(category);
        List<Check> toRemove = checks.stream().filter(check -> check.getPrimaryCategory().getValue().equals(category)).toList();
        for (Check check : toRemove)
            removeCheck(check);
        for (Check check : checks){
            check.getCategories().get().removeIf(candidate -> candidate.equals(category));
            updateCheck(check, Check.Field.CATEGORIES);
        }
        DB_Controller.categories_upd_remove(category);
    }
}
