package nl.topicus.demos.components;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nl.topicus.shared.model.Order;
import nl.topicus.shared.model.OrderItem;
import nl.topicus.shared.model.Product;
import nl.topicus.shared.service.OrderService;
import nl.topicus.shared.service.ProductService;

import java.io.IOException;

/**
 * Handles order submission from Demo 6 component-based page.
 * 
 * URL: /demos/order-product2
 */
@WebServlet("/demos/order-product2")
public class Demo06_OrderProductServlet extends HttpServlet {

    private final ProductService productService = ProductService.getInstance();
    private final OrderService orderService = OrderService.getInstance();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String productId = request.getParameter("productId");
        String quantityStr = request.getParameter("quantity");
        
        if (productId == null || quantityStr == null) {
            response.sendError(400, "Missing parameters");
            return;
        }

        int quantity = Integer.parseInt(quantityStr);
        Product product = productService.getProduct(productId);

        if (product == null) {
            response.sendError(404, "Product not found");
            return;
        }

        // Create order
        Order order = new Order("Component Demo Customer", "demo@example.com");
        order.addItem(new OrderItem(productId, product.getName(), quantity, product.getPrice()));
        orderService.createOrder(order);

        // PRG pattern - redirect to success page
        response.sendRedirect("/demos/order-success?orderId=" + order.getId());
    }
}
