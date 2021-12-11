package com.study.lab1.service;

import com.study.lab1.entity.Goods;
import com.study.lab1.jdbc.JDBConnection;

import java.sql.SQLException;
import java.util.List;

public class GoodsService {
    private JDBConnection jdbConnection;

    public GoodsService(JDBConnection jdbConnection) {
        this.jdbConnection = jdbConnection;
    }

    public List<Goods> findAll() throws SQLException {
        return jdbConnection.findAll();
    }

    public void add(Goods goods) throws SQLException {
        jdbConnection.addProduct(goods);
    }

    public void remove(int id) throws SQLException {
        jdbConnection.remove(id);
    }

    public void update(Goods good, int id) throws SQLException {
        jdbConnection.update(good, id);
    }
}
