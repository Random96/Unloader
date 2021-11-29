package ru.cbr;

import java.io.PrintStream;

public class SQLSaver extends TableSaver {

    @Override
    void SaveHeader(String tableName, String[] cols, PrintStream printWriter) {

    }

    @Override
    String GetRowTemplate(String tableName, String[] cols) {
        return "INSERT INTO " + tableName + "(" + String.join(",", cols ) + ") VALUES (";
    }

    String OutQuoter(String param)
    {
        if(param == null)
            return null;

        param = param.replace("'", "''");

        var posOfAmpersand = param.indexOf('&');

        if( posOfAmpersand >= 0) {
            System.out.println(param);
            if (posOfAmpersand == 0) {
                if( (param.length() > 1))
                    param = "CHR(38)||'" + param.substring(1);
                else {
                    return "CHR(38)";
                }
            } else {
                param = "'" + param;
            }

            if( param.length() > 1 ) {
                if (param.endsWith("&")) {
                    param = param.substring(0, param.length() - 1) + "'||CHR(38)";
                }
                else
                {
                    param = param + "'";
                }
            }
            else
            {
                param = param + "'";
            }

            posOfAmpersand = param.indexOf('&');

            while (posOfAmpersand >= 0) {
                param = param.substring(0, posOfAmpersand) + "'||CHR(38)||'" + param.substring(posOfAmpersand+1);

                posOfAmpersand = param.indexOf('&');
            }
        }
        else
        {
            param = "'" + param + "'";
        }

        return param;
    }

    @Override
    String FormatRow(String rowTemplate, String[] cols) {
        String [] formatted = new String[cols.length];

        for( int i=0; i <cols.length; ++i )
        {
            if( cols[i] != null)
                formatted[i] = OutQuoter(cols[i]);
            else
                formatted[i] = "NULL";
        }

        return rowTemplate + String.join(",", formatted) + ");";
    }
}
