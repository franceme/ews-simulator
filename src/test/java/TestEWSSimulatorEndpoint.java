import ewsSimulator.ws.*;
import org.junit.Before;
import org.junit.Test;

import static ewsSimulator.ws.EWSUtils.getAuthentication;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.will;
import static org.mockito.Mockito.doNothing;

import ewsSimulator.ws.DeregistrationRequest;
import ewsSimulator.ws.DeregistrationResponse;
import ewsSimulator.ws.DetokenizeRequest;
import ewsSimulator.ws.DetokenizeResponse;
import ewsSimulator.ws.EWSSimulatorEndpoint;
import ewsSimulator.ws.MerchantType;
import ewsSimulator.ws.OrderDeregistrationRequest;
import ewsSimulator.ws.OrderDeregistrationResponse;
import ewsSimulator.ws.validator.ValidateAndSimulate;

import org.apache.tomcat.util.codec.binary.Base64;

import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ws.soap.SoapHeaderElement;

import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.*;

import java.sql.DriverManager;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import javax.smartcardio.Card;

@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(SpringRunner.class)
@PrepareForTest({ValidateAndSimulate.class})
public class TestEWSSimulatorEndpoint {

    EWSSimulatorEndpoint ewsSimulatorEndpoint;
    private String requestId;
    private String token;
    private String PAN;
    private String CVV;
    private String expirationDate;
    private String rollupId;
    private String APPLE;
    private String ANDROID;
    private String SAMSUNG;
    private String CRYPTOGRAM;
    private String registrationId;
    private SoapHeaderElement header;




    @Before
    public void setup() {
        ewsSimulatorEndpoint = new EWSSimulatorEndpoint();
        requestId = "f75b9c0f-5348-4621-acdc-a00861b25697";
        token = "468498435168468";
        PAN = "615348948648468";
        CVV = "468";
        expirationDate = "2308";
        rollupId = "1123";
        APPLE = "APPLE";
        ANDROID = "ANDROID";
        SAMSUNG = "SAMSUNG";
        CRYPTOGRAM = "2wABBJQ1AgAAAAAgJDUCAAAAAAA=";
        registrationId = "615348948648648";
        mockStatic(ValidateAndSimulate.class);
        header = null;
    }

    @Test
    public void testDetokenize_success_with_ExpirationDateRequested_CVV2Requested() throws Exception {
        DetokenizeRequest detokenizeRequest = new DetokenizeRequest();
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(requestId);
        detokenizeRequest.setMerchant(merchant);
        detokenizeRequest.setToken(token);
        detokenizeRequest.setCVV2Requested(true);
        detokenizeRequest.setExpirationDateRequested(true);

        PowerMockito.doNothing().when(ValidateAndSimulate.class,"getAuthentication",header);
        PowerMockito.doNothing().when(ValidateAndSimulate.class,"validateAndSimulate",detokenizeRequest);

        DetokenizeResponse testResponse = ewsSimulatorEndpoint.detokenize(detokenizeRequest,header);

        assertEquals(PAN,testResponse.getPrimaryAccountNumber());
        assertNotNull(testResponse.getRequestId());
        assertEquals(CVV,testResponse.getCardSecurityCode());
        assertEquals(expirationDate,testResponse.getExpirationDate());
    }

    @Test
    public void testDetokenize_success_without_ExpirationDateRequested_CVV2Requested() throws Exception {
        DetokenizeRequest detokenizeRequest = new DetokenizeRequest();
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);
        detokenizeRequest.setMerchant(merchant);
        detokenizeRequest.setToken(token);
        detokenizeRequest.setCVV2Requested(false);
        detokenizeRequest.setExpirationDateRequested(false);

        PowerMockito.doNothing().when(ValidateAndSimulate.class,"getAuthentication",header);
        PowerMockito.doNothing().when(ValidateAndSimulate.class,"validateAndSimulate",detokenizeRequest);

        DetokenizeResponse testResponse = ewsSimulatorEndpoint.detokenize(detokenizeRequest,header);

