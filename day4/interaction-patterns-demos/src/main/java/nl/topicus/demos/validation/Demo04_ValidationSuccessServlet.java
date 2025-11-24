package nl.topicus.demos.validation;

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
 * Validation success page - shows order confirmation after successful validation.
 *
 * This demonstrates:
 * - GET handler after PRG redirect
 * - Fetching data by ID
 * - Success page display
 *
 * URL: /demos/validation-success
 */
@WebServlet("/demos/validation-success")
public class Demo04_ValidationSuccessServlet extends HttpServlet {

    private final OrderService orderService = OrderService.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Get order ID from URL parameter
        String orderIdStr = request.getParameter("orderId");

        if (orderIdStr == null) {
            response.sendError(400, "Missing orderId parameter");
            return;
        }

        Long orderId = Long.parseLong(orderIdStr);
        Order order = orderService.getOrder(orderId);

        if (order == null) {
            response.sendError(404, "Order not found");
            return;
        }

        // Build success HTML
        StringBuilder successHtml = new StringBuilder();
        successHtml.append("<div class='success-box'>");
        successHtml.append("<h2>✅ Order Placed Successfully!</h2>");
        successHtml.append("<p><strong>Order ID:</strong> #").append(order.getId()).append("</p>");
        successHtml.append("<p><strong>Customer:</strong> ").append(order.getCustomerName()).append("</p>");
        successHtml.append("<p><strong>Email:</strong> ").append(order.getEmail()).append("</p>");
        successHtml.append("<p><strong>Total:</strong> €").append(String.format("%.2f", order.getTotal())).append("</p>");
        successHtml.append("</div>");

        successHtml.append("<div class='info-box'>");
        successHtml.append("<h3>🎯 What Just Happened:</h3>");
        successHtml.append("<ol>");
        successHtml.append("<li>You submitted the form with valid data</li>");
        successHtml.append("<li>Server validated all fields</li>");
        successHtml.append("<li>Order was created</li>");
        successHtml.append("<li>Server <strong>redirected</strong> here (PRG pattern!)</li>");
        successHtml.append("<li>This GET request fetched the order and displayed it</li>");
        successHtml.append("</ol>");
        successHtml.append("<p><strong>Try hitting F5 (refresh):</strong> No form resubmission warning!</p>");
        successHtml.append("</div>");

        successHtml.append("<div class='info-box'>");
        successHtml.append("<h4>How Wicket Does This:</h4>");
        successHtml.append("<pre><code>// Wicket Form:");
        successHtml.append("\nForm form = new Form(\"orderForm\") {");
        successHtml.append("\n    @Override");
        successHtml.append("\n    protected void onSubmit() {");
        successHtml.append("\n        // Wicket validates automatically");
        successHtml.append("\n        Order order = createOrder();");
        successHtml.append("\n        setResponsePage(new SuccessPage(order));  // PRG!");
        successHtml.append("\n    }");
        successHtml.append("\n};</code></pre>");
        successHtml.append("</div>");

        successHtml.append("<p><a href='/demos/validation-form' class='btn btn-secondary'>Place Another Order</a> ");
        successHtml.append("<a href='/' class='btn btn-secondary'>← Back to demos</a></p>");

        // Render full page
        StringBuilder fullHtml = new StringBuilder();
        fullHtml.append("<!DOCTYPE html>");
        fullHtml.append("<html>");
        fullHtml.append("<head>");
        fullHtml.append("<title>Order Success</title>");
        fullHtml.append("<link rel='stylesheet' href='/static/css/styles.css'>");
        fullHtml.append("</head>");
        fullHtml.append("<body>");
        fullHtml.append("<div class='container'>");
        fullHtml.append("<h1>Demo 4: Validation Success! 🎉</h1>");
        fullHtml.append(successHtml);
        fullHtml.append("</div>");
        fullHtml.append("</body>");
        fullHtml.append("</html>");

        response.setContentType("text/html; charset=UTF-8");
        response.getWriter().write(fullHtml.toString());
    }
}