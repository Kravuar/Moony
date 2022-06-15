package net.kravuar.moony.data;

import net.kravuar.moony.App;
import net.kravuar.moony.checks.Category;
import net.kravuar.moony.checks.Check;

import javax.xml.transform.Result;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class BD_Controller {
    private static final String SQL_SELECT_CATEGORIES = "select * from categories";
    private static final String SQL_GET_CATEGORY_COLOR = "select 1 from categories where name = ?";
    private static final String SQL_INSERT_CATEGORY = "insert into categories (name,color) values (?,?)";
    private static final String SQL_REMOVE_CATEGORY = "delete from categories where name = ?";
    private static final String SQL_UPDATE_CATEGORIES_NAME = "update categories set name = ? where name = ?";
    private static final String SQL_UPDATE_CATEGORIES_COLOR = "update categories set color = ? where name = ?";



    private static final String SQL_SELECT_CHEKCS = "select * from checks order by date desc";
    private static final String SQL_INSERT_CHECK = "insert into checks (amount,description,date,income,primary,categories) values (?,?,?,?,?,?)";
    private static final String SQL_REMOVE_CHECK = "delete from checks where id = ?";
    private static final String SQL_UPDATE_CHECK_AMOUNT = "update checks set amount = ? where id = ?";
    private static final String SQL_UPDATE_CHECK_DESCRIPTION = "update checks set description = ? where id = ?";
    private static final String SQL_UPDATE_CHECK_DATE = "update checks set date = ? where id = ?";
    private static final String SQL_UPDATE_CHECK_INCOME = "update checks set income = ? where id = ?";
    private static final String SQL_UPDATE_CHECK_PRIMARY = "update checks set categoryPrimaryName = ? where id = ?";
    private static final String SQL_UPDATE_CHECK_CATEGORIES_APPEND = "update checks set categories = array_append(categories, ?) where id = ?";
    private static final String SQL_UPDATE_CHECK_CATEGORIES_REMOVE = "update checks set categories = array_remove(categories, ?) where id = ?";

    private static final PreparedStatement categories_get_name;
    private static final PreparedStatement categories_upd_name;
    private static final PreparedStatement categories_upd_color;
    private static final PreparedStatement categories_upd_add;
    private static final PreparedStatement categories_upd_remove;


    private static final PreparedStatement check_upd_amount;
    private static final PreparedStatement check_upd_descr;
    private static final PreparedStatement check_upd_date;
    private static final PreparedStatement check_upd_income;
    private static final PreparedStatement check_upd_primary;
    private static final PreparedStatement check_upd_categories_append;
    private static final PreparedStatement check_upd_categories_remove;
    private static final PreparedStatement check_upd_add;
    private static final PreparedStatement check_upd_remove;


    static {
        try {

            check_upd_amount = App.connection.prepareStatement(SQL_UPDATE_CHECK_AMOUNT);
            check_upd_descr = App.connection.prepareStatement(SQL_UPDATE_CHECK_DESCRIPTION);
            check_upd_date = App.connection.prepareStatement(SQL_UPDATE_CHECK_DATE);
            check_upd_income = App.connection.prepareStatement(SQL_UPDATE_CHECK_INCOME);
            check_upd_primary = App.connection.prepareStatement(SQL_UPDATE_CHECK_PRIMARY);
            check_upd_categories_append = App.connection.prepareStatement(SQL_UPDATE_CHECK_CATEGORIES_APPEND);
            check_upd_categories_remove = App.connection.prepareStatement(SQL_UPDATE_CHECK_CATEGORIES_REMOVE);
            check_upd_add = App.connection.prepareStatement(SQL_INSERT_CHECK);
            check_upd_remove = App.connection.prepareStatement(SQL_REMOVE_CHECK);


            categories_get_name = App.connection.prepareStatement(SQL_GET_CATEGORY_COLOR);
            categories_upd_name = App.connection.prepareStatement(SQL_UPDATE_CATEGORIES_NAME);
            categories_upd_color = App.connection.prepareStatement(SQL_UPDATE_CATEGORIES_COLOR);
            categories_upd_add = App.connection.prepareStatement(SQL_INSERT_CATEGORY);
            categories_upd_remove = App.connection.prepareStatement(SQL_REMOVE_CATEGORY);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public static ArrayList<Check> loadChecks() throws SQLException {
        Statement statement = App.connection.createStatement();
        ResultSet checks = statement.executeQuery(SQL_SELECT_CHEKCS);
        ArrayList<Check> result = new ArrayList<>();
        while (checks.next()){
            ArrayList<Category> categories = new ArrayList<>();
            String[] names = (String[])checks.getArray("categories").getArray();
            for(String name: names)
                categories.add(new Category(name,getColor(name)));
            result.add(new Check(categories,
                                 new Category(checks.getString("categoryPrimaryName"),getColor(checks.getString("categoryPrimaryName"))),
                                 checks.getDouble("amount"),
                                 checks.getBoolean("income"),
                                 checks.getDate("date").toLocalDate(),
                                 checks.getString("description")));
        }
        return result;
    }
    public static void check_upd_amount(double amount, int id) throws SQLException {
        check_upd_amount.setDouble(1,amount);
        check_upd_amount.setInt(2,id);
        check_upd_amount.executeUpdate();
    }
    public static void check_upd_descr(String descr, int id) throws SQLException {
        check_upd_descr.setString(1,descr);
        check_upd_descr.setInt(2,id);
        check_upd_descr.executeUpdate();
    }
    public static void check_upd_date(LocalDate date, int id) throws SQLException {
        check_upd_date.setDate(1, Date.valueOf(date));
        check_upd_date.setInt(2,id);
        check_upd_date.executeUpdate();
    }
    public static void check_upd_income(boolean income, int id) throws SQLException {
        check_upd_income.setBoolean(1,income);
        check_upd_income.setInt(2,id);
        check_upd_income.executeUpdate();
    }
    public static void check_upd_primary(String primary, int id) throws SQLException {
        check_upd_primary.setString(1,primary);
        check_upd_primary.setInt(2,id);
        check_upd_primary.executeUpdate();
    }
    public static void check_upd_categories_append(Category category, int id) throws SQLException {
        check_upd_categories_append.setString(1, category.getName());
        check_upd_categories_append.setInt(2, id);
        check_upd_categories_append.executeUpdate();
    }
    public static void check_upd_categories_remove(Category category, int id) throws SQLException {
        check_upd_categories_remove.setString(1, category.getName());
        check_upd_categories_remove.setInt(2, id);
        check_upd_categories_remove.executeUpdate();
    }
//    public static void check_upd_add(Check check, int id) throws SQLException {
//        check_upd_add.setDouble(1,check.getAmount());
//        check_upd_add.setString(2,check.getDescription());
//        check_upd_add.setDate(3,Date.valueOf(check.getDate()));
//        check_upd_add.setBoolean(4,check.isIncome());
//        check_upd_add.setString(5,check.getPrimaryCategory().getName());
//        check_upd_add.setArray(6, check.getCategories().stream().map(Category::toString).collect(Collectors.toList())); // A kak
//    }
    public static void check_upd_remove(int id) throws SQLException {
        check_upd_remove.setInt(1,id);
    }



    public static ArrayList<Category> loadCategories() throws SQLException {
        Statement statement = App.connection.createStatement();
        ResultSet categories = statement.executeQuery(SQL_SELECT_CATEGORIES);
        ArrayList<Category> result = new ArrayList<>();
        while (categories.next())
            result.add(new Category(categories.getString("name"),categories.getString("color")));
        return result;
    }
    public static String getColor(String name) throws SQLException {
        categories_get_name.setString(1,name);
        ResultSet color = categories_get_name.executeQuery();
        return color.getString("color");
    }
//    public static void categories_upd_name(Category category, String newName) {}
    public static void categories_upd_color(Category category, String name) throws SQLException {
        categories_upd_color.setString(1 ,category.getColor());
        categories_upd_color.setString(2, name);
        categories_upd_color.executeUpdate();
    }
    public static void categories_upd_add(Category category) throws SQLException {
        categories_upd_add.setString(1,category.getName());
        categories_upd_add.setString(2,category.getColor());
        categories_upd_add.executeUpdate();
    }
    public static void categories_upd_remove(Category category) throws SQLException {
        categories_upd_remove.setString(1,category.getName());
        categories_upd_remove.executeUpdate();
    }
}
