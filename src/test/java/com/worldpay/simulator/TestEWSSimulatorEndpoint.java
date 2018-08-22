package com.worldpay.simulator;

import com.worldpay.simulator.utils.HttpHeaderUtils;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import com.worldpay.simulator.validator.ValidatorService;

import org.apache.tomcat.util.codec.binary.Base64;

import static org.mockito.BDDMockito.willDoNothing;
import static org.powermock.api.mockito.PowerMockito.*;

import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ws.soap.SoapHeaderElement;
import static org.mockito.Mockito.*;
import java.util.List;


// Test Case Id: TC15065; TC15066; TC15067; TC15068

@RunWith(PowerMockRunner.class)
@SpringBootTest(classes = SpringTestConfig.class)
@PowerMockRunnerDelegate(SpringRunner.class)
@PrepareForTest({ValidatorService.class})
public class TestEWSSimulatorEndpoint {

    @MockBean
    HttpHeaderUtils httpHeaderUtils;

    @Autowired
    @Qualifier("testEWSSimulator")
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
    private String merchantRefId;


    @Before
    public void setup() {
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
        mockStatic(ValidatorService.class);
        header = null;
        merchantRefId = "00012445653000";
    }

    @Test
    public void testRegistration() throws Exception {
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);

        RegistrationRequest request = new RegistrationRequest();
        request.setMerchantRefId(merchantRefId);
        request.setPrimaryAccountNumber(PAN);
        request.setWalletType(WalletType.fromValue(ANDROID));
        request.setCryptogram(CRYPTOGRAM.getBytes());
        request.setMerchant(merchant);

        PowerMockito.doNothing().when(ValidatorService.class, "validateAndSimulate", request, header);
        willDoNothing().given(httpHeaderUtils).customizeHttpResponseHeader();

        RegistrationResponse response = ewsSimulatorEndpoint.registration(request, header);

        assertEquals("468498435168468", response.getToken());
        assertEquals("615348948648648", response.getRegId());
        assertEquals(false, response.isTokenNewlyGenerated());
        assertNotNull(response.getRequestId());

