package org.dal.repository;

import org.dal.model.Database;
import org.dal.model.User;
import org.dal.model.table.Table;
import org.dal.repository.rowmapper.DatabaseMetaDataRowMapper;
import org.dal.repository.rowmapper.TableRowMapper;
import org.dal.repository.rowmapper.UserRowMapper;
import org.dal.util.CustomFileFormatHandler;
import org.dal.util.CustomFileFormatImpl;
import org.dal.util.interfaces.CustomFileFormat;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.DatabaseMetaData;
import java.time.format.TextStyle;
import java.util.List;

public class QueryHandlerRepository {

    private final String filePath = "database";
    private String dbName = "";
    private String fileName = null;
    private CustomFileFormat customFileFormat = new CustomFileFormatImpl();
    public boolean createDatabase(Database database) throws Exception {
        fileName = database.getDatabaseName();
        dbName = fileName;
        this.customFileFormat.createFolder(filePath);
        byte[] encryptedContent = CustomFileFormatHandler.encryptData(database.toString().concat("||").getBytes());
        return this.customFileFormat.writeToFile(encryptedContent,filePath, fileName);
    }

    public List<Database> fetchDatabaseMetaData(String dbName) throws Exception {
        byte[] encryptedBytes = this.customFileFormat.readFile(filePath,dbName);
        byte[] decryptedBytes = CustomFileFormatHandler.decryptFile(encryptedBytes);

        String dbString = new String(decryptedBytes);
        return DatabaseMetaDataRowMapper.toDatabaseMetaDataList(dbString);
    }

    private void registerTableInDatabase(Table table) throws Exception {
        List<Database> databases = fetchDatabaseMetaData(dbName);
        Database requiredDatabase = databases.stream().filter(database -> database.getDatabaseName().equalsIgnoreCase(dbName)).findFirst()
                .get();
        List<String> tables = requiredDatabase.getTableName();
        if (!tables.contains(table.getTableName())) {
            tables.add(table.getTableName());

            deleteDatabase(dbName);
            for (Database db : databases) {
                createDatabase(db);
            }
        }
    }

    private void deleteDatabase(String dbName) throws UnsupportedEncodingException {
        this.customFileFormat.deleteFile(filePath, dbName);
    }
    public boolean createTable(Table table) throws Exception {
        registerTableInDatabase(table);

        fileName = table.getTableName();

        byte[] encryptedContent = CustomFileFormatHandler.encryptData(table.toString().getBytes());

        return this.customFileFormat.writeToFile(encryptedContent,filePath, fileName);
    }

    public Table fetchTable(String tableName) throws Exception{
        try {
            byte[] encryptedBytes = this.customFileFormat.readFile(filePath, tableName);
            byte[] decryptedBytes = CustomFileFormatHandler.decryptFile(encryptedBytes);

            String tableString = new String(decryptedBytes);
            return TableRowMapper.toTable(tableString);
        }
        catch (Exception e){
            throw new RuntimeException("table does not exist");
        }
    }

    public boolean deleteTable(String tableName) throws Exception {
        return this.customFileFormat.deleteFile(filePath, tableName);
    }

    public CustomFileFormat getCustomFileFormat() {
        return customFileFormat;
    }

    public void setCustomFileFormat(CustomFileFormat customFileFormat) {
        this.customFileFormat = customFileFormat;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }
}
