package com.study.lab1.servlets;

import com.study.lab1.jdbc.JDBConnection;
import com.study.lab1.templater.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AllRequestsServlet extends HttpServlet {

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws IOException {

        Map<String, Object> pageVariables = createPageVariablesMap(request);

        pageVariables.put("message", "");
        try {
            if (pageVariables.get("pathInfo").equals("/goods")) {
                response.getWriter().println(PageGenerator.instance().getPage("goods.html", pageVariables));
            } else if (pageVariables.get("pathInfo").equals("/goods/add")) {
                response.getWriter().println(PageGenerator.instance().getPage("add.html", pageVariables));
            } else if (pageVariables.get("pathInfo").equals("/remove")) {
                response.getWriter().println(PageGenerator.instance().getPage("remove.html", pageVariables));
            } else if (pageVariables.get("pathInfo").equals("/update")) {
                response.getWriter().println(PageGenerator.instance().getPage("update.html", pageVariables));
            } else {

                response.getWriter().println(PageGenerator.instance().getPage("page.html", pageVariables));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);

    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) {
        Map<String, Object> pageVariables = createPageVariablesMap(request);


        if (pageVariables.get("pathInfo").equals("/add.html")) {
            try {
                add(request, response);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } else if (pageVariables.get("pathInfo").equals("/remove.html")) {
            try {
                remove(request, response);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } else if (pageVariables.get("pathInfo").equals("/update.html")) {
            update(request, response);
        }

    }

    private void remove(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        String remove = request.getParameter("remove");
        String tableName = "goods";
        Map<String, Object> pageVariables = createPageVariablesMap(request);
        pageVariables.put("goods", JDBConnection.showAllGoods());

        try {
            JDBConnection.removeById(tableName, remove);
            try {
                response.getWriter().println(PageGenerator.instance().getPage("remove.html", pageVariables));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void update(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");
        String name = request.getParameter("name");
        String price = request.getParameter("price");
        String date = request.getParameter("date");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate LD = LocalDate.parse(date, formatter);
        LocalDateTime dateTime = LocalDateTime.of(LD, LocalDateTime.now().toLocalTime());

        String tableName = "goods";
        Map<String, Object> pageVariables = createPageVariablesMap(request);
        try {
            pageVariables.put("goods", JDBConnection.showAllGoods());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        try {
            JDBConnection.update(tableName, id, name, price, dateTime);
            try {
                response.getWriter().println(PageGenerator.instance().getPage("update.html", pageVariables));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void add(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        String name = request.getParameter("name");
        int price = Integer.parseInt(request.getParameter("price"));
        String tableName = "goods";

        String date = request.getParameter("date");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate LD = LocalDate.parse(date, formatter);
        LocalDateTime dateTime = LocalDateTime.of(LD, LocalDateTime.now().toLocalTime());

        Map<String, Object> pageVariables = createPageVariablesMap(request);


        try {
            JDBConnection.addProduct(tableName, name, price, dateTime);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        try {
            response.getWriter().println(PageGenerator.instance().getPage("goods.html", pageVariables));
        } catch (IOException throwables) {
            throwables.printStackTrace();
        }
    }

    private static Map<String, Object> createPageVariablesMap(HttpServletRequest request) {
        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("method", request.getMethod());
        pageVariables.put("URL", request.getRequestURL().toString());
        pageVariables.put("pathInfo", request.getPathInfo());
        pageVariables.put("sessionId", request.getSession().getId());
        pageVariables.put("parameters", request.getParameterMap().toString());
        return pageVariables;
    }
}
