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

public class RemoveGoodServlet extends HttpServlet {
    private GoodsService goodsService;
    private List<String> userTokens;
    private UserVerificationService userVerificationService;

    public RemoveGoodServlet(GoodsService goodsService, List<String> userTokens, UserVerificationService userVerificationService) {
        this.goodsService = goodsService;
        this.userTokens = userTokens;
        this.userVerificationService = userVerificationService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(userVerificationService.tokenVerification(req.getCookies(), userTokens)) {
            PageGenerator pageGenerator = PageGenerator.instance();
            Map<String, Object> parametrs = new HashMap<>();
            try {
                List<Goods> goods = goodsService.findAll();
                parametrs.put("goods", goods);
                String page = pageGenerator.getPage("remove.html", parametrs);
                resp.getWriter().write(page);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }else {
            resp.sendRedirect("/login");
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
