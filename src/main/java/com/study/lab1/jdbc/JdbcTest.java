package com.study.lab1.jdbc;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

public class JdbcTest {
    static String selectAllGoods = "SELECT * FROM goods;";
    static String clearTable = "DELETE FROM goods;";
    static String url = "jdbc:postgresql://localhost:5432/webstore";
    static String user = "user";
    static String password = "pswd";
    static int id = 0;


    public static void main(String[] args) throws SQLException {
        //fullFilGoodTables(10);
        //clearTable();
        showAllGoods();
    }

    private static void clearTable() throws SQLException {
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            Statement statement = connection.createStatement();
            statement.execute(clearTable);
        }
    }

    private static void fullFilGoodTables(int number) throws SQLException {
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            Statement statement = connection.createStatement();
            for (int i = 0; i < number; i++) {
                statement.execute(goodsGenerator());
            }
        }
    }

    public static void addProduct(String name, int price, Date date) throws SQLException {
        id++;
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            Statement statement = connection.createStatement();
            statement.execute("INSERT INTO goods VALUES (" + id + ", '" + name + "', " + price + ", NOW());");
        }
    }

    public static Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to the PostgreSQL server successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return conn;
    }

    public static ResultSet showAllGoods() throws SQLException {
        Statement statement;
        ResultSet resultSet;
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            statement = connection.createStatement();

            resultSet = statement.executeQuery(selectAllGoods);
        }

        return resultSet;
    }

    private static String goodsGenerator() {

        StringBuilder name = new StringBuilder("");
        int nameLength = new Random().nextInt((8 - 3) + 1) + 3;

        for (int i = 0; i < nameLength; i++) {
            if (i == 0) {
                name.append((char) (new Random().nextInt((90 - 65) + 1) + 65));
            } else {
                name.append((char) (new Random().nextInt((122 - 97) + 1) + 97));
            }
        }

        int price = new Random().nextInt((1999 - 99) + 1) + 99;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date now = new Date();
        String dateNow = sdf.format(now);

        String request = "INSERT INTO goods VALUES (" + id + ", '"
                + name.toString() + "', "
                + price + ", NOW());";

        id++;

        return request;

    }
}
