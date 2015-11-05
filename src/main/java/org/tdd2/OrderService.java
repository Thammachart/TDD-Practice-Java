package org.tdd2;

import org.tdd2.exceptions.InvalidOrderException;

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

        UUID OFSSessionId = orderFulfillmentService.openSession(OFS_USERNAME,OFS_PASSWORD);

        ShoppingCartItem firstItem = shoppingCart.getItems().get(0);

        orderFulfillmentService.isInInventory(OFSSessionId,firstItem.getItemId(),firstItem.getQuantity());

        Map<UUID,Integer> orderForOFS = new TreeMap<>();
        orderForOFS.put(firstItem.getItemId(),firstItem.getQuantity());
        orderFulfillmentService.placeOrder(OFSSessionId,orderForOFS,customer.getShippingAddress().toString());

        orderFulfillmentService.closeSession(OFSSessionId);

        Order order = new Order();
        return orderDataService.save(order);
    }
}
