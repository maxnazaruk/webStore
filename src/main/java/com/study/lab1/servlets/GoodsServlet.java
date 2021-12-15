package com.study.lab1.servlets;

import com.study.lab1.entity.Goods;
import com.study.lab1.service.GoodsService;
import com.study.lab1.service.UserVerificationService;
import com.study.lab1.templater.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GoodsServlet extends HttpServlet {
    private GoodsService goodsService;
    private List<String> userTokens;
    private UserVerificationService userVerificationService;

    public GoodsServlet(GoodsService goodsService, List<String> userTokens, UserVerificationService userVerificationService) {
        this.goodsService = goodsService;
        this.userTokens = userTokens;
        this.userVerificationService = userVerificationService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            PageGenerator pageGenerator = PageGenerator.instance();
            Map<String, Object> parametrs = new HashMap<>();
            try {
                List<Goods> goods = goodsService.findAll();
                parametrs.put("goods", goods);
                String page = pageGenerator.getPage("templates/goods.html", parametrs);
                resp.getWriter().write(page);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
    }
}
