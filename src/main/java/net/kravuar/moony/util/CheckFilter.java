package net.kravuar.moony.util;

import net.kravuar.moony.checks.Category;
import net.kravuar.moony.checks.Check;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

public class CheckFilter implements Util.Filter<Check>{
    @Override
    public boolean processFilter(Check obj) {
        return true;
    }

    public static class byDescription implements Util.Filter<Check> {
        private final Util.Filter<Check> inner;
        private final String description;
        public byDescription(Util.Filter<Check> inner, String description) {
            this.inner = inner;
            this.description = description;
        }
        @Override
        public boolean processFilter(Check obj) {
            return obj.getDescription().getValue().toLowerCase().contains(description)
                    && inner.processFilter(obj);
        }
    }
    public static class byCategories implements Util.Filter<Check> {
        private final Util.Filter<Check> inner;
        private final List<Category> categories;
        public byCategories(Util.Filter<Check> inner, List<Category> categories) {
            this.inner = inner;
            this.categories = categories;
        }
        @Override
        public boolean processFilter(Check obj) {
            return new HashSet<>(obj.getCategories()).containsAll(categories)
                    && inner.processFilter(obj);
        }
    }
    public static class byDateAfter implements Util.Filter<Check> {
        private final Util.Filter<Check> inner;
        private final LocalDate date;
        public byDateAfter(Util.Filter<Check> inner, LocalDate date) {
            this.inner = inner;
            this.date = date;
        }
        @Override
        public boolean processFilter(Check obj) {
            return obj.getDate().getValue().isAfter(date)
                    && inner.processFilter(obj);
        }
    }
    public static class byDateBefore implements Util.Filter<Check> {
        private final Util.Filter<Check> inner;
        private final LocalDate date;
        public byDateBefore(Util.Filter<Check> inner, LocalDate date) {
            this.inner = inner;
            this.date = date;
        }
        @Override
        public boolean processFilter(Check obj) {
            return obj.getDate().getValue().isBefore(date)
                    && inner.processFilter(obj);
        }
    }
    public static class byPrimary implements Util.Filter<Check> {
        private final Util.Filter<Check> inner;
        private final List<Category> primary;
        public byPrimary(Util.Filter<Check> inner, List<Category> primary) {
            this.inner = inner;
            this.primary = primary;
        }
        @Override
        public boolean processFilter(Check obj) {
            return primary.contains(obj.getPrimaryCategory().getValue())
                    && inner.processFilter(obj);
        }
    }
    public static class byIncome implements Util.Filter<Check> {
        private final Util.Filter<Check> inner;
        private final boolean income;
        public byIncome(Util.Filter<Check> inner, boolean income) {
            this.inner = inner;
            this.income = income;
        }
        @Override
        public boolean processFilter(Check obj) {
            return obj.isIncome().getValue().equals(income)
                    && inner.processFilter(obj);
        }
    }
    public static class byAmountHigher implements Util.Filter<Check> {
        private final Util.Filter<Check> inner;
        private final double amount;
        public byAmountHigher(Util.Filter<Check> inner, double amount) {
            this.inner = inner;
            this.amount = amount;
        }
        @Override
        public boolean processFilter(Check obj) {
            return obj.getAmount().getValue() >= amount
                    && inner.processFilter(obj);
        }
    }
    public static class byAmountLower implements Util.Filter<Check> {
        private final Util.Filter<Check> inner;
        private final double amount;
        public byAmountLower(Util.Filter<Check> inner, double amount) {
            this.inner = inner;
            this.amount = amount;
        }
        @Override
        public boolean processFilter(Check obj) {
            return obj.getAmount().getValue() <= amount
                    && inner.processFilter(obj);
        }
    }
}
