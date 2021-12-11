package com.study.lab1.main;

import com.study.lab1.jdbc.JDBConnection;
import com.study.lab1.jdbc.JdbcUserDao;
import com.study.lab1.service.GoodsService;
import com.study.lab1.service.UserVerificationService;
import com.study.lab1.servlets.*;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        JDBConnection jdbConnection = new JDBConnection();
        JdbcUserDao jdbcUserDao = new JdbcUserDao();

        GoodsService goodsService = new GoodsService(jdbConnection);
        UserVerificationService userVerificationService = new UserVerificationService(jdbcUserDao);

        List<String> userTokens = new ArrayList<>();

        AllRequestsServlet allRequestsServlet = new AllRequestsServlet(userTokens, userVerificationService);

        AddGoodServlet addGoodServlet = new AddGoodServlet(goodsService, userTokens, userVerificationService);
        RemoveGoodServlet removeGoodServlet = new RemoveGoodServlet(goodsService, userTokens, userVerificationService);
        UpdateGoodServlet updateGoodServlet = new UpdateGoodServlet(goodsService, userTokens, userVerificationService);
        GoodsServlet goodsServlet = new GoodsServlet(goodsService, userTokens, userVerificationService);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(allRequestsServlet), "/");
        context.addServlet(new ServletHolder(addGoodServlet), "/add");
        context.addServlet(new ServletHolder(removeGoodServlet), "/remove");
        context.addServlet(new ServletHolder(updateGoodServlet), "/update");
        context.addServlet(new ServletHolder(goodsServlet), "/goods");
        context.addServlet(new ServletHolder(new LoginServlet(userTokens, userVerificationService)), "/login");

        Server server = new Server(8080);
        server.setHandler(context);

        server.start();
    }
}
