package nl.topicus.exercises.starter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Success page for Exercise 1 - validation form
 */
@WebServlet("/exercises/validation-success")
public class Exercise01_ValidationSuccessServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String html = "<!DOCTYPE html>" +
                     "<html>" +
                     "<head><title>Validation Success</title>" +
                     "<link rel='stylesheet' href='/static/css/styles.css'></head>" +
                     "<body><div class='container'>" +
                     "<h1>Exercise 1: Success!</h1>" +
                     "<div class='success-box'>" +
                     "<h3>Form Validated Successfully!</h3>" +
                     "<p>Your data has been accepted.</p>" +
                     "</div>" +
                     "<div class='info-box'>" +
                     "<h4>What Happened:</h4>" +
                     "<ul>" +
                     "<li>Form submitted (POST)</li>" +
                     "<li>Server validated all fields</li>" +
                     "<li>Validation passed</li>" +
                     "<li>Server redirected here (PRG pattern)</li>" +
                     "</ul>" +
                     "<p><strong>Try hitting F5:</strong> Safe to refresh!</p>" +
                     "</div>" +
                     "<p><a href='/exercises/validation-form' class='btn btn-secondary'>Try Again</a> " +
                     "<a href='/' class='btn btn-secondary'>Back</a></p>" +
                     "</div></body></html>";

        response.setContentType("text/html; charset=UTF-8");
        response.getWriter().write(html);
    }
}
