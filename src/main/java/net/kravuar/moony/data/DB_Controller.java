package net.kravuar.moony.data;

import net.kravuar.moony.App;
import net.kravuar.moony.checks.Category;
import net.kravuar.moony.checks.Check;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class DB_Controller {
    // CATEGORIES
    private static final String SQL_SELECT_CATEGORIES = "select * from categories";
    private static final String SQL_GET_CATEGORY_COLOR = "select color from categories where id = ?";
    private static final String SQL_GET_CATEGORY_NAME = "select name from categories where id = ?";
    private static final String SQL_UPDATE_CATEGORIES_NAME = "update categories set name = ? where id = ?";
    private static final String SQL_UPDATE_CATEGORIES_COLOR = "update categories set color = ? where id = ?";

    private static final String SQL_INSERT_CATEGORY = "insert into categories (name,color) values (?,?)";
    private static final String SQL_REMOVE_CATEGORY = "delete from categories where id = ?";



    // CHECKS
    private static final String SQL_SELECT_CHECKS = "select * from checks order by date desc";
    private static final String SQL_UPDATE_CHECK_AMOUNT = "update checks set amount = ? where id = ?";
    private static final String SQL_UPDATE_CHECK_DESCRIPTION = "update checks set description = ? where id = ?";
    private static final String SQL_UPDATE_CHECK_DATE = "update checks set date = ? where id = ?";
    private static final String SQL_UPDATE_CHECK_INCOME = "update checks set income = ? where id = ?";
    private static final String SQL_UPDATE_CHECK_PRIMARY = "update checks set primaryid = ? where id = ?";
    private static final String SQL_UPDATE_CHECK_CATEGORIES = "update checks set categories = ? where id = ?";

    private static final String SQL_INSERT_CHECK = "insert into checks (amount,description,date,income,primaryid,categories) values (?,?,?,?,?,?)";
    private static final String SQL_REMOVE_CHECK = "delete from checks where id = ?";




    // CATEGORIES
    private static final PreparedStatement categories_get_name;
    private static final PreparedStatement categories_get_color;
    private static final PreparedStatement categories_upd_name;
    private static final PreparedStatement categories_upd_color;
    private static final PreparedStatement categories_upd_add;
    private static final PreparedStatement categories_upd_remove;


    // CHECKS
    private static final PreparedStatement check_upd_amount;
    private static final PreparedStatement check_upd_descr;
    private static final PreparedStatement check_upd_date;
    private static final PreparedStatement check_upd_income;
    private static final PreparedStatement check_upd_primary;
    private static final PreparedStatement check_upd_categories;
    private static final PreparedStatement check_upd_add;
    private static final PreparedStatement check_upd_remove;


    static {
        try {

            check_upd_amount = App.connection.prepareStatement(SQL_UPDATE_CHECK_AMOUNT);
            check_upd_descr = App.connection.prepareStatement(SQL_UPDATE_CHECK_DESCRIPTION);
            check_upd_date = App.connection.prepareStatement(SQL_UPDATE_CHECK_DATE);
            check_upd_income = App.connection.prepareStatement(SQL_UPDATE_CHECK_INCOME);
            check_upd_primary = App.connection.prepareStatement(SQL_UPDATE_CHECK_PRIMARY);
            check_upd_categories = App.connection.prepareStatement(SQL_UPDATE_CHECK_CATEGORIES);
            check_upd_add = App.connection.prepareStatement(SQL_INSERT_CHECK, Statement.RETURN_GENERATED_KEYS);
            check_upd_remove = App.connection.prepareStatement(SQL_REMOVE_CHECK);

            categories_get_color = App.connection.prepareStatement(SQL_GET_CATEGORY_COLOR);
            categories_get_name = App.connection.prepareStatement(SQL_GET_CATEGORY_NAME);
            categories_upd_name = App.connection.prepareStatement(SQL_UPDATE_CATEGORIES_NAME);
            categories_upd_color = App.connection.prepareStatement(SQL_UPDATE_CATEGORIES_COLOR);
            categories_upd_add = App.connection.prepareStatement(SQL_INSERT_CATEGORY, Statement.RETURN_GENERATED_KEYS);
            categories_upd_remove = App.connection.prepareStatement(SQL_REMOVE_CATEGORY);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    // CATEGORIES
    public static ArrayList<Category> loadCategories() throws SQLException {
        Statement statement = App.connection.createStatement();
        ResultSet categories = statement.executeQuery(SQL_SELECT_CATEGORIES);
        ArrayList<Category> result = new ArrayList<>();
        while (categories.next())
            result.add(new Category(categories.getString("name"), categories.getString("color"), categories.getInt("id")));
        return result;
    }
    public static String categoryGetName(int id) throws SQLException {
        categories_get_name.setInt(1, id);
        ResultSet name = categories_get_name.executeQuery();
        if (name.next())
            return name.getString("name");
        else
            throw new RuntimeException("No category with such id.");
    }
    public static String categoryGetColor(int id) throws SQLException {
        categories_get_color.setInt(1, id);
        ResultSet color = categories_get_color.executeQuery();
        if (color.next())
            return color.getString("color");
        else
            throw new RuntimeException("No category with such id.");
    }
    public static void categories_upd_name(String newName, int id) throws SQLException {
        categories_upd_name.setString(1, newName);
        categories_upd_name.setInt(2, id);
        categories_upd_name.executeUpdate();
    }
    public static void categories_upd_color(String color, int id) throws SQLException {
        categories_upd_color.setString(1, color);
        categories_upd_color.setInt(2, id);
        categories_upd_color.executeUpdate();
    }

    public static int categories_upd_add(Category category) throws SQLException {
        categories_upd_add.setString(1, category.getName().getValue());
        categories_upd_add.setString(2, category.getColor().getValue());
        categories_upd_add.executeUpdate();
        ResultSet set = categories_upd_add.getGeneratedKeys();
        if (set.next())
            return (int) set.getLong(1);
        else
            throw new RuntimeException("No id returned");
    }
    public static void categories_upd_remove(Category category) throws SQLException {
        categories_upd_remove.setInt(1, category.getId().getValue());
        categories_upd_remove.executeUpdate();
    }



    // CHECKS
    public static ArrayList<Check> loadChecks() throws SQLException {
        Statement statement = App.connection.createStatement();
        ResultSet checks = statement.executeQuery(SQL_SELECT_CHECKS);
        ArrayList<Check> result = new ArrayList<>();
        while (checks.next()) {
            Array array = checks.getArray("categories");
            ArrayList<Category> categories = new ArrayList<>();

            if (array != null) {
                Object[] ids = (Object[]) array.getArray();
                categories.addAll(
                        Model.categories.stream().filter(category -> Arrays.stream(ids)
                                .anyMatch(id -> Objects.equals(id, category.getId().getValue()))).toList());
            }

            int primeId = checks.getInt("primaryid");
            Category prime = new Category(categoryGetName(primeId),categoryGetColor(primeId),primeId);
            int primeIndex = Model.categories.indexOf(prime);

            result.add(new Check(categories,
                    Model.categories.get(primeIndex),
                    checks.getDouble("amount"),
                    checks.getBoolean("income"),
                    checks.getDate("date").toLocalDate(),
                    checks.getString("description"),
                    checks.getInt("id")));
        }
        return result;
    }
    public static void check_upd_amount(double amount, int id) throws SQLException {
        check_upd_amount.setDouble(1, amount);
        check_upd_amount.setInt(2, id);
        check_upd_amount.executeUpdate();
    }
    public static void check_upd_descr(String descr, int id) throws SQLException {
        check_upd_descr.setString(1, descr);
        check_upd_descr.setInt(2, id);
        check_upd_descr.executeUpdate();
    }
    public static void check_upd_date(LocalDate date, int id) throws SQLException {
        check_upd_date.setDate(1, Date.valueOf(date));
        check_upd_date.setInt(2, id);
        check_upd_date.executeUpdate();
    }
    public static void check_upd_income(boolean income, int id) throws SQLException {
        check_upd_income.setBoolean(1, income);
        check_upd_income.setInt(2, id);
        check_upd_income.executeUpdate();
    }
    public static void check_upd_primary(Category category, int id) throws SQLException {
        check_upd_primary.setInt(1, category.getId().getValue());
        check_upd_primary.setInt(2, id);
        check_upd_primary.executeUpdate();
    }
    public static void check_upd_categories(List<Category> categories, int id) throws SQLException {
        Integer[] ids = categories
                .stream()
                .map(category -> category.getId().getValue()).toArray(Integer[]::new);

        check_upd_categories.setArray(1, App.connection.createArrayOf("integer", ids));
        check_upd_categories.setInt(2,id);
        check_upd_categories.executeUpdate();
    }

    public static int check_upd_add(Check check) throws SQLException {
        check_upd_add.setDouble(1, check.getAmount().doubleValue());
        check_upd_add.setString(2, check.getDescription().getValue());
        check_upd_add.setDate(3, Date.valueOf(check.getDate().getValue()));
        check_upd_add.setBoolean(4, check.isIncome().getValue());
        check_upd_add.setInt(5, check.getPrimaryCategory().getValue().getId().getValue());
        Integer[] ids = check.getCategories()
                .stream()
                .map(category -> category.getId().getValue()).toArray(Integer[]::new);
        check_upd_add.setArray(6, App.connection.createArrayOf("integer", ids));
        check_upd_add.executeUpdate();
        ResultSet set = check_upd_add.getGeneratedKeys();
        if (set.next())
            return set.getInt(1);
        else
            throw new RuntimeException("No id returned");

    }
    public static void check_upd_remove(Check check) throws SQLException {
        check_upd_remove.setInt(1, check.getId().getValue());
        check_upd_remove.executeUpdate();
    }
}
