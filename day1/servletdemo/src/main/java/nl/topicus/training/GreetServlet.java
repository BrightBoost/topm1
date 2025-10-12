package nl.topicus.training;

// ============================================================================
// DEMO 2: GET with Query Parameters
// ============================================================================

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/greet")
public class GreetServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Parameters uit URL halen
        String name = request.getParameter("name");
        String ageStr = request.getParameter("age");

        // LET OP: Parameters kunnen null zijn!
        if (name == null) {
            name = "Guest";
        }

        int age = 0;
        if (ageStr != null) {
            try {
                age = Integer.parseInt(ageStr);
            } catch (NumberFormatException e) {
                // Ignore invalid age
            }
        }

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html><body>");
        out.println("<h1>Welkom, " + name + "!</h1>");

        if (age > 0) {
            out.println("<p>Je bent " + age + " jaar oud.</p>");
        }

        out.println("<hr>");
        out.println("<h2>Probeer andere parameters:</h2>");
        out.println("<ul>");
        out.println("  <li><a href='/greet?name=Piet&age=30'>Piet, 30</a></li>");
        out.println("  <li><a href='/greet?name=Anna&age=25'>Anna, 25</a></li>");
        out.println("  <li><a href='/greet?name=Test'>Zonder leeftijd</a></li>");
        out.println("</ul>");
        out.println("<p><a href='/'>Terug naar home</a></p>");
        out.println("</body></html>");

        // Debug
        System.out.println("GreetServlet: name=" + name + ", age=" + ageStr);
    }
}
