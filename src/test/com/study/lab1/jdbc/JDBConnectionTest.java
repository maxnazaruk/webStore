package com.study.lab1.jdbc;

import org.junit.jupiter.api.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JDBConnectionTest {
    public static final String tableName = "GoodsTest";

    @BeforeEach
    public void setup() throws SQLException {
        JDBConnection.createTestTable();
    }

    @AfterEach
    public void tearDown() throws SQLException {
        JDBConnection.dropTestTable();
    }

    @Test
    public void testAddRowToTable() throws SQLException {
        JDBConnection.addProduct(tableName, "testproduct", 500, "2021-10-09");

        ResultSet resultSet = JDBConnection.showAllGoods(tableName);

        StringBuilder result = new StringBuilder();

        while(resultSet.next()){
            result.append(resultSet.getInt("id"));
            result.append(resultSet.getString("name"));
            result.append(resultSet.getInt("price"));
            result.append(resultSet.getString("creationdate"));
        }

        assertEquals("1testproduct5002021-10-09", result.toString());
    }

    @Test
    public void testUpdateRow() throws SQLException {
        JDBConnection.addProduct(tableName, "testproduct", 500, "2019-05-05");
        JDBConnection.update(tableName, "1", "update", "100", "2020-10-10");

        ResultSet resultSet = JDBConnection.showAllGoods(tableName);

        StringBuilder result = new StringBuilder();

        while(resultSet.next()){
            result.append(resultSet.getInt("id"));
            result.append(resultSet.getString("name"));
            result.append(resultSet.getInt("price"));
            result.append(resultSet.getString("creationdate"));
        }

        assertEquals("1update1002020-10-10", result.toString());
    }

    @Test
    public void testRemoveRow() throws SQLException {
        JDBConnection.addProduct(tableName, "good1", 100, "2020-06-06");
        JDBConnection.addProduct(tableName, "good2", 200, "2021-06-06");
        JDBConnection.removeById(tableName, "1");

        ResultSet resultSet = JDBConnection.showAllGoods(tableName);

        StringBuilder result = new StringBuilder();

        while(resultSet.next()){
            result.append(resultSet.getInt("id"));
            result.append(resultSet.getString("name"));
            result.append(resultSet.getInt("price"));
            result.append(resultSet.getString("creationdate"));
        }

        assertEquals("2good22002021-06-06", result.toString());
    }

    @Test
    public void testShowAll() throws SQLException {
        JDBConnection.addProduct(tableName, "good1", 100, "2021-01-01");
        JDBConnection.addProduct(tableName, "good2", 200, "2019-01-05");

        ResultSet resultSet = JDBConnection.showAllGoods(tableName);

        StringBuilder result = new StringBuilder();

        while(resultSet.next()){
            result.append(resultSet.getInt("id"));
            result.append(resultSet.getString("name"));
            result.append(resultSet.getInt("price"));
            result.append(resultSet.getString("creationdate"));
        }

        assertEquals("1good11002021-01-012good22002019-01-05", result.toString());
    }

    @Test
    public void testFullFillTable() throws SQLException, ParseException {
        JDBConnection.fullFilGoodTables(tableName, 10);
        ResultSet resultSet = JDBConnection.showAllGoods(tableName);

        StringBuilder result = new StringBuilder();

        while(resultSet.next()){
            result.append(resultSet.getInt("id"));
        }

        assertEquals("12345678910", result.toString());
    }

    @Test
    public void testClearTable() throws SQLException, ParseException {
        JDBConnection.fullFilGoodTables(tableName, 10);
        JDBConnection.clearTable(tableName);
        ResultSet resultSet = JDBConnection.showAllGoods(tableName);

        StringBuilder result = new StringBuilder();

        while(resultSet.next()){
            result.append(resultSet.getInt("id"));
        }

        assertEquals("", result.toString());
    }
}
