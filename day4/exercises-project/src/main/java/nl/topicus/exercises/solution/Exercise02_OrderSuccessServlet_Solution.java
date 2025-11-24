package nl.topicus.exercises.solution;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nl.topicus.shared.model.Product;
import nl.topicus.shared.service.ProductService;

import java.io.IOException;

/**
 * Success page for Exercise 2 Solution
 */
@WebServlet("/exercises-solution/order-success")
public class Exercise02_OrderSuccessServlet_Solution extends HttpServlet {

    private final ProductService productService = ProductService.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String productId = request.getParameter("productId");
        String quantityStr = request.getParameter("quantity");
        
        Product product = productService.getProduct(productId);
        
        String html = "<!DOCTYPE html>" +
                     "<html>" +
                     "<head><title>Order Success</title>" +
                     "<link rel='stylesheet' href='/static/css/styles.css'></head>" +
                     "<body><div class='container'>" +
                     "<h1>Exercise 2 Solution: Order Success!</h1>" +
                     "<div class='success-box'>" +
                     "<h3>Order Placed Successfully!</h3>" +
                     "<p><strong>Product:</strong> " + (product != null ? product.getName() : productId) + "</p>" +
                     "<p><strong>Quantity:</strong> " + quantityStr + "</p>" +
                     "</div>" +
                     "<div class='info-box'>" +
                     "<h4>Error Handling Success:</h4>" +
                     "<ul>" +
                     "<li>Validation errors (400) - Caught and handled</li>" +
                     "<li>Business logic errors (409) - Caught and handled</li>" +
                     "<li>Server errors (500) - Would be caught and logged</li>" +
                     "<li>This time everything worked!</li>" +
                     "</ul>" +
                     "<p><strong>Different error types handled differently!</strong></p>" +
                     "</div>" +
                     "<p><a href='/exercises-solution/order-product' class='btn btn-secondary'>Order Again</a> " +
                     "<a href='/' class='btn btn-secondary'>Back</a></p>" +
                     "</div></body></html>";

        response.setContentType("text/html; charset=UTF-8");
        response.getWriter().write(html);
    }
}
