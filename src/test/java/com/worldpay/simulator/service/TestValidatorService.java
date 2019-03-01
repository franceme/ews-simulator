package com.worldpay.simulator.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ws.soap.SoapHeaderElement;

import com.worldpay.simulator.Account;
import com.worldpay.simulator.BatchDetokenizeRequest;
import com.worldpay.simulator.BatchTokenizeRequest;
import com.worldpay.simulator.Card;
import com.worldpay.simulator.DecryptRequest;
import com.worldpay.simulator.DeregistrationRequest;
import com.worldpay.simulator.DetokenizeRequest;
import com.worldpay.simulator.ECheckDetokenizeRequest;
import com.worldpay.simulator.ECheckToken;
import com.worldpay.simulator.ECheckTokenizeRequest;
import com.worldpay.simulator.EchoRequest;
import com.worldpay.simulator.OrderDeregistrationRequest;
import com.worldpay.simulator.OrderRegistrationRequest;
import com.worldpay.simulator.RegistrationRequest;
import com.worldpay.simulator.SpringTestConfig;
import com.worldpay.simulator.TokenInquiryRequest;
import com.worldpay.simulator.TokenRegistrationRequest;
import com.worldpay.simulator.TokenizeRequest;
import com.worldpay.simulator.VerifoneCryptogram;
import com.worldpay.simulator.service.RequestValidator;
import com.worldpay.simulator.service.ValidatorService;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringTestConfig.class)
public class TestValidatorService {


    private SoapHeaderElement header;

    @MockBean
    @Qualifier("testRequestValidator")
    private RequestValidator requestValidator;

    @Autowired
    private ValidatorService validatorService;
    private ValidatorService spy;

    @Before
    public void setUp() {
        header = null;
        spy = Mockito.spy(validatorService);
    }

    @Test
    public void testHandleExceptionsAndDelayForPAN() throws InterruptedException {
        spy.setDelay(1);
        try {
            spy.handleExceptionsAndDelayForPAN(null);
        } catch (Exception e) {
            fail("Must not throw exception");
        }

        spy.setDelay(-1);
        try {
            spy.handleExceptionsAndDelayForPAN("12021654231102");
            fail("Must throw exception");
        } catch(Exception e) {
        }
    }

    @Test
    public void testHandleExceptionsAndDelayForToken() throws InterruptedException {
        spy.setDelay(1);
        try {
            spy.handleExceptionsAndDelayForToken(null);
        } catch (Exception e) {
            fail("Must not throw exception");
        }

        spy.setDelay(-1);
        try {
            spy.handleExceptionsAndDelayForToken("300010001111851012");
            fail("Must throw exception");
        } catch(Exception e) {
        }
    }

    @Test
    public void testHandleExceptionsAndDelayForRegId() throws InterruptedException {
        spy.setDelay(1);
        try {
            spy.handleExceptionsAndDelayForRegId(null);
        } catch (Exception e) {
            fail("Must not throw exception");
        }

        spy.setDelay(-1);
        try {
            spy.handleExceptionsAndDelayForRegId("1234567890123451019");
            fail("Must throw exception");
        } catch(Exception e) {
        }
    }

    @Test
    public void testHandleExceptionsAndDelayForAccountNumber() throws InterruptedException {
        spy.setDelay(1);
        try {
            spy.handleExceptionsAndDelayForAccountNumber(null);
        } catch (Exception e) {
            fail("Must not throw exception");
        }

        spy.setDelay(-1);
        try {
            spy.handleExceptionsAndDelayForAccountNumber("23745678901231057");
            fail("Must throw exception");
        } catch(Exception e) {
        }
    }

    @Test
    public void testHandleExceptionsAndDelayForCVV() throws InterruptedException {
        spy.setDelay(1);
        try {
            spy.handleExceptionsAndDelayForCVV(null);
        } catch (Exception e) {
            fail("Must not throw exception");
        }

        spy.setDelay(-1);
        try {
            spy.handleExceptionsAndDelayForCVV("101");
            fail("Must throw exception");
        } catch(Exception e) {
        }
    }

