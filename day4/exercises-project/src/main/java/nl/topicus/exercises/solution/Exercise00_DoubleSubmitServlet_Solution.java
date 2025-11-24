package nl.topicus.exercises.solution;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nl.topicus.shared.model.Order;
import nl.topicus.shared.model.OrderItem;
import nl.topicus.shared.service.OrderService;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * EXERCISE 0 SOLUTION: POST-Redirect-GET Pattern
 */
@WebServlet("/exercises-solution/order")
public class Exercise00_DoubleSubmitServlet_Solution extends HttpServlet {

    private final OrderService orderService = OrderService.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String html = buildOrderForm();
        response.setContentType("text/html; charset=UTF-8");
        response.getWriter().write(html);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Get form data
        String customerName = request.getParameter("customerName");
        String item = request.getParameter("item");
        
        // Create order
        Order order = new Order(customerName, customerName + "@example.com");
        order.addItem(new OrderItem("item-1", item, 1, 9.99));
        orderService.createOrder(order);
        
        // SOLUTION: Redirect instead of rendering directly
        String redirectUrl = "/exercises-solution/success?orderId=" + 
                           URLEncoder.encode(order.getId().toString(), StandardCharsets.UTF_8);
        response.sendRedirect(redirectUrl);
    }

    private String buildOrderForm() {
        return "<!DOCTYPE html>" +
               "<html>" +
               "<head><title>Exercise 0 Solution</title>" +
               "<link rel='stylesheet' href='/static/css/styles.css'></head>" +
               "<body><div class='container'>" +
               "<h1>Exercise 0 Solution: PRG Pattern</h1>" +
               "<div class='success-box'>" +
               "<h3>Fixed with POST-Redirect-GET!</h3>" +
               "<p>After submitting, you'll see the redirect in action.</p>" +
               "</div>" +
               "<form method='post' action='/exercises-solution/order'>" +
               "<p><label>Your Name:</label><br>" +
               "<input type='text' name='customerName' required></p>" +
               "<p><label>Item to Order:</label><br>" +
               "<input type='text' name='item' required></p>" +
               "<button type='submit' class='btn btn-primary'>Place Order</button>" +
               "</form>" +
               "<p><a href='/'>Back</a></p>" +
               "</div></body></html>";
    }
}
