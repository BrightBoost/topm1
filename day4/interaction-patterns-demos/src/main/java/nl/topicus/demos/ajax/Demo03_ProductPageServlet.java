package nl.topicus.demos.ajax;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import nl.topicus.shared.model.Cart;
import nl.topicus.shared.model.Product;
import nl.topicus.shared.rendering.TemplateRenderer;
import nl.topicus.shared.service.ProductService;

import java.io.IOException;
import java.util.Map;

/**
 * Product catalog page with AJAX cart functionality.
 * 
 * This demonstrates:
 * - A complete page using AJAX
 * - Progressive enhancement (works without JS)
 * - How to structure AJAX-enabled pages
 * 
 * URL: /demos/cart
 */
@WebServlet("/demos/cart")
public class Demo03_ProductPageServlet extends HttpServlet {

    private final ProductService productService = ProductService.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Get cart from session
        HttpSession session = request.getSession();
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
            session.setAttribute("cart", cart);
        }

        // Build product list HTML
        StringBuilder productsHtml = new StringBuilder();
        Map<String, Product> products = productService.getAllProducts();
        
        for (Product product : products.values()) {
            productsHtml.append("<div class='product-card'>");
            productsHtml.append("<h4>").append(product.getName()).append("</h4>");
            productsHtml.append("<p class='price'>€").append(String.format("%.2f", product.getPrice())).append("</p>");
            productsHtml.append("<p class='stock'>Stock: ").append(product.getStockLevel()).append("</p>");
            
            // Form for adding to cart
            productsHtml.append("<form class='add-to-cart-form' action='/demos/cart/add' method='post'>");
            productsHtml.append("<input type='hidden' name='productId' value='").append(product.getId()).append("'>");
            productsHtml.append("<button type='submit' class='btn btn-primary'>Add to Cart</button>");
            productsHtml.append("</form>");
            
            productsHtml.append("</div>");
        }

        // Build initial cart display
        String cartHtml = buildCartHtml(cart);

        // Render the page
        String html = TemplateRenderer.render(
            "demos/ajax-cart.html",
            TemplateRenderer.data()
                .put("products", productsHtml.toString())
                .put("cart", cartHtml)
                .build()
        );

        response.setContentType("text/html; charset=UTF-8");
        response.getWriter().write(html);
    }

    private String buildCartHtml(Cart cart) {
        StringBuilder html = new StringBuilder();
        html.append("<div class='cart-display'>");
        html.append("<h3>Shopping Cart</h3>");
        
        if (cart.isEmpty()) {
            html.append("<p><em>Your cart is empty</em></p>");
        } else {
            html.append("<div class='cart-items'>");
            for (var item : cart.getItems()) {
                html.append("<div class='cart-item'>");
                html.append("<span>").append(item.getProductName()).append("</span>");
                html.append(" <span>").append(item.getQuantity()).append("x €")
                    .append(String.format("%.2f", item.getPrice())).append("</span>");
                html.append("</div>");
            }
            html.append("</div>");
            html.append("<div class='cart-total'><strong>Total: €")
                .append(String.format("%.2f", cart.getTotal())).append("</strong></div>");
        }
        
        html.append("</div>");
        return html.toString();
    }
}
