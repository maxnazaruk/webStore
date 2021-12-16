package com.study.lab1.servlets;

import com.study.lab1.entity.Goods;
import com.study.lab1.service.GoodsService;
import com.study.lab1.service.UserVerificationService;
import com.study.lab1.templater.PageGenerator;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RemoveGoodServlet extends HttpServlet {
    private GoodsService goodsService;

    public RemoveGoodServlet(GoodsService goodsService) {
        this.goodsService = goodsService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            PageGenerator pageGenerator = PageGenerator.instance();
            Map<String, Object> parametrs = new HashMap<>();
            try {
                List<Goods> goods = goodsService.findAll();
                parametrs.put("goods", goods);
                String page = pageGenerator.getPage("templates/remove.html", parametrs);
                resp.getWriter().write(page);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("remove"));
        try {
            goodsService.remove(id);
            resp.sendRedirect("/remove");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
