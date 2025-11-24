package nl.topicus.demos.errors;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import nl.topicus.shared.model.Product;
import nl.topicus.shared.rendering.HtmlBuilder;
import nl.topicus.shared.rendering.TemplateRenderer;
import nl.topicus.shared.service.ProductService;

import java.io.IOException;

@WebServlet("/demos/product-page")
public class Demo05_ProductPageServlet extends HttpServlet {

    private final ProductService productService = ProductService.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String productId = request.getParameter("id");
        if (productId == null) {
            productId = "limited";  // Default to limited stock item
        }

        Product product = productService.getProduct(productId);
        if (product == null) {
            response.sendError(404, "Product not found");
            return;
        }

        HttpSession session = request.getSession();
        
        // Get flash error if any
        String errorMessage = (String) session.getAttribute("flash_error");
        String errorType = (String) session.getAttribute("flash_errorType");
        Integer requestedQuantity = (Integer) session.getAttribute("flash_requestedQuantity");
        
        // Remove flash data
        session.removeAttribute("flash_error");
        session.removeAttribute("flash_errorType");
        session.removeAttribute("flash_requestedQuantity");
        
        // Build error HTML
        String errorHtml = "";
        if (errorMessage != null) {
            if ("validation".equals(errorType)) {
                errorHtml = "<div class='error-box'><strong>Validation Error:</strong> " + 
                           HtmlBuilder.escape(errorMessage) + "</div>";
            } else if ("business".equals(errorType)) {
                errorHtml = "<div class='warning-box'><strong>Unable to Complete Order:</strong> " + 
                           HtmlBuilder.escape(errorMessage) + "</div>";
            } else {
                errorHtml = "<div class='error-box'><strong>Error:</strong> " + 
                           HtmlBuilder.escape(errorMessage) + "</div>";
            }
        }

        String quantity = requestedQuantity != null ? requestedQuantity.toString() : "1";

        String html = TemplateRenderer.render(
            "demos/product-page.html",
            TemplateRenderer.data()
                .put("error", errorHtml)
                .put("productId", product.getId())
                .put("productName", product.getName())
                .put("productPrice", String.format("%.2f", product.getPrice()))
                .put("productStock", String.valueOf(product.getStockLevel()))
                .put("quantity", quantity)
                .build()
        );

        response.setContentType("text/html; charset=UTF-8");
        response.getWriter().write(html);
    }
}
