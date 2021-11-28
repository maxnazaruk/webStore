package com.study.lab1.jdbc;

import java.sql.*;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class JdbcTest {
    static String selectAllGoods = "SELECT * FROM goods;";
    static String clearTable = "DELETE FROM goods;";
    static String url = "jdbc:postgresql://localhost:5432/webstore";
    static String user = "user";
    static String password = "pswd";


    public static void main(String[] args) throws SQLException, ParseException {
        fullFilGoodTables(10);
        //clearTable();
        //showAllGoods();
    }

    private static void clearTable() throws SQLException {
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            Statement statement = connection.createStatement();
            statement.execute(clearTable);
        }
    }

    private static void fullFilGoodTables(int number) throws SQLException, ParseException {
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            Statement statement = connection.createStatement();
            for (int i = 0; i < number; i++) {
                statement.execute(goodsGenerator());
            }
        }
    }

    public static void addProduct(String name, int price, java.util.Date date) throws SQLException {
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            Statement statement = connection.createStatement();
            statement.execute("INSERT INTO goods (name, price, creationdate) VALUES ('" + name + "', " + price + ", NOW());");
        }
    }

    public static void removeById(String id) throws SQLException {
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            Statement statement = connection.createStatement();
            statement.executeUpdate("DELETE FROM goods WHERE id = " + id + ";");
        }
    }

    public static void update(String id, String name, String price) throws SQLException {
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            Statement statement = connection.createStatement();
            statement.executeUpdate("UPDATE goods SET name = '" + name + "', price = " + price + ", creationdate = NOW() WHERE id = " + id + ";");
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

    private static String goodsGenerator() throws ParseException {

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

        int year = new Random().nextInt((2021 - 2018) + 1) + 2018;
        int month = new Random().nextInt((12 - 1) + 1) + 1;
        int day = new Random().nextInt((28 - 1) + 1) + 1;

        String sMonth = String.valueOf(month);
        String sDay = String.valueOf(day);
        if(month < 10){
            sMonth = "0" + month;
        }

        if(day < 10){
            sDay = "0" + day;
        }

        String from_date = year + "-" + sMonth + "-" + sDay;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(from_date, formatter);

        Date date = Date.valueOf(LocalDate.of(year, month, day));


        String request = "INSERT INTO goods (name, price, creationdate) VALUES ('"
                + name.toString() + "', "
                + price + ", NOW());";

        return request;

    }
}
