package org.tdd2;

import java.util.Map;
import java.util.UUID;

public interface OrderFulfillmentServiceInterface {
    UUID openSession(String user,String password);
    boolean isInInventory(UUID sessionId, UUID ItemNumber, int quantity);
    boolean placeOrder(UUID sessionId, Map<UUID,Integer> items, String mailingAddress);
    void closeSession(UUID sessionId);
}
