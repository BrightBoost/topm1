package nl.topicus.exercises.solution;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nl.topicus.shared.model.Order;
import nl.topicus.shared.service.OrderService;

import java.io.IOException;

/**
 * EXERCISE 0 SOLUTION: Success page (GET handler)
 */
@WebServlet("/exercises-solution/success")
public class Exercise00_SuccessServlet_Solution extends HttpServlet {

    private final OrderService orderService = OrderService.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Get order ID from URL parameter
        String orderIdStr = request.getParameter("orderId");
        
        if (orderIdStr == null) {
            response.sendError(400, "Missing orderId");
            return;
        }

        Long orderId = Long.parseLong(orderIdStr);
        Order order = orderService.getOrder(orderId);

        if (order == null) {
            response.sendError(404, "Order not found");
            return;
        }

        String html = "<!DOCTYPE html>" +
                     "<html>" +
                     "<head><title>Success</title>" +
                     "<link rel='stylesheet' href='/static/css/styles.css'></head>" +
                     "<body><div class='container'>" +
                     "<h1>Order Placed!</h1>" +
                     "<div class='success-box'>" +
                     "<p><strong>Order ID:</strong> #" + order.getId() + "</p>" +
                     "<p><strong>Customer:</strong> " + order.getCustomerName() + "</p>" +
                     "</div>" +
                     "<div class='success-box'>" +
                     "<h3>PRG Pattern Working!</h3>" +
                     "<p>URL changed to: /exercises-solution/success?orderId=" + orderId + "</p>" +
                     "<p>Press F5 now - NO 'Confirm form resubmission' warning!</p>" +
                     "<p>This is a GET request, safe to refresh</p>" +
                     "</div>" +
                     "<p><a href='/exercises-solution/order'>Try Again</a> | " +
                     "<a href='/'>Back</a></p>" +
                     "</div></body></html>";

        response.setContentType("text/html; charset=UTF-8");
        response.getWriter().write(html);
    }
}
