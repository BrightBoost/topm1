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
 * Success page for Exercise 2 - error handling
 */
@WebServlet("/exercises/order-success")
public class Exercise02_OrderSuccessServlet extends HttpServlet {

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
                     "<h1>Exercise 2: Order Success!</h1>" +
                     "<div class='success-box'>" +
                     "<h3>Order Placed Successfully!</h3>" +
                     "<p><strong>Product:</strong> " + (product != null ? product.getName() : productId) + "</p>" +
                     "<p><strong>Quantity:</strong> " + quantityStr + "</p>" +
                     "</div>" +
                     "<div class='info-box'>" +
                     "<h4>What Happened:</h4>" +
                     "<ul>" +
                     "<li>Input validation passed</li>" +
                     "<li>Business logic validation passed (stock available)</li>" +
                     "<li>Order created successfully</li>" +
                     "<li>No errors occurred!</li>" +
                     "</ul>" +
                     "</div>" +
                     "<p><a href='/exercises/order-product' class='btn btn-secondary'>Order Again</a> " +
                     "<a href='/' class='btn btn-secondary'>Back</a></p>" +
                     "</div></body></html>";

        response.setContentType("text/html; charset=UTF-8");
        response.getWriter().write(html);
    }
}
