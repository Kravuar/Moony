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
    public static class byDate implements Util.Filter<Check> {
        private final Util.Filter<Check> inner;
        private final LocalDate from;
        private final LocalDate to;
        public byDate(Util.Filter<Check> inner, LocalDate from, LocalDate to) {
            this.inner = inner;
            this.from = from;
            this.to = to;
        }
        @Override
        public boolean processFilter(Check obj) {
            return obj.getDate().getValue().isBefore(to)
                    && obj.getDate().getValue().isAfter(from)
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
}
