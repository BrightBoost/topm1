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

/**
 * DEMO 1: Using HTML templates but WITHOUT POST-Redirect-GET.
 * 
 * This demonstrates:
 * 1. How to use HTML templates (like Wicket does)
 * 2. The double-submit problem when you don't use PRG
 * 
 * Teaching points:
 * - Templates are much better than hardcoded HTML
 * - But we still have the double-submit problem!
 * - User can hit F5 after submit and create duplicate orders
 * 
 * Compare to Wicket:
 * - Wicket: Reads YourPage.html automatically
 * - Here: We manually read order-form.html with TemplateRenderer
 * 
 * URL: /demos/order-wrong
 */
@WebServlet("/demos/order-wrong")
public class Demo01_TemplateNoRedirectServlet extends HttpServlet {

    private final OrderService orderService = OrderService.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Read and render the HTML template
        // This is what Wicket does when you create a WebPage!
        String html = TemplateRenderer.render(
            "demos/order-form.html",
            TemplateRenderer.data()
                .put("action", "/demos/order-wrong")
                .put("errors", "")
                .put("quantity", "1")
                .build()
        );

        response.setContentType("text/html; charset=UTF-8");
        response.getWriter().write(html);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
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

        // ❌ WRONG: Render success page directly (NO REDIRECT!)
        // This is what causes the double-submit problem
        String html = TemplateRenderer.render(
            "demos/success-wrong.html",
            TemplateRenderer.data()
                .put("orderId", order.getId())
                .put("item", item)
                .put("quantity", quantity)
                .put("total", String.format("%.2f", order.getTotal()))
                .build()
        );

        response.setContentType("text/html; charset=UTF-8");
        response.getWriter().write(html);
        
        // PROBLEM: Browser URL still shows /order-wrong
        // PROBLEM: Browser thinks last action was POST
        // PROBLEM: Refresh will attempt to resubmit the form!
        // PROBLEM: User can create duplicate orders!
    }
}
