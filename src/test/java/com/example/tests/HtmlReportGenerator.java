package com.example.tests;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class HtmlReportGenerator {
    public static void generateReport(List<TestResult> results, String path) {
        try (FileWriter writer = new FileWriter(path)) {
            writer.write("<html><body>");
            writer.write("<h2>Test Report</h2>");
            writer.write("<table border='1'>");
            writer.write("<tr>");
            writer.write("<th>Test</th>");
            writer.write("<th>Status</th>");
            writer.write("<th>Time (ms)</th>");
            writer.write("<th>Error</th>");
            writer.write("<th>Screenshot</th>");
            writer.write("</tr>");

            for (TestResult result : results) {
                writer.write("<tr>");
                writer.write("<td>" + result.name + "</td>");
                writer.write("<td style='color:"
                        + (result.status.equals("Passed") ? "green" : "red")
                        + "'>"
                        + result.status + "</td>");
                writer.write("<td>" + result.duration + "</td>");
                writer.write("<td>"
                        + (result.error == null ? "-" : result.error)
                        + "</td>");

                if (result.screenshot == null) {
                    writer.write("<td>-</td>");
                } else {
                    writer.write("<td><a href='" + result.screenshot + "'>Open</a></td>");
                }

                writer.write("</tr>");
            }
            writer.write("</table>");
            writer.write("</body></html>");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}