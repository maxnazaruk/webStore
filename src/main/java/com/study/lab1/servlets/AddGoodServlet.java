package com.study.lab1.servlets;

import com.study.lab1.entity.Goods;
import com.study.lab1.service.GoodsService;
import com.study.lab1.service.UserVerificationService;
import com.study.lab1.templater.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;

public class AddGoodServlet extends HttpServlet {
    private GoodsService goodsService;
    private List<String> userTokens;
    private UserVerificationService userVerificationService;

    public AddGoodServlet(GoodsService goodsService, List<String> userTokens, UserVerificationService userVerificationService) {
        this.goodsService = goodsService;
        this.userTokens = userTokens;
        this.userVerificationService = userVerificationService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(userVerificationService.tokenVerification(req.getCookies(), userTokens)) {

            PageGenerator pageGenerator = PageGenerator.instance();
            HashMap<String, Object> parameters = new HashMap<>();
            try {
                String page = pageGenerator.getPage("add.html", parameters);
                resp.getWriter().write(page);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }else{
            resp.sendRedirect("/login");
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
