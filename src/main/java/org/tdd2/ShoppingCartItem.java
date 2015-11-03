package org.tdd2;

import java.util.UUID;

public class ShoppingCartItem {

    private UUID itemId;
    private int quantity;

    public ShoppingCartItem(UUID itemId, int quantity) {
        this.itemId = itemId;
        this.quantity = quantity;
    }

    public UUID getItemId() {
        return itemId;
    }

    public void setItemId(UUID itemId) {
        this.itemId = itemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
