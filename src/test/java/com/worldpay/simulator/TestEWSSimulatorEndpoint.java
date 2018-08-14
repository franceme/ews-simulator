package com.worldpay.simulator;

import com.worldpay.simulator.utils.HttpHeaderUtils;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import com.worldpay.simulator.validator.ValidateAndSimulate;

import org.apache.tomcat.util.codec.binary.Base64;

import static org.powermock.api.mockito.PowerMockito.*;

import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.ws.soap.SoapHeaderElement;

import java.util.List;


@RunWith(PowerMockRunner.class)
@PrepareForTest({ValidateAndSimulate.class, HttpHeaderUtils.class})
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
        mockStatic(HttpHeaderUtils.class);
        header = null;
    }

    @Test
    public void testRegistration() {
        RegistrationRequest request = new RegistrationRequest();
        request.setPrimaryAccountNumber("00012445653000");
        request.setWalletType(WalletType.fromValue("ANDROID"));


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

        doNothing().when(ValidateAndSimulate.class, "validateAndSimulate", detokenizeRequest, header);
        doNothing().when(HttpHeaderUtils.class, "customizeHttpResponseHeader");

        DetokenizeResponse testResponse = ewsSimulatorEndpoint.detokenize(detokenizeRequest,header);

        assertEquals(PAN,testResponse.getPrimaryAccountNumber());
        assertNotNull(testResponse.getRequestId());
        assertEquals(CVV,testResponse.getCardSecurityCode());
        assertEquals(expirationDate,testResponse.getExpirationDate());

        verifyStatic();
        ValidateAndSimulate.validateAndSimulate(detokenizeRequest, header);
        verifyStatic();
        HttpHeaderUtils.customizeHttpResponseHeader();
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

        doNothing().when(ValidateAndSimulate.class, "validateAndSimulate", detokenizeRequest, header);
        doNothing().when(HttpHeaderUtils.class, "customizeHttpResponseHeader");

        DetokenizeResponse testResponse = ewsSimulatorEndpoint.detokenize(detokenizeRequest,header);

        assertEquals(PAN,testResponse.getPrimaryAccountNumber());
        assertNotNull(testResponse.getRequestId());
        assertNull(testResponse.getCardSecurityCode());
        assertNull(testResponse.getExpirationDate());

        verifyStatic();
        ValidateAndSimulate.validateAndSimulate(detokenizeRequest, header);
        verifyStatic();
        HttpHeaderUtils.customizeHttpResponseHeader();
    }

    @Test
    public void testOrderDeregistration_CVVInitialNot3() throws Exception {
        OrderDeregistrationRequest orderDeregistrationRequest = new OrderDeregistrationRequest();
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);
        orderDeregistrationRequest.setMerchant(merchant);
        orderDeregistrationRequest.setOrderLVT(CVV);
        orderDeregistrationRequest.setToken(token);

        doNothing().when(ValidateAndSimulate.class, "validateAndSimulate", orderDeregistrationRequest, header);
        doNothing().when(HttpHeaderUtils.class, "customizeHttpResponseHeader");

        OrderDeregistrationResponse testResponse = ewsSimulatorEndpoint.orderDeregistration(orderDeregistrationRequest,header);

        assertEquals(PAN,testResponse.getPrimaryAccountNumber());
        assertNotNull(testResponse.getRequestId());

        verifyStatic();
        ValidateAndSimulate.validateAndSimulate(orderDeregistrationRequest, header);
        verifyStatic();
        HttpHeaderUtils.customizeHttpResponseHeader();
    }

    @Test
    public void testOrderDeregistration_CVVInitial3() throws Exception {
        OrderDeregistrationRequest orderDeregistrationRequest = new OrderDeregistrationRequest();
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);
        orderDeregistrationRequest.setMerchant(merchant);
        orderDeregistrationRequest.setOrderLVT("303");
        orderDeregistrationRequest.setToken(token);

        doNothing().when(ValidateAndSimulate.class, "validateAndSimulate", orderDeregistrationRequest, header);
        doNothing().when(HttpHeaderUtils.class, "customizeHttpResponseHeader");

        OrderDeregistrationResponse testResponse = ewsSimulatorEndpoint.orderDeregistration(orderDeregistrationRequest,header);

        assertEquals(PAN,testResponse.getPrimaryAccountNumber());
        assertNotNull(testResponse.getRequestId());
        assertEquals("303",testResponse.getCardSecurityCode());

        verifyStatic();
        ValidateAndSimulate.validateAndSimulate(orderDeregistrationRequest, header);
        verifyStatic();
        HttpHeaderUtils.customizeHttpResponseHeader();
    }

    @Test
    public void testOrderDeregistration_CVVInitial3EndWith6() throws Exception {
        OrderDeregistrationRequest orderDeregistrationRequest = new OrderDeregistrationRequest();
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);
        orderDeregistrationRequest.setMerchant(merchant);
        orderDeregistrationRequest.setOrderLVT("306");
        orderDeregistrationRequest.setToken(token);

        doNothing().when(ValidateAndSimulate.class, "validateAndSimulate", orderDeregistrationRequest, header);
        doNothing().when(HttpHeaderUtils.class, "customizeHttpResponseHeader");

        OrderDeregistrationResponse testResponse = ewsSimulatorEndpoint.orderDeregistration(orderDeregistrationRequest,header);

        assertEquals(9999,(int)testResponse.getError().get(0).getId());
        assertEquals("GENERIC CHECKOUT_ID ERROR",testResponse.getError().get(0).getMessage());
        assertNotNull(testResponse.getRequestId());
        assertEquals(PAN,testResponse.getPrimaryAccountNumber());

        verifyStatic();
        ValidateAndSimulate.validateAndSimulate(orderDeregistrationRequest, header);
        verifyStatic();
        HttpHeaderUtils.customizeHttpResponseHeader();
    }

    @Test
    public void testOrderDeregistration_CVVInitial3EndWith7() throws Exception {
        OrderDeregistrationRequest orderDeregistrationRequest = new OrderDeregistrationRequest();
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);
        orderDeregistrationRequest.setMerchant(merchant);
        orderDeregistrationRequest.setOrderLVT("307");
        orderDeregistrationRequest.setToken(token);

        doNothing().when(ValidateAndSimulate.class, "validateAndSimulate", orderDeregistrationRequest, header);
        doNothing().when(HttpHeaderUtils.class, "customizeHttpResponseHeader");

        OrderDeregistrationResponse testResponse = ewsSimulatorEndpoint.orderDeregistration(orderDeregistrationRequest,header);

        assertEquals(2,(int)testResponse.getError().get(0).getId());
        assertEquals("GENERIC CHECKOUT_ID ERROR",testResponse.getError().get(0).getMessage());
        assertNotNull(testResponse.getRequestId());
        assertEquals(PAN,testResponse.getPrimaryAccountNumber());

        verifyStatic();
        ValidateAndSimulate.validateAndSimulate(orderDeregistrationRequest, header);
        verifyStatic();
        HttpHeaderUtils.customizeHttpResponseHeader();
    }

    @Test
    public void testOrderDeregistration_CVVInitial3EndWith8() throws Exception {
        OrderDeregistrationRequest orderDeregistrationRequest = new OrderDeregistrationRequest();
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);
        orderDeregistrationRequest.setMerchant(merchant);
        orderDeregistrationRequest.setOrderLVT("308");
        orderDeregistrationRequest.setToken(token);

        doNothing().when(ValidateAndSimulate.class, "validateAndSimulate", orderDeregistrationRequest, header);
        doNothing().when(HttpHeaderUtils.class, "customizeHttpResponseHeader");

        OrderDeregistrationResponse testResponse = ewsSimulatorEndpoint.orderDeregistration(orderDeregistrationRequest,header);

        assertEquals(4,(int)testResponse.getError().get(0).getId());
        assertEquals("CHECKOUT_ID INVALID",testResponse.getError().get(0).getMessage());
        assertNotNull(testResponse.getRequestId());
        assertEquals(PAN,testResponse.getPrimaryAccountNumber());

        verifyStatic();
        ValidateAndSimulate.validateAndSimulate(orderDeregistrationRequest, header);
        verifyStatic();
        HttpHeaderUtils.customizeHttpResponseHeader();
    }

    @Test
    public void testOrderDeregistration_CVInitial3VEndWith9() throws Exception {
        OrderDeregistrationRequest orderDeregistrationRequest = new OrderDeregistrationRequest();
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);
        orderDeregistrationRequest.setMerchant(merchant);
        orderDeregistrationRequest.setOrderLVT("309");
        orderDeregistrationRequest.setToken(token);

        doNothing().when(ValidateAndSimulate.class, "validateAndSimulate", orderDeregistrationRequest, header);
        doNothing().when(HttpHeaderUtils.class, "customizeHttpResponseHeader");

        OrderDeregistrationResponse testResponse = ewsSimulatorEndpoint.orderDeregistration(orderDeregistrationRequest,header);

        assertEquals(6,(int)testResponse.getError().get(0).getId());
        assertEquals("CHECKOUT_ID NOT_FOUND", testResponse.getError().get(0).getMessage());
        assertNotNull(testResponse.getRequestId());
        assertEquals(PAN,testResponse.getPrimaryAccountNumber());

        verifyStatic();
        ValidateAndSimulate.validateAndSimulate(orderDeregistrationRequest, header);
        verifyStatic();
        HttpHeaderUtils.customizeHttpResponseHeader();
    }

    @Test
    public void testTokenize_simple() throws Exception {
        TokenizeRequest request = new TokenizeRequest();
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);
        request.setMerchant(merchant);
        request.setPrimaryAccountNumber(PAN);

        doNothing().when(ValidateAndSimulate.class, "validateAndSimulate", request, header);
        doNothing().when(HttpHeaderUtils.class, "customizeHttpResponseHeader");

        TokenizeResponse response = ewsSimulatorEndpoint.tokenize(request, header);

        assertEquals("468498435168468", response.getToken());
        assertEquals(false, response.isTokenNewlyGenerated());
        assertNotNull(response.getRequestId());

        verifyStatic();
        ValidateAndSimulate.validateAndSimulate(request, header);
        verifyStatic();
        HttpHeaderUtils.customizeHttpResponseHeader();
    }

    @Test
    public void testTokenize_PANLast3digitsZero() throws Exception {
        TokenizeRequest request = new TokenizeRequest();
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);
        request.setMerchant(merchant);
        request.setPrimaryAccountNumber("615348948648000");

        doNothing().when(ValidateAndSimulate.class, "validateAndSimulate", request, header);
        doNothing().when(HttpHeaderUtils.class, "customizeHttpResponseHeader");

        TokenizeResponse response = ewsSimulatorEndpoint.tokenize(request, header);

        assertEquals("468498435168000", response.getToken());
        assertEquals(true, response.isTokenNewlyGenerated());
        assertNotNull(response.getRequestId());

        verifyStatic();
        ValidateAndSimulate.validateAndSimulate(request, header);
        verifyStatic();
        HttpHeaderUtils.customizeHttpResponseHeader();
    }

    @Test
    public void testDeregistration_returnCvv2IfAsked() throws Exception {
        DeregistrationRequest deregistrationRequest = new DeregistrationRequest();
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);
        deregistrationRequest.setMerchant(merchant);
        deregistrationRequest.setRegId(registrationId);
        deregistrationRequest.setCardSecurityCodeRequested(true);

        doNothing().when(ValidateAndSimulate.class, "validateAndSimulate", deregistrationRequest, header);
        doNothing().when(HttpHeaderUtils.class, "customizeHttpResponseHeader");

        DeregistrationResponse testResponse = ewsSimulatorEndpoint.deregistration(deregistrationRequest,header);

        assertNotNull(testResponse.getRequestId());
        assertEquals(token,testResponse.getToken());
        assertEquals(PAN,testResponse.getPrimaryAccountNumber());
        assertEquals(CVV,testResponse.getCardSecurityCode());
        assertEquals(expirationDate,testResponse.getExpirationDate());

        verifyStatic();
        ValidateAndSimulate.validateAndSimulate(deregistrationRequest, header);
        verifyStatic();
        HttpHeaderUtils.customizeHttpResponseHeader();
    }

    @Test
    public void testDeregistration_returnCvv2OnlyIfAsked() throws Exception {
        DeregistrationRequest deregistrationRequest = new DeregistrationRequest();
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);
        deregistrationRequest.setMerchant(merchant);
        deregistrationRequest.setRegId(registrationId);
        deregistrationRequest.setCardSecurityCodeRequested(false);

        doNothing().when(ValidateAndSimulate.class, "validateAndSimulate", deregistrationRequest, header);
        doNothing().when(HttpHeaderUtils.class, "customizeHttpResponseHeader");

        DeregistrationResponse testResponse = ewsSimulatorEndpoint.deregistration(deregistrationRequest,header);

        assertNotNull(testResponse.getRequestId());
        assertEquals(token,testResponse.getToken());
        assertEquals(PAN,testResponse.getPrimaryAccountNumber());
        assertNull(testResponse.getCardSecurityCode());
        assertEquals(expirationDate,testResponse.getExpirationDate());

        verifyStatic();
        ValidateAndSimulate.validateAndSimulate(deregistrationRequest, header);
        verifyStatic();
        HttpHeaderUtils.customizeHttpResponseHeader();
    }

    @Test
    public void testDeregistration_DPAN_ANDROID() throws Exception {
        DeregistrationRequest deregistrationRequest = new DeregistrationRequest();
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);
        deregistrationRequest.setMerchant(merchant);
        deregistrationRequest.setRegId("615348948648641");
        deregistrationRequest.setCardSecurityCodeRequested(false);

        doNothing().when(ValidateAndSimulate.class, "validateAndSimulate", deregistrationRequest, header);
        doNothing().when(HttpHeaderUtils.class, "customizeHttpResponseHeader");

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

        verifyStatic();
        ValidateAndSimulate.validateAndSimulate(deregistrationRequest, header);
        verifyStatic();
        HttpHeaderUtils.customizeHttpResponseHeader();
    }

    @Test
    public void testDeregistration_DPAN_APPLE() throws Exception {
        DeregistrationRequest deregistrationRequest = new DeregistrationRequest();
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);
        deregistrationRequest.setMerchant(merchant);
        deregistrationRequest.setRegId("615348948648642");
        deregistrationRequest.setCardSecurityCodeRequested(false);

        doNothing().when(ValidateAndSimulate.class, "validateAndSimulate", deregistrationRequest, header);
        doNothing().when(HttpHeaderUtils.class, "customizeHttpResponseHeader");

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

        verifyStatic();
        ValidateAndSimulate.validateAndSimulate(deregistrationRequest, header);
        verifyStatic();
        HttpHeaderUtils.customizeHttpResponseHeader();
    }


    @Test
    public void testDeregistration_DPAN_SAMSUNG() throws Exception {
        DeregistrationRequest deregistrationRequest = new DeregistrationRequest();
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);
        deregistrationRequest.setMerchant(merchant);
        deregistrationRequest.setRegId("615348948648643");
        deregistrationRequest.setCardSecurityCodeRequested(false);

        doNothing().when(ValidateAndSimulate.class, "validateAndSimulate", deregistrationRequest, header);
        doNothing().when(HttpHeaderUtils.class, "customizeHttpResponseHeader");

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

        verifyStatic();
        ValidateAndSimulate.validateAndSimulate(deregistrationRequest, header);
        verifyStatic();
        HttpHeaderUtils.customizeHttpResponseHeader();
    }

    @Test
    public void testTokenInquiry() throws Exception {
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

        doNothing().when(ValidateAndSimulate.class, "validateAndSimulate", request, header);
        doNothing().when(HttpHeaderUtils.class, "customizeHttpResponseHeader");

        TokenInquiryResponse response = ewsSimulatorEndpoint.tokenInquiry(request, header);
        List<Token> tokens = response.getToken();
        assertNotNull(tokens.get(0));
        assertNull(tokens.get(1));

        verifyStatic();
        ValidateAndSimulate.validateAndSimulate(request, header);
        verifyStatic();
        HttpHeaderUtils.customizeHttpResponseHeader();
    }

    @Test
    public void testTokenRegistration() throws Exception {
        TokenRegistrationRequest request = new TokenRegistrationRequest();
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);
        request.setMerchant(merchant);
        request.setToken(token);

        doNothing().when(ValidateAndSimulate.class, "validateAndSimulate", request, header);
        doNothing().when(HttpHeaderUtils.class, "customizeHttpResponseHeader");

        TokenRegistrationResponse response = ewsSimulatorEndpoint.tokenRegistration(request, header);
        assertNotNull(response.getRegId());

        verifyStatic();
        ValidateAndSimulate.validateAndSimulate(request, header);
        verifyStatic();
        HttpHeaderUtils.customizeHttpResponseHeader();
    }

    @Test
    public void testEchoRequest() throws Exception {
        EchoRequest echoRequest = new EchoRequest();
        String test = "asdahfiuahofo";
        echoRequest.setTest(test);

        PowerMockito.doNothing().when(ValidateAndSimulate.class,"validateAndSimulate",echoRequest,header);

        EchoResponse echoResponse = ewsSimulatorEndpoint.echo(echoRequest,header);

        assertEquals(test,echoResponse.getResponse());
    }
}
