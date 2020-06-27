package com.worldpay.simulator.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ws.soap.SoapHeaderElement;

import com.worldpay.simulator.Account;
import com.worldpay.simulator.AccountType;
import com.worldpay.simulator.BatchDetokenizeRequest;
import com.worldpay.simulator.BatchDetokenizeResponse;
import com.worldpay.simulator.BatchTokenizeRequest;
import com.worldpay.simulator.BatchTokenizeResponse;
import com.worldpay.simulator.Card;
import com.worldpay.simulator.DecryptRequest;
import com.worldpay.simulator.DecryptResponse;
import com.worldpay.simulator.DeregistrationRequest;
import com.worldpay.simulator.DeregistrationResponse;
import com.worldpay.simulator.DetokenizeRequest;
import com.worldpay.simulator.DetokenizeResponse;
import com.worldpay.simulator.ECheckDetokenizeRequest;
import com.worldpay.simulator.ECheckDetokenizeResponse;
import com.worldpay.simulator.ECheckToken;
import com.worldpay.simulator.ECheckTokenizeRequest;
import com.worldpay.simulator.ECheckTokenizeResponse;
import com.worldpay.simulator.EchoRequest;
import com.worldpay.simulator.EchoResponse;
import com.worldpay.simulator.MerchantType;
import com.worldpay.simulator.OrderDeregistrationRequest;
import com.worldpay.simulator.OrderDeregistrationResponse;
import com.worldpay.simulator.OrderRegistrationRequest;
import com.worldpay.simulator.OrderRegistrationResponse;
import com.worldpay.simulator.RegistrationRequest;
import com.worldpay.simulator.RegistrationResponse;
import com.worldpay.simulator.SpringTestConfig;
import com.worldpay.simulator.Token;
import com.worldpay.simulator.TokenInquiryRequest;
import com.worldpay.simulator.TokenInquiryResponse;
import com.worldpay.simulator.TokenRegistrationRequest;
import com.worldpay.simulator.TokenRegistrationResponse;
import com.worldpay.simulator.TokenizeRequest;
import com.worldpay.simulator.TokenizeResponse;
import com.worldpay.simulator.VError;
import com.worldpay.simulator.VerifoneCryptogram;
import com.worldpay.simulator.VerifoneMerchantKeyType;
import com.worldpay.simulator.VerifoneTerminal;
import com.worldpay.simulator.VoltageCryptogram;
import com.worldpay.simulator.WalletType;
import com.worldpay.simulator.pojo.OutputFields;
import com.worldpay.simulator.service.SimulatorResponseService;
import com.worldpay.simulator.service.ValidatorService;
import com.worldpay.simulator.utils.HttpHeaderUtils;


// Test Case Id: TC15065; TC15066; TC15067; TC15068
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringTestConfig.class)
public class TestEWSSimulatorEndpoint {

    @MockBean
    HttpHeaderUtils httpHeaderUtils;

    @MockBean
    ValidatorService validatorService;

    @MockBean
    SimulatorResponseService simulatorResponseService;

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
    private String EcheckToken;

    private String APPLEPAN;
    private String APPLEToken;
    private String APPLERegId;

    private String ANDROIDPAN;
    private String ANDROIDToken;
    private String ANDROIDRegId;

    private String SAMSUNGPAN;
    private String SAMSUNGToken;
    private String SAMSUNGRegId;

    private String AMEXPAN;
    private String AMEXToken;
    private String AMEXRegId;


    @Before
    public void setup() {
        requestId = "f75b9c0f-5348-4621-acdc-a00861b25697";
        token = "468498435169468";
        PAN = "468498615349468";
        CVV = "736";
        expirationDate = "5001";
        rollupId = "1123";
        APPLE = "APPLE";
        ANDROID = "ANDROID";
        SAMSUNG = "SAMSUNG";
        CRYPTOGRAM = "2wABBJQ1AgAAAAAgJDUCAAAAAAA=";
        registrationId = "894864615348649";
        header = null;
        merchantRefId = "00012445653000";
        EcheckToken = "2468498435169468123";

        APPLEPAN = "4684983846512426268";
        APPLEToken= "4684982421564836268";
        APPLERegId= "8948646153487578626";

        ANDROIDPAN = "4684983846512429168";
        ANDROIDToken= "4684982421564839168";
        ANDROIDRegId = "8948646153487578619";

        SAMSUNGPAN ="4684983846512427368";
        SAMSUNGToken="4684982421564837368";
        SAMSUNGRegId= "8948646153487578637";

        AMEXPAN = "378810200335459";
        AMEXToken = "378810330025459";
        AMEXRegId = "0188739999799669545";
    }

    @Test
    public void testClearAllResponses() {
        ResponseEntity actual = ewsSimulatorEndpoint.clearAllResponses();
        verify(simulatorResponseService).clearAllResponses();
        assertTrue(actual.getStatusCode().is2xxSuccessful());
    }

    @Test
    public void testAddRegistrationResponse() {
        ResponseEntity actual = ewsSimulatorEndpoint.addRegistrationResponse("pan", "regId", "token", "new");
        verify(simulatorResponseService).addRegistrationResponseToMap(eq("pan"), any(RegistrationResponse.class));
        assertTrue(actual.getStatusCode().is2xxSuccessful());
    }

    @Test
    public void testAddTokenizeResponse() {
        ResponseEntity actual = ewsSimulatorEndpoint.addTokenizeResponse("pan", "token", "new");
        verify(simulatorResponseService).addTokenizeResponseToMap(eq("pan"), any(TokenizeResponse.class));
        assertTrue(actual.getStatusCode().is2xxSuccessful());
    }

    @Test
    public void testAddOrderRegistrationResponse() {
        ResponseEntity actual = ewsSimulatorEndpoint.addOrderRegistrationResponse("cvv", "lvt");
        verify(simulatorResponseService).addOrderRegistrationResponseToMap(eq("cvv"), any(OrderRegistrationResponse.class));
        assertTrue(actual.getStatusCode().is2xxSuccessful());
    }

    @Test
    public void testAddTokenRegistrationResponse() {
        ResponseEntity actual = ewsSimulatorEndpoint.addTokenRegistrationResponse("token", "regid");
        verify(simulatorResponseService).addTokenRegistrationResponseToMap(eq("token"), any(TokenRegistrationResponse.class));
        assertTrue(actual.getStatusCode().is2xxSuccessful());
    }