    @Test
    public void testValidateRequestDecryptRequest() throws Exception {
        DecryptRequest request = new DecryptRequest();
        String pan = "pan";
        VerifoneCryptogram verifoneCryptogram = new VerifoneCryptogram();
        Card encryptedCard = new Card();
        encryptedCard.setPrimaryAccountNumber(pan);
        verifoneCryptogram.setEncryptedCard(encryptedCard);
        request.setVerifoneCryptogram(verifoneCryptogram);
        doNothing().when(requestValidator).validateSoapHeader(header);
        doNothing().when(requestValidator).validateDecryptRequest(request);
        doNothing().when(spy).handleExceptionsAndDelayForPAN(pan);
        spy.validateRequest(request, header);
        verify(requestValidator).validateSoapHeader(header);
        verify(requestValidator).validateDecryptRequest(request);
        verify(spy).handleExceptionsAndDelayForPAN(pan);
    }

    @Test
    public void testValidateRequestOrderRegistrationRequest() throws Exception {
        OrderRegistrationRequest request = new OrderRegistrationRequest();
        String cvv = "cvv";
        request.setCardSecurityCode(cvv);
        doNothing().when(requestValidator).validateSoapHeader(header);
        doNothing().when(requestValidator).validateOrderRegistrationRequest(request);
        doNothing().when(spy).handleExceptionsAndDelayForCVV(cvv);
        spy.validateRequest(request, header);
        verify(requestValidator).validateSoapHeader(header);
        verify(requestValidator).validateOrderRegistrationRequest(request);
        verify(spy).handleExceptionsAndDelayForCVV(cvv);
    }

    @Test
    public void testValidateRequestTokenizeRequest() throws Exception {
        TokenizeRequest request = new TokenizeRequest();
        String pan = "1234567890123";
        request.setPrimaryAccountNumber(pan);
        doNothing().when(requestValidator).validateSoapHeader(header);
        doNothing().when(requestValidator).validateTokenizeRequest(request);
        doNothing().when(spy).handleExceptionsAndDelayForPAN(pan);
        spy.validateRequest(request, header);
        verify(requestValidator).validateSoapHeader(header);
        verify(requestValidator).validateTokenizeRequest(request);
        verify(spy).handleExceptionsAndDelayForPAN(pan);
    }

    @Test
    public void testValidateRequestDetokenizeRequest() throws Exception {
        DetokenizeRequest request = new DetokenizeRequest();
        String token = "1234569870123";
        request.setToken(token);
        doNothing().when(requestValidator).validateSoapHeader(header);
        doNothing().when(requestValidator).validateDetokenizeRequest(request);
        doNothing().when(spy).handleExceptionsAndDelayForToken(token);
        spy.validateRequest(request, header);
        verify(requestValidator).validateSoapHeader(header);
        verify(requestValidator).validateDetokenizeRequest(request);
        verify(spy).handleExceptionsAndDelayForToken(token);
    }

    @Test
    public void testValidateRequestBatchTokenizeRequest() throws Exception {
        BatchTokenizeRequest request = new BatchTokenizeRequest();
        String merchantRefId = "merchantRefId";
        request.setMerchantRefId(merchantRefId);
        doNothing().when(requestValidator).validateSoapHeader(header);
        doNothing().when(requestValidator).validateBatchTokenizeRequest(request);
        spy.validateRequest(request, header);
        verify(requestValidator).validateSoapHeader(header);
        verify(requestValidator).validateBatchTokenizeRequest(request);
    }

    @Test
    public void testValidateRequestBatchDetokenizeRequest() throws Exception {
        BatchDetokenizeRequest request = new BatchDetokenizeRequest();
        String merchantRefId = "merchantRefId";
        request.setMerchantRefId(merchantRefId);
        doNothing().when(requestValidator).validateSoapHeader(header);
        doNothing().when(requestValidator).validateBatchDetokenizeRequest(request);
        spy.validateRequest(request, header);
        verify(requestValidator).validateSoapHeader(header);
        verify(requestValidator).validateBatchDetokenizeRequest(request);
    }

    @Test
    public void testValidateRequestTokenInquiryRequest() throws Exception {
        TokenInquiryRequest request = new TokenInquiryRequest();
        String merchantRefId = "merchantRefId";
        request.setMerchantRefId(merchantRefId);
        doNothing().when(requestValidator).validateSoapHeader(header);
        doNothing().when(requestValidator).validateTokenInquiryRequest(request);
        spy.validateRequest(request, header);
        verify(requestValidator).validateSoapHeader(header);
        verify(requestValidator).validateTokenInquiryRequest(request);
    }

