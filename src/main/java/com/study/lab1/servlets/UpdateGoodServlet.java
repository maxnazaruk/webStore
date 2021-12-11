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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpdateGoodServlet extends HttpServlet {
    private GoodsService goodsService;
    private List<String> userTokens;
    private UserVerificationService userVerificationService;

    public UpdateGoodServlet(GoodsService goodsService, List<String> userTokens, UserVerificationService userVerificationService) {
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
                String page = pageGenerator.getPage("/update.html", parametrs);
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
        int id = Integer.parseInt(req.getParameter("id"));
        String name = req.getParameter("name");
        int price = Integer.parseInt(req.getParameter("price"));
        String date = req.getParameter("date");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate LD = LocalDate.parse(date, formatter);
        LocalDateTime dateTime = LocalDateTime.of(LD, LocalDateTime.now().toLocalTime());

        Map<String, Object> parametrs = new HashMap<>();

        Goods good = Goods.builder().
                name(name)
                .price(price)
                .date(dateTime)
                .build();
        try {
            goodsService.update(good, id);
            List<Goods> goods = goodsService.findAll();
            parametrs.put("goods", goods);
            resp.sendRedirect("/update");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
