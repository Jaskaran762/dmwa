package org.dal.model.table;

import java.util.List;

public class Column {

    private List<String> values;
    private String columnName;
    private String columnDataType;
    private Integer size;
    private boolean nullable;
    private boolean primaryKey;
    private boolean uniqueness;

    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnDataType() {
        return columnDataType;
    }

    public void setColumnDataType(String columnDataType) {
        this.columnDataType = columnDataType;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public boolean isNullable() {
        return nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    public boolean isPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(boolean primaryKey) {
        this.primaryKey = primaryKey;
    }

    public boolean isUniqueness() {
        return uniqueness;
    }

    public void setUniqueness(boolean uniqueness) {
        this.uniqueness = uniqueness;
    }

    @Override
    public String toString() {
        return "Column" + values + "~" + columnName + "~" + columnDataType + "~" + size + "~" + nullable + "~" + primaryKey
                + "~" + uniqueness;
    }
}
