package nl.topicus.training;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@WebServlet("/buggy")
public class NoBuggyServlet extends HttpServlet {

    // Bug 3 fix: GEEN instance variabele meer!
    // private String lastUser; <- VERWIJDERD

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws IOException {

        // Bug 1 fix: null check
        String name = request.getParameter("name");
        if (name == null || name.isEmpty()) {
            name = "Guest";
        }

        // Bug 3 fix: gebruik session voor user-specific data
        HttpSession session = request.getSession();
        String previousUser = (String) session.getAttribute("lastUser");
        session.setAttribute("lastUser", name);

        // Bug 2 fix: content type instellen
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<html><body>");
        out.println("<h1>Hello " + name.toUpperCase() + "</h1>");
        out.println("<p>Previous user: " + (previousUser != null ? previousUser : "Nobody") + "</p>");
        out.println("</body></html>");
    }

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws IOException {

        // Bug 1 + Bug 4 fix: null check en URL encoding
        String name = request.getParameter("name");
        if (name == null) {
            name = "Guest";
        }
        String encodedName = URLEncoder.encode(name, StandardCharsets.UTF_8);

        response.sendRedirect("/success?name=" + encodedName);

        // Bug 5 fix: ALLE code na redirect verwijderd!
    }
}
