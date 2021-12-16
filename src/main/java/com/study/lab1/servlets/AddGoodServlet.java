package com.study.lab1.servlets;

import com.study.lab1.entity.Goods;
import com.study.lab1.service.GoodsService;
import com.study.lab1.templater.PageGenerator;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class AddGoodServlet extends HttpServlet {
    private GoodsService goodsService;

    public AddGoodServlet(GoodsService goodsService) {
        this.goodsService = goodsService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            PageGenerator pageGenerator = PageGenerator.instance();
            HashMap<String, Object> parameters = new HashMap<>();
            try {
                String page = pageGenerator.getPage("templates/add.html", parameters);
                resp.getWriter().write(page);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        int price = Integer.parseInt(req.getParameter("price"));
        String date = req.getParameter("date");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate LD = LocalDate.parse(date, formatter);
        LocalDateTime dateTime = LocalDateTime.of(LD, LocalDateTime.now().toLocalTime());

        Goods goods = Goods.builder().
                name(name)
                .price(price)
                .date(dateTime)
                .build();

        try {
            goodsService.add(goods);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        resp.sendRedirect("/goods");
    }
}
