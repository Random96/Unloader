package ru.cbr;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface ISourceList {
    List<String> getList(Connection con) throws SQLException;
}
