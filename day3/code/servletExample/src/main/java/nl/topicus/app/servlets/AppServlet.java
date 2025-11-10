package nl.topicus.app.servlets;



import nl.topicus.app.Step1Page;
import nl.topicus.app.Step2Page;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Main application servlet
 *
 * This servlet routes requests to the appropriate Page class.
 * Somewhat similar to how Wicket's WicketServlet works!
 *
 * URL: /app
 */
@WebServlet("/app")
public class AppServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        // Check which step we're on
        String step = request.getParameter("step");

        if ("2".equals(step)) {
            // Show step 2 (details)
            Step2Page page = new Step2Page(request, response);
            page.render();
        } else {
            // Default: show step 1 (choices)
            Step1Page page = new Step1Page(request, response);
            page.render();
        }
    }
}
