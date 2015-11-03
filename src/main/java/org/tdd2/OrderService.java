package org.tdd2;

import org.tdd2.exceptions.InvalidOrderException;

import java.util.UUID;

public class OrderService {
    private OrderDataServiceInterface _orderDataService;

    public OrderService(OrderDataServiceInterface orderDataServiceInterface) {
        this._orderDataService = orderDataServiceInterface;
    }

    public UUID placeOrder(UUID customerId, ShoppingCart shoppingCart) {
        if(!shoppingCart.getItems().stream().allMatch(item -> item.getQuantity() != 0)) {
            throw new InvalidOrderException();
        }

        Order order = new Order();
        return _orderDataService.save(order);
    }
}
