package nl.topicus.demos.prg;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nl.topicus.shared.model.Order;
import nl.topicus.shared.model.OrderItem;
import nl.topicus.shared.service.OrderService;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * DEMO 0: The PAINFUL way - Hardcoded HTML in Java.
 * 
 * This demonstrates why we need templates!
 * 
 * Teaching points:
 * - Writing HTML in Java strings is tedious and error-prone
 * - Hard to maintain
 * - Designers can't work on it
 * - This is why Wicket (and all frameworks) use HTML templates!
 * 
 * URL: /demos/order-hardcoded
 */
@WebServlet("/demos/order-hardcoded")
public class Demo00_HardcodedHtmlServlet extends HttpServlet {

    private final OrderService orderService = OrderService.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();

        // Writing HTML as strings - THIS IS TERRIBLE!
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("    <title>Order Form (Hardcoded)</title>");
        out.println("    <link rel='stylesheet' href='/static/css/styles.css'>");
        out.println("</head>");
        out.println("<body>");
        out.println("    <div class='container'>");
        out.println("        <h1>Order Form - Hardcoded HTML</h1>");
        out.println("        <div class='warning-box'>");
        out.println("            <strong>This is painful!</strong> Writing HTML in Java strings is:");
        out.println("            <ul>");
        out.println("                <li>Tedious and error-prone</li>");
        out.println("                <li>Hard to maintain</li>");
        out.println("                <li>Designers can't work on it</li>");
        out.println("                <li>No syntax highlighting</li>");
        out.println("            </ul>");
        out.println("            <p><strong>This is why we need HTML templates!</strong></p>");
        out.println("        </div>");
        out.println("        <form action='/demos/order-hardcoded' method='post'>");
        out.println("            <div class='form-group'>");
        out.println("                <label for='item'>Item:</label>");
        out.println("                <select id='item' name='item' required>");
        out.println("                    <option value=''>-- Select an item --</option>");
        out.println("                    <option value='burger'>Classic Burger - €8.99</option>");
        out.println("                    <option value='pizza'>Margherita Pizza - €12.99</option>");
        out.println("                    <option value='pasta'>Spaghetti Carbonara - €10.99</option>");
        out.println("                </select>");
        out.println("            </div>");
        out.println("            <div class='form-group'>");
        out.println("                <label for='quantity'>Quantity:</label>");
        out.println("                <input type='number' id='quantity' name='quantity' value='1' min='1' max='10' required>");
        out.println("            </div>");
        out.println("            <button type='submit' class='btn btn-primary'>Place Order</button>");
        out.println("        </form>");
        out.println("        <p><a href='/'>Back to demos</a></p>");
        out.println("    </div>");
        out.println("</body>");
        out.println("</html>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        
        // Extract parameters
        String item = request.getParameter("item");
        String quantityStr = request.getParameter("quantity");
        int quantity = Integer.parseInt(quantityStr);

        // Create order
        Order order = new Order();
        order.setCustomerName("Demo User");
        order.setEmail("demo@example.com");
        order.addItem(new OrderItem(item, item, quantity, 9.99));
        orderService.createOrder(order);

        // Show success - MORE HARDCODED HTML! 😭
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("    <title>Order Success</title>");
        out.println("    <link rel='stylesheet' href='/static/css/styles.css'>");
        out.println("</head>");
        out.println("<body>");
        out.println("    <div class='container'>");
        out.println("        <div class='success-box'>");
        out.println("            <h1>✅ Order Placed Successfully!</h1>");
        out.println("            <p>Order ID: #" + order.getId() + "</p>");
        out.println("            <p>Item: " + item + "</p>");
        out.println("            <p>Quantity: " + quantity + "</p>");
        out.println("            <p>Total: €" + String.format("%.2f", order.getTotal()) + "</p>");
        out.println("        </div>");
        out.println("        <div class='info-box'>");
        out.println("            <h3>🧪 Test This:</h3>");
        out.println("            <p><strong>Hit F5 (Refresh)</strong> - Notice the browser asks to resubmit!</p>");
        out.println("            <p>This is the <strong>double-submit problem</strong> we'll solve next.</p>");
        out.println("        </div>");
        out.println("        <p><a href='/'>Back to demos</a></p>");
        out.println("    </div>");
        out.println("</body>");
        out.println("</html>");
    }
}
