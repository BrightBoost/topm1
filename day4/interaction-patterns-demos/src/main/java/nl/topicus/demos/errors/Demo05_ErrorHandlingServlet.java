package nl.topicus.demos.errors;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import nl.topicus.shared.model.Order;
import nl.topicus.shared.model.OrderItem;
import nl.topicus.shared.model.Product;
import nl.topicus.shared.rendering.TemplateRenderer;
import nl.topicus.shared.service.OrderService;
import nl.topicus.shared.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * DEMO 5: Error Handling Patterns.
 * 
 * This demonstrates:
 * 1. Business logic errors (out of stock)
 * 2. User-friendly error messages
 * 3. Proper error logging
 * 4. Redirecting with error context
 * 5. How Wicket handles errors
 * 
 * Teaching points:
 * - Validation errors vs Business logic errors
 * - Validation: input format wrong (user's fault)
 * - Business logic: input valid but business rule prevents action (not user's fault)
 * - Always log errors server-side
 * - Show friendly messages to users
 * 
 * Types of errors:
 * 1. Validation errors (400) - Input format wrong
 * 2. Business logic errors (409) - Business rules prevent action
 * 3. Server errors (500) - Something broke
 * 
 * Compare to Wicket:
 * - Wicket: form.error("message") shows in FeedbackPanel
 * - Wicket: setResponsePage() with parameters for error context
 * - Here: We manually redirect with error in session
 * 
 * URL: /demos/order-product
 */
@WebServlet("/demos/order-product")
public class Demo05_ErrorHandlingServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(Demo05_ErrorHandlingServlet.class);
    
    private final OrderService orderService = OrderService.getInstance();
    private final ProductService productService = ProductService.getInstance();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            String productId = request.getParameter("productId");
            String quantityStr = request.getParameter("quantity");
            
            // Basic validation (input format)
            if (quantityStr == null || quantityStr.trim().isEmpty()) {
                handleValidationError(request, response, "Quantity is required", productId);
                return;
            }
            
            int quantity;
            try {
                quantity = Integer.parseInt(quantityStr);
            } catch (NumberFormatException e) {
                handleValidationError(request, response, "Quantity must be a valid number", productId);
                return;
            }
            
            if (quantity < 1) {
                handleValidationError(request, response, "Quantity must be at least 1", productId);
                return;
            }

            // Validation passed - now check business rules
            Product product = productService.getProduct(productId);
            
            if (product == null) {
                handleNotFoundError(response, "Product not found", productId);
                return;
            }

            // Business logic check: Is there enough stock?
            if (!product.isInStock(quantity)) {
                // This is a BUSINESS LOGIC error, not a validation error
                // The input (quantity) is valid, but business rules prevent the action
                
                logger.warn("Insufficient stock for product {}. Requested: {}, Available: {}", 
                           productId, quantity, product.getStockLevel());
                
                handleBusinessLogicError(
                    request,
                    response,
                    "Sorry, only " + product.getStockLevel() + " items available. Please reduce quantity.",
                    productId,
                    quantity
                );
                return;
            }

            // All checks passed - create order
            Order order = new Order();
            order.setCustomerName("Demo User");
            order.setEmail("demo@example.com");
            order.addItem(new OrderItem(productId, product.getName(), quantity, product.getPrice()));
            orderService.createOrder(order);
            
            // Reduce stock
            productService.reduceStock(productId, quantity);
            
            logger.info("Order created successfully. OrderId: {}, Product: {}, Quantity: {}", 
                       order.getId(), productId, quantity);

            // Success - redirect
            response.sendRedirect("/demos/order-success?orderId=" + order.getId());
            
        } catch (Exception e) {
            // Catch all unexpected errors
            handleServerError(request, response, e);
        }
    }

    /**
     * Handle validation errors (400 - Bad Request).
     * User input format is wrong.
     */
    private void handleValidationError(HttpServletRequest request,
                                       HttpServletResponse response,
                                      String errorMessage, 
                                      String productId) throws IOException {
        logger.debug("Validation error: {}", errorMessage);
        
        HttpSession session = request.getSession();
        session.setAttribute("flash_error", errorMessage);
        session.setAttribute("flash_errorType", "validation");
        
        response.sendRedirect("/demos/product-page?id=" + productId);
    }

    /**
     * Handle business logic errors (409 - Conflict).
     * Input is valid but business rules prevent the action.
     */
    private void handleBusinessLogicError(HttpServletRequest request,
                                          HttpServletResponse response,
                                         String errorMessage,
                                         String productId,
                                         int requestedQuantity) throws IOException {
        HttpSession session = request.getSession();
        session.setAttribute("flash_error", errorMessage);
        session.setAttribute("flash_errorType", "business");
        session.setAttribute("flash_requestedQuantity", requestedQuantity);
        
        response.sendRedirect("/demos/product-page?id=" + productId);
    }

    /**
     * Handle not found errors (404).
     */
    private void handleNotFoundError(HttpServletResponse response,
                                    String errorMessage,
                                    String productId) throws IOException {
        logger.warn("Product not found: {}", productId);
        
        response.sendError(404, errorMessage);
    }

    /**
     * Handle server errors (500 - Internal Server Error).
     * Something broke in our code.
     */
    private void handleServerError(HttpServletRequest request,
                                  HttpServletResponse response,
                                  Exception e) throws IOException {
        // Log with full stack trace
        logger.error("Unexpected error during order processing", e);
        
        // Show user-friendly message (no technical details!)
        HttpSession session = request.getSession();
        session.setAttribute("flash_error", 
            "We're experiencing technical difficulties. Please try again later.");
        session.setAttribute("flash_errorType", "server");
        
        response.sendRedirect("/demos/product-page?id=burger");
    }
}
