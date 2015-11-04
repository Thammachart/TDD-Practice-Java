package org.tdd2;

import org.tdd2.exceptions.InvalidOrderException;

import java.util.UUID;

public class OrderService {
    private OrderDataServiceInterface _orderDataService;
    private CustomerServiceInterface _customerServiceInterface;

    public OrderService(OrderDataServiceInterface orderDataServiceInterface,CustomerServiceInterface customerServiceInterface) {
        this._orderDataService = orderDataServiceInterface;
        this._customerServiceInterface = customerServiceInterface;
    }

    public UUID placeOrder(UUID customerId, ShoppingCart shoppingCart) {
        if(!shoppingCart.getItems().stream().allMatch(item -> item.getQuantity() != 0)) {
            throw new InvalidOrderException();
        }

        Customer customer = _customerServiceInterface.getCustomer(customerId);

        Order order = new Order();
        return _orderDataService.save(order);
    }
}
