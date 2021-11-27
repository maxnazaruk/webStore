package com.study.lab1.jdbc;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;

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

    public static void showAllGoods() throws SQLException {
        ResultSet resultSet;
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            Statement statement = connection.createStatement();

             resultSet = statement.executeQuery(selectAllGoods);
        }

        while(resultSet.next()){
            int id  = resultSet.getInt("id");
            int price  = resultSet.getInt("price");
            String name = resultSet.getString("name");
            Date date = resultSet.getDate("creationdate");

            System.out.print(id + ", " + name + ", " + price + ", " + date);
            System.out.println();
        }


    }

    private static String goodsGenerator() {

        StringBuilder name = new StringBuilder("");
        int nameLength = new Random().nextInt((8 - 3) + 1) + 3;

        for (int i = 0; i < nameLength; i++) {
            if(i == 0){
                name.append((char) (new Random().nextInt((90 - 65) + 1) + 65));
            }else{
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