        assertEquals(PAN,testResponse.getPrimaryAccountNumber());
        assertNotNull(testResponse.getRequestId());
        assertNull(testResponse.getCardSecurityCode());
        assertNull(testResponse.getExpirationDate());
    }

    @Test
    public void testOrderDeregistration_CVVInitialNot3() throws Exception {
        OrderDeregistrationRequest orderDeregistrationRequest = new OrderDeregistrationRequest();
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);
        orderDeregistrationRequest.setMerchant(merchant);
        orderDeregistrationRequest.setOrderLVT(CVV);
        orderDeregistrationRequest.setToken(token);

        PowerMockito.doNothing().when(ValidateAndSimulate.class,"getAuthentication",header);
        PowerMockito.doNothing().when(ValidateAndSimulate.class,"validateAndSimulate",orderDeregistrationRequest);

        OrderDeregistrationResponse testResponse = ewsSimulatorEndpoint.orderDeregistration(orderDeregistrationRequest,header);

        assertEquals(PAN,testResponse.getPrimaryAccountNumber());
        assertNotNull(testResponse.getRequestId());
    }

    @Test
    public void testOrderDeregistration_CVVInitial3() throws Exception {
        OrderDeregistrationRequest orderDeregistrationRequest = new OrderDeregistrationRequest();
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);
        orderDeregistrationRequest.setMerchant(merchant);
        orderDeregistrationRequest.setOrderLVT("303");
        orderDeregistrationRequest.setToken(token);

        PowerMockito.doNothing().when(ValidateAndSimulate.class,"getAuthentication",header);
        PowerMockito.doNothing().when(ValidateAndSimulate.class,"validateAndSimulate",orderDeregistrationRequest);

        OrderDeregistrationResponse testResponse = ewsSimulatorEndpoint.orderDeregistration(orderDeregistrationRequest,header);

        assertEquals(PAN,testResponse.getPrimaryAccountNumber());
        assertNotNull(testResponse.getRequestId());
        assertEquals("303",testResponse.getCardSecurityCode());
    }

    @Test
    public void testOrderDeregistration_CVVInitial3EndWith6() throws Exception {
        OrderDeregistrationRequest orderDeregistrationRequest = new OrderDeregistrationRequest();
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);
        orderDeregistrationRequest.setMerchant(merchant);
        orderDeregistrationRequest.setOrderLVT("306");
        orderDeregistrationRequest.setToken(token);

        PowerMockito.doNothing().when(ValidateAndSimulate.class,"getAuthentication",header);
        PowerMockito.doNothing().when(ValidateAndSimulate.class,"validateAndSimulate",orderDeregistrationRequest);

        OrderDeregistrationResponse testResponse = ewsSimulatorEndpoint.orderDeregistration(orderDeregistrationRequest,header);

        assertEquals(9999,(int)testResponse.getError().get(0).getId());
        assertEquals("GENERIC CHECKOUT_ID ERROR",testResponse.getError().get(0).getMessage());
        assertNotNull(testResponse.getRequestId());
        assertEquals(PAN,testResponse.getPrimaryAccountNumber());
    }

    @Test
    public void testOrderDeregistration_CVVInitial3EndWith7() throws Exception {
        OrderDeregistrationRequest orderDeregistrationRequest = new OrderDeregistrationRequest();
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);
        orderDeregistrationRequest.setMerchant(merchant);
        orderDeregistrationRequest.setOrderLVT("307");
        orderDeregistrationRequest.setToken(token);

        PowerMockito.doNothing().when(ValidateAndSimulate.class,"getAuthentication",header);
        PowerMockito.doNothing().when(ValidateAndSimulate.class,"validateAndSimulate",orderDeregistrationRequest);

        OrderDeregistrationResponse testResponse = ewsSimulatorEndpoint.orderDeregistration(orderDeregistrationRequest,header);

        assertEquals(2,(int)testResponse.getError().get(0).getId());
        assertEquals("GENERIC CHECKOUT_ID ERROR",testResponse.getError().get(0).getMessage());
        assertNotNull(testResponse.getRequestId());
        assertEquals(PAN,testResponse.getPrimaryAccountNumber());
    }

    @Test
    public void testOrderDeregistration_CVVInitial3EndWith8() throws Exception {
        OrderDeregistrationRequest orderDeregistrationRequest = new OrderDeregistrationRequest();
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);
        orderDeregistrationRequest.setMerchant(merchant);
        orderDeregistrationRequest.setOrderLVT("308");
        orderDeregistrationRequest.setToken(token);

        PowerMockito.doNothing().when(ValidateAndSimulate.class,"getAuthentication",header);
        PowerMockito.doNothing().when(ValidateAndSimulate.class,"validateAndSimulate",orderDeregistrationRequest);

        OrderDeregistrationResponse testResponse = ewsSimulatorEndpoint.orderDeregistration(orderDeregistrationRequest,header);

        assertEquals(4,(int)testResponse.getError().get(0).getId());
        assertEquals("CHECKOUT_ID INVALID",testResponse.getError().get(0).getMessage());
        assertNotNull(testResponse.getRequestId());
        assertEquals(PAN,testResponse.getPrimaryAccountNumber());
    }

    @Test
    public void testOrderDeregistration_CVInitial3VEndWith9() throws Exception {
        OrderDeregistrationRequest orderDeregistrationRequest = new OrderDeregistrationRequest();
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);
        orderDeregistrationRequest.setMerchant(merchant);
        orderDeregistrationRequest.setOrderLVT("309");
        orderDeregistrationRequest.setToken(token);

        PowerMockito.doNothing().when(ValidateAndSimulate.class,"getAuthentication",header);
        PowerMockito.doNothing().when(ValidateAndSimulate.class,"validateAndSimulate",orderDeregistrationRequest);

        OrderDeregistrationResponse testResponse = ewsSimulatorEndpoint.orderDeregistration(orderDeregistrationRequest,header);

        assertEquals(6,(int)testResponse.getError().get(0).getId());
        assertEquals("CHECKOUT_ID NOT_FOUND", testResponse.getError().get(0).getMessage());
        assertNotNull(testResponse.getRequestId());
        assertEquals(PAN,testResponse.getPrimaryAccountNumber());
    }