        verifyStatic();
        ValidatorService.validateAndSimulate(request, header);
        verify(httpHeaderUtils, times(1)).customizeHttpResponseHeader();

    }

    @Test
    public void testRegistration_PANLast3digitsZero() throws Exception {
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);

        RegistrationRequest request = new RegistrationRequest();
        request.setMerchantRefId(merchantRefId);
        request.setPrimaryAccountNumber("615348948648000");
        request.setWalletType(WalletType.fromValue(ANDROID));
        request.setCryptogram(CRYPTOGRAM.getBytes());
        request.setMerchant(merchant);

        PowerMockito.doNothing().when(ValidatorService.class, "validateAndSimulate", request, header);
        willDoNothing().given(httpHeaderUtils).customizeHttpResponseHeader();

        RegistrationResponse response = ewsSimulatorEndpoint.registration(request, header);

        assertEquals("468498435168000", response.getToken());
        assertEquals("615348948640008", response.getRegId());
        assertEquals(true, response.isTokenNewlyGenerated());
        assertNotNull(response.getRequestId());

        verifyStatic();
        ValidatorService.validateAndSimulate(request, header);
        verify(httpHeaderUtils, times(1)).customizeHttpResponseHeader();

    }

    @Test
    public void testDetokenize_success_with_ExpirationDateRequested_CVV2Requested() throws Exception {
        DetokenizeRequest detokenizeRequest = new DetokenizeRequest();
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);
        detokenizeRequest.setMerchant(merchant);
        detokenizeRequest.setToken(token);
        detokenizeRequest.setCVV2Requested(true);
        detokenizeRequest.setExpirationDateRequested(true);

        PowerMockito.doNothing().when(ValidatorService.class, "validateAndSimulate", detokenizeRequest, header);
        willDoNothing().given(httpHeaderUtils).customizeHttpResponseHeader();

        DetokenizeResponse testResponse = ewsSimulatorEndpoint.detokenize(detokenizeRequest,header);

        assertEquals(PAN,testResponse.getPrimaryAccountNumber());
        assertNotNull(testResponse.getRequestId());
        assertEquals(CVV,testResponse.getCardSecurityCode());
        assertEquals(expirationDate,testResponse.getExpirationDate());

        verifyStatic();
        ValidatorService.validateAndSimulate(detokenizeRequest, header);
        verify(httpHeaderUtils, times(1)).customizeHttpResponseHeader();
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

        PowerMockito.doNothing().when(ValidatorService.class, "validateAndSimulate", detokenizeRequest, header);
        willDoNothing().given(httpHeaderUtils).customizeHttpResponseHeader();

        DetokenizeResponse testResponse = ewsSimulatorEndpoint.detokenize(detokenizeRequest,header);

        assertEquals(PAN,testResponse.getPrimaryAccountNumber());
        assertNotNull(testResponse.getRequestId());
        assertNull(testResponse.getCardSecurityCode());
        assertNull(testResponse.getExpirationDate());

        verifyStatic();
        ValidatorService.validateAndSimulate(detokenizeRequest, header);
        verify(httpHeaderUtils, times(1)).customizeHttpResponseHeader();
    }

    @Test
    public void testOrderDeregistration_CVVInitialNot3() throws Exception {
        OrderDeregistrationRequest orderDeregistrationRequest = new OrderDeregistrationRequest();
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);
        orderDeregistrationRequest.setMerchant(merchant);
        orderDeregistrationRequest.setOrderLVT(CVV);
        orderDeregistrationRequest.setToken(token);

        PowerMockito.doNothing().when(ValidatorService.class, "validateAndSimulate", orderDeregistrationRequest, header);
        willDoNothing().given(httpHeaderUtils).customizeHttpResponseHeader();

        OrderDeregistrationResponse testResponse = ewsSimulatorEndpoint.orderDeregistration(orderDeregistrationRequest,header);

        assertEquals(PAN,testResponse.getPrimaryAccountNumber());
        assertNotNull(testResponse.getRequestId());

        verifyStatic();
        ValidatorService.validateAndSimulate(orderDeregistrationRequest, header);
        verify(httpHeaderUtils, times(1)).customizeHttpResponseHeader();
    }

    @Test
    public void testOrderDeregistration_InvalidToken() throws Exception {
        OrderDeregistrationRequest orderDeregistrationRequest = new OrderDeregistrationRequest();
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);
        orderDeregistrationRequest.setMerchant(merchant);
        orderDeregistrationRequest.setOrderLVT(CVV);
        orderDeregistrationRequest.setToken("12");

        PowerMockito.doNothing().when(ValidatorService.class, "validateAndSimulate", orderDeregistrationRequest, header);
        willDoNothing().given(httpHeaderUtils).customizeHttpResponseHeader();

        OrderDeregistrationResponse testResponse = ewsSimulatorEndpoint.orderDeregistration(orderDeregistrationRequest,header);

        assertEquals("1111000100038566",testResponse.getPrimaryAccountNumber());
        assertNotNull(testResponse.getRequestId());

        verifyStatic();
        ValidatorService.validateAndSimulate(orderDeregistrationRequest, header);
        verify(httpHeaderUtils, times(1)).customizeHttpResponseHeader();
    }

    @Test
    public void testOrderDeregistration_CVVInitial3() throws Exception {
        OrderDeregistrationRequest orderDeregistrationRequest = new OrderDeregistrationRequest();
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);
        orderDeregistrationRequest.setMerchant(merchant);
        orderDeregistrationRequest.setOrderLVT("303");
        orderDeregistrationRequest.setToken(token);

        PowerMockito.doNothing().when(ValidatorService.class, "validateAndSimulate", orderDeregistrationRequest, header);
        willDoNothing().given(httpHeaderUtils).customizeHttpResponseHeader();

        OrderDeregistrationResponse testResponse = ewsSimulatorEndpoint.orderDeregistration(orderDeregistrationRequest,header);

        assertEquals(PAN,testResponse.getPrimaryAccountNumber());
        assertNotNull(testResponse.getRequestId());
        assertEquals("468",testResponse.getCardSecurityCode());

        verifyStatic();
        ValidatorService.validateAndSimulate(orderDeregistrationRequest, header);
        verify(httpHeaderUtils, times(1)).customizeHttpResponseHeader();
    }

    @Test
    public void testOrderDeregistration_CVVInitial3EndWith6() throws Exception {
        OrderDeregistrationRequest orderDeregistrationRequest = new OrderDeregistrationRequest();
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);
        orderDeregistrationRequest.setMerchant(merchant);
        orderDeregistrationRequest.setOrderLVT("306");
        orderDeregistrationRequest.setToken(token);

        PowerMockito.doNothing().when(ValidatorService.class, "validateAndSimulate", orderDeregistrationRequest, header);
        willDoNothing().given(httpHeaderUtils).customizeHttpResponseHeader();

        OrderDeregistrationResponse testResponse = ewsSimulatorEndpoint.orderDeregistration(orderDeregistrationRequest,header);

        assertEquals(9999,(int)testResponse.getError().get(0).getId());
        assertEquals("GENERIC CHECKOUT_ID ERROR",testResponse.getError().get(0).getMessage());
        assertNotNull(testResponse.getRequestId());
        assertEquals(PAN,testResponse.getPrimaryAccountNumber());

        verifyStatic();
        ValidatorService.validateAndSimulate(orderDeregistrationRequest, header);
        verify(httpHeaderUtils, times(1)).customizeHttpResponseHeader();
    }

    @Test
    public void testOrderDeregistration_CVVInitial3EndWith7() throws Exception {
        OrderDeregistrationRequest orderDeregistrationRequest = new OrderDeregistrationRequest();
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);
        orderDeregistrationRequest.setMerchant(merchant);
        orderDeregistrationRequest.setOrderLVT("307");
        orderDeregistrationRequest.setToken(token);

        PowerMockito.doNothing().when(ValidatorService.class, "validateAndSimulate", orderDeregistrationRequest, header);
        willDoNothing().given(httpHeaderUtils).customizeHttpResponseHeader();

        OrderDeregistrationResponse testResponse = ewsSimulatorEndpoint.orderDeregistration(orderDeregistrationRequest,header);

        assertEquals(2,(int)testResponse.getError().get(0).getId());
        assertEquals("GENERIC CHECKOUT_ID ERROR",testResponse.getError().get(0).getMessage());
        assertNotNull(testResponse.getRequestId());
        assertEquals(PAN,testResponse.getPrimaryAccountNumber());

        verifyStatic();
        ValidatorService.validateAndSimulate(orderDeregistrationRequest, header);
        verify(httpHeaderUtils, times(1)).customizeHttpResponseHeader();
    }

    @Test
    public void testOrderDeregistration_CVVInitial3EndWith8() throws Exception {
        OrderDeregistrationRequest orderDeregistrationRequest = new OrderDeregistrationRequest();
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);
        orderDeregistrationRequest.setMerchant(merchant);
        orderDeregistrationRequest.setOrderLVT("308");
        orderDeregistrationRequest.setToken(token);

        PowerMockito.doNothing().when(ValidatorService.class, "validateAndSimulate", orderDeregistrationRequest, header);
        willDoNothing().given(httpHeaderUtils).customizeHttpResponseHeader();

        OrderDeregistrationResponse testResponse = ewsSimulatorEndpoint.orderDeregistration(orderDeregistrationRequest,header);

        assertEquals(4,(int)testResponse.getError().get(0).getId());
        assertEquals("CHECKOUT_ID INVALID",testResponse.getError().get(0).getMessage());
        assertNotNull(testResponse.getRequestId());
        assertEquals(PAN,testResponse.getPrimaryAccountNumber());

        verifyStatic();
        ValidatorService.validateAndSimulate(orderDeregistrationRequest, header);
        verify(httpHeaderUtils, times(1)).customizeHttpResponseHeader();
    }

    @Test
    public void testOrderDeregistration_CVInitial3VEndWith9() throws Exception {
        OrderDeregistrationRequest orderDeregistrationRequest = new OrderDeregistrationRequest();
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);
        orderDeregistrationRequest.setMerchant(merchant);
        orderDeregistrationRequest.setOrderLVT("309");
        orderDeregistrationRequest.setToken(token);

        PowerMockito.doNothing().when(ValidatorService.class, "validateAndSimulate", orderDeregistrationRequest, header);
        willDoNothing().given(httpHeaderUtils).customizeHttpResponseHeader();

        OrderDeregistrationResponse testResponse = ewsSimulatorEndpoint.orderDeregistration(orderDeregistrationRequest,header);

        assertEquals(6,(int)testResponse.getError().get(0).getId());
        assertEquals("CHECKOUT_ID NOT_FOUND", testResponse.getError().get(0).getMessage());
        assertNotNull(testResponse.getRequestId());
        assertEquals(PAN,testResponse.getPrimaryAccountNumber());

        verifyStatic();
        ValidatorService.validateAndSimulate(orderDeregistrationRequest, header);
        verify(httpHeaderUtils, times(1)).customizeHttpResponseHeader();
    }

    @Test
    public void testTokenize_simple() throws Exception {
        TokenizeRequest request = new TokenizeRequest();
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);
        request.setMerchant(merchant);
        request.setPrimaryAccountNumber(PAN);

        PowerMockito.doNothing().when(ValidatorService.class, "validateAndSimulate", request, header);
        willDoNothing().given(httpHeaderUtils).customizeHttpResponseHeader();

        TokenizeResponse response = ewsSimulatorEndpoint.tokenize(request, header);

        assertEquals("468498435168468", response.getToken());
        assertEquals(false, response.isTokenNewlyGenerated());
        assertNotNull(response.getRequestId());

        verifyStatic();
        ValidatorService.validateAndSimulate(request, header);
        verify(httpHeaderUtils, times(1)).customizeHttpResponseHeader();
    }

    @Test
    public void testTokenize_PANLast3digitsZero() throws Exception {
        TokenizeRequest request = new TokenizeRequest();
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);
        request.setMerchant(merchant);
        request.setPrimaryAccountNumber("615348948648000");

        PowerMockito.doNothing().when(ValidatorService.class, "validateAndSimulate", request, header);
        willDoNothing().given(httpHeaderUtils).customizeHttpResponseHeader();

        TokenizeResponse response = ewsSimulatorEndpoint.tokenize(request, header);

        assertEquals("468498435168000", response.getToken());
        assertEquals(true, response.isTokenNewlyGenerated());
        assertNotNull(response.getRequestId());

        verifyStatic();
        ValidatorService.validateAndSimulate(request, header);
        verify(httpHeaderUtils, times(1)).customizeHttpResponseHeader();
    }

    @Test
    public void testDeregistration_returnCvv2IfAsked() throws Exception {
        DeregistrationRequest deregistrationRequest = new DeregistrationRequest();
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);
        deregistrationRequest.setMerchant(merchant);
        deregistrationRequest.setRegId(registrationId);
        deregistrationRequest.setCardSecurityCodeRequested(true);

        PowerMockito.doNothing().when(ValidatorService.class, "validateAndSimulate", deregistrationRequest, header);
        willDoNothing().given(httpHeaderUtils).customizeHttpResponseHeader();

        DeregistrationResponse testResponse = ewsSimulatorEndpoint.deregistration(deregistrationRequest,header);

        assertNotNull(testResponse.getRequestId());
        assertEquals(token,testResponse.getToken());
        assertEquals(PAN,testResponse.getPrimaryAccountNumber());
        assertEquals(CVV,testResponse.getCardSecurityCode());
        assertEquals(expirationDate,testResponse.getExpirationDate());

        verifyStatic();
        ValidatorService.validateAndSimulate(deregistrationRequest, header);
        verify(httpHeaderUtils, times(1)).customizeHttpResponseHeader();
    }

    @Test
    public void testDeregistration_returnCvv2OnlyIfAsked() throws Exception {
        DeregistrationRequest deregistrationRequest = new DeregistrationRequest();
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);
        deregistrationRequest.setMerchant(merchant);
        deregistrationRequest.setRegId(registrationId);
        deregistrationRequest.setCardSecurityCodeRequested(false);

        PowerMockito.doNothing().when(ValidatorService.class, "validateAndSimulate", deregistrationRequest, header);
        willDoNothing().given(httpHeaderUtils).customizeHttpResponseHeader();

        DeregistrationResponse testResponse = ewsSimulatorEndpoint.deregistration(deregistrationRequest,header);

        assertNotNull(testResponse.getRequestId());
        assertEquals(token,testResponse.getToken());
        assertEquals(PAN,testResponse.getPrimaryAccountNumber());
        assertNull(testResponse.getCardSecurityCode());
        assertEquals(expirationDate,testResponse.getExpirationDate());

        verifyStatic();
        ValidatorService.validateAndSimulate(deregistrationRequest, header);
        verify(httpHeaderUtils, times(1)).customizeHttpResponseHeader();
    }

    @Test
    public void testDeregistration_DPAN_ANDROID() throws Exception {
        DeregistrationRequest deregistrationRequest = new DeregistrationRequest();
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);
        deregistrationRequest.setMerchant(merchant);
        deregistrationRequest.setRegId("615348948648641");
        deregistrationRequest.setCardSecurityCodeRequested(false);

        PowerMockito.doNothing().when(ValidatorService.class, "validateAndSimulate", deregistrationRequest, header);
        willDoNothing().given(httpHeaderUtils).customizeHttpResponseHeader();

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
        ValidatorService.validateAndSimulate(deregistrationRequest, header);
        verify(httpHeaderUtils, times(1)).customizeHttpResponseHeader();
    }

    @Test
    public void testDeregistration_DPAN_APPLE() throws Exception {
        DeregistrationRequest deregistrationRequest = new DeregistrationRequest();
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);
        deregistrationRequest.setMerchant(merchant);
        deregistrationRequest.setRegId("615348948648642");
        deregistrationRequest.setCardSecurityCodeRequested(false);

        PowerMockito.doNothing().when(ValidatorService.class, "validateAndSimulate", deregistrationRequest, header);
        willDoNothing().given(httpHeaderUtils).customizeHttpResponseHeader();

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
        ValidatorService.validateAndSimulate(deregistrationRequest, header);
        verify(httpHeaderUtils, times(1)).customizeHttpResponseHeader();
    }


    @Test
    public void testDeregistration_DPAN_SAMSUNG() throws Exception {
        DeregistrationRequest deregistrationRequest = new DeregistrationRequest();
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);
        deregistrationRequest.setMerchant(merchant);
        deregistrationRequest.setRegId("615348948648643");
        deregistrationRequest.setCardSecurityCodeRequested(false);

        PowerMockito.doNothing().when(ValidatorService.class, "validateAndSimulate", deregistrationRequest, header);
        willDoNothing().given(httpHeaderUtils).customizeHttpResponseHeader();

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
        ValidatorService.validateAndSimulate(deregistrationRequest, header);
        verify(httpHeaderUtils, times(1)).customizeHttpResponseHeader();
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

        PowerMockito.doNothing().when(ValidatorService.class, "validateAndSimulate", request, header);
        willDoNothing().given(httpHeaderUtils).customizeHttpResponseHeader();

        TokenInquiryResponse response = ewsSimulatorEndpoint.tokenInquiry(request, header);
        List<Token> tokens = response.getToken();
        assertNotNull(tokens.get(0));
        assertNull(tokens.get(1));

        verifyStatic();
        ValidatorService.validateAndSimulate(request, header);
        verify(httpHeaderUtils, times(1)).customizeHttpResponseHeader();
    }

    @Test
    public void testTokenRegistration() throws Exception {
        TokenRegistrationRequest request = new TokenRegistrationRequest();
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);
        request.setMerchant(merchant);
        request.setToken(token);

        PowerMockito.doNothing().when(ValidatorService.class, "validateAndSimulate", request, header);
        willDoNothing().given(httpHeaderUtils).customizeHttpResponseHeader();

        TokenRegistrationResponse response = ewsSimulatorEndpoint.tokenRegistration(request, header);
        assertNotNull(response.getRegId());

        verifyStatic();
        ValidatorService.validateAndSimulate(request, header);
        verify(httpHeaderUtils, times(1)).customizeHttpResponseHeader();
    }

    @Test
    public void testDecrypt_VerifoneWithPAN() throws Exception {
        DecryptRequest request = new DecryptRequest();
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);
        request.setMerchant(merchant);
        request.setMerchantRefId(merchantRefId);

        VerifoneCryptogram cryptogram = new VerifoneCryptogram();
        Card encryptedCard = new Card();
        encryptedCard.setPrimaryAccountNumber("123456789101112");
        encryptedCard.setExpirationDate("0818");
        cryptogram.setEncryptedCard(encryptedCard);
        cryptogram.setMerchantKeyType(VerifoneMerchantKeyType.SHARED);

        VerifoneTerminal terminal = new VerifoneTerminal();
        terminal.setRegisterId("1234");
        terminal.setLaneId("1234");
        terminal.setChainCode("1234");
        terminal.setMerchantId("1234");
        cryptogram.setTerminal(terminal);

        request.setVerifoneCryptogram(cryptogram);

        PowerMockito.doNothing().when(ValidatorService.class, "validateAndSimulate", request, header);
        willDoNothing().given(httpHeaderUtils).customizeHttpResponseHeader();

        DecryptResponse response = ewsSimulatorEndpoint.decrypt(request, header);
        assertEquals(merchantRefId, response.getMerchantRefId());
        assertEquals("211101987654321", response.getDecryptedCard().getPrimaryAccountNumber());
        assertEquals("0818", response.getDecryptedCard().getExpirationDate());

        verifyStatic();
        ValidatorService.validateAndSimulate(request, header);
        verify(httpHeaderUtils, times(1)).customizeHttpResponseHeader();
    }


    @Test
    public void testDecrypt_VerifoneWithTrack1() throws Exception {
        DecryptRequest request = new DecryptRequest();
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);
        request.setMerchant(merchant);
        request.setMerchantRefId(merchantRefId);

        VerifoneCryptogram cryptogram = new VerifoneCryptogram();
        Card encryptedCard = new Card();
        encryptedCard.setTrack1("123456");
        cryptogram.setEncryptedCard(encryptedCard);
        cryptogram.setMerchantKeyType(VerifoneMerchantKeyType.SHARED);

        VerifoneTerminal terminal = new VerifoneTerminal();
        terminal.setRegisterId("1234");
        terminal.setLaneId("1234");
        terminal.setChainCode("1234");
        terminal.setMerchantId("1234");
        cryptogram.setTerminal(terminal);

        request.setVerifoneCryptogram(cryptogram);

        PowerMockito.doNothing().when(ValidatorService.class, "validateAndSimulate", request, header);
        willDoNothing().given(httpHeaderUtils).customizeHttpResponseHeader();

        DecryptResponse response = ewsSimulatorEndpoint.decrypt(request, header);
        assertEquals(merchantRefId, response.getMerchantRefId());
        assertEquals("654321", response.getDecryptedCard().getTrack1());

        verifyStatic();
        ValidatorService.validateAndSimulate(request, header);
        verify(httpHeaderUtils, times(1)).customizeHttpResponseHeader();
    }


    @Test
    public void testDecrypt_VerifoneWithTrack2() throws Exception {
        DecryptRequest request = new DecryptRequest();
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);
        request.setMerchant(merchant);
        request.setMerchantRefId(merchantRefId);

        VerifoneCryptogram cryptogram = new VerifoneCryptogram();
        Card encryptedCard = new Card();
        encryptedCard.setTrack2("123456");
        cryptogram.setEncryptedCard(encryptedCard);
        cryptogram.setMerchantKeyType(VerifoneMerchantKeyType.SHARED);

        VerifoneTerminal terminal = new VerifoneTerminal();
        terminal.setRegisterId("1234");
        terminal.setLaneId("1234");
        terminal.setChainCode("1234");
        terminal.setMerchantId("1234");
        cryptogram.setTerminal(terminal);

        request.setVerifoneCryptogram(cryptogram);

        PowerMockito.doNothing().when(ValidatorService.class, "validateAndSimulate", request, header);
        willDoNothing().given(httpHeaderUtils).customizeHttpResponseHeader();

        DecryptResponse response = ewsSimulatorEndpoint.decrypt(request, header);
        assertEquals(merchantRefId, response.getMerchantRefId());
        assertEquals("654321", response.getDecryptedCard().getTrack2());

        verifyStatic();
        ValidatorService.validateAndSimulate(request, header);
        verify(httpHeaderUtils, times(1)).customizeHttpResponseHeader();
    }

    @Test
    public void testDecrypt_VoltageWithPAN() throws Exception {
        DecryptRequest request = new DecryptRequest();
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);
        request.setMerchant(merchant);
        request.setMerchantRefId(merchantRefId);

        VoltageCryptogram cryptogram = new VoltageCryptogram();
        Card encryptedCard = new Card();
        encryptedCard.setPrimaryAccountNumber("123456789101112");
        encryptedCard.setSecurityCode("123");
        cryptogram.setEncryptedCard(encryptedCard);

        request.setVoltageCryptogram(cryptogram);

        PowerMockito.doNothing().when(ValidatorService.class, "validateAndSimulate", request, header);
        willDoNothing().given(httpHeaderUtils).customizeHttpResponseHeader();

        DecryptResponse response = ewsSimulatorEndpoint.decrypt(request, header);
        assertEquals(merchantRefId, response.getMerchantRefId());
        assertEquals("211101987654321", response.getDecryptedCard().getPrimaryAccountNumber());
        assertEquals("123", response.getDecryptedCard().getSecurityCode());

        verifyStatic();
        ValidatorService.validateAndSimulate(request, header);
        verify(httpHeaderUtils, times(1)).customizeHttpResponseHeader();
    }


    @Test
    public void testDecrypt_VoltageWithTrack1() throws Exception {
        DecryptRequest request = new DecryptRequest();
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);
        request.setMerchant(merchant);
        request.setMerchantRefId(merchantRefId);

        VoltageCryptogram cryptogram = new VoltageCryptogram();
        Card encryptedCard = new Card();
        encryptedCard.setTrack1("123456");
        cryptogram.setEncryptedCard(encryptedCard);

        request.setVoltageCryptogram(cryptogram);

        PowerMockito.doNothing().when(ValidatorService.class, "validateAndSimulate", request, header);
        willDoNothing().given(httpHeaderUtils).customizeHttpResponseHeader();

        DecryptResponse response = ewsSimulatorEndpoint.decrypt(request, header);
        assertEquals(merchantRefId, response.getMerchantRefId());
        assertEquals("654321", response.getDecryptedCard().getTrack1());

        verifyStatic();
        ValidatorService.validateAndSimulate(request, header);
        verify(httpHeaderUtils, times(1)).customizeHttpResponseHeader();
    }


    @Test
    public void testDecrypt_VoltageWithTrack2() throws Exception {
        DecryptRequest request = new DecryptRequest();
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);
        request.setMerchant(merchant);
        request.setMerchantRefId(merchantRefId);

        VoltageCryptogram cryptogram = new VoltageCryptogram();
        Card encryptedCard = new Card();
        encryptedCard.setTrack2("123456");
        cryptogram.setEncryptedCard(encryptedCard);

        request.setVoltageCryptogram(cryptogram);

        PowerMockito.doNothing().when(ValidatorService.class, "validateAndSimulate", request, header);
        willDoNothing().given(httpHeaderUtils).customizeHttpResponseHeader();

        DecryptResponse response = ewsSimulatorEndpoint.decrypt(request, header);
        assertEquals(merchantRefId, response.getMerchantRefId());
        assertEquals("654321", response.getDecryptedCard().getTrack2());

        verifyStatic();
        ValidatorService.validateAndSimulate(request, header);
        verify(httpHeaderUtils, times(1)).customizeHttpResponseHeader();
    }

    @Test
    public void testEchoRequest() throws Exception {
        EchoRequest echoRequest = new EchoRequest();
        String test = "asdahfiuahofo";
        echoRequest.setTest(test);

        PowerMockito.doNothing().when(ValidatorService.class,"validateAndSimulate",echoRequest,header);

        EchoResponse echoResponse = ewsSimulatorEndpoint.echo(echoRequest,header);

        assertEquals(test,echoResponse.getResponse());
    }

    @Test
    public void testBatchTokenize() throws Exception {

        BatchTokenizeRequest request = new BatchTokenizeRequest();
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);
        request.setMerchant(merchant);

        Card card1 = new Card();
        card1.setPrimaryAccountNumber(PAN);

        Card card2 = new Card();
        card2.setPrimaryAccountNumber("615348948648000");

        Card card3 = new Card();
        card3.setPrimaryAccountNumber("615348948648004");

        request.getCard().add(card1);
        request.getCard().add(card2);
        request.getCard().add(card3);

        PowerMockito.doNothing().when(ValidatorService.class, "validateAndSimulate", request, header);
        willDoNothing().given(httpHeaderUtils).customizeHttpResponseHeader();

        BatchTokenizeResponse response = ewsSimulatorEndpoint.batchTokenize(request,header);
        assertNotNull(response.getToken());
        verifyStatic();
        ValidatorService.validateAndSimulate(request, header);
        verify(httpHeaderUtils, times(1)).customizeHttpResponseHeader();

    }


    @Test
    public void testBatchDetokenize() throws Exception {
        BatchDetokenizeRequest request = new BatchDetokenizeRequest();

        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);


        Token token1 = new Token();
        Token token2 = new Token();
        Token token3 = new Token();

        token1.setTokenValue(token);
        token2.setTokenValue("468498435168000");
        token3.setTokenValue("468498435168004");

        request.setMerchant(merchant);
        request.getToken().add(token1);
        request.getToken().add(token2);
        request.getToken().add(token3);

        PowerMockito.doNothing().when(ValidatorService.class, "validateAndSimulate", request, header);
        willDoNothing().given(httpHeaderUtils).customizeHttpResponseHeader();

        BatchDetokenizeResponse response = ewsSimulatorEndpoint.batchDetokenize(request,header);
        assertEquals( true,response.getCard().size() > 0);
        verifyStatic();
        ValidatorService.validateAndSimulate(request, header);
        verify(httpHeaderUtils, times(1)).customizeHttpResponseHeader();
    }

    @Test
    public void testECheckTokenize() throws Exception {
        ECheckTokenizeRequest request = new ECheckTokenizeRequest();
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);
        request.setMerchant(merchant);
        Account account = new Account();
        account.setAccountNumber("1234567890123");
        account.setAccountType(AccountType.CHECKING);
        account.setRoutingNumber("123456");
        request.setAccount(account);

        PowerMockito.doNothing().when(ValidatorService.class, "validateAndSimulate", request, header);
        willDoNothing().given(httpHeaderUtils).customizeHttpResponseHeader();

        ECheckTokenizeResponse response = ewsSimulatorEndpoint.echeckTokenize(request,header);
        assertEquals( true,response.getToken().getTokenValue().length() > 0);
        verifyStatic();
        ValidatorService.validateAndSimulate(request, header);
        verify(httpHeaderUtils, times(1)).customizeHttpResponseHeader();
    }

    @Test
    public void testECheckTokenizeError() throws Exception {
        ECheckTokenizeRequest request = new ECheckTokenizeRequest();
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);
        request.setMerchant(merchant);

        Account errorAccount = new Account();
        errorAccount.setAccountNumber("1234567890004");
        errorAccount.setAccountType(AccountType.CHECKING);
        errorAccount.setRoutingNumber("123456");
        request.setAccount(errorAccount);

        PowerMockito.doNothing().when(ValidatorService.class, "validateAndSimulate", request, header);
        willDoNothing().given(httpHeaderUtils).customizeHttpResponseHeader();

        ECheckTokenizeResponse response = ewsSimulatorEndpoint.echeckTokenize(request,header);
        assertEquals( true,response.getToken().getError().getId() > 0);
        verifyStatic();
        ValidatorService.validateAndSimulate(request, header);
        verify(httpHeaderUtils, times(1)).customizeHttpResponseHeader();
    }

    @Test
    public void testECheckDetokenize() throws Exception {
        ECheckDetokenizeRequest request = new ECheckDetokenizeRequest();
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);
        request.setMerchant(merchant);
        ECheckToken batchtoken = new ECheckToken();
        batchtoken.setTokenValue(token);
        request.setToken(batchtoken);

        PowerMockito.doNothing().when(ValidatorService.class, "validateAndSimulate", request, header);
        willDoNothing().given(httpHeaderUtils).customizeHttpResponseHeader();

        ECheckDetokenizeResponse response = ewsSimulatorEndpoint.eCheckDetokenizeResponse(request,header);
        assertEquals( true,response.getAccount().getAccountNumber().length() > 0);
        verifyStatic();
        ValidatorService.validateAndSimulate(request, header);
        verify(httpHeaderUtils, times(1)).customizeHttpResponseHeader();
    }


    @Test
    public void testOrderRegistration() throws Exception {

        OrderRegistrationRequest request = new OrderRegistrationRequest();
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);
        request.setMerchant(merchant);
        request.setCardSecurityCode("123");


        PowerMockito.doNothing().when(ValidatorService.class, "validateAndSimulate", request, header);
        willDoNothing().given(httpHeaderUtils).customizeHttpResponseHeader();

        OrderRegistrationResponse response = ewsSimulatorEndpoint.orderRegistration(request,header);
        assertEquals( true,response.getOrderLVT().startsWith("3"));
        verifyStatic();
        ValidatorService.validateAndSimulate(request, header);
        verify(httpHeaderUtils, times(1)).customizeHttpResponseHeader();
    }
}
