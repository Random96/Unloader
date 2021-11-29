package ru.cbr;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SourceList implements ISourceList {
    @Override
    public List<String> getList(Connection conn) throws SQLException {
        List<String> ret = new ArrayList<>();

        Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet rset = stmt.executeQuery("SELECT TABLE_NAME FROM USER_TABLES WHERE TABLE_NAME LIKE '%_DICT_%'");

        PreparedStatement pstmt =
                conn.prepareStatement("SELECT COUNT(1) FROM DUAL WHERE EXISTS ( SELECT 1 FROM USER_TAB_COLUMNS WHERE TABLE_NAME = ? AND COLUMN_NAME = 'SQN')");

        while (rset.next()) {
            String table_name = rset.getString(1);
            pstmt.setString(1, table_name);
            ResultSet count_rset = pstmt.executeQuery();     // Do the update
            count_rset.next();
            if (count_rset.getInt(1) > 0)
                ret.add(table_name);
        }

        return ret;
    }
}