    @Test
    public void testAddBatchTokenizePanToTokenResponse() {
        ResponseEntity actual = ewsSimulatorEndpoint.addBatchTokenizePanToTokenResponse("pan", "token", "new");
        verify(simulatorResponseService).addBatchTokenizePanToTokenResponseToMap(eq("pan"), any(Token.class));
        assertTrue(actual.getStatusCode().is2xxSuccessful());
    }

    @Test
    public void testAddBatchDetokenizeTokenToPANResponse() {
        ResponseEntity actual = ewsSimulatorEndpoint.addBatchDetokenizeTokenToPanResponseToMap("pan", "token");
        verify(simulatorResponseService).addBatchDetokenizeTokenToPanResponseToMap("pan", "token");
        assertTrue(actual.getStatusCode().is2xxSuccessful());
    }

    @Test
    public void testAddEcheckTokenizePanToTokenResponse() {
        ResponseEntity actual = ewsSimulatorEndpoint.addECheckTokenizePanToTokenResponse("pan", "token", "new");
        verify(simulatorResponseService).addECheckTokenizePanToTokenResponseToMap(eq("pan"), any(ECheckToken.class));
        assertTrue(actual.getStatusCode().is2xxSuccessful());
    }

    @Test
    public void testAddEcheckTokenizeErrorTokenResponse() {
        ResponseEntity actual = ewsSimulatorEndpoint.addECheckTokenizeErrorResponse("pan", 103);
        ECheckToken token = new ECheckToken();
        VError vError = new VError();
        vError.setId(103);
        vError.setCode("INTERNAL_ERROR");
        vError.setMessage("Internal Error.");
        token.setError(vError);
        verify(simulatorResponseService).addECheckTokenizePanToErrorTokenResponseToMap(eq("pan"), any(ECheckToken.class));
        assertTrue(actual.getStatusCode().is2xxSuccessful());
    }

    @Test
    public void testAddECheckDetokenizeTokenToAccountResponse() {
        ResponseEntity actual = ewsSimulatorEndpoint.addECheckDetokenizeTokenToAccountResponse("token", "accNum", AccountType.CHECKING.value(), "routingNum");
        verify(simulatorResponseService).addECheckDetokenizeTokenToAccountResponseToMap(eq("token"), any(Account.class));
        assertTrue(actual.getStatusCode().is2xxSuccessful());
    }

    @Test
    public void testAddTokenInquiryPanToTokenResponse() {
        ResponseEntity actual = ewsSimulatorEndpoint.addTokenInquiryPanToTokenResponse("pan", "token", "new");
        verify(simulatorResponseService).addTokenInquiryPanToTokenResponseToMap(eq("pan"), any(Token.class));
        assertTrue(actual.getStatusCode().is2xxSuccessful());
    }

    @Test
    public void testAddDetokenizeResponse() {
        ResponseEntity actual = ewsSimulatorEndpoint.addDetokenizeResponse("token", "pan", "cvv", "expdate");
        verify(simulatorResponseService).addDetokenizeResponseToMap(eq("token"), any(DetokenizeResponse.class));
        assertTrue(actual.getStatusCode().is2xxSuccessful());
    }

    @Test
    public void testAddOrderDeregistrationResponse() {
        ResponseEntity actual = ewsSimulatorEndpoint.addOrderDeregistrationResponse("token", "pan", "cvv");
        verify(simulatorResponseService).addOrderDeregistrationResponseToMap(eq("token"), any(OrderDeregistrationResponse.class));
        assertTrue(actual.getStatusCode().is2xxSuccessful());
    }

    @Test
    public void testAddDeregistrationResponse() {
        ResponseEntity actual = ewsSimulatorEndpoint.addDeregistrationResponse("regId", "token", "pan", "cvv", "expdate", WalletType.ANDROID.value(), "eci", "cryptogram");
        verify(simulatorResponseService).addDeregistrationResponseToMap(eq("regId"), any(DeregistrationResponse.class));
        assertTrue(actual.getStatusCode().is2xxSuccessful());
    }

    @Test
    public void testAddRegistrationException() {
        ResponseEntity actual = ewsSimulatorEndpoint.addRegistrationException("pan", 101);
        verify(simulatorResponseService).addRegistrationExceptionToMap("pan", 101);
        assertTrue(actual.getStatusCode().is2xxSuccessful());
    }

    @Test
    public void testAddTokenizeException() {
        ResponseEntity actual = ewsSimulatorEndpoint.addTokenizeException("pan", 101);
        verify(simulatorResponseService).addTokenizeExceptionToMap("pan", 101);
        assertTrue(actual.getStatusCode().is2xxSuccessful());
    }

    @Test
    public void testAddOrderRegistrationException() {
        ResponseEntity actual = ewsSimulatorEndpoint.addOrderRegistrationException("cvv", 101);
        verify(simulatorResponseService).addOrderRegistrationExceptionToMap("cvv", 101);
        assertTrue(actual.getStatusCode().is2xxSuccessful());
    }

    @Test
    public void testAddTokenRegistrationException() {
        ResponseEntity actual = ewsSimulatorEndpoint.addTokenRegistrationException("token", 101);
        verify(simulatorResponseService).addTokenRegistrationExceptionToMap("token", 101);
        assertTrue(actual.getStatusCode().is2xxSuccessful());
    }

    @Test
    public void testAddBatchTokenizeException() {
        ResponseEntity actual = ewsSimulatorEndpoint.addBatchTokenizeException("pan", 101);
        verify(simulatorResponseService).addBatchTokenizeExceptionToMap("pan", 101);
        assertTrue(actual.getStatusCode().is2xxSuccessful());
    }

    @Test
    public void testAddBatchDetokenizeException() {
        ResponseEntity actual = ewsSimulatorEndpoint.addBatchDetokenizeException("token", 101);
        verify(simulatorResponseService).addBatchDetokenizeExceptionToMap("token", 101);
        assertTrue(actual.getStatusCode().is2xxSuccessful());
    }

    @Test
    public void testAddEcheckTokenizeException() {
        ResponseEntity actual = ewsSimulatorEndpoint.addEcheckTokenizeException("pan", 101);
        verify(simulatorResponseService).addECheckTokenizeExceptionToMap("pan", 101);
        assertTrue(actual.getStatusCode().is2xxSuccessful());
    }

    @Test
    public void testAddECheckDetokenizeException() {
        ResponseEntity actual = ewsSimulatorEndpoint.addECheckDetokenizeException("token", 101);
        verify(simulatorResponseService).addECheckDetokenizeExceptionToMap("token", 101);
        assertTrue(actual.getStatusCode().is2xxSuccessful());
    }

