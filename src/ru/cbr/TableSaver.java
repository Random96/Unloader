package ru.cbr;

import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public abstract class TableSaver implements ITableSaver {

    abstract void SaveHeader(String tableName, String [] cols, PrintStream printWriter);
    abstract String GetRowTemplate(String tableName, String [] cols);
    abstract String  FormatRow(String rowTemplate, String [] cols);

    @Override
    public int Save(String tableName, ResultSet rs, PrintStream printWriter ) throws SQLException {
        if( rs == null )
            throw new IllegalArgumentException("Result set may not be null");

        if( tableName == null)
            throw new IllegalArgumentException("tableName may not be null");

        if( printWriter == null)
            throw new IllegalArgumentException("output stream may not be null");

        String [] colsValues = GetColuns(rs);
        int column_count = colsValues.length;

        String rowTemplate = GetRowTemplate(tableName, colsValues);

        SaveHeader(tableName, colsValues, printWriter);

        while( rs.next()){
            for( int i=0; i<column_count; ++i) {
                colsValues[i] = rs.getString(i+1);
            }

            String row = FormatRow(rowTemplate, colsValues);

            printWriter.println(row);
        }
        return 0;
    }

    String [] GetColuns(ResultSet rs) throws SQLException {

        ResultSetMetaData rsmd = rs.getMetaData();

        int column_count = rsmd.getColumnCount();

        String [] colsValues = new String[column_count];

        for( int i=0; i<column_count; ++i) {
            colsValues[i] = rsmd.getColumnName(i+1);
        }

        return  colsValues;
    }

}
