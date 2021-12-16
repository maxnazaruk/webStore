package com.study.lab1.servlets;

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

public class AllRequestsServlet extends HttpServlet {
    private List<String> userTokens;
    private UserVerificationService userVerificationService;

    public AllRequestsServlet(List<String> userTokens, UserVerificationService userVerificationService) {
        this.userTokens = userTokens;
        this.userVerificationService = userVerificationService;
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
            PageGenerator pageGenerator = PageGenerator.instance();
            HashMap<String, Object> parameters = new HashMap<>();
            try {
                String page = pageGenerator.getPage("templates/page.html", parameters);
                resp.getWriter().write(page);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        userTokens.clear();
        resp.sendRedirect("/login");
    }
}
