package com.study.lab1.jdbc.mapper;

import com.study.lab1.entity.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper {
    public User mapResultSet(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String email = resultSet.getString("email");
        String pass = resultSet.getString("pass");
        String sole = resultSet.getString("sole");

        User user = User.builder().
                id(id)
                .email(email)
                .pass(pass)
                .sole(sole)
                .build();

        return user;
    }
}
