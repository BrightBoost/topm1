package nl.topicus.exercises.starter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nl.topicus.shared.model.Order;
import nl.topicus.shared.model.OrderItem;
import nl.topicus.shared.service.OrderService;

import java.io.IOException;

/**
 * EXERCISE 0: Fix the Double Submit Bug
 * 
 * Problem: This servlet renders success page directly after POST,
 * causing the double-submit problem (F5 shows "Confirm form resubmission").
 * 
 * Your Task:
 * 1. Change POST handler to redirect instead of rendering
 * 2. Create a success page servlet
 * 3. Pass order ID via URL parameter
 * 
 * Hint: Use response.sendRedirect("/exercises/success?orderId=" + orderId)
 */
@WebServlet("/exercises/order")
public class Exercise00_DoubleSubmitServlet extends HttpServlet {

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

        String html = buildSuccessPage(order);
        response.setContentType("text/html; charset=UTF-8");
        response.getWriter().write(html);
        
    }

    private String buildOrderForm() {
        return "<!DOCTYPE html>" +
               "<html>" +
               "<head><title>Exercise 0</title>" +
               "<link rel='stylesheet' href='/static/css/styles.css'></head>" +
               "<body><div class='container'>" +
               "<h1>Exercise 0: Fix Double Submit</h1>" +
               "<div class='warning-box'>" +
               "<h3>This has the double-submit bug!</h3>" +
               "<p>After submitting, try pressing F5 to refresh.</p>" +
               "</div>" +
               "<form method='post' action='/exercises/order'>" +
               "<p><label>Your Name:</label><br>" +
               "<input type='text' name='customerName' required></p>" +
               "<p><label>Item to Order:</label><br>" +
               "<input type='text' name='item' required></p>" +
               "<button type='submit' class='btn btn-primary'>Place Order</button>" +
               "</form>" +
               "<p><a href='/'>Back</a></p>" +
               "</div></body></html>";
    }

    private String buildSuccessPage(Order order) {
        return "<!DOCTYPE html>" +
               "<html>" +
               "<head><title>Success</title>" +
               "<link rel='stylesheet' href='/static/css/styles.css'></head>" +
               "<body><div class='container'>" +
               "<h1>Order Placed!</h1>" +
               "<div class='success-box'>" +
               "<p><strong>Order ID:</strong> #" + order.getId() + "</p>" +
               "<p><strong>Customer:</strong> " + order.getCustomerName() + "</p>" +
               "</div>" +
               "<div class='warning-box'>" +
               "<h3>Bug Still Present!</h3>" +
               "<p>Press F5 now - you'll see the 'Confirm form resubmission' warning!</p>" +
               "<p>The URL is still /exercises/order (the POST URL)</p>" +
               "</div>" +
               "<p><a href='/exercises/order'>Try Again</a></p>" +
               "</div></body></html>";
    }
}
