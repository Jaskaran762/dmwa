package org.dal.model;

import org.dal.model.table.Table;

import java.util.List;

public class Database {

    private String databaseName;
    private List<String> tableName;
    private List<String> userName;

    public List<String> getTableName() {
        return tableName;
    }

    public void setTableName(List<String> tableName) {
        this.tableName = tableName;
    }

    public List<String> getUserName() {
        return userName;
    }

    public void setUserName(List<String> userName) {
        this.userName = userName;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    @Override
    public String toString() {
        return "Database" + databaseName + "~" + tableName + "~" + userName;
    }
}
