package nl.topicus.training;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

// ============================================================================
// DEMO 1: Simple Hello World Servlet
// ============================================================================

@WebServlet("/hello")
public class HelloServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // BELANGRIJK: Altijd content type instellen!
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html>");
        out.println("<html><body>");
        out.println("<h1>Hello World!</h1>");
        out.println("<p>Dit is de meest simpele servlet.</p>");
        out.println("<p><a href='/'>Terug naar home</a></p>");
        out.println("</body></html>");

        // Debug output naar console
        System.out.println("HelloServlet: doGet() called");
    }
}
