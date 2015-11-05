package org.tdd2;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.mockito.stubbing.OngoingStubbing;
import org.tdd2.exceptions.InvalidOrderException;

import java.util.Map;
import java.util.UUID;

public class OrderServiceTests {

    private OrderService _orderService;
    private OrderDataServiceInterface _mockedOrderDataServiceInterface;
    private CustomerServiceInterface _mockedCustomerServiceInterface;
    private OrderFulfillmentServiceInterface _mockedOrderFulfillmentServiceInterface;
    private ShoppingCart _shoppingCart;

    @Before
    public void setUp() {
        _mockedOrderDataServiceInterface = Mockito.mock(OrderDataServiceInterface.class);
        _mockedCustomerServiceInterface = Mockito.mock(CustomerServiceInterface.class);
        _mockedOrderFulfillmentServiceInterface = Mockito.mock(OrderFulfillmentServiceInterface.class);
        _orderService = new OrderService(_mockedOrderDataServiceInterface,_mockedCustomerServiceInterface,_mockedOrderFulfillmentServiceInterface);

        _shoppingCart = new ShoppingCart();
    }

    @Test
    public void WhenUserPlacesACorrectOrderThenAnOrderNumberShouldBeReturned() {
        _shoppingCart.getItems().add(new ShoppingCartItem(UUID.randomUUID(),1));
        UUID customerId = UUID.randomUUID();
        UUID expectedOrderId = UUID.randomUUID();
        Mockito.when(_mockedOrderDataServiceInterface.save(Mockito.any(Order.class))).thenReturn(expectedOrderId);

        Customer customer = new Customer();
        customer.setId(customerId);
        Mockito.when(_mockedCustomerServiceInterface.getCustomer(customerId)).thenReturn(customer);

        UUID result = _orderService.placeOrder(customerId,_shoppingCart);

        Mockito.verify(_mockedOrderDataServiceInterface,Mockito.times(1)).save(Mockito.any(Order.class));
        Assert.assertEquals(expectedOrderId,result);
    }

    @Test
    public void WhenUserAttemptsToOrderAnItemWithAQuantityOfZeroThrowInvalidOrderException() {
        _shoppingCart.getItems().add(new ShoppingCartItem(UUID.randomUUID(),0));
        UUID customerId = UUID.randomUUID();
        UUID expectedOrderId = UUID.randomUUID();
        Mockito.when(_mockedOrderDataServiceInterface.save(Mockito.any(Order.class))).thenReturn(expectedOrderId);

        try {
            _orderService.placeOrder(customerId, _shoppingCart);
        } catch (InvalidOrderException e) {
            Mockito.verify(_mockedOrderDataServiceInterface,Mockito.never()).save(Mockito.any(Order.class));
            return;
        }

        Assert.fail();
    }

    @Test
    public void WhenValidCustomerPlacesAValidOrderTheOrderServiceShouldBeAbleToGetACustomerFromTheCustomerService() {
        _shoppingCart.getItems().add(new ShoppingCartItem(UUID.randomUUID(),1));
        UUID customerId = UUID.randomUUID();
        Customer customerToReturn = new Customer();
        customerToReturn.setId(customerId);
        customerToReturn.setFirstName("Fred");
        customerToReturn.setLastName("Flinstone");
        Mockito.when(_mockedCustomerServiceInterface.getCustomer(customerId)).thenReturn(customerToReturn);

        _orderService.placeOrder(customerId,_shoppingCart);

        Mockito.verify(_mockedCustomerServiceInterface,Mockito.times(1)).getCustomer(customerId);
    }

    @Test
    public void WhenUserPlacesOrderWithItemThatIsInInventoryOrderFulfillmentWorkflowShouldComplete() {
        UUID itemId = UUID.randomUUID();
        _shoppingCart.getItems().add(new ShoppingCartItem(itemId,1));

        UUID customerId = UUID.randomUUID();
        Customer customer = new Customer();
        customer.setId(customerId);
        UUID orderFulfillmentSessionId = UUID.randomUUID();

        Mockito.when(_mockedCustomerServiceInterface.getCustomer(customerId))
                .thenReturn(customer);

        Mockito.when(_mockedOrderFulfillmentServiceInterface.openSession(Mockito.anyString(),Mockito.anyString()))
                .thenReturn(orderFulfillmentSessionId);
        Mockito.when(_mockedOrderFulfillmentServiceInterface.isInInventory(orderFulfillmentSessionId, itemId, 1))
                .thenReturn(true);
        Mockito.when(_mockedOrderFulfillmentServiceInterface.placeOrder(Mockito.eq(orderFulfillmentSessionId),Mockito.anyMapOf(UUID.class,Integer.class),Mockito.anyString()))
                .thenReturn(true);

        final InOrder inOrder = Mockito.inOrder(_mockedOrderFulfillmentServiceInterface);

        _orderService.placeOrder(customerId,_shoppingCart);

        inOrder.verify(_mockedOrderFulfillmentServiceInterface).openSession(Mockito.anyString(),Mockito.anyString());
        inOrder.verify(_mockedOrderFulfillmentServiceInterface).isInInventory(orderFulfillmentSessionId, itemId, 1);
        inOrder.verify(_mockedOrderFulfillmentServiceInterface).placeOrder(Mockito.eq(orderFulfillmentSessionId),Mockito.anyMapOf(UUID.class,Integer.class),Mockito.anyString());
        inOrder.verify(_mockedOrderFulfillmentServiceInterface).closeSession(orderFulfillmentSessionId);
    }
}
