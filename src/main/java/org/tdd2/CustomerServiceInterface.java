package org.tdd2;

import java.util.UUID;

public interface CustomerServiceInterface {
    Customer getCustomer(UUID customerId);
}
