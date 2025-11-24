package nl.topicus.shared.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Order model - represents a customer order.
 */
public class Order {
    private Long id;
    private String customerName;
    private String email;
    private List<OrderItem> items;
    private LocalDateTime createdAt;
    private OrderStatus status;

    public Order() {
        this.items = new ArrayList<>();
        this.createdAt = LocalDateTime.now();
        this.status = OrderStatus.PENDING;
    }


    public Order(String customerName, String email) {
        this();
        this.customerName = customerName;
        this.email = email;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }
    public void addItem(OrderItem item) { this.items.add(item); }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }

    public double getTotal() {
        return items.stream().mapToDouble(item -> item.getPrice() * item.getQuantity()).sum();
    }

    public int getTotalItemCount() {
        return items.stream().mapToInt(OrderItem::getQuantity).sum();
    }
}
