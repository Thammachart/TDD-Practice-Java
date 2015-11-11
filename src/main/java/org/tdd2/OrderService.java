package org.tdd2;

import org.tdd2.exceptions.InvalidOrderException;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

public class OrderService {
    private OrderDataServiceInterface orderDataService;
    private CustomerServiceInterface customerService;
    private OrderFulfillmentServiceInterface orderFulfillmentService;

    private final static String OFS_USERNAME = "foo";
    private final static String OFS_PASSWORD = "bar";

    public OrderService(OrderDataServiceInterface orderDataServiceInterface,CustomerServiceInterface customerServiceInterface,OrderFulfillmentServiceInterface orderFulfillmentServiceInterface) {
        this.orderDataService = orderDataServiceInterface;
        this.customerService = customerServiceInterface;
        this.orderFulfillmentService = orderFulfillmentServiceInterface;
    }

    public UUID placeOrder(UUID customerId, ShoppingCart shoppingCart) {
        if(!shoppingCart.getItems().stream().allMatch(item -> item.getQuantity() != 0)) {
            throw new InvalidOrderException();
        }

        Customer customer = customerService.getCustomer(customerId);

        placeOrderWithFulfillmentService(shoppingCart, customer);

        Order order = new Order();
        return orderDataService.save(order);
    }

    private void placeOrderWithFulfillmentService(ShoppingCart shoppingCart, Customer customer) {
        UUID OFSSessionId = openOrderFulfillmentSession();

        placeOrderWithFulfillmentService(OFSSessionId, customer, shoppingCart);

        closeOrderFulfillmentService(OFSSessionId);
    }

    private void placeOrderWithFulfillmentService(UUID OFSSessionId, Customer customer, ShoppingCart shoppingCart) {
        Map<UUID, Integer> orderForOFS = CheckInventoryLevels(OFSSessionId, shoppingCart);
        orderFulfillmentService.placeOrder(OFSSessionId,orderForOFS,customer.getShippingAddress().toString());
    }

    private Map<UUID, Integer> CheckInventoryLevels(UUID OFSSessionId, ShoppingCart shoppingCart) {
        Map<UUID,Integer> orderForOFS = new TreeMap<>();

        for (ShoppingCartItem item : shoppingCart.getItems()) {
            orderFulfillmentService.isInInventory(OFSSessionId,item.getItemId(),item.getQuantity());
            orderForOFS.put(item.getItemId(),item.getQuantity());
        }

        return orderForOFS;
    }

    private void closeOrderFulfillmentService(UUID OFSSessionId) {
        orderFulfillmentService.closeSession(OFSSessionId);
    }

    private UUID openOrderFulfillmentSession() {
        return orderFulfillmentService.openSession(OFS_USERNAME,OFS_PASSWORD);
    }
}
