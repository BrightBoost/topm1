package nl.topicus.exercises.starter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nl.topicus.shared.model.Product;
import nl.topicus.shared.service.ProductService;

import java.io.IOException;

/**
 * EXERCISE 2: Add Servlet Error Handling
 * 
 * Problem: This servlet has NO error handling - it will crash on invalid input!
 * 
 * Your Task: Add proper error handling for:
 * 1. Validation errors (400) - Wrong input format
 * 2. Business logic errors (409) - Valid input but business rules prevent action
 * 3. Server errors (500) - Something broke
 * 
 * Example scenarios:
 * - Missing quantity → 400
 * - Quantity = "abc" → 400
 * - Quantity = 100 (more than stock) → 409
 * - Valid quantity → Success!
 */
@WebServlet("/exercises/order-product")
public class Exercise02_ErrorHandlingServlet extends HttpServlet {

    private final ProductService productService = ProductService.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String html = buildProductPage();
        response.setContentType("text/html; charset=UTF-8");
        response.getWriter().write(html);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // TODO: Add error handling!
        // This code will crash if quantity is missing or invalid
        
        String productId = request.getParameter("productId");
        String quantityStr = request.getParameter("quantity");
        
        // TODO: Validate parameters
        // if (quantityStr == null || quantityStr.isEmpty()) {
        //     handleValidationError(response, "Please enter a quantity");
        //     return;
        // }
        
        // TODO: Wrap in try-catch for NumberFormatException
        int quantity = Integer.parseInt(quantityStr);  // Can crash!
        
        Product product = productService.getProduct(productId);
        
        // TODO: Check if product exists
        // if (product == null) { ... }
        
        // TODO: Check stock (business logic error)
        if(!product.isInStock(quantity)) {
            throw new RuntimeException("Not that many in stock");
        }
        // if (!product.isInStock(quantity)) {
        //     handleBusinessError(response, "Only " + product.getStockLevel() + " in stock");
        //     return;
        // }
        
        // Success - redirect to success page
        response.sendRedirect("/exercises/order-success?productId=" + productId + "&quantity=" + quantity);
    }
    
    // TODO: Implement these helper methods
    
    // Validation Error (400) - User's fault
    private void handleValidationError(HttpServletResponse response, String message) 
            throws IOException {
        // Set status 400
        // Store error in session (flash pattern)
        // Redirect back to form
    }
    
    // Business Logic Error (409) - Not user's fault  
    private void handleBusinessError(HttpServletResponse response, String message) 
            throws IOException {
        // Set status 409
        // Store error in session (flash pattern)
        // Redirect back to form
    }
    
    // Server Error (500) - Our fault
    private void handleServerError(HttpServletResponse response, String message) 
            throws IOException {
        // Set status 500
        // Log the error
        // Store user-friendly message in session
        // Redirect back to form
    }

    private String buildProductPage() {
        return "<!DOCTYPE html>" +
               "<html>" +
               "<head><title>Exercise 2</title>" +
               "<link rel='stylesheet' href='/static/css/styles.css'></head>" +
               "<body><div class='container'>" +
               "<h1>Exercise 2: Error Handling</h1>" +
               "<div class='warning-box'>" +
               "<h3>⚠️ No Error Handling!</h3>" +
               "<p>Try these to crash the servlet:</p>" +
               "<ul>" +
               "<li>Enter 'abc' as quantity (NumberFormatException)</li>" +
               "<li>Enter 100 as quantity (more than stock)</li>" +
               "</ul>" +
               "</div>" +
               "<div class='product-card'>" +
               "<h3>Limited Item</h3>" +
               "<p class='price'>€19.99</p>" +
               "<p class='stock'>Stock: 3</p>" +
               "<form method='post' action='/exercises/order-product'>" +
               "<input type='hidden' name='productId' value='limited'>" +
               "<label>Quantity:</label>" +
               "<input type='text' name='quantity' value='1' min='1'>" +
               "<button type='submit' class='btn btn-primary'>Order</button>" +
               "</form>" +
               "</div>" +
               "<p><a href='/'>Back</a></p>" +
               "</div></body></html>";
    }
}
