package org.tdd2;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.UUID;

public class OrderServiceTests {

    @Test
    public void WhenUserPlacesACorrectOrderThenAnOrderNumberShouldBeReturned() {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.getItems().add(new ShoppingCartItem(UUID.randomUUID(),1));

        UUID customerId = UUID.randomUUID();
        UUID expectedOrderId = UUID.randomUUID();

        final OrderDataServiceInterface mockedOrderDataService = Mockito.mock(OrderDataServiceInterface.class);
        Mockito.when(mockedOrderDataService.save(Mockito.any(Order.class))).thenReturn(expectedOrderId);
        OrderService orderService = new OrderService(mockedOrderDataService);
        UUID result = orderService.placeOrder(customerId,shoppingCart);

        Mockito.verify(mockedOrderDataService,Mockito.times(1)).save(Mockito.any(Order.class));
        Assert.assertEquals(expectedOrderId,result);
    }
}
