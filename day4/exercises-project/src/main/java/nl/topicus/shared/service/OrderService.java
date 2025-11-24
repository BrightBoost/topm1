package nl.topicus.shared.service;

import nl.topicus.shared.model.Order;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class OrderService {
    private static final OrderService INSTANCE = new OrderService();
    private final Map<Long, Order> orders = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    private OrderService() {}
    public static OrderService getInstance() { return INSTANCE; }

    public Order createOrder(Order order) {
        Long id = idGenerator.getAndIncrement();
        order.setId(id);
        orders.put(id, order);
        return order;
    }

    public Order getOrder(Long id) {
        return orders.get(id);
    }

    public void clearAll() {
        orders.clear();
        idGenerator.set(1);
    }
}
