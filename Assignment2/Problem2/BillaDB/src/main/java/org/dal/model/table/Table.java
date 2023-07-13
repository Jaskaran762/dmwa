package org.dal.model.table;

import java.util.List;

public class Table {

    private String tableName;
    private List<Column> columns;
    private Column primaryKey;

    public List<Column> getColumns() {
        return columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

    public Column getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(Column primaryKey) {
        this.primaryKey = primaryKey;
    }

    @Override
    public String toString() {
        return "Table" + tableName + "@" + columns + "@" + primaryKey;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}
