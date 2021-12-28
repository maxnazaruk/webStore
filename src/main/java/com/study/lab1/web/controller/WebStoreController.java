package com.study.lab1.web.controller;

import com.study.lab1.entity.Goods;
import com.study.lab1.entity.User;
import com.study.lab1.service.GoodsService;
import com.study.lab1.service.UserVerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
public class WebStoreController {

    @Autowired
    private GoodsService goodsService;
    @Autowired
    private UserVerificationService userVerificationService;

    public void setGoodsService(GoodsService goodsService) {
        this.goodsService = goodsService;
    }

    public void setUserVerificationService(UserVerificationService userVerificationService) {
        this.userVerificationService = userVerificationService;
    }

    private List<String> userTokens = new ArrayList<>();

    @RequestMapping(path = "/", method = RequestMethod.GET)
    protected String main() {
        return "page";
    }

    @RequestMapping(path = "/", method = RequestMethod.POST)
    protected String logout() {
        userTokens.clear();
        return "login";
    }

    @RequestMapping(path = "/add", method = RequestMethod.GET)
    protected String showAdd() {
        return"add";
    }

    @RequestMapping(path = "/add", method = RequestMethod.POST)
    protected String add(@RequestParam(value = "name", required = false) String name,
                         @RequestParam(value = "price", required = false) String price,
                         @RequestParam(value = "date", required = false) String date) throws SQLException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate LD = LocalDate.parse(date, formatter);
        LocalDateTime dateTime = LocalDateTime.of(LD, LocalDateTime.now().toLocalTime());

        Goods goods = Goods.builder().
                name(name)
                .price(Integer.parseInt(price))
                .date(dateTime)
                .build();

            goodsService.add(goods);

        return "redirect:/";
    }

    @RequestMapping(path = "/goods", method = RequestMethod.GET)
    protected String goods(Model model) throws SQLException {
        List<Goods> goods = goodsService.findAll();
        model.addAttribute("goods", goods);
        return "goods";
    }

    @RequestMapping(path = "/login", method = RequestMethod.GET)
    protected String loginShow() {
        return "login";
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    protected String login(@RequestParam(value = "email", required = true) String email,
                           @RequestParam(value = "password", required = true) String password,
                           HttpServletResponse resp,
                           Model model)  {
        User user = User.builder().email(email).pass(password).sole("123").build();

        try {
            if (userVerificationService.signInUser(user)) {
                String userToken = UUID.randomUUID().toString();
                userTokens.add(userToken);
                Cookie cookie = new Cookie("user-token", userToken);
                resp.addCookie(cookie);
                return "redirect:/";
            } else {
                user.setSole("123");
                user.setPass(userVerificationService.passwordConverter(user, password));
                userVerificationService.addNewUser(user);
                String userToken = UUID.randomUUID().toString();
                userTokens.add(userToken);
                Cookie cookie = new Cookie("user-token", userToken);
                resp.addCookie(cookie);
                return "redirect:/";
            }
        } catch (IllegalArgumentException ex) {
            String incorrectPass = " Password is incorrect";
            model.addAttribute("error", incorrectPass);
                return "login";
        }
    }

    @RequestMapping(path = "/remove", method = RequestMethod.GET)
    protected String removeShow(Model model) throws SQLException {
        List<Goods> goods = goodsService.findAll();
        model.addAttribute("goods", goods);
        return "remove";
    }

    @RequestMapping(path = "/remove", method = RequestMethod.POST)
    protected String remove(@RequestParam (value = "remove") String remove) throws SQLException {
        int id = Integer.parseInt(remove);
        goodsService.remove(id);
        return "redirect:/remove";
    }

    @RequestMapping(path = "/update", method = RequestMethod.GET)
    protected String updateShow(Model model) throws SQLException {
            List<Goods> goods = goodsService.findAll();
            model.addAttribute("goods", goods);
            return "update";
    }

    @RequestMapping(path = "/update", method = RequestMethod.POST)
    protected String update(@RequestParam(value = "id") String id,
                            @RequestParam(value = "name") String name,
                            @RequestParam(value = "price") String price,
                            @RequestParam(value = "date") String date,
                            Model model) throws SQLException {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate LD = LocalDate.parse(date, formatter);
        LocalDateTime dateTime = LocalDateTime.of(LD, LocalDateTime.now().toLocalTime());

        Goods good = Goods.builder().
                name(name)
                .price(Integer.parseInt(price))
                .date(dateTime)
                .build();
            goodsService.update(good, Integer.parseInt(id));
            List<Goods> goods = goodsService.findAll();
            model.addAttribute("goods", goods);
        return "redirect:/update";
    }
}
