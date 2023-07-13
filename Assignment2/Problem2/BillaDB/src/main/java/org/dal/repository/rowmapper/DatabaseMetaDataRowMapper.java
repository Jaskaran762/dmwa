package org.dal.repository.rowmapper;

import org.dal.model.Database;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DatabaseMetaDataRowMapper {
    public static List<Database> toDatabaseMetaDataList(String dbString) {

        String[] databasesArray = dbString.split("Database");
        List<Database> databases = new ArrayList<>();
        Database database = new Database();
        List<String> tables = new ArrayList<>();
        List<String> users = new ArrayList<>();
        for (String db : databasesArray) {
            if (db.length() == 0) {}
            else {
                db = db.replace("\n", "").replace("\r", "")
                        .replace("||", "");
                String[] databaseProperties = db.split("~");
                //name
                if (!databaseProperties[0].equalsIgnoreCase("null")) {
                    database.setDatabaseName(databaseProperties[0]);
                }
                //tables
                if (!databaseProperties[1].equalsIgnoreCase("null")) {
                    databaseProperties[1] = databaseProperties[1].replace("[", "")
                            .replace("]", "");
                    String[] tableNames = databaseProperties[1].split(", ");
                    Collections.addAll(tables, tableNames);
                }
                //users
                if (!databaseProperties[2].equalsIgnoreCase("null")) {
                    databaseProperties[2] = databaseProperties[2].replace("[", "")
                            .replace("]", "");
                    String[] usersName = databaseProperties[2].split(", ");
                    Collections.addAll(users, usersName);
                }
                database.setTableName(tables);
                database.setUserName(users);
                databases.add(database);
            }
        }
        return databases;
    }
}
