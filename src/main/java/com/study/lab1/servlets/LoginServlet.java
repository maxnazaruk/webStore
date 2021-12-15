package com.study.lab1.servlets;

import com.study.lab1.entity.User;
import com.study.lab1.service.UserVerificationService;
import com.study.lab1.templater.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

public class LoginServlet extends HttpServlet {
    private List<String> userTokens;
    private UserVerificationService userVerificationService;

    public LoginServlet(List<String> userTokens, UserVerificationService userVerificationService) {
        this.userTokens = userTokens;
        this.userVerificationService = userVerificationService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PageGenerator pageGenerator = PageGenerator.instance();
        String page = null;
        try {
            page = pageGenerator.getPage("templates/login.html", Collections.EMPTY_MAP);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        resp.getWriter().write(page);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PageGenerator pageGenerator = PageGenerator.instance();

        String email = req.getParameter("email");
        String password = req.getParameter("password");

        User user = User.builder().email(email).pass(password).sole("123").build();

        try {
            if (userVerificationService.signInUser(user)) {
                String userToken = UUID.randomUUID().toString();
                userTokens.add(userToken);
                Cookie cookie = new Cookie("user-token", userToken);
                resp.addCookie(cookie);
                resp.sendRedirect("/");
            }else{
                user.setSole("123");
                user.setPass(userVerificationService.passwordConverter(user, password));
                userVerificationService.addNewUser(user);
                String userToken = UUID.randomUUID().toString();
                userTokens.add(userToken);
                Cookie cookie = new Cookie("user-token", userToken);
                resp.addCookie(cookie);
                resp.sendRedirect("/");
            }
        } catch(IllegalArgumentException ex){
            Map<String, Object> parametrs = new HashMap<>();
            String incorrectPass = " Password is incorrect";
            parametrs.put("error", incorrectPass);
            try {
                String page = pageGenerator.getPage("templates/login.html", parametrs);
                resp.getWriter().write(page);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
}
