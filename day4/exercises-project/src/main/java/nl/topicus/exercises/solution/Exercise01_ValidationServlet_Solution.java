package nl.topicus.exercises.solution;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * EXERCISE 1 SOLUTION: AJAX Enhancement with PRG Fallback
 */
@WebServlet({"/exercises-solution/validation-form", "/exercises-solution/validation"})
public class Exercise01_ValidationServlet_Solution extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        
        @SuppressWarnings("unchecked")
        List<String> errors = (List<String>) session.getAttribute("flash_errors");
        String name = (String) session.getAttribute("flash_name");
        String email = (String) session.getAttribute("flash_email");
        String quantity = (String) session.getAttribute("flash_quantity");
        
        session.removeAttribute("flash_errors");
        session.removeAttribute("flash_name");
        session.removeAttribute("flash_email");
        session.removeAttribute("flash_quantity");
        
        String errorHtml = "";
        if (errors != null && !errors.isEmpty()) {
            StringBuilder sb = new StringBuilder("<div class='error-box'><h3>Please fix these errors:</h3><ul>");
            for (String error : errors) {
                sb.append("<li>").append(error).append("</li>");
            }
            sb.append("</ul></div>");
            errorHtml = sb.toString();
        }
        
        String html = buildFormHtml(errorHtml, name, email, quantity);
        response.setContentType("text/html; charset=UTF-8");
        response.getWriter().write(html);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String quantityStr = request.getParameter("quantity");
        
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
        
        // SOLUTION: Detect AJAX request
        String ajaxHeader = request.getHeader("X-Requested-With");
        boolean isAjax = "XMLHttpRequest".equals(ajaxHeader);
        
        if (!errors.isEmpty()) {
            if (isAjax) {
                // AJAX: Return JSON
                response.setContentType("application/json");
                String json = "{\"success\":false,\"errors\":" + buildJsonArray(errors) + "}";
                response.getWriter().write(json);
                return;
            } else {
                // Fallback: PRG pattern
                HttpSession session = request.getSession();
                session.setAttribute("flash_errors", errors);
                session.setAttribute("flash_name", name);
                session.setAttribute("flash_email", email);
                session.setAttribute("flash_quantity", quantityStr);
                
                response.sendRedirect("/exercises-solution/validation-form");
                return;
            }
        }
        
        // Success!
        if (isAjax) {
            // AJAX: Return JSON with redirect URL
            response.setContentType("application/json");
            String json = "{\"success\":true,\"redirectUrl\":\"/exercises-solution/validation-success\"}";
            response.getWriter().write(json);
        } else {
            // Fallback: PRG redirect
            response.sendRedirect("/exercises-solution/validation-success");
        }
    }
    
    private String buildJsonArray(List<String> items) {
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < items.size(); i++) {
            json.append("\"").append(items.get(i).replace("\"", "\\\"")).append("\"");
            if (i < items.size() - 1) json.append(",");
        }
        json.append("]");
        return json.toString();
    }
    
    private String buildFormHtml(String errorHtml, String name, String email, String quantity) {
        return "<!DOCTYPE html>" +
               "<html>" +
               "<head><title>Exercise 1 Solution</title>" +
               "<link rel='stylesheet' href='/static/css/styles.css'></head>" +
               "<body><div class='container'>" +
               "<h1>Exercise 1 Solution: AJAX + PRG</h1>" +
               "<div class='success-box'>" +
               "<h3>Progressive Enhancement Working!</h3>" +
               "<ul>" +
               "<li>With JavaScript: AJAX submission, no page reload</li>" +
               "<li>Without JavaScript: PRG pattern still works</li>" +
               "</ul>" +
               "</div>" +
               "<div id='error-container'>" + errorHtml + "</div>" +
               "<form id='validation-form' method='post' action='/exercises-solution/validation'>" +
               "<p><label>Name (2-50 chars):</label><br>" +
               "<input type='text' name='name' value='" + (name != null ? name : "") + "' required></p>" +
               "<p><label>Email:</label><br>" +
               "<input type='email' name='email' value='" + (email != null ? email : "") + "' required></p>" +
               "<p><label>Quantity (1-10):</label><br>" +
               "<input type='number' name='quantity' value='" + (quantity != null ? quantity : "1") + "' min='1' max='10' required></p>" +
               "<button type='submit' class='btn btn-primary'>Submit</button>" +
               "</form>" +
               "<p><a href='/'>Back</a></p>" +
               "</div>" +
               
               // AJAX JavaScript
               "<script>" +
               "const form = document.getElementById('validation-form');" +
               "const errorContainer = document.getElementById('error-container');" +
               "" +
               "form.addEventListener('submit', function(e) {" +
               "    e.preventDefault();" +
               "    " +
               "    const formData = new URLSearchParams(new FormData(this));" +
               "    " +
               "    fetch(this.action, {" +
               "        method: 'POST'," +
               "        headers: {" +
               "            'X-Requested-With': 'XMLHttpRequest'," +
               "            'Content-Type': 'application/x-www-form-urlencoded'" +
               "        }," +
               "        body: formData" +
               "    })" +
               "    .then(response => response.json())" +
               "    .then(data => {" +
               "        if (data.success) {" +
               "            window.location.href = data.redirectUrl;" +
               "        } else {" +
               "            let errorHtml = '<div class=\"error-box\"><h3>Please fix these errors:</h3><ul>';" +
               "            data.errors.forEach(error => {" +
               "                errorHtml += '<li>' + error + '</li>';" +
               "            });" +
               "            errorHtml += '</ul></div>';" +
               "            errorContainer.innerHTML = errorHtml;" +
               "        }" +
               "    })" +
               "    .catch(error => {" +
               "        console.error('AJAX failed, falling back:', error);" +
               "        this.submit();" +
               "    });" +
               "});" +
               "</script>" +
               "</body></html>";
    }
}
