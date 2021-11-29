package ru.cbr;

import java.io.PrintStream;

public class CSVSaver extends TableSaver {
    @Override
    void SaveHeader(String tableName, String[] cols, PrintStream printWriter) {
        String row = String.join(";", cols );
        printWriter.println(row);
    }

    @Override
    String GetRowTemplate(String tableName, String[] cols) {
        return null;
    }


    @Override
    String FormatRow(String rowTemplate, String[] cols) {
        return String.join(";", cols );
    }
}
