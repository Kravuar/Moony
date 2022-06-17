package net.kravuar.moony.checks;

import net.kravuar.moony.data.DB_Controller;

import java.sql.SQLException;

public class Category {
    public static final class Field {
        public static final String NAME = "name";
        public static final String COLOR = "color";

        private Field() {}
    }

    private String name;
    private String color;


    public Category(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }
    public String getColor() {
        return color;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setColor(String color) {
        this.color = color;
    }


    @Override
    public String toString(){
        return name;
    }

    public static final Category placeholder;
    static {
        try { placeholder = new Category("Placeholder",DB_Controller.categoryGetColor(1)); }
        catch (SQLException e) { throw new RuntimeException(e); }
    }
}
