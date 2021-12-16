package com.study.lab1.main;

import com.study.lab1.jdbc.JDBConnection;
import com.study.lab1.jdbc.JdbcUserDao;
import com.study.lab1.service.GoodsService;
import com.study.lab1.service.UserVerificationService;
import com.study.lab1.servlets.*;
import com.study.lab1.servlets.filter.SecurityFilter;
import jakarta.servlet.DispatcherType;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

//import jakarta.servlet.*;

import javax.servlet.*;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        JDBConnection jdbConnection = new JDBConnection();
        JdbcUserDao jdbcUserDao = new JdbcUserDao();

        GoodsService goodsService = new GoodsService(jdbConnection);
        UserVerificationService userVerificationService = new UserVerificationService(jdbcUserDao);

        List<String> userTokens = new ArrayList<>();

        AllRequestsServlet allRequestsServlet = new AllRequestsServlet(userTokens, userVerificationService);

        AddGoodServlet addGoodServlet = new AddGoodServlet(goodsService);
        RemoveGoodServlet removeGoodServlet = new RemoveGoodServlet(goodsService);
        UpdateGoodServlet updateGoodServlet = new UpdateGoodServlet(goodsService, userTokens, userVerificationService);
        GoodsServlet goodsServlet = new GoodsServlet(goodsService, userTokens, userVerificationService);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder( allRequestsServlet), "/");
        context.addServlet(new ServletHolder( addGoodServlet), "/add");
        context.addServlet(new ServletHolder(removeGoodServlet), "/remove");
        context.addServlet(new ServletHolder(updateGoodServlet), "/update");
        context.addServlet(new ServletHolder(goodsServlet), "/goods");
        context.addServlet(new ServletHolder(new LoginServlet(userTokens, userVerificationService)), "/login");
        context.addFilter(new FilterHolder(new SecurityFilter(userTokens, userVerificationService)), "/*", (EnumSet.of(DispatcherType.REQUEST)));

        Server server = new Server(8080);
        server.setHandler(context);

        server.start();
    }
}
