package com.study.lab1.templater;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;


public class PageGenerator {
    private static final String HTML_DIR = "templates/lab1";

    private static PageGenerator pageGenerator;
    private final Configuration cfg;

    public static PageGenerator instance() {
        if (pageGenerator == null)
            pageGenerator = new PageGenerator();
        return pageGenerator;
    }

    public String getPage(String filename, Map<String, Object> data) {
        Writer stream = new StringWriter();
        try {
            Template template = cfg.getTemplate(HTML_DIR + File.separator + filename);
            template.process(data, stream);
        } catch (IOException | TemplateException e) {
            throw new RuntimeException(e);
        }
        return stream.toString();
    }

    public String getPage(String filename, ResultSet resultSet) throws IOException, SQLException {
        Writer stream = new StringWriter();
        FileInputStream fileInputStream = new FileInputStream(HTML_DIR + File.separator + filename);
        String goods = "";
        byte[] buf = new byte[8192];

        int length;
        while ((length = fileInputStream.read(buf)) > 0) {
            goods = new String(buf, StandardCharsets.UTF_8);
        }

        StringBuilder row = new StringBuilder("");

        while (resultSet.next()) {
            row.append("<tr>");

            row.append("<td>");
            row.append(resultSet.getInt("id"));
            row.append("</td>");

            row.append("<td>");
            row.append(resultSet.getString("name"));
            row.append("</td>");

            row.append("<td>");
            row.append(resultSet.getInt("price"));
            row.append("</td>");

            row.append("<td>");
            row.append(resultSet.getDate("creationdate"));
            row.append("</td>");

            row.append("<tr>");
        }

        return goods.replaceAll(".*&goods.*", row.toString());
    }

    private PageGenerator() {
        cfg = new Configuration();
    }
}
