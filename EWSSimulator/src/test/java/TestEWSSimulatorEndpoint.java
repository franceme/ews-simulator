import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import ewsSimulator.ws.DetokenizeRequest;
import ewsSimulator.ws.DetokenizeResponse;
import ewsSimulator.ws.EWSSimulatorEndpoint;
import ewsSimulator.ws.EWSUtils;
import ewsSimulator.ws.MerchantType;
import ewsSimulator.ws.OrderDeregistrationRequest;
import ewsSimulator.ws.OrderDeregistrationResponse;


public class TestEWSSimulatorEndpoint {

    EWSSimulatorEndpoint ewsSimulatorEndpoint;
    private EWSUtils testUtils;
    private String requestId;
    private String token;
    private String PAN;
    private String CVV;
    private String expirationDate;
    private String rollupId;


    @Before
    public void setup() {
        ewsSimulatorEndpoint = new EWSSimulatorEndpoint();
        requestId = "f75b9c0f-5348-4621-acdc-a00861b25697";
        token = "468498435168468";
        PAN = "615348948648468";
        CVV = "468";
        expirationDate = "2308";
        rollupId = "1123";
    }

    @Test
    public void testDetokenize_success_with_ExpirationDateRequested_CVV2Requested() throws InterruptedException {
        DetokenizeRequest detokenizeRequest = new DetokenizeRequest();
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(requestId);
        detokenizeRequest.setMerchant(merchant);
        detokenizeRequest.setToken(token);
        detokenizeRequest.setCVV2Requested(true);
        detokenizeRequest.setExpirationDateRequested(true);

        DetokenizeResponse testResponse = ewsSimulatorEndpoint.detokenize(detokenizeRequest);

        assertEquals(PAN,testResponse.getPrimaryAccountNumber());
        assertNotNull(testResponse.getRequestId());
        assertEquals(CVV,testResponse.getCardSecurityCode());
        assertEquals(expirationDate,testResponse.getExpirationDate());
    }

    @Test
    public void testDetokenize_success_without_ExpirationDateRequested_CVV2Requested() throws InterruptedException {
        DetokenizeRequest detokenizeRequest = new DetokenizeRequest();
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);
        detokenizeRequest.setMerchant(merchant);
        detokenizeRequest.setToken(token);
        detokenizeRequest.setCVV2Requested(false);
        detokenizeRequest.setExpirationDateRequested(false);

        DetokenizeResponse testResponse = ewsSimulatorEndpoint.detokenize(detokenizeRequest);

        assertEquals(PAN,testResponse.getPrimaryAccountNumber());
        assertNotNull(testResponse.getRequestId());
        assertNull(testResponse.getCardSecurityCode());
        assertNull(testResponse.getExpirationDate());
    }

    @Test
    public void testOrderDeregistration_CVVInitialNot3() throws InterruptedException {
        OrderDeregistrationRequest orderDeregistrationRequest = new OrderDeregistrationRequest();
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);
        orderDeregistrationRequest.setMerchant(merchant);
        orderDeregistrationRequest.setOrderLVT(CVV);
        orderDeregistrationRequest.setToken(token);

        OrderDeregistrationResponse testResponse = ewsSimulatorEndpoint.orderDeregistration(orderDeregistrationRequest);

        assertEquals(PAN,testResponse.getPrimaryAccountNumber());
        assertNotNull(testResponse.getRequestId());
    }

    @Test
    public void testOrderDeregistration_CVVInitial3() throws InterruptedException {
        OrderDeregistrationRequest orderDeregistrationRequest = new OrderDeregistrationRequest();
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);
        orderDeregistrationRequest.setMerchant(merchant);
        orderDeregistrationRequest.setOrderLVT("303");
        orderDeregistrationRequest.setToken(token);

        OrderDeregistrationResponse testResponse = ewsSimulatorEndpoint.orderDeregistration(orderDeregistrationRequest);

        assertEquals(PAN,testResponse.getPrimaryAccountNumber());
        assertNotNull(testResponse.getRequestId());
        assertEquals("303",testResponse.getCardSecurityCode());
    }

    @Test
    public void testOrderDeregistration_CVVInitial3EndWith6() throws InterruptedException {
        OrderDeregistrationRequest orderDeregistrationRequest = new OrderDeregistrationRequest();
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);
        orderDeregistrationRequest.setMerchant(merchant);
        orderDeregistrationRequest.setOrderLVT("306");
        orderDeregistrationRequest.setToken(token);

        OrderDeregistrationResponse testResponse = ewsSimulatorEndpoint.orderDeregistration(orderDeregistrationRequest);

        assertEquals(9999,(int)testResponse.getError().get(0).getId());
        assertTrue("GENERIC CHECKOUT_ID ERROR".equals(testResponse.getError().get(0).getMessage()));
        assertNotNull(testResponse.getRequestId());
        assertEquals(PAN,testResponse.getPrimaryAccountNumber());
    }

    @Test
    public void testOrderDeregistration_CVVInitial3EndWith7() throws InterruptedException {
        OrderDeregistrationRequest orderDeregistrationRequest = new OrderDeregistrationRequest();
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);
        orderDeregistrationRequest.setMerchant(merchant);
        orderDeregistrationRequest.setOrderLVT("307");
        orderDeregistrationRequest.setToken(token);

        OrderDeregistrationResponse testResponse = ewsSimulatorEndpoint.orderDeregistration(orderDeregistrationRequest);

        assertEquals(2,(int)testResponse.getError().get(0).getId());
        assertTrue("GENERIC CHECKOUT_ID ERROR".equals(testResponse.getError().get(0).getMessage()));
        assertNotNull(testResponse.getRequestId());
        assertEquals(PAN,testResponse.getPrimaryAccountNumber());
    }

    @Test
    public void testOrderDeregistration_CVVInitial3EndWith8() throws InterruptedException {
        OrderDeregistrationRequest orderDeregistrationRequest = new OrderDeregistrationRequest();
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);
        orderDeregistrationRequest.setMerchant(merchant);
        orderDeregistrationRequest.setOrderLVT("308");
        orderDeregistrationRequest.setToken(token);

        OrderDeregistrationResponse testResponse = ewsSimulatorEndpoint.orderDeregistration(orderDeregistrationRequest);

        assertEquals(4,(int)testResponse.getError().get(0).getId());
        assertTrue("CHECKOUT_ID INVALID".equals(testResponse.getError().get(0).getMessage()));
        assertNotNull(testResponse.getRequestId());
        assertEquals(PAN,testResponse.getPrimaryAccountNumber());
    }

    @Test
    public void testOrderDeregistration_CVInitial3VEndWith9() throws InterruptedException {
        OrderDeregistrationRequest orderDeregistrationRequest = new OrderDeregistrationRequest();
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);
        orderDeregistrationRequest.setMerchant(merchant);
        orderDeregistrationRequest.setOrderLVT("309");
        orderDeregistrationRequest.setToken(token);

        OrderDeregistrationResponse testResponse = ewsSimulatorEndpoint.orderDeregistration(orderDeregistrationRequest);

        assertEquals(6,(int)testResponse.getError().get(0).getId());
        assertTrue("CHECKOUT_ID NOT_FOUND".equals(testResponse.getError().get(0).getMessage()));
        assertNotNull(testResponse.getRequestId());
        assertEquals(PAN,testResponse.getPrimaryAccountNumber());
    }
}
