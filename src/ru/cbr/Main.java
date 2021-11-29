package ru.cbr;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;


public class Main {

    public static void main(String[] args) {

        ISourceList sourceList = new SourceList();
        ITableSaver saver = new SQLSaver(); //new CSVSaver();

        String userName;
        String userPassword;
        String folderName;
        String serverName;

        try {
            userName = args[0];
            userPassword = args[1];
            folderName = args[2];
            serverName = args[3];
        } catch (ArrayIndexOutOfBoundsException ex) {
            System.out.println("bad command line");
            return;
        }


        Path dataPath;

        try {
            dataPath = Paths.get(folderName);

            if (Files.exists(dataPath)) {
                if (!Files.isDirectory(dataPath)) {
                    Files.delete(dataPath);
                }
            }

            dataPath = Files.createDirectories(dataPath);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        try {
            Connection conn = DriverManager.getConnection(
                    "jdbc:oracle:oci:@" + serverName, userName, userPassword);

            var tblList = sourceList.getList(conn);

            Statement stmt = conn.createStatement();
            for (String tableName : tblList) {
                System.out.println(tableName);

                ResultSet ds = getDS(tableName, stmt);
                try (
                        OutputStream fos = Files.newOutputStream(dataPath.resolve(tableName + "-data.sql"));
                        PrintStream printStream = new PrintStream(fos, true, "windows-1251")) {
                    saver.Save(tableName, ds, printStream);
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static ResultSet getDS(String tableName, Statement stmt) throws SQLException {
        return stmt.executeQuery("select * from " + tableName + " order by sqn" );
    }
}