package uk.ac.ebi.ddi.ddidomaindb.dataset;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Field {
    private String name;
    private FieldType type;
    private String fullName;
    private FieldCategory category;

    private static List<Field> fields = new ArrayList<>();

    public Field(String name, FieldType type, FieldCategory category, String fullName) {
        this.name = name;
        this.type = type;
        this.fullName = fullName;
        this.category = category;
        fields.add(this);
    }

    public static List<Field> getFields() {
        return fields;
    }

    @Override
    public String toString() {
        return name;
    }

    public String getName() {
        return name;
    }

    public String key() {
        return name;
    }

    public FieldType getType() {
        return type;
    }

    public FieldCategory getCategory() {
        return category;
    }

    public static List<Field> getValuesByCategory(FieldCategory category) {
        List<Field> vReturn = new ArrayList<>();
        for (Field value : fields) {
            if (value.getCategory() == category) {
                vReturn.add(value);
            }
        }
        return vReturn;
    }

    public String getFullName() {
        return fullName;
    }

    public static List<Field> getValuesByCategory(FieldCategory category, FieldType unknown) {
        List<Field> vReturn = new ArrayList<>();
        for (Field value : fields) {
            if (value.getCategory() == category && !(value.getType() == unknown)) {
                vReturn.add(value);
            }
        }
        return vReturn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Field field = (Field) o;
        return Objects.equals(getName(), field.getName()) &&
                getType() == field.getType() &&
                getCategory() == field.getCategory();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getType(), getCategory());
    }
}
