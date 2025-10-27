package nl.topicus;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * Demo: Cookie basics
 *
 * This servlet demonstrates:
 * 1. Reading cookies from a request
 * 2. Setting cookies in a response
 * 3. Cookie attributes (MaxAge, Path, HttpOnly)
 * 4. Personalizing user experience with cookies
 *
 * Try this:
 * - First visit: "Welcome stranger!"
 * - Enter name: "Welcome back, [name]!"
 * - Close browser and return: still remembers you!
 * - Check DevTools >> Application >> Cookies to see the cookie
 */
@WebServlet("/cookie-demo")
public class CookieServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        // STEP 1: Try to read existing cookies
        String visitorName = null;
        Integer visitCount = null;

        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("visitor_name".equals(cookie.getName())) {
                    visitorName = cookie.getValue();
                }
                if ("visit_count".equals(cookie.getName())) {
                    try {
                        visitCount = Integer.parseInt(cookie.getValue());
                    } catch (NumberFormatException e) {
                        visitCount = 0;
                    }
                }
            }
        }

        // STEP 2: Increment visit count
        if (visitCount == null) {
            visitCount = 1; // First visit
        } else {
            visitCount++; // Returning visitor
        }

        // STEP 3: Update visit count cookie
        Cookie visitCookie = new Cookie("visit_count", String.valueOf(visitCount));
        visitCookie.setMaxAge(365 * 24 * 60 * 60); // 1 year
        visitCookie.setPath("/");
        visitCookie.setHttpOnly(true); // Security: prevent JavaScript access
        response.addCookie(visitCookie);

        // STEP 4: Generate HTML response
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("    <title>Cookie Demo</title>");
        out.println("    <style>");
        out.println("        body {");
        out.println("            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;");
        out.println("            max-width: 700px;");
        out.println("            margin: 50px auto;");
        out.println("            padding: 30px;");
        out.println("            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);");
        out.println("            border-radius: 15px;");
        out.println("            box-shadow: 0 10px 30px rgba(0,0,0,0.3);");
        out.println("            color: white;");
        out.println("        }");
        out.println("        .greeting {");
        out.println("            background-color: rgba(255, 255, 255, 0.2);");
        out.println("            padding: 20px;");
        out.println("            border-radius: 10px;");
        out.println("            margin: 20px 0;");
        out.println("            backdrop-filter: blur(10px);");
        out.println("        }");
        out.println("        .greeting h2 {");
        out.println("            margin: 0;");
        out.println("            font-size: 28px;");
        out.println("        }");
        out.println("        .stats {");
        out.println("            background-color: rgba(255, 255, 255, 0.1);");
        out.println("            padding: 15px;");
        out.println("            border-radius: 8px;");
        out.println("            margin: 20px 0;");
        out.println("        }");
        out.println("        .stats p {");
        out.println("            margin: 8px 0;");
        out.println("        }");
        out.println("        form {");
        out.println("            background-color: rgba(255, 255, 255, 0.2);");
        out.println("            padding: 20px;");
        out.println("            border-radius: 10px;");
        out.println("            margin: 20px 0;");
        out.println("        }");
        out.println("        input[type='text'] {");
        out.println("            padding: 12px;");
        out.println("            width: 300px;");
        out.println("            font-size: 16px;");
        out.println("            border: none;");
        out.println("            border-radius: 5px;");
        out.println("            margin-right: 10px;");
        out.println("        }");
        out.println("        button {");
        out.println("            padding: 12px 25px;");
        out.println("            font-size: 16px;");
        out.println("            background-color: #4CAF50;");
        out.println("            color: white;");
        out.println("            border: none;");
        out.println("            border-radius: 5px;");
        out.println("            cursor: pointer;");
        out.println("            transition: background-color 0.3s;");
        out.println("        }");
        out.println("        button:hover {");
        out.println("            background-color: #45a049;");
        out.println("        }");
        out.println("        .reset-btn {");
        out.println("            background-color: #f44336;");
        out.println("            margin-top: 10px;");
        out.println("        }");
        out.println("        .reset-btn:hover {");
        out.println("            background-color: #da190b;");
        out.println("        }");
        out.println("        .code-box {");
        out.println("            background-color: rgba(0, 0, 0, 0.3);");
        out.println("            padding: 15px;");
        out.println("            border-radius: 8px;");
        out.println("            font-family: 'Courier New', monospace;");
        out.println("            margin: 20px 0;");
        out.println("            font-size: 14px;");
        out.println("        }");
        out.println("        .emoji {");
        out.println("            font-size: 32px;");
        out.println("        }");
        out.println("    </style>");
        out.println("</head>");
        out.println("<body>");

        out.println("    <h1>Cookie: Personal greeting</h1>");

        // STEP 5: Display personalized greeting
        out.println("    <div class='greeting'>");
        if (visitorName != null) {
            out.println("        <span class='emoji'>WELCOME!</span>");
            out.println("        <h2>Welcome back " + escapeHtml(visitorName) + "!</h2>");
            out.println("        <p>Great to see you again!</p>");
        } else {
            out.println("        <span class='emoji'>WHO'S THAT?!</span>");
            out.println("        <h2>Welcome stranger!</h2>");
            out.println("        <p>I don't know you yet. Want to introduce yourself?</p>");
        }
        out.println("    </div>");

        // STEP 6: Display visit statistics
        out.println("    <div class='stats'>");
        out.println("        <h3>Your visit stats:</h3>");
        out.println("        <p><strong>Total visits:</strong> " + visitCount + "</p>");

        if (visitCount == 1) {
            out.println("        <p>This is your first visit!</p>");
        } else if (visitCount < 5) {
            out.println("        <p>You're getting familiar with this page!</p>");
        } else if (visitCount < 10) {
            out.println("        <p>You're a regular visitor!</p>");
        } else {
            out.println("        <p>Wow! You're a super fan! (" + visitCount + " visits)</p>");
        }
        out.println("    </div>");

        // STEP 7: Form to set/update name
        if (visitorName == null) {
            out.println("    <form method='POST'>");
            out.println("        <h3>Introduce Yourself:</h3>");
            out.println("        <input type='text' name='name' placeholder='Enter your name' required />");
            out.println("        <button type='submit'>Remember me</button>");
            out.println("    </form>");
        } else {
            out.println("    <form method='POST'>");
            out.println("        <h3>Change Your Name:</h3>");
            out.println("        <input type='text' name='name' placeholder='Enter new name' value='" + escapeHtml(visitorName) + "' required />");
            out.println("        <button type='submit'>Update Name</button>");
            out.println("    </form>");
        }

        // STEP 8: Reset button
        out.println("    <form method='POST'>");
        out.println("        <input type='hidden' name='action' value='reset' />");
        out.println("        <button type='submit' class='reset-btn'>Forget me (clear cookies)</button>");
        out.println("    </form>");

        // STEP 9: Show cookie information
        out.println("    <div class='code-box'>");
        out.println("        <h3>🔍 Cookie Information:</h3>");
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                out.println("        <p><strong>" + cookie.getName() + ":</strong> " + cookie.getValue() + "</p>");
            }
        } else {
            out.println("        <p>No cookies found!</p>");
        }
        out.println("        <p style='margin-top: 15px; font-size: 12px; opacity: 0.8;'>");
        out.println("        <strong>Tip:</strong> Open DevTools (F12) > Application > Cookies > http://localhost:8080");
        out.println("        </p>");
        out.println("    </div>");

        // STEP 10: Explanation
        out.println("    <div style='background-color: rgba(255,255,255,0.1); padding: 15px; border-radius: 8px; margin-top: 20px;'>");
        out.println("        <h3>🎓 What's Happening Here?</h3>");
        out.println("        <ol style='line-height: 1.8;'>");
        out.println("            <li>Reading cookies: Server checks if you've visited before</li>");
        out.println("            <li>Visit counter: Increments on every page load</li>");
        out.println("            <li>Personal greeting: Shows your name if cookie exists</li>");
        out.println("            <li>Cookie persistence: Data survives browser restart (1 year expiry)</li>");
        out.println("            <li>HttpOnly flag: JavaScript can't access these cookies (security!)</li>");
        out.println("        </ol>");
        out.println("        <p style='margin-top: 15px;'>");
        out.println("        <strong>Try this:</strong> Close your browser completely, reopen, and visit this page again. Your name and visit count will still be there!");
        out.println("        </p>");
        out.println("    </div>");

        out.println("</body>");
        out.println("</html>");
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("reset".equals(action)) {
            // RESET: Delete all cookies
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    Cookie deleteCookie = new Cookie(cookie.getName(), "");
                    deleteCookie.setMaxAge(0); // Delete immediately
                    deleteCookie.setPath("/");
                    response.addCookie(deleteCookie);
                }
            }
        } else {
            // SET NAME: Create or update name cookie
            String name = request.getParameter("name");

            if (name != null && !name.trim().isEmpty()) {
                // Sanitize input (prevent XSS)
                name = name.trim().replaceAll("[^a-zA-Z0-9 ]", "");

                if (!name.isEmpty()) {
                    Cookie nameCookie = new Cookie("visitor_name", name);
                    nameCookie.setMaxAge(365 * 24 * 60 * 60); // 1 year
                    nameCookie.setPath("/");
                    nameCookie.setHttpOnly(true); // Security!
                    response.addCookie(nameCookie);
                }
            }
        }

        // Redirect to GET (POST-Redirect-GET pattern)
        response.sendRedirect("/cookie-demo");
    }

    /**
     * Escape HTML to prevent XSS attacks.
     * Always escape user input when displaying it!
     */
    private String escapeHtml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#x27;");
    }
}
