package com.study.lab1.goods;

import com.study.lab1.jdbc.JDBConnection;

import java.time.LocalDateTime;
import java.util.List;

public class Goods {
    private int id;
    private String name;
    private int price;
    private LocalDateTime date;

    public Goods(int id, String name, int price, LocalDateTime date) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.date = date;
    }

    @Override
    public String toString() {
        return "Goods{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", date=" + date +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
