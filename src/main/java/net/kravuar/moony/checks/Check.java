package net.kravuar.moony.checks;

import java.time.LocalDate;
import java.util.ArrayList;

public class Check {
    private ArrayList<Category> categories;
    private Category primaryCategory;
    private double amount;
    private boolean income;
    private LocalDate date;
    private String description;

    public Check(ArrayList<Category> categories, Category primaryCategory, double amount, boolean income, LocalDate date, String description) {
        this.categories = categories;
        this.primaryCategory = primaryCategory;
        this.amount = amount;
        this.income = income;
        this.date = date;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
    public boolean isIncome() {
        return income;
    }
    public Double getAmount() {
        return amount;
    }
    public ArrayList<Category> getCategories() {
        return categories;
    }
    public Category getPrimaryCategory() {
        return primaryCategory;
    }
    public LocalDate getDate() {
        return date;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public void setIncome(boolean income) {
        this.income = income;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }
    public void setCategories(ArrayList<Category> categories) {
        this.categories = categories;
    }
    public void setPrimaryCategory(Category primaryCategory) {
        this.primaryCategory = primaryCategory;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }
}
