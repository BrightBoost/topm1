package nl.topicus.exercises.solution;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import nl.topicus.shared.model.Product;
import nl.topicus.shared.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * EXERCISE 2 SOLUTION: Proper Error Handling
 */
@WebServlet("/exercises-solution/order-product")
public class Exercise02_ErrorHandlingServlet_Solution extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(Exercise02_ErrorHandlingServlet_Solution.class);
    private final ProductService productService = ProductService.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        String error = (String) session.getAttribute("flash_error");
        String errorType = (String) session.getAttribute("flash_error_type");
        session.removeAttribute("flash_error");
        session.removeAttribute("flash_error_type");
        
        String html = buildProductPage(error, errorType);
        response.setContentType("text/html; charset=UTF-8");
        response.getWriter().write(html);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            String productId = request.getParameter("productId");
            String quantityStr = request.getParameter("quantity");
            
            // VALIDATION ERRORS (400) - User's fault
            if (quantityStr == null || quantityStr.isEmpty()) {
                handleValidationError(request, response, "Please enter a quantity");
                return;
            }
            
            int quantity;
            try {
                quantity = Integer.parseInt(quantityStr);
            } catch (NumberFormatException e) {
                handleValidationError(request, response, "Please enter a valid number");
                return;
            }
            
            if (quantity < 1) {
                handleValidationError(request, response, "Quantity must be at least 1");
                return;
            }
            
            Product product = productService.getProduct(productId);
            
            if (product == null) {
                handleValidationError(request, response, "Product not found");
                return;
            }
            
            // BUSINESS LOGIC ERRORS (409) - Not user's fault
            if (!product.isInStock(quantity)) {
                handleBusinessError(request, response, 
                    "Sorry, only " + product.getStockLevel() + " items available. " +
                    "This is not your fault - stock levels changed!");
                return;
            }
            
            // Success!
            response.sendRedirect("/exercises-solution/order-success?productId=" + productId + "&quantity=" + quantity);
            
        } catch (Exception e) {
            // SERVER ERRORS (500) - Our fault
            logger.error("Failed to process order", e);
            handleServerError(request, response, 
                "Something went wrong on our end. Please try again later. " +
                "This is our fault, not yours!");
        }
    }
    
    // Validation Error (400) - User's fault
    private void handleValidationError(HttpServletRequest request, HttpServletResponse response, 
                                      String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);  // 400
        logger.info("Validation error: {}", message);
        
        HttpSession session = request.getSession();
        session.setAttribute("flash_error", message);
        session.setAttribute("flash_error_type", "validation");
        
        response.sendRedirect("/exercises-solution/order-product");
    }
    
    // Business Logic Error (409) - Not user's fault  
    private void handleBusinessError(HttpServletRequest request, HttpServletResponse response, 
                                    String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_CONFLICT);  // 409
        logger.warn("Business logic error: {}", message);
        
        HttpSession session = request.getSession();
        session.setAttribute("flash_error", message);
        session.setAttribute("flash_error_type", "business");
        
        response.sendRedirect("/exercises-solution/order-product");
    }
    
    // Server Error (500) - Our fault
    private void handleServerError(HttpServletRequest request, HttpServletResponse response, 
                                  String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);  // 500
        
        HttpSession session = request.getSession();
        session.setAttribute("flash_error", message);
        session.setAttribute("flash_error_type", "server");
        
        response.sendRedirect("/exercises-solution/order-product");
    }

    private String buildProductPage(String error, String errorType) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>");
        html.append("<html>");
        html.append("<head><title>Exercise 2 Solution</title>");
        html.append("<link rel='stylesheet' href='/static/css/styles.css'></head>");
        html.append("<body><div class='container'>");
        html.append("<h1>Exercise 2 Solution: Error Handling</h1>");
        
        if (error != null) {
            String boxClass = "error-box";
            String icon = "❌";
            String title = "Error";
            
            if ("validation".equals(errorType)) {
                boxClass = "error-box";
                icon = "❌";
                title = "Validation Error (400)";
            } else if ("business".equals(errorType)) {
                boxClass = "warning-box";
                icon = "⚠️";
                title = "Business Logic Error (409)";
            } else if ("server".equals(errorType)) {
                boxClass = "error-box";
                icon = "🔥";
                title = "Server Error (500)";
            }
            
            html.append("<div class='").append(boxClass).append("'>");
            html.append("<h3>").append(icon).append(" ").append(title).append("</h3>");
            html.append("<p>").append(error).append("</p>");
            html.append("</div>");
        }
        
        html.append("<div class='success-box'>");
        html.append("<h3>Proper Error Handling</h3>");
        html.append("<p>Try these scenarios:</p>");
        html.append("<ul>");
        html.append("<li>Enter 'abc' >><strong>Validation error (400)</strong></li>");
        html.append("<li>Enter 100 >><strong>Business error (409)</strong></li>");
        html.append("<li>Enter 2 >><strong>Success!</strong></li>");
        html.append("</ul>");
        html.append("</div>");
        
        html.append("<div class='product-card'>");
        html.append("<h3>Limited Item</h3>");
        html.append("<p class='price'>€19.99</p>");
        html.append("<p class='stock'>Stock: 3</p>");
        html.append("<form method='post' action='/exercises-solution/order-product'>");
        html.append("<input type='hidden' name='productId' value='limited'>");
        html.append("<label>Quantity:</label>");
        html.append("<input type='text' name='quantity' value='1' min='1'>");
        html.append("<button type='submit' class='btn btn-primary'>Order</button>");
        html.append("</form>");
        html.append("</div>");
        html.append("<p><a href='/'>Back</a></p>");
        html.append("</div></body></html>");
        
        return html.toString();
    }
}
