package nl.topicus.training;


// ============================================================================
// DEMO 4: Redirect vs Forward
// ============================================================================

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/redirect-demo")
public class RedirectDemoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("RedirectDemoServlet: Redirecting to /redirected");

        // REDIRECT: Browser krijgt 302 status en nieuwe URL
        // Browser maakt nieuwe request naar /redirected
        response.sendRedirect("/redirected");
    }
}
