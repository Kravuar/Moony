package net.kravuar.moony.data;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Callback;
import net.kravuar.moony.checks.Category;
import net.kravuar.moony.checks.Check;

import java.sql.SQLException;

public class Model {
    public static final ObservableList<Check> checks = FXCollections.observableArrayList(new Callback<Check, Observable[]>() {
        @Override
        public Observable[] call(Check check) {
            return new Observable[]{
                    check.getAmount(),
                    check.getDescription(),
                    check.isIncome(),
                    check.getDate(),
                    check.getPrimaryCategory(),
                    check.getCategories(),
                    check.getId()
            };
        }
    });
    static { try { checks.addAll(DB_Controller.loadChecks()); } catch (SQLException e) { throw new RuntimeException(e); } }
    public static final ObservableList<Category> categories = FXCollections.observableArrayList();
    static { try { categories.addAll(DB_Controller.loadCategories()); } catch (SQLException e) { throw new RuntimeException(e); } }


    public static void updateCheck(Check check, String field) throws SQLException {
        int id = check.getId().getValue();
        switch (field) {
            case Check.Field.AMOUNT -> DB_Controller.check_upd_amount(check.getAmount().getValue(),id);
            case Check.Field.DESCRIPTION -> DB_Controller.check_upd_descr(check.getDescription().getValue(),id);
            case Check.Field.INCOME -> DB_Controller.check_upd_income(check.isIncome().getValue(),id);
            case Check.Field.DATE -> DB_Controller.check_upd_date(check.getDate().getValue(),id);
            case Check.Field.PRIMARY -> DB_Controller.check_upd_primary(check.getPrimaryCategory().getValue(),id);
            case Check.Field.CATEGORIES -> DB_Controller.check_upd_categories(check.getCategories().getValue(),id);
            default -> throw new RuntimeException("No such field");
        }
    }
    public static void addCheck(Check check) throws SQLException {
        checks.add(check);
        DB_Controller.check_upd_add(check);
    }
    public static void removeCheck(Check check) throws SQLException {
        checks.remove(check);
        DB_Controller.check_upd_remove(check);
    }


    public static void updateCategory(Category category, String field) throws SQLException {
        switch (field) {
            case Category.Field.NAME -> {
                DB_Controller.categories_upd_name(category.getName(),DB_Controller.categoryGetId(category.getName()));
            }
            case Category.Field.COLOR -> DB_Controller.categories_upd_color(category.getColor(),DB_Controller.categoryGetId(category.getName()));
            default -> throw new RuntimeException("No such field");
        }
    }
    public static void addCategory(Category category) throws SQLException {
        categories.add(category);
        DB_Controller.categories_upd_add(category);
    }
    public static void removeCategory(Category category) throws SQLException {
        categories.add(category);
        DB_Controller.categories_upd_remove(category);
    }
}
