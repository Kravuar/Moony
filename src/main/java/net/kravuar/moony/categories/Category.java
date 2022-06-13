package net.kravuar.moony.categories;

public class Category {
    private String name;
    private String color;  // #......

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

        return null;
    }


}
