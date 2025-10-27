package nl.topicus;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Session Demo: User Profile Manager
 *
 * Demonstrates:
 * 1. Creating and retrieving sessions
 * 2. Storing multiple attributes in a session
 * 3. Modifying session data
 * 4. Session lifecycle (creation, use, invalidation)
 * 5. Difference between session data and cookies
 *
 * Use case: User can set their profile info (name, favorite language, color)
 * and it persists across pages and browser sessions (until they logout)
 */
@WebServlet("/session-demo")
public class SessionDemoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        // STEP 1: Get or create session
        // true = create new session if none exists
        HttpSession session = request.getSession(true);

        // STEP 2: Try to GET user profile data from session
        String username = (String) session.getAttribute("username");
        String favLanguage = (String) session.getAttribute("favLanguage");
        String favColor = (String) session.getAttribute("favColor");
        Integer visitCount = (Integer) session.getAttribute("visitCount");

        // STEP 3: Initialize defaults if this is first visit
        if (visitCount == null) {
            visitCount = 0;
        }
        visitCount++;  // Increment visit counter

        // STEP 4: Save updated visit count back to session
        session.setAttribute("visitCount", visitCount);

        // Format session timestamps
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        String createdAt = dateFormat.format(new Date(session.getCreationTime()));
        String lastAccessedAt = dateFormat.format(new Date(session.getLastAccessedTime()));
        long sessionAge = (System.currentTimeMillis() - session.getCreationTime()) / 1000;

        // STEP 5: Generate HTML response
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("    <title>Session Demo - User Profile</title>");
        out.println("    <style>");
        out.println("        * { margin: 0; padding: 0; box-sizing: border-box; }");
        out.println("        body {");
        out.println("            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;");
        out.println("            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);");
        out.println("            min-height: 100vh;");
        out.println("            padding: 40px 20px;");
        out.println("        }");
        out.println("        .container {");
        out.println("            max-width: 900px;");
        out.println("            margin: 0 auto;");
        out.println("        }");
        out.println("        .card {");
        out.println("            background: white;");
        out.println("            border-radius: 15px;");
        out.println("            padding: 30px;");
        out.println("            margin-bottom: 20px;");
        out.println("            box-shadow: 0 10px 30px rgba(0,0,0,0.2);");
        out.println("        }");
        out.println("        .header {");
        out.println("            text-align: center;");
        out.println("            color: white;");
        out.println("            margin-bottom: 30px;");
        out.println("        }");
        out.println("        .header h1 {");
        out.println("            font-size: 42px;");
        out.println("            margin-bottom: 10px;");
        out.println("            text-shadow: 2px 2px 4px rgba(0,0,0,0.3);");
        out.println("        }");
        out.println("        .profile-status {");
        out.println("            background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);");
        out.println("            color: white;");
        out.println("            padding: 20px;");
        out.println("            border-radius: 10px;");
        out.println("            margin-bottom: 20px;");
        out.println("        }");
        out.println("        .profile-status h2 {");
        out.println("            margin-bottom: 15px;");
        out.println("        }");
        out.println("        .profile-info {");
        out.println("            display: grid;");
        out.println("            grid-template-columns: 1fr 1fr;");
        out.println("            gap: 15px;");
        out.println("        }");
        out.println("        .info-item {");
        out.println("            background: rgba(255,255,255,0.2);");
        out.println("            padding: 10px;");
        out.println("            border-radius: 5px;");
        out.println("        }");
        out.println("        .info-label {");
        out.println("            font-size: 12px;");
        out.println("            opacity: 0.8;");
        out.println("        }");
        out.println("        .info-value {");
        out.println("            font-size: 18px;");
        out.println("            font-weight: bold;");
        out.println("            margin-top: 5px;");
        out.println("        }");
        out.println("        .form-section {");
        out.println("            margin: 20px 0;");
        out.println("        }");
        out.println("        .form-section h3 {");
        out.println("            color: #333;");
        out.println("            margin-bottom: 15px;");
        out.println("        }");
        out.println("        .form-group {");
        out.println("            margin-bottom: 15px;");
        out.println("        }");
        out.println("        label {");
        out.println("            display: block;");
        out.println("            margin-bottom: 5px;");
        out.println("            color: #555;");
        out.println("            font-weight: 500;");
        out.println("        }");
        out.println("        input[type='text'], select {");
        out.println("            width: 100%;");
        out.println("            padding: 12px;");
        out.println("            border: 2px solid #ddd;");
        out.println("            border-radius: 8px;");
        out.println("            font-size: 16px;");
        out.println("            transition: border-color 0.3s;");
        out.println("        }");
        out.println("        input[type='text']:focus, select:focus {");
        out.println("            outline: none;");
        out.println("            border-color: #667eea;");
        out.println("        }");
        out.println("        button {");
        out.println("            padding: 12px 30px;");
        out.println("            font-size: 16px;");
        out.println("            border: none;");
        out.println("            border-radius: 8px;");
        out.println("            cursor: pointer;");
        out.println("            transition: all 0.3s;");
        out.println("            font-weight: bold;");
        out.println("        }");
        out.println("        .save-btn {");
        out.println("            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);");
        out.println("            color: white;");
        out.println("            margin-right: 10px;");
        out.println("        }");
        out.println("        .save-btn:hover {");
        out.println("            transform: translateY(-2px);");
        out.println("            box-shadow: 0 5px 15px rgba(102, 126, 234, 0.4);");
        out.println("        }");
        out.println("        .logout-btn {");
        out.println("            background-color: #f44336;");
        out.println("            color: white;");
        out.println("        }");
        out.println("        .logout-btn:hover {");
        out.println("            background-color: #da190b;");
        out.println("        }");
        out.println("        .session-info {");
        out.println("            background-color: #f8f9fa;");
        out.println("            padding: 20px;");
        out.println("            border-radius: 10px;");
        out.println("            border-left: 4px solid #667eea;");
        out.println("        }");
        out.println("        .session-info h3 {");
        out.println("            color: #333;");
        out.println("            margin-bottom: 15px;");
        out.println("        }");
        out.println("        .session-detail {");
        out.println("            display: flex;");
        out.println("            justify-content: space-between;");
        out.println("            padding: 8px 0;");
        out.println("            border-bottom: 1px solid #dee2e6;");
        out.println("        }");
        out.println("        .session-detail:last-child {");
        out.println("            border-bottom: none;");
        out.println("        }");
        out.println("        .session-detail strong {");
        out.println("            color: #555;");
        out.println("        }");
        out.println("        .session-detail code {");
        out.println("            background-color: #e9ecef;");
        out.println("            padding: 2px 8px;");
        out.println("            border-radius: 4px;");
        out.println("            font-family: 'Courier New', monospace;");
        out.println("            font-size: 14px;");
        out.println("        }");
        out.println("        .explanation {");
        out.println("            background-color: #fff3cd;");
        out.println("            border-left: 4px solid #ffc107;");
        out.println("            padding: 20px;");
        out.println("            border-radius: 10px;");
        out.println("            margin-top: 20px;");
        out.println("        }");
        out.println("        .explanation h3 {");
        out.println("            color: #856404;");
        out.println("            margin-bottom: 10px;");
        out.println("        }");
        out.println("        .explanation ul {");
        out.println("            margin-left: 20px;");
        out.println("            color: #856404;");
        out.println("        }");
        out.println("        .explanation li {");
        out.println("            margin: 8px 0;");
        out.println("        }");
        out.println("        .welcome-message {");
        out.println("            font-size: 24px;");
        out.println("            margin-bottom: 10px;");
        out.println("        }");
        out.println("        .no-profile {");
        out.println("            text-align: center;");
        out.println("            padding: 40px;");
        out.println("            color: rgba(255,255,255,0.9);");
        out.println("        }");
        out.println("    </style>");
        out.println("</head>");
        out.println("<body>");

        out.println("    <div class='container'>");

        // Header
        out.println("        <div class='header'>");
        out.println("            <h1>🔐 Session Demo: User Profile</h1>");
        out.println("            <p>See how sessions store data on the server!</p>");
        out.println("        </div>");

        // Profile Status Card
        out.println("        <div class='card'>");
        out.println("            <div class='profile-status'>");

        if (username != null) {
            // User has profile
            out.println("                <div class='welcome-message'>👋 Welcome back, <strong>" + escapeHtml(username) + "</strong>!</div>");
            out.println("                <p>This is visit #" + visitCount + "</p>");
            out.println("                <div class='profile-info' style='margin-top: 20px;'>");

            out.println("                    <div class='info-item'>");
            out.println("                        <div class='info-label'>Favorite Language</div>");
            out.println("                        <div class='info-value'>" +
                    (favLanguage != null ? "💻 " + escapeHtml(favLanguage) : "Not set") + "</div>");
            out.println("                    </div>");

            out.println("                    <div class='info-item'>");
            out.println("                        <div class='info-label'>Favorite Color</div>");
            out.println("                        <div class='info-value' style='color: " +
                    (favColor != null ? escapeHtml(favColor) : "#000") + ";'>🎨 " +
                    (favColor != null ? escapeHtml(favColor) : "Not set") + "</div>");
            out.println("                    </div>");

            out.println("                </div>");
        } else {
            // No profile yet
            out.println("                <div class='no-profile'>");
            out.println("                    <div style='font-size: 48px; margin-bottom: 10px;'>👤</div>");
            out.println("                    <h2>No Profile Yet</h2>");
            out.println("                    <p>Fill in the form below to create your profile!</p>");
            out.println("                    <p style='margin-top: 10px; font-size: 14px; opacity: 0.9;'>This is visit #" + visitCount + "</p>");
            out.println("                </div>");
        }

        out.println("            </div>");
        out.println("        </div>");

        // Profile Form Card
        out.println("        <div class='card'>");
        out.println("            <div class='form-section'>");
        out.println("                <h3>" + (username != null ? "Update Your Profile" : "Create Your Profile") + "</h3>");
        out.println("                <form method='POST'>");

        out.println("                    <div class='form-group'>");
        out.println("                        <label for='username'>Your Name:</label>");
        out.println("                        <input type='text' id='username' name='username' " +
                "value='" + (username != null ? escapeHtml(username) : "") + "' " +
                "placeholder='Enter your name' required />");
        out.println("                    </div>");

        out.println("                    <div class='form-group'>");
        out.println("                        <label for='favLanguage'>Favorite Programming Language:</label>");
        out.println("                        <select id='favLanguage' name='favLanguage'>");
        out.println("                            <option value=''>-- Select --</option>");

        String[] languages = {"Java", "Python", "JavaScript", "C#", "Go", "Rust", "TypeScript", "PHP"};
        for (String lang : languages) {
            String selected = lang.equals(favLanguage) ? "selected" : "";
            out.println("                            <option value='" + lang + "' " + selected + ">" + lang + "</option>");
        }

        out.println("                        </select>");
        out.println("                    </div>");

        out.println("                    <div class='form-group'>");
        out.println("                        <label for='favColor'>Favorite Color:</label>");
        out.println("                        <select id='favColor' name='favColor'>");
        out.println("                            <option value=''>-- Select --</option>");

        String[] colors = {"Blue", "Red", "Green", "Purple", "Orange", "Pink", "Yellow", "Black"};
        for (String color : colors) {
            String selected = color.equals(favColor) ? "selected" : "";
            out.println("                            <option value='" + color + "' " + selected + ">" + color + "</option>");
        }

        out.println("                        </select>");
        out.println("                    </div>");

        out.println("                    <div style='margin-top: 20px;'>");
        out.println("                        <button type='submit' name='action' value='save' class='save-btn'>💾 Save Profile</button>");

        if (username != null) {
            out.println("                        <button type='submit' name='action' value='logout' class='logout-btn'>🚪 Logout (Clear Session)</button>");
        }

        out.println("                    </div>");
        out.println("                </form>");
        out.println("            </div>");
        out.println("        </div>");

        // Session Info Card
        out.println("        <div class='card'>");
        out.println("            <div class='session-info'>");
        out.println("                <h3>🔍 Session Technical Details</h3>");

        out.println("                <div class='session-detail'>");
        out.println("                    <strong>Session ID:</strong>");
        out.println("                    <code>" + session.getId() + "</code>");
        out.println("                </div>");

        out.println("                <div class='session-detail'>");
        out.println("                    <strong>Created at:</strong>");
        out.println("                    <span>" + createdAt + "</span>");
        out.println("                </div>");

        out.println("                <div class='session-detail'>");
        out.println("                    <strong>Last accessed:</strong>");
        out.println("                    <span>" + lastAccessedAt + "</span>");
        out.println("                </div>");

        out.println("                <div class='session-detail'>");
        out.println("                    <strong>Session age:</strong>");
        out.println("                    <span>" + sessionAge + " seconds</span>");
        out.println("                </div>");

        out.println("                <div class='session-detail'>");
        out.println("                    <strong>Max inactive interval:</strong>");
        out.println("                    <span>" + session.getMaxInactiveInterval() + " seconds (" +
                (session.getMaxInactiveInterval() / 60) + " minutes)</span>");
        out.println("                </div>");

        out.println("                <div class='session-detail'>");
        out.println("                    <strong>Is new session:</strong>");
        out.println("                    <span>" + (session.isNew() ? "✓ Yes (first request)" : "✗ No (returning)") + "</span>");
        out.println("                </div>");

        out.println("                <div class='session-detail'>");
        out.println("                    <strong>Data stored in session:</strong>");
        out.println("                    <span>");
        if (username != null) {
            out.println("username, favLanguage, favColor, visitCount");
        } else {
            out.println("visitCount only");
        }
        out.println("                    </span>");
        out.println("                </div>");

        out.println("            </div>");
        out.println("        </div>");

        // Explanation Card
        out.println("        <div class='card'>");
        out.println("            <div class='explanation'>");
        out.println("                <h3>💡 How This Works</h3>");
        out.println("                <ul>");
        out.println("                    <li><strong>Session created:</strong> When you first visit, server creates a session with unique ID</li>");
        out.println("                    <li><strong>JSESSIONID cookie:</strong> Browser receives cookie with session ID (check DevTools!)</li>");
        out.println("                    <li><strong>Data stored on SERVER:</strong> Your profile is stored in server memory, NOT in the browser</li>");
        out.println("                    <li><strong>Each request:</strong> Browser sends JSESSIONID → Server looks up your session → Retrieves your data</li>");
        out.println("                    <li><strong>New tabs:</strong> Share the same session (same JSESSIONID cookie)</li>");
        out.println("                    <li><strong>Incognito:</strong> Different session (different cookie jar)</li>");
        out.println("                    <li><strong>Session expires:</strong> After " + (session.getMaxInactiveInterval() / 60) + " minutes of inactivity</li>");
        out.println("                    <li><strong>Logout:</strong> Calls <code>session.invalidate()</code> to destroy the session</li>");
        out.println("                </ul>");
        out.println("            </div>");
        out.println("        </div>");

        out.println("    </div>");
        out.println("</body>");
        out.println("</html>");
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null) {
            response.sendRedirect("/session-demo");
            return;
        }

        String action = request.getParameter("action");

        if ("save".equals(action)) {
            // SAVE PROFILE: Get form data and store in session
            String username = request.getParameter("username");
            String favLanguage = request.getParameter("favLanguage");
            String favColor = request.getParameter("favColor");

            // Validate and sanitize
            if (username != null && !username.trim().isEmpty()) {
                username = username.trim();

                // STORE IN SESSION (this is the key!)
                session.setAttribute("username", username);
                session.setAttribute("favLanguage", favLanguage);
                session.setAttribute("favColor", favColor);

                System.out.println("Profile saved for: " + username);
                System.out.println("  Language: " + favLanguage);
                System.out.println("  Color: " + favColor);
            }

        } else if ("logout".equals(action)) {
            // LOGOUT: Destroy the session
            System.out.println("User logged out. Session invalidated: " + session.getId());
            session.invalidate();  // Destroys the session completely!
        }

        // Redirect to GET (POST-Redirect-GET pattern)
        response.sendRedirect("/session-demo");
    }

    /**
     * Escape HTML to prevent XSS attacks
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