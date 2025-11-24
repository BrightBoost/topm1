package nl.topicus.demos.validation;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import nl.topicus.shared.model.Order;
import nl.topicus.shared.model.OrderItem;
import nl.topicus.shared.rendering.HtmlBuilder;
import nl.topicus.shared.rendering.TemplateRenderer;
import nl.topicus.shared.service.OrderService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DEMO 4: Form Validation with PRG + Flash pattern.
 * 
 * This demonstrates:
 * 1. Server-side validation (never trust the client!)
 * 2. Flash pattern for temporary session data
 * 3. Preserving user input on validation errors
 * 4. Clean navigation (no back button issues)
 * 5. How Wicket's Form validation works
 * 
 * Teaching points:
 * - Client-side validation is for UX only
 * - Server-side validation is mandatory
 * - Flash pattern = store in session, retrieve once, delete immediately
 * - This prevents errors from showing on refresh
 * 
 * Compare to Wicket:
 * - Wicket: form.add(new RequiredValidator())
 * - Wicket: error() method adds to FeedbackPanel
 * - Wicket: Automatically preserves form values
 * - Here: We do it all manually to understand how!
 * 
 * URL: /demos/validation
 */
@WebServlet("/demos/validation")
public class Demo04_ValidationServlet extends HttpServlet {

    private final OrderService orderService = OrderService.getInstance();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Extract form parameters
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String quantityStr = request.getParameter("quantity");
        String item = request.getParameter("item");

        // Validate all fields (collect ALL errors, not just first one!)
        List<String> errors = new ArrayList<>();
        
        // Validate name
        if (name == null || name.trim().isEmpty()) {
            errors.add("Name is required");
        } else if (name.trim().length() < 2) {
            errors.add("Name must be at least 2 characters");
        } else if (name.trim().length() > 50) {
            errors.add("Name must be less than 50 characters");
        }

        // Validate email
        if (email == null || email.trim().isEmpty()) {
            errors.add("Email is required");
        } else if (!email.contains("@")) {
            errors.add("Email must contain @");
        } else if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            errors.add("Invalid email format");
        }

        // Validate quantity
        Integer quantity = null;
        if (quantityStr == null || quantityStr.trim().isEmpty()) {
            errors.add("Quantity is required");
        } else {
            try {
                quantity = Integer.parseInt(quantityStr);
                if (quantity < 1) {
                    errors.add("Quantity must be at least 1");
                } else if (quantity > 10) {
                    errors.add("Quantity cannot exceed 10");
                }
            } catch (NumberFormatException e) {
                errors.add("Quantity must be a valid number");
            }
        }

        // Validate item selection
        if (item == null || item.trim().isEmpty()) {
            errors.add("Please select an item");
        }

        // If there are validation errors, use FLASH pattern
        if (!errors.isEmpty()) {
            // Store errors and form data in session (temporary!)
            HttpSession session = request.getSession();
            session.setAttribute("flash_errors", errors);
            
            // Store form data to re-populate form
            Map<String, String> formData = new HashMap<>();
            formData.put("name", name != null ? name : "");
            formData.put("email", email != null ? email : "");
            formData.put("quantity", quantityStr != null ? quantityStr : "");
            formData.put("item", item != null ? item : "");
            session.setAttribute("flash_formData", formData);
            
            // REDIRECT back to form (PRG pattern!)
            response.sendRedirect("/demos/validation-form");
            return;
        }

        // All valid - create order
        Order order = new Order();
        order.setCustomerName(name.trim());
        order.setEmail(email.trim());
        order.addItem(new OrderItem(item, item, quantity, 9.99));
        orderService.createOrder(order);

        // Success - redirect to success page
        response.sendRedirect("/demos/validation-success?orderId=" + order.getId());
    }
}
