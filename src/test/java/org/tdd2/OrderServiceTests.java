package org.tdd2;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.tdd2.exceptions.InvalidOrderException;

import java.util.UUID;

public class OrderServiceTests {

    private OrderService _orderService;
    private OrderDataServiceInterface _mockedOrderDataServiceInterface;
    private ShoppingCart _shoppingCart;

    @Before
    public void setUp() {
        _mockedOrderDataServiceInterface = Mockito.mock(OrderDataServiceInterface.class);
        _orderService = new OrderService(_mockedOrderDataServiceInterface);

        _shoppingCart = new ShoppingCart();
    }

    @Test
    public void WhenUserPlacesACorrectOrderThenAnOrderNumberShouldBeReturned() {
        _shoppingCart.getItems().add(new ShoppingCartItem(UUID.randomUUID(),1));
        UUID customerId = UUID.randomUUID();
        UUID expectedOrderId = UUID.randomUUID();
        Mockito.when(_mockedOrderDataServiceInterface.save(Mockito.any(Order.class))).thenReturn(expectedOrderId);

        UUID result = _orderService.placeOrder(customerId,_shoppingCart);

        Mockito.verify(_mockedOrderDataServiceInterface,Mockito.times(1)).save(Mockito.any(Order.class));
        Assert.assertEquals(expectedOrderId,result);
    }

    @Test(expected = InvalidOrderException.class)
    public void WhenUserAttemptsToOrderAnItemWithAQuantityOfZeroThrowInvalidOrderException() {
        _shoppingCart.getItems().add(new ShoppingCartItem(UUID.randomUUID(),0));
        UUID customerId = UUID.randomUUID();
        UUID expectedOrderId = UUID.randomUUID();
        Mockito.when(_mockedOrderDataServiceInterface.save(Mockito.any(Order.class))).thenReturn(expectedOrderId);

        _orderService.placeOrder(customerId,_shoppingCart);

        Mockito.verify(_mockedOrderDataServiceInterface,Mockito.never()).save(Mockito.any(Order.class));
    }
}