//
//    @Test
//    public void testTokenize_simple() {
//        TokenizeRequest request = new TokenizeRequest();
//        MerchantType merchant = new MerchantType();
//        merchant.setRollupId(rollupId);
//        request.setMerchant(merchant);
//        request.setPrimaryAccountNumber(PAN);
//        TokenizeResponse response = ewsSimulatorEndpoint.tokenize(request);
//
//        assertEquals("468498435168468", response.getToken());
//        assertEquals(false, response.isTokenNewlyGenerated());
//        assertNotNull(response.getRequestId());
//    }
//
//    @Test
//    public void testTokenize_PANLast3digitsZero() {
//        TokenizeRequest request = new TokenizeRequest();
//        MerchantType merchant = new MerchantType();
//        merchant.setRollupId(rollupId);
//        request.setMerchant(merchant);
//        request.setPrimaryAccountNumber("615348948648000");
//        TokenizeResponse response = ewsSimulatorEndpoint.tokenize(request);
//
//        assertEquals("468498435168000", response.getToken());
//        assertEquals(true, response.isTokenNewlyGenerated());
//        assertNotNull(response.getRequestId());
//    }

//    @Test
//    public void testTokenize_Exception() {
//        TokenizeRequest request = new TokenizeRequest();
//        request.setMerchantRefId("112233002");
//        MerchantType merchant = new MerchantType();
//        merchant.setRollupId(rollupId);
//        request.setMerchant(merchant);
//        request.setPrimaryAccountNumber("615348948648468");
//
//        try{
//            TokenizeResponse response = ewsSimulatorEndpoint.tokenize(request);
//            fail("ServerFaultException expected. None thrown");
//        } catch (ServerFaultException ex) {
//            ServerFault serverFault = ex.getServerFault();
//            assertEquals(2, (int) serverFault.getId());
//            assertEquals("UNKNOWN_ERROR", serverFault.getCode());
//            assertEquals("an unspecified error occurred.", serverFault.getMessage());
//        }
//    }

    @Test
    public void testDeregistration_returnCvv2IfAsked() throws Exception {
        DeregistrationRequest deregistrationRequest = new DeregistrationRequest();
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);
        deregistrationRequest.setMerchant(merchant);
        deregistrationRequest.setRegId(registrationId);
        deregistrationRequest.setCardSecurityCodeRequested(true);

        PowerMockito.doNothing().when(ValidateAndSimulate.class,"getAuthentication",header);
        PowerMockito.doNothing().when(ValidateAndSimulate.class,"validateAndSimulate",deregistrationRequest);

        DeregistrationResponse testResponse = ewsSimulatorEndpoint.deregistration(deregistrationRequest,header);

        assertNotNull(testResponse.getRequestId());
        assertEquals(token,testResponse.getToken());
        assertEquals(PAN,testResponse.getPrimaryAccountNumber());
        assertEquals(CVV,testResponse.getCardSecurityCode());
        assertEquals(expirationDate,testResponse.getExpirationDate());
    }

    @Test
    public void testDeregistration_returnCvv2OnlyIfAsked() throws Exception {
        DeregistrationRequest deregistrationRequest = new DeregistrationRequest();
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);
        deregistrationRequest.setMerchant(merchant);
        deregistrationRequest.setRegId(registrationId);
        deregistrationRequest.setCardSecurityCodeRequested(false);

        PowerMockito.doNothing().when(ValidateAndSimulate.class,"getAuthentication",header);
        PowerMockito.doNothing().when(ValidateAndSimulate.class,"validateAndSimulate",deregistrationRequest);

        DeregistrationResponse testResponse = ewsSimulatorEndpoint.deregistration(deregistrationRequest,header);

        assertNotNull(testResponse.getRequestId());
        assertEquals(token,testResponse.getToken());
        assertEquals(PAN,testResponse.getPrimaryAccountNumber());
        assertNull(testResponse.getCardSecurityCode());
        assertEquals(expirationDate,testResponse.getExpirationDate());
    }

    @Test
    public void testDeregistration_DPAN_ANDROID() throws Exception {
        DeregistrationRequest deregistrationRequest = new DeregistrationRequest();
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);
        deregistrationRequest.setMerchant(merchant);
        deregistrationRequest.setRegId("615348948648641");
        deregistrationRequest.setCardSecurityCodeRequested(false);

        PowerMockito.doNothing().when(ValidateAndSimulate.class,"getAuthentication",header);
        PowerMockito.doNothing().when(ValidateAndSimulate.class,"validateAndSimulate",deregistrationRequest);


        DeregistrationResponse testResponse = ewsSimulatorEndpoint.deregistration(deregistrationRequest,header);

        assertNotNull(testResponse.getRequestId());
        assertEquals("468498435161468",testResponse.getToken());
        assertEquals("615348948641468",testResponse.getPrimaryAccountNumber());
        assertNull(testResponse.getCardSecurityCode());
        assertNull(testResponse.getCardSecurityCode());
        assertEquals(expirationDate,testResponse.getExpirationDate());
        assertEquals(ANDROID,testResponse.getWalletType().value());
        assertEquals("01",testResponse.getElectronicCommerceIndicator());
        assertEquals(CRYPTOGRAM,new Base64().encodeAsString(testResponse.getCryptogram()));
    }

    @Test
    public void testDeregistration_DPAN_APPLE() throws Exception {
        DeregistrationRequest deregistrationRequest = new DeregistrationRequest();
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);
        deregistrationRequest.setMerchant(merchant);
        deregistrationRequest.setRegId("615348948648642");
        deregistrationRequest.setCardSecurityCodeRequested(false);

        PowerMockito.doNothing().when(ValidateAndSimulate.class,"getAuthentication",header);
        PowerMockito.doNothing().when(ValidateAndSimulate.class,"validateAndSimulate",deregistrationRequest);

        DeregistrationResponse testResponse = ewsSimulatorEndpoint.deregistration(deregistrationRequest,header);

        assertNotNull(testResponse.getRequestId());
        assertEquals("468498435162468",testResponse.getToken());
        assertEquals("615348948642468",testResponse.getPrimaryAccountNumber());
        assertNull(testResponse.getCardSecurityCode());
        assertNull(testResponse.getCardSecurityCode());
        assertEquals(expirationDate,testResponse.getExpirationDate());
        assertEquals(APPLE,testResponse.getWalletType().value());
        assertEquals("02",testResponse.getElectronicCommerceIndicator());
        assertEquals(CRYPTOGRAM,new Base64().encodeAsString(testResponse.getCryptogram()));
    }


    @Test
    public void testDeregistration_DPAN_SAMSUNG() throws Exception {
        DeregistrationRequest deregistrationRequest = new DeregistrationRequest();
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);
        deregistrationRequest.setMerchant(merchant);
        deregistrationRequest.setRegId("615348948648643");
        deregistrationRequest.setCardSecurityCodeRequested(false);

        PowerMockito.doNothing().when(ValidateAndSimulate.class,"getAuthentication",header);
        PowerMockito.doNothing().when(ValidateAndSimulate.class,"validateAndSimulate",deregistrationRequest);

        DeregistrationResponse testResponse = ewsSimulatorEndpoint.deregistration(deregistrationRequest,header);

        assertNotNull(testResponse.getRequestId());
        assertEquals("468498435163468",testResponse.getToken());
        assertEquals("615348948643468",testResponse.getPrimaryAccountNumber());
        assertNull(testResponse.getCardSecurityCode());
        assertNull(testResponse.getCardSecurityCode());
        assertEquals(expirationDate,testResponse.getExpirationDate());
        assertEquals(SAMSUNG,testResponse.getWalletType().value());
        assertEquals("03",testResponse.getElectronicCommerceIndicator());
        assertEquals(CRYPTOGRAM,new Base64().encodeAsString(testResponse.getCryptogram()));
    }


