package ru.cbr;


import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface ITableSaver {
    int Save(String tableName, ResultSet rs, PrintStream printWriter ) throws SQLException;
}
