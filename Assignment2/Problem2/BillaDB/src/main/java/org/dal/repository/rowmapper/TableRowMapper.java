package org.dal.repository.rowmapper;

import org.dal.model.table.Column;
import org.dal.model.table.Table;

import java.util.ArrayList;
import java.util.List;

public class TableRowMapper {
    public static Table toTable(String tableString) throws Exception {

        Table table = new Table();
        List<Column> columns = new ArrayList<>();
        Column column = new Column();
        Column pk = new Column();

        //name
        String[] metaDataArray = tableString.split("@");
        metaDataArray[0] = metaDataArray[0].replace("Table", "");
        table.setTableName(metaDataArray[0]);
        //columns
        metaDataArray[1] = metaDataArray[1].replace("[","").replace("]","");
        String[] columnsArray = metaDataArray[1].split(", Column");
        for (String c : columnsArray) {
            if (c.length() == 0) {
            } else {
                column = new Column();
                c = c.replace("Column", "");
                String[] colParts = c.split("~");

                List<String> values = new ArrayList<>();
                //values
                if (!colParts[0].equalsIgnoreCase("null")) {
                    String[] valuesArray = colParts[0].split(", ");
                    for (String val : valuesArray) {
                        values.add(val);
                    }
                }
                column.setValues(values);
                //datatype
                column.setColumnDataType(colParts[2]);
                column.setColumnName(colParts[1]);
                column.setSize(values.size());
                //null
                if (colParts[4].equalsIgnoreCase("true"))
                    column.setNullable(true);
                //primary key
                if (colParts[5].equalsIgnoreCase("true")) {
                    column.setPrimaryKey(true);
                    pk = column;
                }
                //unique
                if (colParts[6].equalsIgnoreCase("true"))
                    column.setUniqueness(true);

                columns.add(column);
            }
        }
        table.setColumns(columns);
        table.setPrimaryKey(pk);
        return table;
    }
}