//    @Test
//
//    public void testTokenInquiry() {
//        TokenInquiryRequest request = new TokenInquiryRequest();
//        MerchantType merchant = new MerchantType();
//        merchant.setRollupId(rollupId);
//        request.setMerchant(merchant);
//        List<Card> cards = request.getCard();
//
//        Card card1 = new Card();
//        card1.setPrimaryAccountNumber("1234567891011123");
//        cards.add(card1);
//
//        Card card2 = new Card();
//        card2.setPrimaryAccountNumber("1234567891011000");
//        cards.add(card2);
//
//        TokenInquiryResponse response = ewsSimulatorEndpoint.tokenInquiry(request);
//        List<Token> tokens = response.getToken();
//        assertNotNull(tokens.get(0));
//        assertNull(tokens.get(1));
//    }
//
//
//
//    @Test
//    public void testTokenInquiry_Exception() {
//        TokenInquiryRequest request = new TokenInquiryRequest();
//        request.setMerchantRefId("112233002");
//        MerchantType merchant = new MerchantType();
//        merchant.setRollupId(rollupId);
//        request.setMerchant(merchant);
//        List<Card> cards = request.getCard();
//
//        Card card1 = new Card();
//        card1.setPrimaryAccountNumber("1234567891011123");
//        cards.add(card1);
//
//        try {
//            TokenInquiryResponse response = ewsSimulatorEndpoint.tokenInquiry(request);
//            fail("ServerFaultException expected. None thrown");
//        } catch (ServerFaultException ex) {
//            ServerFault serverFault = ex.getServerFault();
//            assertEquals(2, (int) serverFault.getId());
//            assertEquals("UNKNOWN_ERROR", serverFault.getCode());
//            assertEquals("an unspecified error occurred.", serverFault.getMessage());
//        }
//    }
}
