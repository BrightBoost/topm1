package nl.topicus.exercises.solution;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Success page for Exercise 1 Solution
 */
@WebServlet("/exercises-solution/validation-success")
public class Exercise01_ValidationSuccessServlet_Solution extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String html = "<!DOCTYPE html>" +
                     "<html>" +
                     "<head><title>Validation Success</title>" +
                     "<link rel='stylesheet' href='/static/css/styles.css'></head>" +
                     "<body><div class='container'>" +
                     "<h1>Exercise 1 Solution: Success!</h1>" +
                     "<div class='success-box'>" +
                     "<h3>Form Validated Successfully!</h3>" +
                     "<p>Your data has been accepted.</p>" +
                     "</div>" +
                     "<div class='info-box'>" +
                     "<h4>Progressive Enhancement Working:</h4>" +
                     "<ul>" +
                     "<li>With JavaScript: Form submitted via AJAX, redirected here</li>" +
                     "<li>Without JavaScript: PRG pattern brought you here</li>" +
                     "<li>Both paths work!</li>" +
                     "</ul>" +
                     "<p><strong>This is exactly how Wicket works!</strong></p>" +
                     "</div>" +
                     "<p><a href='/exercises-solution/validation-form' class='btn btn-secondary'>Try Again</a> " +
                     "<a href='/' class='btn btn-secondary'>Back</a></p>" +
                     "</div></body></html>";

        response.setContentType("text/html; charset=UTF-8");
        response.getWriter().write(html);
    }
}
