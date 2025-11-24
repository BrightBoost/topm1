package nl.topicus.exercises.starter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import nl.topicus.shared.rendering.TemplateRenderer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * EXERCISE 1: Add AJAX Enhancement
 * 
 * Current State: PRG pattern works (try disabling JavaScript!)
 * 
 * Your Task:
 * 1. Detect AJAX requests (check X-Requested-With header)
 * 2. Return JSON for AJAX requests: {"success": true/false, "errors": [...], "redirectUrl": "..."}
 * 3. Keep existing PRG fallback working
 * 4. Add JavaScript to submit via AJAX
 * 
 * Test:
 * - With JS enabled: Should show errors without page reload
 * - With JS disabled: Should still work with PRG pattern
 */
@WebServlet({"/exercises/validation-form", "/exercises/validation"})
public class Exercise01_ValidationServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        
        // Get flash data (errors from previous request)
        @SuppressWarnings("unchecked")
        List<String> errors = (List<String>) session.getAttribute("flash_errors");
        String name = (String) session.getAttribute("flash_name");
        String email = (String) session.getAttribute("flash_email");
        String quantity = (String) session.getAttribute("flash_quantity");
        
        // Clear flash data (one-time use!)
        session.removeAttribute("flash_errors");
        session.removeAttribute("flash_name");
        session.removeAttribute("flash_email");
        session.removeAttribute("flash_quantity");
        
        // Build error HTML
        String errorHtml = "";
        if (errors != null && !errors.isEmpty()) {
            StringBuilder sb = new StringBuilder("<div class='error-box'><h3>Please fix these errors:</h3><ul>");
            for (String error : errors) {
                sb.append("<li>").append(error).append("</li>");
            }
            sb.append("</ul></div>");
            errorHtml = sb.toString();
        }
        
        // Render form
        String html = TemplateRenderer.render(
            "exercises/validation-form.html",
            TemplateRenderer.data()
                .put("action", "/exercises/validation")
                .put("errors", errorHtml)
                .put("name", name != null ? name : "")
                .put("email", email != null ? email : "")
                .put("quantity", quantity != null ? quantity : "1")
                .build()
        );
        
        response.setContentType("text/html; charset=UTF-8");
        response.getWriter().write(html);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Get form data
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String quantityStr = request.getParameter("quantity");
        
        // Validate
        List<String> errors = new ArrayList<>();
        
        if (name == null || name.length() < 2 || name.length() > 50) {
            errors.add("Name must be 2-50 characters");
        }
        
        if (email == null || !email.contains("@")) {
            errors.add("Please enter a valid email");
        }
        
        try {
            int quantity = Integer.parseInt(quantityStr);
            if (quantity < 1 || quantity > 10) {
                errors.add("Quantity must be between 1 and 10");
            }
        } catch (NumberFormatException e) {
            errors.add("Please enter a valid quantity");
        }
        
        // TODO: Detect if this is an AJAX request
        // String ajaxHeader = request.getHeader("X-Requested-With");
        // boolean isAjax = "XMLHttpRequest".equals(ajaxHeader);
        
        if (!errors.isEmpty()) {
            // TODO: If AJAX, return JSON instead of redirect
            // if (isAjax) {
            //     response.setContentType("application/json");
            //     String json = "{\"success\":false,\"errors\":" + buildJsonArray(errors) + "}";
            //     response.getWriter().write(json);
            //     return;
            // }
            
            // PRG fallback: Store errors in session and redirect
            HttpSession session = request.getSession();
            session.setAttribute("flash_errors", errors);
            session.setAttribute("flash_name", name);
            session.setAttribute("flash_email", email);
            session.setAttribute("flash_quantity", quantityStr);
            
            response.sendRedirect("/exercises/validation-form");
            return;
        }
        
        // Success!
        // TODO: If AJAX, return JSON with redirect URL
        // if (isAjax) {
        //     response.setContentType("application/json");
        //     String json = "{\"success\":true,\"redirectUrl\":\"/exercises/validation-success\"}";
        //     response.getWriter().write(json);
        //     return;
        // }
        
        // PRG fallback: Redirect to success page
        response.sendRedirect("/exercises/validation-success");
    }
    
    // Helper method for JSON array (simple approach, no Gson needed for exercise)
    private String buildJsonArray(List<String> items) {
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < items.size(); i++) {
            json.append("\"").append(items.get(i).replace("\"", "\\\"")).append("\"");
            if (i < items.size() - 1) json.append(",");
        }
        json.append("]");
        return json.toString();
    }
}
