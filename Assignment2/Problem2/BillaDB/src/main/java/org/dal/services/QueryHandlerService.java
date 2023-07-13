package org.dal.services;

import org.dal.model.Database;
import org.dal.model.table.Column;
import org.dal.model.table.Table;
import org.dal.repository.QueryHandlerRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QueryHandlerService {

    private static final String databaseConstant = "database";
    private static final String tableConstant = "table";

    private String operationName;
    private QueryHandlerRepository queryHandlerRepository = new QueryHandlerRepository();

    public void queryHandler(String sql) throws Exception {
        String[] queryParts = sql.split(" ");
        setOperationName(queryParts[0].toLowerCase());

        switch (operationName.toLowerCase()) {
            case "create" -> queryHandlerForCreateStatement(queryParts, sql);
            case "select" -> queryHandlerForSelectStatement(sql);
            case "insert" -> queryHandlerForInsertStatement(sql);
            case "update" -> queryHandlerForUpdateStatement(sql);
            case "delete" -> queryHandlerForDeleteStatement(sql);
            case "use" -> queryHandlerForUseStatement(sql);
            case "begin" -> queryHandlerForTransaction(sql);
            case "erd" -> cardinalityHandler(sql);
            default -> throw new RuntimeException("Incorrect operation in query");
        }
    }

    private void cardinalityHandler(String sql) throws Exception {

        String databaseName = this.queryHandlerRepository.getDbName();
        List<Database> databases = this.queryHandlerRepository.fetchDatabaseMetaData(databaseName);
        Database datab = databases.stream().filter(db -> db.getDatabaseName().equalsIgnoreCase(databaseName)).findFirst().get();
        System.out.println(datab.toString());
    }

    private void queryHandlerForTransaction(String sql) throws Exception {

        String[] sqlParts = sql.split(" ");
        sqlParts[1] = sqlParts[1].replace(";", "");
        sqlParts[sqlParts.length-1]= sqlParts[sqlParts.length-1].replace(";", "");

        if (sqlParts[1].equalsIgnoreCase("transaction") &&
                sqlParts[sqlParts.length-1].equalsIgnoreCase("transaction")
                && sqlParts[sqlParts.length-2].equalsIgnoreCase("end")){

            int index = -1;
            for (int i =0; i< sqlParts.length; i++){
                sqlParts[i].replace(";","");
                if (sqlParts[i].equalsIgnoreCase("commit")){
                    index = i;
                }
            }
            if (index!=-1) {
                String newSql = "";
                for (int i = 2; i < index; i++) {
                    newSql = newSql.concat(sqlParts[i]);
                }
                queryHandler(newSql);
                System.out.println("TRANSACTION COMMITTED");
            }
        }
        else{
            System.out.println("wrong transaction format");
        }
    }

    public boolean queryHandlerForCreateStatement(String[] queryParts, String sql) throws Exception {
        if (queryParts[1].equalsIgnoreCase(databaseConstant)) {
            String regex = "CREATE DATABASE\\s+(\\w+);";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(sql);

            if (matcher.find()) {
                Database database = new Database();
                queryParts[2] = queryParts[2].replace(";","");
                database.setDatabaseName(queryParts[2].toLowerCase());
                System.out.println("Database created successfully");
                return queryHandlerRepository.createDatabase(database);
            } else {
                throw new IllegalArgumentException("Invalid CREATE Database statement");
            }

        }
        if (queryParts[1].equalsIgnoreCase(tableConstant)) {
            String regex = "(?i)\\bCREATE\\b\\s+\\bTABLE\\b\\s+\\w+\\s*\\(.*\\);";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(sql);

            if (matcher.find()) {
            } else {
                throw new IllegalArgumentException("Invalid CREATE TABLE statement");
            }

            Table table = new Table();
            table.setTableName(queryParts[2].toLowerCase());

            List<Column> columns = new ArrayList<>();

            int startIndex = sql.indexOf("(");
            int lastIndex = sql.lastIndexOf(")");
            String columnStringBody = sql.substring(startIndex + 1, lastIndex);
            String[] columnStringParts = columnStringBody.split(",\\s*");

            for (String columnString : columnStringParts) {
                Column c1 = new Column();
                if (columnString.toLowerCase().contains("int") || columnString.toLowerCase().contains("date")
                        || columnString.toLowerCase().contains("decimal") || columnString.toLowerCase().contains("varchar")
                        || columnString.toLowerCase().contains("boolean")) {

                    String[] columnWords = columnString.split(" ");
                    //name
                    String columnName = columnWords[0];
                    //datatype
                    String columnDataType = columnWords[1];
                    String dataType;
                    try {
                        if (columnDataType.equalsIgnoreCase("int"))
                            dataType = "Integer";
                        else if (columnDataType.equalsIgnoreCase("date"))
                            dataType = "Date";
                        else if (columnDataType.equalsIgnoreCase("varchar"))
                            dataType = "String";
                        else if (columnDataType.equalsIgnoreCase("boolean"))
                            dataType = "Boolean";
                        else if (columnDataType.equalsIgnoreCase("decimal"))
                            dataType = "BigDecimal";
                        else
                            throw new ClassNotFoundException();

                        c1.setColumnName(columnName);
                        c1.setSize(0);
                        c1.setColumnDataType(columnDataType);

                        //constraints
                        String constraint = "";
                        for (int i = 2; i < columnWords.length; i++) {
                            if (i == 2)
                                constraint = columnWords[i];
                            else
                                constraint = constraint.concat(columnWords[i]);
                        }
                        if (constraint.equalsIgnoreCase("NOT NULL"))
                            c1.setNullable(false);
                        if (constraint.equalsIgnoreCase("UNIQUE"))
                            c1.setUniqueness(true);
                        if (constraint.equalsIgnoreCase("Primary Key")) {
                            c1.setPrimaryKey(true);
                            table.setPrimaryKey(c1);
                        }
                        columns.add(c1);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                // if column body defines separate constraint
                else {
                    if (columnString.toLowerCase().contains("unique")) {
                        Column c2;

                        int start = columnString.indexOf("(");
                        int last = columnString.lastIndexOf(")");
                        String columnNm = columnString.substring(start, last);
                        c2 = columns.stream().filter(column -> column.getColumnName().equalsIgnoreCase(columnNm))
                                .findFirst().get();
                        c2.setUniqueness(true);
                    } else if (columnString.toLowerCase().contains("primary key")) {
                        Column c2;

                        int start = columnString.indexOf("(");
                        int last = columnString.lastIndexOf(")");
                        String columnNm = columnString.substring(start, last);
                        c2 = columns.stream().filter(column -> column.getColumnName().equalsIgnoreCase(columnNm))
                                .findFirst().get();
                        c2.setPrimaryKey(true);
                        table.setPrimaryKey(c2);
                    } else
                        throw new RuntimeException("constraint not supported");
                }
                table.setColumns(columns);
            }
            System.out.println("Table created successfully");
            return queryHandlerRepository.createTable(table);
        }
        throw new RuntimeException("wrong create statement");
    }

    private void queryHandlerForUseStatement(String sql) {

        try{
            String[] sqlParts = sql.split(" ");
            String databaseName = sqlParts[1];
            databaseName = databaseName.replace(";", "");
            this.queryHandlerRepository.setDbName(databaseName);
            System.out.println("Database selected");
        }catch (Exception e){
            throw new RuntimeException("wrong use statement");
        }
    }
    private void queryHandlerForDeleteStatement(String sql) throws Exception {

        String regex = "DELETE FROM\\s+(\\w+)\\s+WHERE\\s+(.*?);";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(sql);

        if (matcher.find()) {
            String tableName = matcher.group(1);
            String whereCondition = matcher.group(2);

            String[] whereConditionParts = whereCondition.split("=\\s*");
            Table table = queryHandlerRepository.fetchTable(tableName);
            List<Column> columns = table.getColumns();

            Column column = columns.stream().filter(column1 -> column1.getColumnName()
                    .equalsIgnoreCase(whereConditionParts[0].trim())).findFirst().get();
            //find index
            List<String> conditionValues = column.getValues();
            int index = -1;
            for (int i = 0; i < conditionValues.size(); i++) {
                if (conditionValues.get(i).equalsIgnoreCase(whereConditionParts[1]))
                    index = i;
            }
            if (index == -1)
                throw new RuntimeException("no match found");
            //update value
            for (Column c: columns) {
                c.getValues().remove(index);
            }
            this.queryHandlerRepository.deleteTable(tableName);
            this.queryHandlerRepository.createTable(table);
            System.out.println("One row deleted");
        } else {
            throw new IllegalArgumentException("Invalid DELETE statement");
        }
    }

    private void queryHandlerForUpdateStatement(String sql) throws Exception {

        String regex = "UPDATE\\s+(\\w+)\\s+SET\\s+(.*?)\\s+WHERE\\s+(.*?);";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(sql);

        if (matcher.find()) {
            String tableName = matcher.group(1);
            String setExpressions = matcher.group(2);
            String whereCondition = matcher.group(3);

            String[] setExpressionParts = setExpressions.split("=\\s*");
            String[] whereConditionParts = whereCondition.split("=\\s*");


            Table table = queryHandlerRepository.fetchTable(tableName);
            List<Column> columns = table.getColumns();

            Column column = columns.stream().filter(column1 -> column1.getColumnName()
                    .equalsIgnoreCase(setExpressionParts[0].trim())).findFirst().get();
            Column column2 = columns.stream().filter(column1 -> column1.getColumnName()
                    .equalsIgnoreCase(whereConditionParts[0].trim())).findFirst().get();
            //find index
            List<String> conditionValues = column2.getValues();
            int index = -1;
            for (int i = 0; i < conditionValues.size(); i++) {
                if (conditionValues.get(i).equalsIgnoreCase(whereConditionParts[1]))
                    index = i;
            }
            if (index == -1)
                throw new RuntimeException("no match found");
            //update value
            column.getValues().set(index, setExpressionParts[1]);
            this.queryHandlerRepository.deleteTable(tableName);
            this.queryHandlerRepository.createTable(table);
            System.out.println("One row updated");

        } else {
            throw new IllegalArgumentException("Invalid update statement");
        }
    }

        private void queryHandlerForInsertStatement(String sql) throws Exception {

        String regex = "INSERT INTO\\s+(\\w+)\\s+\\((.*?)\\)\\s+VALUES\\s+\\((.*?)\\);";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(sql);

        if (matcher.find()) {
            String tableName = matcher.group(1);
            String columnNames = matcher.group(2);
            String values = matcher.group(3);

            String[] columnNameArray = columnNames.split(",\\s*");
            String[] valuesNameArray = values.split(",\\s*");
            for (int i = 0; i < valuesNameArray.length; i++) {
                valuesNameArray[i] = valuesNameArray[i].replace("'", "")
                        .replace("\"", "");
            }
            Table table = queryHandlerRepository.fetchTable(tableName);
            List<Column> columns = table.getColumns();
            for (int i = 0; i < columns.size(); i++) {
                Column c = columns.get(i);
                for (int j = 0; j < columnNameArray.length; j++) {
                    if (c.getColumnName().equalsIgnoreCase(columnNameArray[j])) {
                        c.getValues().add(valuesNameArray[j]);
                        c.setSize(c.getSize() + 1);

                    }
                }
            }
            queryHandlerRepository.deleteTable(tableName);
            queryHandlerRepository.createTable(table);
        } else {
            throw new IllegalArgumentException("Invalid INSERT statement");
        }
    }

private void queryHandlerForSelectStatement(String sql) throws Exception {

    String regex = "SELECT\\s+(\\*|.*?)\\s+FROM\\s+(\\w+)\\s+WHERE\\s+(.*?);";
    Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(sql);

        if (matcher.find()) {
            String tableName = matcher.group(2);
            String whereCondition = matcher.group(3);

            String[] whereConditionParts = null;
            if (whereCondition != null)
                whereConditionParts = whereCondition.split("=\\s*");

            Table table = queryHandlerRepository.fetchTable(tableName);
            List<Column> columns = table.getColumns();
            //find index
            String columnName = new String(whereConditionParts[0]);

            Optional<Column> column5 = columns.stream().filter(c1 -> c1.getColumnName()
                    .equals(columnName.trim())).findAny();
            Column column = column5.get();
            List<String> conditionValues = column.getValues();
            int index = -1;
            for (int i = 0; i < conditionValues.size(); i++) {
                if (conditionValues.get(i).equalsIgnoreCase(whereConditionParts[1].trim()))
                    index = i;
            }
            for (Column c :columns){
                System.out.format("%14s", c.getColumnName());
            }
            System.out.println();
            for (Column c :columns){
                System.out.format("%14s", c.getValues().get(index));

            }
            if (index == -1)
                throw new RuntimeException("no match found");
        }
        else{
            throw new IllegalArgumentException("Invalid SELECT statement");
        }
    }


    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    public QueryHandlerRepository getQueryHandlerRepository() {
        return queryHandlerRepository;
    }

    public void setQueryHandlerRepository(QueryHandlerRepository queryHandlerRepository) {
        this.queryHandlerRepository = queryHandlerRepository;
    }
}