    @Test
    public void testAddDetokenizeException() {
        ResponseEntity actual = ewsSimulatorEndpoint.addDetokenizeException("token", 101);
        verify(simulatorResponseService).addDetokenizeExceptionToMap("token", 101);
        assertTrue(actual.getStatusCode().is2xxSuccessful());
    }

    @Test
    public void testAddOrderDeregistrationException() {
        ResponseEntity actual = ewsSimulatorEndpoint.addOrderDeregistrationException("token", 101);
        verify(simulatorResponseService).addOrderDeregistrationExceptionToMap("token", 101);
        assertTrue(actual.getStatusCode().is2xxSuccessful());
    }

    @Test
    public void testAddDeregistrationException() {
        ResponseEntity actual = ewsSimulatorEndpoint.addDeregistrationException("regId", 101);
        verify(simulatorResponseService).addDeregistrationExceptionToMap("regId", 101);
        assertTrue(actual.getStatusCode().is2xxSuccessful());
    }




    @Test
    public void testRegistration() throws Exception {
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);

        RegistrationRequest request = new RegistrationRequest();
        request.setMerchantRefId(merchantRefId);
        request.setPrimaryAccountNumber(ANDROIDPAN);
        request.setWalletType(WalletType.fromValue(ANDROID));
        request.setCryptogram(CRYPTOGRAM.getBytes());
        request.setMerchant(merchant);

        willDoNothing().given(validatorService).validateRequest(request, header);
        willDoNothing().given(httpHeaderUtils).customizeHttpResponseHeader();

        RegistrationResponse response = ewsSimulatorEndpoint.registration(request, header);

        assertEquals(ANDROIDToken, response.getToken());
        assertEquals(ANDROIDRegId, response.getRegId());
        assertEquals(false, response.isTokenNewlyGenerated());
        assertNotNull(response.getRequestId());

        verify(validatorService, times(1)).validateRequest(request, header);
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

        willDoNothing().given(validatorService).validateRequest(request, header);
        willDoNothing().given(httpHeaderUtils).customizeHttpResponseHeader();

        RegistrationResponse response = ewsSimulatorEndpoint.registration(request, header);

        assertEquals("615348468498000", response.getToken());
        assertEquals("8435169999051350008", response.getRegId());
        assertEquals(false, response.isTokenNewlyGenerated());
        assertNotNull(response.getRequestId());

