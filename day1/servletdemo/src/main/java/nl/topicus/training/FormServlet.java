package nl.topicus.training;


// ============================================================================
// DEMO 3: Form with GET and POST
// ============================================================================

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/form")
public class FormServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html><body>");
        out.println("<h1>Formulier Demo</h1>");

        // GET Form - data komt in URL
        out.println("<h2>1. GET Form (data in URL)</h2>");
        out.println("<form method='GET' action='/form'>");
        out.println("  <label>Naam: <input type='text' name='name'></label><br><br>");
        out.println("  <label>Email: <input type='email' name='email'></label><br><br>");
        out.println("  <button type='submit'>Verzenden (GET)</button>");
        out.println("</form>");

        out.println("<hr>");

        // POST Form - data komt in request body
        out.println("<h2>2. POST Form (data in body)</h2>");
        out.println("<form method='POST' action='/form'>");
        out.println("  <label>Naam: <input type='text' name='name'></label><br><br>");
        out.println("  <label>Email: <input type='email' name='email'></label><br><br>");
        out.println("  <label>Bericht:<br>");
        out.println("    <textarea name='message' rows='4' cols='40'></textarea>");
        out.println("  </label><br><br>");
        out.println("  <button type='submit'>Verzenden (POST)</button>");
        out.println("</form>");

        out.println("<hr>");
        out.println("<p><strong>Let op het verschil in de URL na submit!</strong></p>");
        out.println("<p><a href='/'>Terug naar home</a></p>");
        out.println("</body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // POST data uitlezen
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String message = request.getParameter("message");

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html><body>");
        out.println("<h1>Formulier Ontvangen (POST)</h1>");
        out.println("<div style='background: #e8f5e9; padding: 20px; margin: 20px 0;'>");
        out.println("  <p><strong>Naam:</strong> " + (name != null ? name : "Niet ingevuld") + "</p>");
        out.println("  <p><strong>Email:</strong> " + (email != null ? email : "Niet ingevuld") + "</p>");
        out.println("  <p><strong>Bericht:</strong> " + (message != null ? message : "Niet ingevuld") + "</p>");
        out.println("</div>");
        out.println("<p><strong>Let op:</strong> De data staat NIET in de URL! (Check de browser URL)</p>");
        out.println("<p><a href='/form'>Terug naar formulier</a> | <a href='/'>Home</a></p>");
        out.println("</body></html>");

        // Debug
        System.out.println("FormServlet POST: " + name + ", " + email);
    }
}
