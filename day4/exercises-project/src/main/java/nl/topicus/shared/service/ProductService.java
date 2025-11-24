package nl.topicus.shared.service;

import nl.topicus.shared.model.Product;
import java.util.HashMap;
import java.util.Map;

public class ProductService {
    private static final ProductService INSTANCE = new ProductService();
    private final Map<String, Product> products = new HashMap<>();

    private ProductService() {
        products.put("burger", new Product("burger", "Classic Burger", 8.99, 50));
        products.put("pizza", new Product("pizza", "Margherita Pizza", 12.99, 30));
        products.put("pasta", new Product("pasta", "Spaghetti Carbonara", 10.99, 25));
        products.put("salad", new Product("salad", "Caesar Salad", 7.99, 40));
        products.put("limited", new Product("limited", "Limited Item", 19.99, 3));
    }

    public static ProductService getInstance() { return INSTANCE; }
    public Product getProduct(String id) { return products.get(id); }
    public Map<String, Product> getAllProducts() { return new HashMap<>(products); }
    
    public void reduceStock(String productId, int quantity) {
        Product product = products.get(productId);
        if (product != null) {
            int newStock = product.getStockLevel() - quantity;
            product.setStockLevel(Math.max(0, newStock));
        }
    }
}
