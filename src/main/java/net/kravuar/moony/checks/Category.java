package net.kravuar.moony.checks;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


public class Category {

    public static final class Field {
        public static final String NAME = "name";
        public static final String COLOR = "color";

        private Field() {}
    }

    private final StringProperty name;
    private final StringProperty color;
    private final IntegerProperty id;


    public Category(String name, String color, int id) {
        this.name = new SimpleStringProperty(name);
        this.color = new SimpleStringProperty(color);
        this.id = new SimpleIntegerProperty(id);
    }

    public StringProperty getName() {
        return name;
    }
    public StringProperty getColor() {
        return color;
    }
    public IntegerProperty getId() {
        return id;
    }
    public void setName(String name) {
        this.name.setValue(name);
    }
    public void setColor(String color) {
        this.color.setValue(color);
    }
    public void setId(int id) {
        this.id.setValue(id);
    }


    @Override
    public String toString(){
        return name.getValue();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) { return true; }
        if (!(obj instanceof Category category)) { return false; }

        return id.get() == category.id.get();
    }
}
