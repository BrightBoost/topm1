package nl.topicus.demos.ajax;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import nl.topicus.shared.model.Cart;
import nl.topicus.shared.model.Product;
import nl.topicus.shared.rendering.HtmlBuilder;
import nl.topicus.shared.service.ProductService;

import java.io.IOException;

/**
 * DEMO 3: AJAX returning HTML fragments (partial page updates).
 *
 * This demonstrates:
 * 1. How AJAX can update parts of a page without full reload
 * 2. Returning HTML fragments (not JSON!)
 * 3. Progressive enhancement (works with and without JavaScript)
 * 4. How Wicket's AJAX works internally
 *
 * Teaching points:
 * - Server can return just a piece of HTML
 * - JavaScript inserts it into the page
 * - No page reload needed!
 * - This is exactly what Wicket's AjaxLink does
 *
 * Compare to Wicket:
 * - Wicket: AjaxLink.onClick() → component.render()
 * - Here: We manually detect AJAX and return HTML fragment
 *
 * URL: /demos/cart/add
 */
@WebServlet("/demos/cart/add")
public class Demo03_AjaxCartServlet extends HttpServlet {

    private final ProductService productService = ProductService.getInstance();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("=== AJAX Cart Servlet ===");
        System.out.println("Received POST request to /demos/cart/add");

        // Get or create cart in session (like Wicket's session)
        HttpSession session = request.getSession();
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
            session.setAttribute("cart", cart);
            System.out.println("Created new cart");
        } else {
            System.out.println("Using existing cart with " + cart.getItemCount() + " items");
        }

        // Get product and add to cart
        String productId = request.getParameter("productId");
        System.out.println("Product ID: " + productId);

        Product product = productService.getProduct(productId);

        if (product != null) {
            cart.addItem(productId, product.getName(), product.getPrice());
            System.out.println("Added " + product.getName() + " to cart");
        } else {
            System.out.println("WARNING: Product not found: " + productId);
        }

        // Detect if this is an AJAX request
        // This is how we distinguish between AJAX and traditional requests
        String ajaxHeader = request.getHeader("X-Requested-With");
        boolean isAjax = "XMLHttpRequest".equals(ajaxHeader);
        System.out.println("Is AJAX request: " + isAjax + " (header: " + ajaxHeader + ")");

        if (isAjax) {
            // AJAX request - return just the cart HTML fragment
            // This is what Wicket does with AjaxRequestTarget.add()
            String cartHtml = renderCartFragment(cart);
            System.out.println("Returning cart HTML fragment, length: " + cartHtml.length());

            response.setContentType("text/html; charset=UTF-8");
            response.getWriter().write(cartHtml);

            // Teaching point: We're returning ONLY the cart HTML, not a full page!
            // JavaScript will insert this into the existing page

        } else {
            // Traditional request - use PRG pattern
            // This ensures the page works even without JavaScript!
            System.out.println("Not AJAX, redirecting to /demos/cart");
            response.sendRedirect("/demos/cart");
        }
    }

    /**
     * Render the cart as an HTML fragment.
     *
     * This is similar to how Wicket components render themselves.
     * In Wicket, when you do target.add(cartPanel), Wicket calls
     * cartPanel.render() and returns that HTML.
     */
    private String renderCartFragment(Cart cart) {
        StringBuilder html = new StringBuilder();

        html.append("<div class='cart-display'>");
        html.append("<h3>🛒 Shopping Cart</h3>");

        if (cart.isEmpty()) {
            html.append("<p><em>Your cart is empty</em></p>");
        } else {
            html.append("<div class='cart-items'>");
            for (var item : cart.getItems()) {
                html.append("<div class='cart-item'>");
                html.append("<span class='item-name'>").append(HtmlBuilder.escape(item.getProductName())).append("</span>");
                html.append(" <span class='item-quantity'>").append(item.getQuantity()).append("x</span>");
                html.append(" <span class='item-price'>€").append(String.format("%.2f", item.getPrice())).append("</span>");
                html.append("</div>");
            }
            html.append("</div>");

            html.append("<div class='cart-total'>");
            html.append("<strong>Total: €").append(String.format("%.2f", cart.getTotal())).append("</strong>");
            html.append(" (").append(cart.getItemCount()).append(" items)");
            html.append("</div>");
        }

        html.append("</div>");

        return html.toString();
    }
}

