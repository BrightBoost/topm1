package nl.topicus.demos.components;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nl.topicus.shared.model.Order;
import nl.topicus.shared.service.OrderService;

import java.io.IOException;

/**
 * Success page for Demo 6 component orders.
 * 
 * URL: /demos/order-success
 */
@WebServlet("/demos/order-success")
public class Demo06_OrderSuccessServlet extends HttpServlet {

    private final OrderService orderService = OrderService.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
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

        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>");
        html.append("<html>");
        html.append("<head>");
        html.append("<title>Order Success</title>");
        html.append("<link rel='stylesheet' href='/static/css/styles.css'>");
        html.append("</head>");
        html.append("<body>");
        html.append("<div class='container'>");
        html.append("<h1>Demo 6: Order Success! 🎉</h1>");
        
        html.append("<div class='success-box'>");
        html.append("<h2>✅ Order Placed Successfully!</h2>");
        html.append("<p><strong>Order ID:</strong> #").append(order.getId()).append("</p>");
        html.append("<p><strong>Customer:</strong> ").append(order.getCustomerName()).append("</p>");
        html.append("<p><strong>Total:</strong> €").append(String.format("%.2f", order.getTotal())).append("</p>");
        html.append("</div>");

        html.append("<div class='info-box'>");
        html.append("<h3>Component-Based Architecture:</h3>");
        html.append("<p>The page you just ordered from was built using components:</p>");
        html.append("<ul>");
        html.append("<li>Each <strong>ProductCard</strong> is a reusable component</li>");
        html.append("<li>Components encapsulate their own rendering logic</li>");
        html.append("<li>The <strong>Page</strong> component contains all other components</li>");
        html.append("<li>Calling <code>page.render()</code> renders the entire tree</li>");
        html.append("</ul>");
        html.append("<p><strong>This is exactly how Wicket works!</strong></p>");
        html.append("</div>");

        html.append("<div class='info-box'>");
        html.append("<h4>Wicket Equivalent:</h4>");
        html.append("<pre><code>public class ProductPage extends WebPage {");
        html.append("\n    public ProductPage() {");
        html.append("\n        add(new HeaderPanel(\"header\"));");
        html.append("\n        add(new ProductPanel(\"product\", product));");
        html.append("\n        add(new FooterPanel(\"footer\"));");
        html.append("\n    }");
        html.append("\n}</code></pre>");
        html.append("<p>Wicket automatically renders this component tree to HTML!</p>");
        html.append("</div>");

        html.append("<p><a href='/demos/components' class='btn btn-secondary'>View Products Again</a> ");
        html.append("<a href='/' class='btn btn-secondary'>← Back to demos</a></p>");
        
        html.append("</div>");
        html.append("</body>");
        html.append("</html>");

        response.setContentType("text/html; charset=UTF-8");
        response.getWriter().write(html.toString());
    }
}