    @Test
    public void testValidateRequestRegistrationRequest() throws Exception {
        RegistrationRequest request = new RegistrationRequest();
        String pan = "1234567890123";
        request.setPrimaryAccountNumber(pan);
        doNothing().when(requestValidator).validateSoapHeader(header);
        doNothing().when(requestValidator).validateRegistrationRequest(request);
        doNothing().when(spy).handleExceptionsAndDelayForPAN(pan);
        spy.validateRequest(request, header);
        verify(requestValidator).validateSoapHeader(header);
        verify(requestValidator).validateRegistrationRequest(request);
        verify(spy).handleExceptionsAndDelayForPAN(pan);
    }

    @Test
    public void testValidateRequestDeregistrationRequest() throws Exception {
        DeregistrationRequest request = new DeregistrationRequest();
        String regId = "regId";
        request.setRegId(regId);
        doNothing().when(requestValidator).validateSoapHeader(header);
        doNothing().when(requestValidator).validateDeregistrationRequest(request);
        doNothing().when(spy).handleExceptionsAndDelayForRegId(regId);
        spy.validateRequest(request, header);
        verify(requestValidator).validateSoapHeader(header);
        verify(requestValidator).validateDeregistrationRequest(request);
        verify(spy).handleExceptionsAndDelayForRegId(regId);
    }

    @Test
    public void testValidateRequestTokenRegistrationRequest() throws Exception {
        TokenRegistrationRequest request = new TokenRegistrationRequest();
        String token = "1234569870123";
        request.setToken(token);
        doNothing().when(requestValidator).validateSoapHeader(header);
        doNothing().when(requestValidator).validateTokenRegistrationRequest(request);
        doNothing().when(spy).handleExceptionsAndDelayForToken(token);
        spy.validateRequest(request, header);
        verify(requestValidator).validateSoapHeader(header);
        verify(requestValidator).validateTokenRegistrationRequest(request);
        verify(spy).handleExceptionsAndDelayForToken(token);
    }

    @Test
    public void testValidateRequestECheckTokenizeRequest() throws Exception {
        ECheckTokenizeRequest request = new ECheckTokenizeRequest();
        String accountNum = "23745678901234567";
        Account account = new Account();
        account.setAccountNumber(accountNum);
        request.setAccount(account);
        doNothing().when(requestValidator).validateSoapHeader(header);
        doNothing().when(requestValidator).validateECheckTokenizeRequest(request);
        doNothing().when(spy).handleExceptionsAndDelayForAccountNumber(account.getAccountNumber());
        spy.validateRequest(request, header);
        verify(requestValidator).validateSoapHeader(header);
        verify(requestValidator).validateECheckTokenizeRequest(request);
        verify(spy).handleExceptionsAndDelayForAccountNumber(account.getAccountNumber());
    }

    @Test
    public void testValidateRequestECheckDetokenizeRequest() throws Exception {
        ECheckDetokenizeRequest request = new ECheckDetokenizeRequest();
        ECheckToken token = new ECheckToken();
        token.setTokenValue("1234569870123");
        request.setToken(token);
        doNothing().when(requestValidator).validateSoapHeader(header);
        doNothing().when(requestValidator).validateECheckDetokenizeRequest(request);
        doNothing().when(spy).handleExceptionsAndDelayForToken(token.getTokenValue());
        spy.validateRequest(request, header);
        verify(requestValidator).validateSoapHeader(header);
        verify(requestValidator).validateECheckDetokenizeRequest(request);
        verify(spy).handleExceptionsAndDelayForToken(token.getTokenValue());
    }

    @Test
    public void testValidateRequestOrderDeregistrationRequest() throws Exception {
        OrderDeregistrationRequest request = new OrderDeregistrationRequest();
        String token = "1234569870123";
        request.setToken(token);
        doNothing().when(requestValidator).validateSoapHeader(header);
        doNothing().when(requestValidator).validateOrderDeregistrationRequest(request);
        doNothing().when(spy).handleExceptionsAndDelayForToken(token);
        spy.validateRequest(request, header);
        verify(requestValidator).validateSoapHeader(header);
        verify(requestValidator).validateOrderDeregistrationRequest(request);
        verify(spy).handleExceptionsAndDelayForToken(token);
    }

    @Test
    public void testValidateRequestEchoRequest() throws Exception {
        EchoRequest request = new EchoRequest();
        doNothing().when(requestValidator).validateSoapHeader(header);
        spy.validateRequest(request, header);
        verify(requestValidator).validateSoapHeader(header);

    }

    @Test
    public void testAddDelayBasedOnMerchantRefId() throws InterruptedException {
        final long startTime = System.nanoTime();
        spy.addDelay("0032222222033");
        final long duration = System.nanoTime() - startTime;
        assertTrue(300 <= (duration / 1000000));
    }
}