        verify(validatorService, times(1)).validateRequest(request, header);
        verify(httpHeaderUtils, times(1)).customizeHttpResponseHeader();

    }

    @Test
    public void testDetokenize_success_with_ExpirationDateRequested_CVV2Requested() throws Exception {
        DetokenizeRequest request = new DetokenizeRequest();
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);
        request.setMerchant(merchant);
        request.setToken(token);
        request.setCVV2Requested(true);
        request.setExpirationDateRequested(true);

        willDoNothing().given(validatorService).validateRequest(request, header);
        willDoNothing().given(httpHeaderUtils).customizeHttpResponseHeader();

        DetokenizeResponse testResponse = ewsSimulatorEndpoint.detokenize(request,header);

        assertEquals(PAN,testResponse.getPrimaryAccountNumber());
        assertNotNull(testResponse.getRequestId());
        assertEquals("946",testResponse.getCardSecurityCode());
        assertEquals(expirationDate,testResponse.getExpirationDate());

        verify(validatorService, times(1)).validateRequest(request, header);
        verify(httpHeaderUtils, times(1)).customizeHttpResponseHeader();
    }

    @Test
    public void testDetokenize_success_without_ExpirationDateRequested_CVV2Requested() throws Exception {
        DetokenizeRequest request = new DetokenizeRequest();
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);
        request.setMerchant(merchant);
        request.setToken(token);
        request.setCVV2Requested(false);
        request.setExpirationDateRequested(false);

        willDoNothing().given(validatorService).validateRequest(request, header);
        willDoNothing().given(httpHeaderUtils).customizeHttpResponseHeader();

        DetokenizeResponse testResponse = ewsSimulatorEndpoint.detokenize(request,header);

        assertEquals(PAN,testResponse.getPrimaryAccountNumber());
        assertNotNull(testResponse.getRequestId());
        assertNull(testResponse.getCardSecurityCode());
        assertNull(testResponse.getExpirationDate());

        verify(validatorService, times(1)).validateRequest(request, header);
        verify(httpHeaderUtils, times(1)).customizeHttpResponseHeader();
    }

    @Test
    public void testOrderDeregistration_CVVInitialNot3() throws Exception {
        OrderDeregistrationRequest request = new OrderDeregistrationRequest();
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);
        request.setMerchant(merchant);
        request.setOrderLVT(CVV);
        request.setToken(token);

        willDoNothing().given(validatorService).validateRequest(request, header);
        willDoNothing().given(httpHeaderUtils).customizeHttpResponseHeader();

        OrderDeregistrationResponse testResponse = ewsSimulatorEndpoint.orderDeregistration(request,header);

        assertEquals(PAN,testResponse.getPrimaryAccountNumber());
        assertNotNull(testResponse.getRequestId());

        verify(validatorService, times(1)).validateRequest(request, header);
        verify(httpHeaderUtils, times(1)).customizeHttpResponseHeader();
    }

    @Test
    public void testOrderDeregistration_InvalidToken() throws Exception {
        OrderDeregistrationRequest request = new OrderDeregistrationRequest();
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);
        request.setMerchant(merchant);
        request.setOrderLVT(CVV);
        request.setToken(token);

        willDoNothing().given(validatorService).validateRequest(request, header);
        willDoNothing().given(httpHeaderUtils).customizeHttpResponseHeader();

        OrderDeregistrationResponse testResponse = ewsSimulatorEndpoint.orderDeregistration(request,header);

        assertEquals(PAN,testResponse.getPrimaryAccountNumber());
        assertNotNull(testResponse.getRequestId());

        verify(validatorService, times(1)).validateRequest(request, header);
        verify(httpHeaderUtils, times(1)).customizeHttpResponseHeader();
    }

    @Test
    public void testOrderDeregistration_CVVInitial3() throws Exception {
        OrderDeregistrationRequest request = new OrderDeregistrationRequest();
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);
        request.setMerchant(merchant);
        request.setOrderLVT("303");
        request.setToken(token);

        willDoNothing().given(validatorService).validateRequest(request, header);
        willDoNothing().given(httpHeaderUtils).customizeHttpResponseHeader();

        OrderDeregistrationResponse testResponse = ewsSimulatorEndpoint.orderDeregistration(request,header);

        assertEquals(PAN,testResponse.getPrimaryAccountNumber());
        assertNotNull(testResponse.getRequestId());
        assertEquals("946",testResponse.getCardSecurityCode());

        verify(validatorService, times(1)).validateRequest(request, header);
        verify(httpHeaderUtils, times(1)).customizeHttpResponseHeader();
    }

    @Test
    public void testOrderDeregistration_CVVInitial3EndWith6() throws Exception {
        OrderDeregistrationRequest request = new OrderDeregistrationRequest();
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);
        request.setMerchant(merchant);
        request.setOrderLVT("306");
        request.setToken(token);

        willDoNothing().given(validatorService).validateRequest(request, header);
        willDoNothing().given(httpHeaderUtils).customizeHttpResponseHeader();

        OrderDeregistrationResponse testResponse = ewsSimulatorEndpoint.orderDeregistration(request,header);

        assertEquals(9999,(int)testResponse.getError().get(0).getId());
        assertEquals("GENERIC CHECKOUT_ID ERROR",testResponse.getError().get(0).getMessage());
        assertNotNull(testResponse.getRequestId());
        assertEquals(PAN,testResponse.getPrimaryAccountNumber());

        verify(validatorService, times(1)).validateRequest(request, header);
        verify(httpHeaderUtils, times(1)).customizeHttpResponseHeader();
    }

    @Test
    public void testOrderDeregistration_CVVInitial3EndWith7() throws Exception {
        OrderDeregistrationRequest request = new OrderDeregistrationRequest();
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);
        request.setMerchant(merchant);
        request.setOrderLVT("307");
        request.setToken(token);

        willDoNothing().given(validatorService).validateRequest(request, header);
        willDoNothing().given(httpHeaderUtils).customizeHttpResponseHeader();

        OrderDeregistrationResponse testResponse = ewsSimulatorEndpoint.orderDeregistration(request,header);

        assertEquals(2,(int)testResponse.getError().get(0).getId());
        assertEquals("GENERIC CHECKOUT_ID ERROR",testResponse.getError().get(0).getMessage());
        assertNotNull(testResponse.getRequestId());
        assertEquals(PAN,testResponse.getPrimaryAccountNumber());

        verify(validatorService, times(1)).validateRequest(request, header);
        verify(httpHeaderUtils, times(1)).customizeHttpResponseHeader();
    }

    @Test
    public void testOrderDeregistration_CVVInitial3EndWith8() throws Exception {
        OrderDeregistrationRequest request = new OrderDeregistrationRequest();
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);
        request.setMerchant(merchant);
        request.setOrderLVT("308");
        request.setToken(token);

        willDoNothing().given(validatorService).validateRequest(request, header);
        willDoNothing().given(httpHeaderUtils).customizeHttpResponseHeader();

        OrderDeregistrationResponse testResponse = ewsSimulatorEndpoint.orderDeregistration(request,header);

        assertEquals(4,(int)testResponse.getError().get(0).getId());
        assertEquals("CHECKOUT_ID INVALID",testResponse.getError().get(0).getMessage());
        assertNotNull(testResponse.getRequestId());
        assertEquals(PAN,testResponse.getPrimaryAccountNumber());

        verify(validatorService, times(1)).validateRequest(request, header);
        verify(httpHeaderUtils, times(1)).customizeHttpResponseHeader();
    }

    @Test
    public void testOrderDeregistration_CVInitial3VEndWith9() throws Exception {
        OrderDeregistrationRequest request = new OrderDeregistrationRequest();
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);
        request.setMerchant(merchant);
        request.setOrderLVT("309");
        request.setToken(token);

        willDoNothing().given(validatorService).validateRequest(request, header);
        willDoNothing().given(httpHeaderUtils).customizeHttpResponseHeader();

        OrderDeregistrationResponse testResponse = ewsSimulatorEndpoint.orderDeregistration(request,header);

        assertEquals(6,(int)testResponse.getError().get(0).getId());
        assertEquals("CHECKOUT_ID NOT_FOUND", testResponse.getError().get(0).getMessage());
        assertNotNull(testResponse.getRequestId());
        assertEquals(PAN,testResponse.getPrimaryAccountNumber());

        verify(validatorService, times(1)).validateRequest(request, header);
        verify(httpHeaderUtils, times(1)).customizeHttpResponseHeader();
    }

    @Test
    public void testTokenize_simple() throws Exception {
        TokenizeRequest request = new TokenizeRequest();
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);
        request.setMerchant(merchant);
        request.setPrimaryAccountNumber(PAN);

        willDoNothing().given(validatorService).validateRequest(request, header);
        willDoNothing().given(httpHeaderUtils).customizeHttpResponseHeader();

        TokenizeResponse response = ewsSimulatorEndpoint.tokenize(request, header);

        assertEquals(token, response.getToken());
        assertEquals(false, response.isTokenNewlyGenerated());
        assertNotNull(response.getRequestId());

        verify(validatorService, times(1)).validateRequest(request, header);
        verify(httpHeaderUtils, times(1)).customizeHttpResponseHeader();
    }

    @Test
    public void testTokenize_PANLast3digitsZero() throws Exception {
        TokenizeRequest request = new TokenizeRequest();
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);
        request.setMerchant(merchant);
        request.setPrimaryAccountNumber("615348948648000");

        willDoNothing().given(validatorService).validateRequest(request, header);
        willDoNothing().given(httpHeaderUtils).customizeHttpResponseHeader();

        TokenizeResponse response = ewsSimulatorEndpoint.tokenize(request, header);

        assertEquals("615348468498000", response.getToken());
        assertEquals(false, response.isTokenNewlyGenerated());
        assertNotNull(response.getRequestId());

        verify(validatorService, times(1)).validateRequest(request, header);
        verify(httpHeaderUtils, times(1)).customizeHttpResponseHeader();
    }

    @Test
    public void testDeregistration_returnCvv2IfAsked() throws Exception {
        DeregistrationRequest request = new DeregistrationRequest();
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);
        request.setMerchant(merchant);
        request.setRegId(SAMSUNGRegId);
        request.setCardSecurityCodeRequested(true);

        willDoNothing().given(validatorService).validateRequest(request, header);
        willDoNothing().given(httpHeaderUtils).customizeHttpResponseHeader();

        DeregistrationResponse testResponse = ewsSimulatorEndpoint.deregistration(request,header);

        assertNotNull(testResponse.getRequestId());
        assertEquals(SAMSUNGToken,testResponse.getToken());
        assertEquals(SAMSUNGPAN,testResponse.getPrimaryAccountNumber());
        assertEquals(CVV,testResponse.getCardSecurityCode());
        assertEquals(expirationDate,testResponse.getExpirationDate());

        verify(validatorService, times(1)).validateRequest(request, header);
        verify(httpHeaderUtils, times(1)).customizeHttpResponseHeader();
    }

    @Test
    public void testDeregistration_000CVV() throws InterruptedException {
        String regId = "8948646153487578090";
        DeregistrationRequest request = new DeregistrationRequest();
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);
        request.setMerchant(merchant);
        request.setRegId(regId);
        request.setCardSecurityCodeRequested(true);

        willDoNothing().given(validatorService).validateRequest(request, header);
        willDoNothing().given(httpHeaderUtils).customizeHttpResponseHeader();
        DeregistrationResponse testResponse = ewsSimulatorEndpoint.deregistration(request,header);

        assertNotNull(testResponse.getRequestId());
        assertEquals("4684982421564830908",testResponse.getToken());
        assertEquals("4684983846512420908",testResponse.getPrimaryAccountNumber());
        assertNull(testResponse.getCardSecurityCode());
        assertEquals(expirationDate,testResponse.getExpirationDate());

        verify(validatorService, times(1)).validateRequest(request, header);
        verify(httpHeaderUtils, times(1)).customizeHttpResponseHeader();
    }

    @Test
    public void testDeregistration_returnCvv2OnlyIfAsked() throws Exception {
        DeregistrationRequest request = new DeregistrationRequest();
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);
        request.setMerchant(merchant);
        request.setRegId(APPLERegId);
        request.setCardSecurityCodeRequested(false);

        willDoNothing().given(validatorService).validateRequest(request, header);
        willDoNothing().given(httpHeaderUtils).customizeHttpResponseHeader();

        DeregistrationResponse testResponse = ewsSimulatorEndpoint.deregistration(request,header);

        assertNotNull(testResponse.getRequestId());
        assertEquals(APPLEToken,testResponse.getToken());
        assertEquals(APPLEPAN,testResponse.getPrimaryAccountNumber());
        assertNull(testResponse.getCardSecurityCode());
        assertEquals(expirationDate,testResponse.getExpirationDate());

        verify(validatorService, times(1)).validateRequest(request, header);
        verify(httpHeaderUtils, times(1)).customizeHttpResponseHeader();
    }

    @Test
    public void testDeregistration_DPAN_APPLE() throws Exception {
        DeregistrationRequest request = new DeregistrationRequest();
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);
        request.setMerchant(merchant);
        request.setRegId(APPLERegId);
        request.setCardSecurityCodeRequested(false);

        willDoNothing().given(validatorService).validateRequest(request, header);
        willDoNothing().given(httpHeaderUtils).customizeHttpResponseHeader();

        DeregistrationResponse testResponse = ewsSimulatorEndpoint.deregistration(request,header);
        assertNotNull(testResponse.getRequestId());
        assertEquals(APPLEPAN,testResponse.getPrimaryAccountNumber());
        assertEquals(APPLEToken,testResponse.getToken());
        assertNull(testResponse.getCardSecurityCode());
        assertNull(testResponse.getCardSecurityCode());
        assertEquals(expirationDate,testResponse.getExpirationDate());
        assertEquals(APPLE,testResponse.getWalletType().value());
        assertEquals("05",testResponse.getElectronicCommerceIndicator());
        assertEquals(CRYPTOGRAM,new Base64().encodeAsString(testResponse.getCryptogram()));

        verify(validatorService, times(1)).validateRequest(request, header);
        verify(httpHeaderUtils, times(1)).customizeHttpResponseHeader();
    }

    @Test
    public void testDeregistration_DPAN__ANDROID() throws Exception {
        DeregistrationRequest request = new DeregistrationRequest();
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);
        request.setMerchant(merchant);
        request.setRegId(ANDROIDRegId);
        request.setCardSecurityCodeRequested(false);

        willDoNothing().given(validatorService).validateRequest(request, header);
        willDoNothing().given(httpHeaderUtils).customizeHttpResponseHeader();

        DeregistrationResponse testResponse = ewsSimulatorEndpoint.deregistration(request,header);

        assertNotNull(testResponse.getRequestId());
        assertEquals(ANDROIDToken,testResponse.getToken());
        assertEquals(ANDROIDPAN,testResponse.getPrimaryAccountNumber());
        assertNull(testResponse.getCardSecurityCode());
        assertNull(testResponse.getCardSecurityCode());
        assertEquals(expirationDate,testResponse.getExpirationDate());
        assertEquals(ANDROID,testResponse.getWalletType().value());
        assertEquals("07",testResponse.getElectronicCommerceIndicator());
        assertEquals(CRYPTOGRAM,new Base64().encodeAsString(testResponse.getCryptogram()));

        verify(validatorService, times(1)).validateRequest(request, header);
        verify(httpHeaderUtils, times(1)).customizeHttpResponseHeader();
    }


    @Test
    public void testDeregistration_DPAN_SAMSUNG() throws Exception {
        DeregistrationRequest request = new DeregistrationRequest();
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);
        request.setMerchant(merchant);
        request.setRegId(SAMSUNGRegId);
        request.setCardSecurityCodeRequested(false);

        willDoNothing().given(validatorService).validateRequest(request, header);
        willDoNothing().given(httpHeaderUtils).customizeHttpResponseHeader();

        DeregistrationResponse testResponse = ewsSimulatorEndpoint.deregistration(request,header);

        assertNotNull(testResponse.getRequestId());
        assertEquals(SAMSUNGToken,testResponse.getToken());
        assertEquals(SAMSUNGPAN,testResponse.getPrimaryAccountNumber());
        assertNull(testResponse.getCardSecurityCode());
        assertNull(testResponse.getCardSecurityCode());
        assertEquals(expirationDate,testResponse.getExpirationDate());
        assertEquals(SAMSUNG,testResponse.getWalletType().value());
        assertEquals("07",testResponse.getElectronicCommerceIndicator());
        assertEquals(CRYPTOGRAM,new Base64().encodeAsString(testResponse.getCryptogram()));

        verify(validatorService, times(1)).validateRequest(request, header);
        verify(httpHeaderUtils, times(1)).customizeHttpResponseHeader();
    }

    @Test
    public void testDeregistration_DPAN_AMEX() throws Exception {
        DeregistrationRequest request = new DeregistrationRequest();
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);
        request.setMerchant(merchant);
        request.setRegId(AMEXRegId);
        request.setCardSecurityCodeRequested(false);

        willDoNothing().given(validatorService).validateRequest(request, header);
        willDoNothing().given(httpHeaderUtils).customizeHttpResponseHeader();

        DeregistrationResponse testResponse = ewsSimulatorEndpoint.deregistration(request,header);

        assertNotNull(testResponse.getRequestId());
        assertEquals(AMEXToken,testResponse.getToken());
        assertEquals(AMEXPAN,testResponse.getPrimaryAccountNumber());
        assertNull(testResponse.getCardSecurityCode());
        assertNull(testResponse.getCardSecurityCode());
        assertEquals(expirationDate,testResponse.getExpirationDate());

        verify(validatorService, times(1)).validateRequest(request, header);
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
        card1.setPrimaryAccountNumber("1234567891011133");
        cards.add(card1);

        Card card2 = new Card();
        card2.setPrimaryAccountNumber("1234567891010000");
        cards.add(card2);

        willDoNothing().given(validatorService).validateRequest(request, header);
        willDoNothing().given(httpHeaderUtils).customizeHttpResponseHeader();

        TokenInquiryResponse response = ewsSimulatorEndpoint.tokenInquiry(request, header);
        List<Token> tokens = response.getToken();
        assertNotNull(tokens.get(0));
        assertNull(tokens.get(1));

        verify(validatorService, times(1)).validateRequest(request, header);
        verify(httpHeaderUtils, times(1)).customizeHttpResponseHeader();
    }

    @Test
    public void testTokenRegistration() throws Exception {
        TokenRegistrationRequest request = new TokenRegistrationRequest();
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);
        request.setMerchant(merchant);
        request.setToken(token);

        willDoNothing().given(validatorService).validateRequest(request, header);
        willDoNothing().given(httpHeaderUtils).customizeHttpResponseHeader();

        TokenRegistrationResponse response = ewsSimulatorEndpoint.tokenRegistration(request, header);
        assertNotNull(response.getRegId());

        verify(validatorService, times(1)).validateRequest(request, header);
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

        willDoNothing().given(validatorService).validateRequest(request, header);
        willDoNothing().given(httpHeaderUtils).customizeHttpResponseHeader();

        DecryptResponse response = ewsSimulatorEndpoint.decrypt(request, header);
        assertEquals(merchantRefId, response.getMerchantRefId());
        assertEquals("211101987654321", response.getDecryptedCard().getPrimaryAccountNumber());
        assertEquals("0818", response.getDecryptedCard().getExpirationDate());

        verify(validatorService, times(1)).validateRequest(request, header);
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

        willDoNothing().given(validatorService).validateRequest(request, header);
        willDoNothing().given(httpHeaderUtils).customizeHttpResponseHeader();

        DecryptResponse response = ewsSimulatorEndpoint.decrypt(request, header);
        assertEquals(merchantRefId, response.getMerchantRefId());
        assertEquals("654321", response.getDecryptedCard().getTrack1());

        verify(validatorService, times(1)).validateRequest(request, header);
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

        willDoNothing().given(validatorService).validateRequest(request, header);
        willDoNothing().given(httpHeaderUtils).customizeHttpResponseHeader();

        DecryptResponse response = ewsSimulatorEndpoint.decrypt(request, header);
        assertEquals(merchantRefId, response.getMerchantRefId());
        assertEquals("654321", response.getDecryptedCard().getTrack2());

        verify(validatorService, times(1)).validateRequest(request, header);
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

        willDoNothing().given(validatorService).validateRequest(request, header);
        willDoNothing().given(httpHeaderUtils).customizeHttpResponseHeader();

        DecryptResponse response = ewsSimulatorEndpoint.decrypt(request, header);
        assertEquals(merchantRefId, response.getMerchantRefId());
        assertEquals("211101987654321", response.getDecryptedCard().getPrimaryAccountNumber());
        assertEquals("123", response.getDecryptedCard().getSecurityCode());

        verify(validatorService, times(1)).validateRequest(request, header);
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

        willDoNothing().given(validatorService).validateRequest(request, header);
        willDoNothing().given(httpHeaderUtils).customizeHttpResponseHeader();

        DecryptResponse response = ewsSimulatorEndpoint.decrypt(request, header);
        assertEquals(merchantRefId, response.getMerchantRefId());
        assertEquals("654321", response.getDecryptedCard().getTrack1());

        verify(validatorService, times(1)).validateRequest(request, header);
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

        willDoNothing().given(validatorService).validateRequest(request, header);
        willDoNothing().given(httpHeaderUtils).customizeHttpResponseHeader();

        DecryptResponse response = ewsSimulatorEndpoint.decrypt(request, header);
        assertEquals(merchantRefId, response.getMerchantRefId());
        assertEquals("654321", response.getDecryptedCard().getTrack2());

        verify(validatorService, times(1)).validateRequest(request, header);
        verify(httpHeaderUtils, times(1)).customizeHttpResponseHeader();
    }

    @Test
    public void testEchoRequest() throws Exception {
        EchoRequest request = new EchoRequest();
        String test = "asdahfiuahofo";
        request.setTest(test);

        willDoNothing().given(validatorService).validateRequest(request, header);

        EchoResponse echoResponse = ewsSimulatorEndpoint.echo(request,header);

        assertEquals(test,echoResponse.getResponse());
        assertNotNull(echoResponse.getProjectVersion());
        assertNotNull(echoResponse.getBuildNumber());
        assertNotNull(echoResponse.getRevisionNumber());
        assertNotNull(echoResponse.getHostEnvironment());
        verify(validatorService, times(1)).validateRequest(request, header);

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

        willDoNothing().given(validatorService).validateRequest(request, header);
        willDoNothing().given(httpHeaderUtils).customizeHttpResponseHeader();

        BatchTokenizeResponse response = ewsSimulatorEndpoint.batchTokenize(request,header);
        assertNotNull(response.getToken());
        verify(validatorService, times(1)).validateRequest(request, header);
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

        willDoNothing().given(validatorService).validateRequest(request, header);
        willDoNothing().given(httpHeaderUtils).customizeHttpResponseHeader();

        BatchDetokenizeResponse response = ewsSimulatorEndpoint.batchDetokenize(request,header);
        assertEquals( true,response.getCard().size() > 0);
        verify(validatorService, times(1)).validateRequest(request, header);
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

        willDoNothing().given(validatorService).validateRequest(request, header);
        willDoNothing().given(httpHeaderUtils).customizeHttpResponseHeader();

        ECheckTokenizeResponse response = ewsSimulatorEndpoint.echeckTokenize(request,header);
        assertEquals( true,response.getToken().getTokenValue().length() > 0);
        verify(validatorService, times(1)).validateRequest(request, header);
        verify(httpHeaderUtils, times(1)).customizeHttpResponseHeader();
    }

    @Ignore
    @Test
    public void testECheckTokenizeError() throws Exception {
        ECheckTokenizeRequest request = new ECheckTokenizeRequest();
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);
        request.setMerchant(merchant);

        Account errorAccount = new Account();
        errorAccount.setAccountNumber("123456781044");
        errorAccount.setAccountType(AccountType.CHECKING);
        errorAccount.setRoutingNumber("123456789");
        request.setAccount(errorAccount);

        willDoNothing().given(validatorService).validateRequest(request, header);
        willDoNothing().given(httpHeaderUtils).customizeHttpResponseHeader();

        ECheckTokenizeResponse response = ewsSimulatorEndpoint.echeckTokenize(request,header);
        assertEquals( true,response.getToken().getError().getId() > 0);
        verify(validatorService, times(1)).validateRequest(request, header);
        verify(httpHeaderUtils, times(1)).customizeHttpResponseHeader();
    }

    @Test
    public void testECheckDetokenize() throws Exception {
        ECheckDetokenizeRequest request = new ECheckDetokenizeRequest();
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);
        request.setMerchant(merchant);
        ECheckToken batchtoken = new ECheckToken();
        batchtoken.setTokenValue(EcheckToken);
        request.setToken(batchtoken);

        willDoNothing().given(validatorService).validateRequest(request, header);
        willDoNothing().given(httpHeaderUtils).customizeHttpResponseHeader();

        ECheckDetokenizeResponse response = ewsSimulatorEndpoint.eCheckDetokenizeResponse(request,header);
        assertEquals( true,response.getAccount().getAccountNumber().length() > 0);
        verify(validatorService, times(1)).validateRequest(request, header);
        verify(httpHeaderUtils, times(1)).customizeHttpResponseHeader();
    }





    @Test
    public void testOrderRegistration() throws Exception {

        OrderRegistrationRequest request = new OrderRegistrationRequest();
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);
        request.setMerchant(merchant);
        request.setCardSecurityCode("123");


        willDoNothing().given(validatorService).validateRequest(request, header);
        willDoNothing().given(httpHeaderUtils).customizeHttpResponseHeader();

        OrderRegistrationResponse response = ewsSimulatorEndpoint.orderRegistration(request,header);
        String orderLVT = response.getOrderLVT();
        assertEquals( true, orderLVT.startsWith("3"));
        assertEquals("312312312312312312", orderLVT);
        assertEquals(18, orderLVT.length());



        verify(validatorService, times(1)).validateRequest(request, header);
        verify(httpHeaderUtils, times(1)).customizeHttpResponseHeader();
    }

    @Test
    public void testOrderRegistrationEmptyCVV() throws Exception {

        OrderRegistrationRequest request = new OrderRegistrationRequest();
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);
        request.setMerchant(merchant);
        request.setCardSecurityCode("");


        willDoNothing().given(validatorService).validateRequest(request, header);
        willDoNothing().given(httpHeaderUtils).customizeHttpResponseHeader();

        OrderRegistrationResponse response = ewsSimulatorEndpoint.orderRegistration(request,header);
        String orderLVT = response.getOrderLVT();
        assertEquals( true, orderLVT.startsWith("3"));
        assertEquals("300000000000000000", orderLVT);
        assertEquals(18, orderLVT.length());



        verify(validatorService, times(1)).validateRequest(request, header);
        verify(httpHeaderUtils, times(1)).customizeHttpResponseHeader();
    }

    @Test
    public void testOrderRegistrationCVVLong() throws Exception {

        OrderRegistrationRequest request = new OrderRegistrationRequest();
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);
        request.setMerchant(merchant);
        request.setCardSecurityCode("123456789123456789");


        willDoNothing().given(validatorService).validateRequest(request, header);
        willDoNothing().given(httpHeaderUtils).customizeHttpResponseHeader();

        OrderRegistrationResponse response = ewsSimulatorEndpoint.orderRegistration(request,header);
        String orderLVT = response.getOrderLVT();
        assertEquals( true, orderLVT.startsWith("3"));
        assertEquals("312345678912345678", orderLVT);
        assertEquals(18, orderLVT.length());



        verify(validatorService, times(1)).validateRequest(request, header);
        verify(httpHeaderUtils, times(1)).customizeHttpResponseHeader();
    }

    @Test
    public void testInputPAN() {
        OutputFields actual = ewsSimulatorEndpoint.inputPAN("1234567890123456");
        assertEquals("1234562109873456", actual.getToken());
        assertEquals("6543219992109876543", actual.getRegId());
        assertEquals(false, actual.getTokenNewlyGenerated());
    }

    @Test
    public void testInputToken() {
        OutputFields actual = ewsSimulatorEndpoint.inputToken("1234562109873456");
        assertEquals("1234567890123456", actual.getPAN());
        assertEquals("6543219992109876543", actual.getRegId());
        assertEquals("5001", actual.getExpDate());
        assertEquals("345", actual.getCVV());
    }

    @Test
    public void testInputRegId() {
        OutputFields actual = ewsSimulatorEndpoint.inputRegId("6543219992109876543");
        assertEquals("1234567890123456", actual.getPAN());
        assertEquals("1234562109873456", actual.getToken());
        assertEquals("5001", actual.getExpDate());
        assertEquals(null, actual.getECI());
        assertEquals(null, actual.getWalletType());
    }

    @Test
    public void testInputCVV() {
        OutputFields actual = ewsSimulatorEndpoint.inputCVV("345");
        assertEquals("334534534534534534", actual.getOrderLVT());
    }

    @Test
    public void registration() throws InterruptedException {
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);

        RegistrationRequest request = new RegistrationRequest();
        request.setPrimaryAccountNumber("123");
        request.setMerchant(merchant);
        request.setMerchantRefId("merchantRefId");

        willDoNothing().given(httpHeaderUtils).customizeHttpResponseHeader();
        willReturn(new Integer(101)).given(simulatorResponseService).getRegistrationExceptionSavedIfAny("123");

        try{
            ewsSimulatorEndpoint.registration(request, header);
            fail("Must throw exception");
        } catch (Exception e) {
            assertEquals("Server Fault Exception", e.getMessage());
        }
        RegistrationResponse response = new RegistrationResponse();
        willReturn(null).given(simulatorResponseService).getRegistrationExceptionSavedIfAny("123");
        willReturn(response).given(simulatorResponseService).getRegistrationResponseSavedIfAny("123");
        try{
            RegistrationResponse actual = ewsSimulatorEndpoint.registration(request, header);
            assertEquals("merchantRefId", actual.getMerchantRefId());
        } catch (Exception e) {
            fail("Must not throw exception");
        }
    }

    @Test
    public void tokenize() throws InterruptedException {
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);

        TokenizeRequest request = new TokenizeRequest();
        request.setPrimaryAccountNumber("123");
        request.setMerchant(merchant);
        request.setMerchantRefId("merchantRefId");

        willDoNothing().given(httpHeaderUtils).customizeHttpResponseHeader();
        willReturn(new Integer(101)).given(simulatorResponseService).getTokenizeExceptionSavedIfAny("123");

        try{
            ewsSimulatorEndpoint.tokenize(request, header);
            fail("Must throw exception");
        } catch (Exception e) {
            assertEquals("Server Fault Exception", e.getMessage());
        }
        TokenizeResponse response = new TokenizeResponse();
        willReturn(null).given(simulatorResponseService).getTokenizeExceptionSavedIfAny("123");
        willReturn(response).given(simulatorResponseService).getTokenizeResponseSavedIfAny("123");
        try{
            TokenizeResponse actual = ewsSimulatorEndpoint.tokenize(request, header);
            assertEquals("merchantRefId", actual.getMerchantRefId());
        } catch (Exception e) {
            fail("Must not throw exception");
        }
    }

    @Test
    public void orderRegistration() throws InterruptedException {
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);

        OrderRegistrationRequest request = new OrderRegistrationRequest();
        request.setCardSecurityCode("123");
        request.setMerchant(merchant);
        request.setMerchantRefId("merchantRefId");

        willDoNothing().given(httpHeaderUtils).customizeHttpResponseHeader();
        willReturn(new Integer(101)).given(simulatorResponseService).getOrderRegistrationExceptionSavedIfAny("123");

        try{
            ewsSimulatorEndpoint.orderRegistration(request, header);
            fail("Must throw exception");
        } catch (Exception e) {
            assertEquals("Server Fault Exception", e.getMessage());
        }
        OrderRegistrationResponse response = new OrderRegistrationResponse();
        willReturn(null).given(simulatorResponseService).getOrderRegistrationExceptionSavedIfAny("123");
        willReturn(response).given(simulatorResponseService).getOrderRegistrationResponseSavedIfAny("123");
        try{
            OrderRegistrationResponse actual = ewsSimulatorEndpoint.orderRegistration(request, header);
            assertEquals("merchantRefId", actual.getMerchantRefId());
        } catch (Exception e) {
            fail("Must not throw exception");
        }
    }

    @Test
    public void tokenRegistration() throws InterruptedException {
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);

        TokenRegistrationRequest request = new TokenRegistrationRequest();
        request.setToken("123");
        request.setMerchant(merchant);
        request.setMerchantRefId("merchantRefId");

        willDoNothing().given(httpHeaderUtils).customizeHttpResponseHeader();
        willReturn(new Integer(101)).given(simulatorResponseService).getTokenRegistrationExceptionSavedIfAny("123");

        try{
            ewsSimulatorEndpoint.tokenRegistration(request, header);
            fail("Must throw exception");
        } catch (Exception e) {
            assertEquals("Server Fault Exception", e.getMessage());
        }
        TokenRegistrationResponse response = new TokenRegistrationResponse();
        willReturn(null).given(simulatorResponseService).getTokenRegistrationExceptionSavedIfAny("123");
        willReturn(response).given(simulatorResponseService).getTokenRegistrationResponseSavedIfAny("123");
        try{
            TokenRegistrationResponse actual = ewsSimulatorEndpoint.tokenRegistration(request, header);
            assertEquals("merchantRefId", actual.getMerchantRefId());
        } catch (Exception e) {
            fail("Must not throw exception");
        }
    }

    @Test
    public void detokenize() throws InterruptedException {
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);

        DetokenizeRequest request = new DetokenizeRequest();
        request.setToken("123");
        request.setMerchant(merchant);
        request.setMerchantRefId("merchantRefId");

        willDoNothing().given(httpHeaderUtils).customizeHttpResponseHeader();
        willReturn(new Integer(101)).given(simulatorResponseService).getDetokenizeExceptionSavedIfAny("123");

        try{
            ewsSimulatorEndpoint.detokenize(request, header);
            fail("Must throw exception");
        } catch (Exception e) {
            assertEquals("Server Fault Exception", e.getMessage());
        }
        DetokenizeResponse response = new DetokenizeResponse();
        willReturn(null).given(simulatorResponseService).getDetokenizeExceptionSavedIfAny("123");
        willReturn(response).given(simulatorResponseService).getDetokenizeResponseSavedIfAny("123");
        try{
            DetokenizeResponse actual = ewsSimulatorEndpoint.detokenize(request, header);
            assertEquals("merchantRefId", actual.getMerchantRefId());
        } catch (Exception e) {
            fail("Must not throw exception");
        }
    }

    @Test
    public void deregistration() throws InterruptedException {
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);

        DeregistrationRequest request = new DeregistrationRequest();
        request.setRegId("123");
        request.setMerchant(merchant);
        request.setMerchantRefId("merchantRefId");

        willDoNothing().given(httpHeaderUtils).customizeHttpResponseHeader();
        willReturn(new Integer(101)).given(simulatorResponseService).getDeregistrationExceptionSavedIfAny("123");

        try{
            ewsSimulatorEndpoint.deregistration(request, header);
            fail("Must throw exception");
        } catch (Exception e) {
            assertEquals("Server Fault Exception", e.getMessage());
        }
        DeregistrationResponse response = new DeregistrationResponse();
        willReturn(null).given(simulatorResponseService).getDeregistrationExceptionSavedIfAny("123");
        willReturn(response).given(simulatorResponseService).getDeregistrationResponseSavedIfAny("123");
        try{
            DeregistrationResponse actual = ewsSimulatorEndpoint.deregistration(request, header);
            assertEquals("merchantRefId", actual.getMerchantRefId());
        } catch (Exception e) {
            fail("Must not throw exception");
        }
    }
}
