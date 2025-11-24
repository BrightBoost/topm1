package nl.topicus.shared.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Cart {
    private List<OrderItem> items;

    public Cart() {
        this.items = new ArrayList<>();
    }

    public List<OrderItem> getItems() { return items; }

    public void addItem(String productId, String productName, double price) {
        addItem(productId, productName, 1, price);
    }

    public void addItem(String productId, String productName, int quantity, double price) {
        Optional<OrderItem> existing = items.stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst();

        if (existing.isPresent()) {
            OrderItem item = existing.get();
            item.setQuantity(item.getQuantity() + quantity);
        } else {
            items.add(new OrderItem(productId, productName, quantity, price));
        }
    }

    public void removeItem(String productId) {
        items.removeIf(item -> item.getProductId().equals(productId));
    }

    public void clear() {
        items.clear();
    }

    public double getTotal() {
        return items.stream().mapToDouble(OrderItem::getSubtotal).sum();
    }

    public int getItemCount() {
        return items.stream().mapToInt(OrderItem::getQuantity).sum();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }
}
