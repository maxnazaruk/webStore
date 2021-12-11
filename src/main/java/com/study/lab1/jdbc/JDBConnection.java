package com.study.lab1.jdbc;

import com.study.lab1.entity.Goods;
import com.study.lab1.jdbc.mapper.GoodMapper;
import org.postgresql.util.PSQLException;

import java.sql.*;
import java.util.*;

public class JDBConnection {
    private static final GoodMapper GOOD_MAPPER = new GoodMapper();
    private static final String SELECT_ALL = "SELECT id, name, price, date FROM goods";
    private static final String INSERT_INTO = "INSERT INTO goods (name, price, date) VALUES (?, ?, ?);";
    private static final String UPDATE = "UPDATE goods SET name = ?, price = ?, date = ? WHERE id = ?;";
    private static final String REMOVE = "DELETE FROM goods WHERE id = ?;";

    private Connection getConnection() throws SQLException, PSQLException {
        return DriverManager.getConnection("jdbc:postgresql://localhost:5432/webstore",
                "user", "pswd");
    }

    public void addProduct(Goods good) throws SQLException {
        try (Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_INTO);
            preparedStatement.setString(1, good.getName());
            preparedStatement.setInt(2, good.getPrice());
            preparedStatement.setTimestamp(3, Timestamp.valueOf(good.getDate()));
            preparedStatement.executeUpdate();
        }
    }

    public List<Goods> findAll() throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            List<Goods> goods = new ArrayList<>();
            while (resultSet.next()) {
                Goods good = GOOD_MAPPER.mapResultSet(resultSet);
                goods.add(good);
            }
            return goods;
        }
    }

    public void update(Goods good, int id) throws SQLException {
        try (Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE);
            preparedStatement.setString(1, good.getName());
            preparedStatement.setInt(2, good.getPrice());
            preparedStatement.setTimestamp(3, Timestamp.valueOf(good.getDate()));
            preparedStatement.setInt(4, id);
            preparedStatement.executeUpdate();
        }
    }

    public void remove(int id) throws SQLException {
        try (Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(REMOVE);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
    }
}
