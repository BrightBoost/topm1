package nl.topicus.training;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/redirected")
public class RedirectedServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html><body>");
        out.println("<h1>Je bent geredirect!</h1>");
        out.println("<p>Check de URL in je browser - deze is veranderd naar /redirected</p>");
        out.println("<p>Dit was een <strong>redirect</strong> (302 status code)</p>");
        out.println("<p><a href='/'>Terug naar home</a></p>");
        out.println("</body></html>");
    }
}
