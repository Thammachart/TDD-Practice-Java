package org.tdd2;

import java.util.UUID;

public class OrderService {
    private OrderDataServiceInterface _orderDataService;

    public OrderService(OrderDataServiceInterface orderDataServiceInterface) {
        this._orderDataService = orderDataServiceInterface;
    }

    public UUID placeOrder(UUID customerId, ShoppingCart shoppingCart) {
        Order order = new Order();
        return _orderDataService.save(order);
    }
}
