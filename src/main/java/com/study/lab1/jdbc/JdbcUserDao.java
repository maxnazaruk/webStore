package com.study.lab1.jdbc;

import com.study.lab1.entity.Goods;
import com.study.lab1.entity.User;
import com.study.lab1.jdbc.mapper.GoodMapper;
import com.study.lab1.jdbc.mapper.UserMapper;
import org.postgresql.util.PSQLException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcUserDao {
    private static final UserMapper USER_MAPPER = new UserMapper();
    private static final String SELECT_ALL = "SELECT id, email, pass, sole FROM users;";
    private static final String CREATE_TABLE = "CREATE TABLE users (id SERIAL, email varchar(255), pass varchar(255), sole varchar(255));";
    private static final String INSERT_INTO = "INSERT INTO users (email, pass, sole) VALUES (?, ?, ?);";

    private Connection getConnection() throws SQLException, PSQLException {
        return DriverManager.getConnection("jdbc:postgresql://localhost:5432/webstore",
                "user", "pswd");
    }

    public void addUser(User user) throws SQLException {
        if(!checkTableExists()){
            createUsersTable();
        }
        try (Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_INTO);
            preparedStatement.setString(1, user.getEmail());
            preparedStatement.setString(2, user.getPass());
            preparedStatement.setString(3, user.getSole());
            preparedStatement.executeUpdate();
        }
    }

    public List<User> findAll() throws SQLException {
        if(!checkTableExists()){
            createUsersTable();
        }
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            List<User> users = new ArrayList<>();
            while (resultSet.next()) {
                User user = USER_MAPPER.mapResultSet(resultSet);
                users.add(user);
            }
            return users;
        }
    }

    public void createUsersTable() {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(CREATE_TABLE)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public  boolean checkTableExists() {
        try (Connection connection = getConnection();
        ) {
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            ResultSet resultSet = databaseMetaData.getTables(null, null, "users", null);
            if(resultSet.next()){
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
