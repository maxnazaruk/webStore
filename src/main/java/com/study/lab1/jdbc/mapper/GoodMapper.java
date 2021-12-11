package com.study.lab1.jdbc.mapper;

import com.study.lab1.entity.Goods;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class GoodMapper {
    public Goods mapResultSet(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String name = resultSet.getString("name");
        int price = resultSet.getInt("price");
        LocalDateTime localDateTime = resultSet.getTimestamp("date").toLocalDateTime();

        Goods good = Goods.builder().
                id(id)
                .name(name)
                .price(price)
                .date(localDateTime).
                build();

        return good;
    }
}
