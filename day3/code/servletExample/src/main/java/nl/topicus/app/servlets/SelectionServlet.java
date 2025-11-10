package nl.topicus.app.servlets;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * API Servlet for AJAX requests
 *
 * Handles the AJAX interaction between client and server.
 * This is where the "Wicket magic" happens!
 *
 * URL: /api/select
 */
@WebServlet("/api/select")
public class SelectionServlet extends HttpServlet {

    private Gson gson = new Gson();


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)  throws IOException {

        // Read JSON from request body
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }

        // Parse JSON
        Map<String, String> requestData = gson.fromJson(sb.toString(),
                new TypeToken<Map<String, String>>(){}.getType());

        String selectedLanguage = requestData.get("language");

        // Rest blijft hetzelfde...
        Map<String, Object> jsonResponse = new HashMap<>();

        if (selectedLanguage == null || selectedLanguage.trim().isEmpty()) {
            jsonResponse.put("success", false);
            jsonResponse.put("message", "Geen taal geselecteerd");
        } else {
            HttpSession session = request.getSession();
            session.setAttribute("selectedLanguage", selectedLanguage);

            jsonResponse.put("success", true);
            jsonResponse.put("message", "Taal opgeslagen!");
            jsonResponse.put("selectedLanguage", selectedLanguage);
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(gson.toJson(jsonResponse));
    }


    /**
     * GET endpoint to retrieve current selection
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        Map<String, Object> jsonResponse = new HashMap<>();

        if (session != null) {
            String selectedLanguage = (String) session.getAttribute("selectedLanguage");
            jsonResponse.put("selectedLanguage", selectedLanguage);
            jsonResponse.put("hasSelection", selectedLanguage != null);
        } else {
            jsonResponse.put("hasSelection", false);
        }

        response.getWriter().write(gson.toJson(jsonResponse));
    }
}
