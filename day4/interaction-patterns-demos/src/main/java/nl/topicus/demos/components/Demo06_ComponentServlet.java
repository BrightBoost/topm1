package nl.topicus.demos.components;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nl.topicus.shared.model.Product;
import nl.topicus.shared.service.ProductService;

import java.io.IOException;
import java.util.Map;

/**
 * DEMO 6: Component-Based Rendering.
 * 
 * This demonstrates:
 * 1. Building HTML using component classes (like Wicket)
 * 2. Component hierarchy and composition
 * 3. Reusable components
 * 4. How Wicket's component model works
 * 
 * Teaching points:
 * - Components encapsulate rendering logic
 * - Components can contain other components (hierarchy)
 * - Components are reusable
 * - This is the foundation of Wicket's architecture!
 * 
 * Compare to Wicket:
 * - Wicket: add(new Label("id", "text"))
 * - Here: We create Label, Form, Panel classes manually
 * - Wicket: Component tree automatically renders
 * - Here: We manually call render()
 * 
 * Component hierarchy example:
 * Page
 * ├── HeaderPanel
 * │   └── Label (title)
 * ├── ProductPanel
 * │   ├── Label (name)
 * │   ├── Label (price)
 * │   └── Form
 * │       ├── HiddenField (productId)
 * │       └── Button (submit)
 * └── FooterPanel
 *     └── Label (copyright)
 * 
 * URL: /demos/components
 */
@WebServlet("/demos/components")
public class Demo06_ComponentServlet extends HttpServlet {

    private final ProductService productService = ProductService.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Build page using components (like Wicket does!)
        Page page = new Page("Product Catalog");
        
        // Add header
        page.add(new HeaderPanel("Our Products"));
        
        // Add products using reusable ProductCard components
        Map<String, Product> products = productService.getAllProducts();
        for (Product product : products.values()) {
            page.add(new ProductCard(product));
        }
        
        // Add footer
        page.add(new FooterPanel());
        
        // Render the entire component tree
        String html = page.render();
        
        response.setContentType("text/html; charset=UTF-8");
        response.getWriter().write(html);
    }
}

/**
 * Base component class - like Wicket's Component.
 * All components extend this.
 */
abstract class Component {
    protected String id;
    
    public Component(String id) {
        this.id = id;
    }
    
    /**
     * Render this component to HTML.
     * Like Wicket's onRender() method.
     */
    public abstract String render();
    
    /**
     * Escape HTML to prevent XSS.
     * Wicket does this automatically!
     */
    protected String escape(String text) {
        if (text == null) return "";
        return text
            .replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;");
    }
}

/**
 * Page component - like Wicket's WebPage.
 * Contains other components.
 */
class Page extends Component {
    private final String title;
    private final java.util.List<Component> components = new java.util.ArrayList<>();
    
    public Page(String title) {
        super("page");
        this.title = title;
    }
    
    /**
     * Add a component to this page.
     * Like Wicket's add() method!
     */
    public void add(Component component) {
        components.add(component);
    }
    
    @Override
    public String render() {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>");
        html.append("<html>");
        html.append("<head>");
        html.append("<title>").append(escape(title)).append("</title>");
        html.append("<link rel='stylesheet' href='/static/css/styles.css'>");
        html.append("<style>");
        html.append(".product-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(250px, 1fr)); gap: 20px; margin: 20px 0; }");
        html.append(".product-card { border: 1px solid #ddd; padding: 20px; border-radius: 8px; background: #f9f9f9; }");
        html.append("</style>");
        html.append("</head>");
        html.append("<body>");
        html.append("<div class='container'>");
        
        // Render all child components
        for (Component component : components) {
            html.append(component.render());
        }
        
        html.append("</div>");
        html.append("</body>");
        html.append("</html>");
        return html.toString();
    }
}

/**
 * Panel component - like Wicket's Panel.
 * A reusable container for other components.
 */
class HeaderPanel extends Component {
    private final String title;
    
    public HeaderPanel(String title) {
        super("header");
        this.title = title;
    }
    
    @Override
    public String render() {
        StringBuilder html = new StringBuilder();
        html.append("<header>");
        html.append("<h1>").append(escape(title)).append("</h1>");
        html.append("<div class='info-box'>");
        html.append("<strong>This page is built using components!</strong>");
        html.append("<p>Each product card is a reusable ProductCard component.</p>");
        html.append("<p><strong>This is exactly how Wicket works:</strong></p>");
        html.append("<ul>");
        html.append("<li>Page contains Panels</li>");
        html.append("<li>Panels contain Labels, Forms, Buttons</li>");
        html.append("<li>Each component renders itself</li>");
        html.append("<li>Component tree builds the final HTML</li>");
        html.append("</ul>");
        html.append("</div>");
        html.append("</header>");
        return html.toString();
    }
}

/**
 * ProductCard component - a reusable component for displaying products.
 * Like creating a custom Panel in Wicket.
 */
class ProductCard extends Component {
    private final Product product;
    
    public ProductCard(Product product) {
        super("product-" + product.getId());
        this.product = product;
    }
    
    @Override
    public String render() {
        StringBuilder html = new StringBuilder();
        html.append("<div class='product-card'>");
        
        // Product name (like a Label component)
        html.append("<h3>").append(escape(product.getName())).append("</h3>");
        
        // Price (like another Label)
        html.append("<p class='price'>€").append(String.format("%.2f", product.getPrice())).append("</p>");
        
        // Stock level (conditional rendering, like Wicket's visibility)
        if (product.getStockLevel() > 10) {
            html.append("<p class='stock' style='color: green;'>✓ In stock</p>");
        } else if (product.getStockLevel() > 0) {
            html.append("<p class='stock' style='color: orange;'>⚠ Only ").append(product.getStockLevel()).append(" left!</p>");
        } else {
            html.append("<p class='stock' style='color: red;'>✗ Out of stock</p>");
        }
        
        // Buy button (like a Button component in a Form)
        if (product.getStockLevel() > 0) {
            html.append("<form action='/demos/order-product2' method='post'>");
            html.append("<input type='hidden' name='productId' value='").append(escape(product.getId())).append("'>");
            html.append("<input type='number' name='quantity' value='1' min='1' max='")
                .append(product.getStockLevel()).append("' style='width: 60px;'>");
            html.append("<button type='submit' class='btn btn-primary'>Add to Cart</button>");
            html.append("</form>");
        } else {
            html.append("<button class='btn btn-secondary' disabled>Out of Stock</button>");
        }
        
        html.append("</div>");
        return html.toString();
    }
}

/**
 * Footer component.
 */
class FooterPanel extends Component {
    public FooterPanel() {
        super("footer");
    }
    
    @Override
    public String render() {
        return "<footer><div class='info-box' style='margin-top: 30px;'>" +
               "<h4>Understanding Component-Based Rendering:</h4>" +
               "<p>This entire page was built by creating component objects and calling render():</p>" +
               "<pre><code>Page page = new Page(\"Products\");\n" +
               "page.add(new HeaderPanel(\"title\"));\n" +
               "page.add(new ProductCard(product1));\n" +
               "page.add(new ProductCard(product2));\n" +
               "page.add(new FooterPanel());\n" +
               "String html = page.render();</code></pre>" +
               "<p><strong>Wicket does the exact same thing!</strong></p>" +
               "<p>When you do <code>add(new Label(\"id\", \"text\"))</code>, " +
               "Wicket stores that component and later calls its render method.</p>" +
               "</div>" +
               "<p><a href='/'>← Back to demos</a></p></footer>";
    }
}
