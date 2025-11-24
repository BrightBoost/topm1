package nl.topicus.demos.prg;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nl.topicus.shared.model.Order;
import nl.topicus.shared.model.OrderItem;
import nl.topicus.shared.rendering.TemplateRenderer;
import nl.topicus.shared.service.OrderService;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * DEMO 2: Using HTML templates WITH POST-Redirect-GET pattern.
 * 
 * This demonstrates:
 * 1. Proper POST-Redirect-GET implementation
 *
 * Teaching points:
 * - POST processes the form and changes data
 * - REDIRECT separates the action from displaying the result
 * - GET shows the result (safe to refresh)
 *
 * URL: /demos/order-correct
 */
@WebServlet("/demos/order-correct")
public class Demo02_TemplateWithRedirectServlet extends HttpServlet {

    private final OrderService orderService = OrderService.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Read and render the HTML template
        String html = TemplateRenderer.render(
            "demos/order-form.html",
            TemplateRenderer.data()
                .put("action", "/demos/order-correct")
                .put("errors", "")
                .put("quantity", "1")
                .build()
        );

        response.setContentType("text/html; charset=UTF-8");
        response.getWriter().write(html);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        
        // Extract form parameters
        String item = request.getParameter("item");
        String quantityStr = request.getParameter("quantity");
        int quantity = Integer.parseInt(quantityStr);

        // Create the order
        Order order = new Order();
        order.setCustomerName("Demo User");
        order.setEmail("demo@example.com");
        order.addItem(new OrderItem(item, item, quantity, 9.99));

        // Save the order
        orderService.createOrder(order);

        // ✅ CORRECT: Redirect to success page
        // URL encoding is important for special characters
        String encodedItem = URLEncoder.encode(item, StandardCharsets.UTF_8);
        response.sendRedirect("/demos/success-correct?orderId=" + order.getId() + 
                              "&item=" + encodedItem + 
                              "&quantity=" + quantity);
        
        // SUCCESS: Browser will make a new GET request
        // SUCCESS: URL will show /success-correct
        // SUCCESS: Refresh is safe - just shows the success page again
        // SUCCESS: No duplicate orders!
        
    }
}
