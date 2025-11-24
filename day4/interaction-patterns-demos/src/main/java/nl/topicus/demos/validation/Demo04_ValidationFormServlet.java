package nl.topicus.demos.validation;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import nl.topicus.shared.rendering.HtmlBuilder;
import nl.topicus.shared.rendering.TemplateRenderer;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Form display servlet that handles flash data.
 * 
 * This demonstrates:
 * - Retrieving flash data from session
 * - Immediately removing it (flash = one-time use!)
 * - Pre-populating form with user's previous input
 * - Showing validation errors
 * 
 * URL: /demos/validation-form
 */
@WebServlet("/demos/validation-form")
public class Demo04_ValidationFormServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        
        // Retrieve flash data from session
        @SuppressWarnings("unchecked")
        List<String> errors = (List<String>) session.getAttribute("flash_errors");
        
        @SuppressWarnings("unchecked")
        Map<String, String> formData = (Map<String, String>) session.getAttribute("flash_formData");
        
        // CRITICAL: Remove from session immediately (flash pattern!)
        // This ensures errors don't show up if user refreshes the page
        session.removeAttribute("flash_errors");
        session.removeAttribute("flash_formData");
        
        // Build error HTML if there are errors
        String errorsHtml = "";
        if (errors != null && !errors.isEmpty()) {
            errorsHtml = HtmlBuilder.errorList(errors);
        }

        // Get form values (empty string if null)
        String name = formData != null ? formData.getOrDefault("name", "") : "";
        String email = formData != null ? formData.getOrDefault("email", "") : "";
        String quantity = formData != null ? formData.getOrDefault("quantity", "1") : "1";
        String item = formData != null ? formData.getOrDefault("item", "") : "";

        // Render the form
        String html = TemplateRenderer.render(
            "demos/validation-form.html",
            TemplateRenderer.data()
                .put("errors", errorsHtml)
                .put("name", name)
                .put("email", email)
                .put("quantity", quantity)
                .put("itemBurger", "burger".equals(item) ? "selected" : "")
                .put("itemPizza", "pizza".equals(item) ? "selected" : "")
                .put("itemPasta", "pasta".equals(item) ? "selected" : "")
                .put("itemSalad", "salad".equals(item) ? "selected" : "")
                .build()
        );

        response.setContentType("text/html; charset=UTF-8");
        response.getWriter().write(html);
    }
}
