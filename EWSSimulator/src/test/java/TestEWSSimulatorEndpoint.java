import ewsSimulator.ws.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.List;


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


    @Test
    public void testTokenize_simple() {
        TokenizeRequest request = new TokenizeRequest();
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);
        request.setMerchant(merchant);
        request.setPrimaryAccountNumber(PAN);
        TokenizeResponse response = ewsSimulatorEndpoint.tokenize(request);

        assertEquals("468498435168468", response.getToken());
        assertEquals(false, response.isTokenNewlyGenerated());
        assertNotNull(response.getRequestId());
    }

    @Test
    public void testTokenize_PANLast3digitsZero() {
        TokenizeRequest request = new TokenizeRequest();
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);
        request.setMerchant(merchant);
        request.setPrimaryAccountNumber("615348948648000");
        TokenizeResponse response = ewsSimulatorEndpoint.tokenize(request);

        assertEquals("468498435168000", response.getToken());
        assertEquals(true, response.isTokenNewlyGenerated());
        assertNotNull(response.getRequestId());
    }

    @Test
    public void testTokenize_Exception() {
        TokenizeRequest request = new TokenizeRequest();
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);
        request.setMerchant(merchant);
        request.setPrimaryAccountNumber("615348948648002");

        try{
            TokenizeResponse response = ewsSimulatorEndpoint.tokenize(request);
            fail("ServerFaultException expected. None thrown");
        } catch (ServerFaultException ex) {
            ServerFault serverFault = ex.getServerFault();
            assertEquals(2, (int) serverFault.getId());
            assertEquals("UNKNOWN_ERROR", serverFault.getCode());
            assertEquals("an unspecified error occurred.", serverFault.getMessage());
        }
    }

    @Test

    public void testTokenInquiry() {
        TokenInquiryRequest request = new TokenInquiryRequest();
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);
        request.setMerchant(merchant);
        List<Card> cards = request.getCard();

        Card card1 = new Card();
        card1.setPrimaryAccountNumber("1234567891011123");
        cards.add(card1);

        Card card2 = new Card();
        card2.setPrimaryAccountNumber("1234567891011000");
        cards.add(card2);

        TokenInquiryResponse response = ewsSimulatorEndpoint.tokenInquiry(request);
        List<Token> tokens = response.getToken();
        assertNotNull(tokens.get(0));
        assertNull(tokens.get(1));
    }
}
