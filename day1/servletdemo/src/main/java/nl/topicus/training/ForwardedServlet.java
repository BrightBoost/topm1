package nl.topicus.training;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/forwarded")
public class ForwardedServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String message = (String) request.getAttribute("message");

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html><body>");
        out.println("<h1>Je bent geforward!</h1>");
        out.println("<p>Check de URL, deze is niet veranderd! Nog steeds /forward-demo</p>");
        out.println("<p>Dit was een <strong>forward</strong> (server-side)</p>");
        out.println("<p>Message: " + message + "</p>");
        out.println("<p><a href='/'>Terug naar home</a></p>");
        out.println("</body></html>");
    }
}


