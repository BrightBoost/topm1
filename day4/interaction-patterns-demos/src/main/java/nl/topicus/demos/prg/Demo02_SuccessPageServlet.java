package nl.topicus.demos.prg;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nl.topicus.shared.model.Order;
import nl.topicus.shared.rendering.TemplateRenderer;
import nl.topicus.shared.service.OrderService;

import java.io.IOException;

/**
 * Success page servlet - handles the GET request after PRG redirect.
 * 
 * This demonstrates:
 * - GET handler that displays results
 * - Fetching data based on URL parameters
 * - Safe to refresh (just re-displays the same data)
 * 
 * Compare to Wicket:
 * - Wicket: setResponsePage(new SuccessPage(orderId))
 * - Here: We manually redirect to this servlet
 * 
 * URL: /demos/success-correct
 */
@WebServlet("/demos/success-correct")
public class Demo02_SuccessPageServlet extends HttpServlet {

    private final OrderService orderService = OrderService.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Get parameters from URL (passed via redirect)
        String orderIdStr = request.getParameter("orderId");
        String item = request.getParameter("item");
        String quantityStr = request.getParameter("quantity");

        // Fetch the order from service (demonstrates read-only operation)
        Long orderId = Long.parseLong(orderIdStr);
        Order order = orderService.getOrder(orderId);

        // Render the success template
        String html = TemplateRenderer.render(
            "demos/success-correct.html",
            TemplateRenderer.data()
                .put("orderId", orderId)
                .put("item", item)
                .put("quantity", quantityStr)
                .put("total", String.format("%.2f", order.getTotal()))
                .build()
        );

        response.setContentType("text/html; charset=UTF-8");
        response.getWriter().write(html);
        
        // This is a GET request - safe to repeat!
        // User can refresh as many times as they want
        // No duplicate orders will be created
    }
}
