package com.worldpay.simulator.validator;

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

import com.worldpay.simulator.BatchDetokenizeRequest;
import com.worldpay.simulator.BatchTokenizeRequest;
import com.worldpay.simulator.DecryptRequest;
import com.worldpay.simulator.DeregistrationRequest;
import com.worldpay.simulator.DetokenizeRequest;
import com.worldpay.simulator.ECheckDetokenizeRequest;
import com.worldpay.simulator.ECheckTokenizeRequest;
import com.worldpay.simulator.EchoRequest;
import com.worldpay.simulator.OrderDeregistrationRequest;
import com.worldpay.simulator.OrderRegistrationRequest;
import com.worldpay.simulator.RegistrationRequest;
import com.worldpay.simulator.SpringTestConfig;
import com.worldpay.simulator.TokenInquiryRequest;
import com.worldpay.simulator.TokenRegistrationRequest;
import com.worldpay.simulator.TokenizeRequest;
import com.worldpay.simulator.exceptions.ServerFaultException;


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
    public void testValidateRequestDecryptRequest() throws Exception {
        DecryptRequest request = new DecryptRequest();
        String merchantRefId = "merchantRefId";
        request.setMerchantRefId(merchantRefId);
        doNothing().when(requestValidator).validateSoapHeader(header);
        doNothing().when(requestValidator).validateDecryptRequest(request);
        doNothing().when(spy).handleExceptionsAndDelay(merchantRefId);
        spy.validateRequest(request, header);
        verify(requestValidator).validateSoapHeader(header);
        verify(requestValidator).validateDecryptRequest(request);
        verify(spy).handleExceptionsAndDelay(merchantRefId);
    }

    @Test
    public void testValidateRequestOrderRegistrationRequest() throws Exception {
        OrderRegistrationRequest request = new OrderRegistrationRequest();
        String merchantRefId = "merchantRefId";
        request.setMerchantRefId(merchantRefId);
        doNothing().when(requestValidator).validateSoapHeader(header);
        doNothing().when(requestValidator).validateOrderRegistrationRequest(request);
        doNothing().when(spy).handleExceptionsAndDelay(merchantRefId);
        spy.validateRequest(request, header);
        verify(requestValidator).validateSoapHeader(header);
        verify(requestValidator).validateOrderRegistrationRequest(request);
        verify(spy).handleExceptionsAndDelay(merchantRefId);
    }

    @Test
    public void testHandleExceptionsAndDelay() throws InterruptedException {
        spy.setDelay(1);
        try {
            spy.handleExceptionsAndDelay(null);
        } catch (Exception e) {
            fail("Must not throw exception");
        }

        spy.setDelay(-1);
        try {
            spy.handleExceptionsAndDelay("001001");
            fail("Must throw exception");
        } catch(Exception e) {
        }
    }

    @Test
    public void testValidateRequestTokenizeRequest() throws Exception {
        TokenizeRequest request = new TokenizeRequest();
        String merchantRefId = "merchantRefId";
        request.setMerchantRefId(merchantRefId);
        doNothing().when(requestValidator).validateSoapHeader(header);
        doNothing().when(requestValidator).validateTokenizeRequest(request);
        doNothing().when(spy).handleExceptionsAndDelay(merchantRefId);
        spy.validateRequest(request, header);
        verify(requestValidator).validateSoapHeader(header);
        verify(requestValidator).validateTokenizeRequest(request);
        verify(spy).handleExceptionsAndDelay(merchantRefId);
    }

    @Test
    public void testValidateRequestDetokenizeRequest() throws Exception {
        DetokenizeRequest request = new DetokenizeRequest();
        String merchantRefId = "merchantRefId";
        request.setMerchantRefId(merchantRefId);
        doNothing().when(requestValidator).validateSoapHeader(header);
        doNothing().when(requestValidator).validateDetokenizeRequest(request);
        doNothing().when(spy).handleExceptionsAndDelay(merchantRefId);
        spy.validateRequest(request, header);
        verify(requestValidator).validateSoapHeader(header);
        verify(requestValidator).validateDetokenizeRequest(request);
        verify(spy).handleExceptionsAndDelay(merchantRefId);
    }

    @Test
    public void testValidateRequestBatchTokenizeRequest() throws Exception {
        BatchTokenizeRequest request = new BatchTokenizeRequest();
        String merchantRefId = "merchantRefId";
        request.setMerchantRefId(merchantRefId);
        doNothing().when(requestValidator).validateSoapHeader(header);
        doNothing().when(requestValidator).validateBatchTokenizeRequest(request);
        doNothing().when(spy).handleExceptionsAndDelay(merchantRefId);
        spy.validateRequest(request, header);
        verify(requestValidator).validateSoapHeader(header);
        verify(requestValidator).validateBatchTokenizeRequest(request);
        verify(spy).handleExceptionsAndDelay(merchantRefId);
    }

    @Test
    public void testValidateRequestBatchDetokenizeRequest() throws Exception {
        BatchDetokenizeRequest request = new BatchDetokenizeRequest();
        String merchantRefId = "merchantRefId";
        request.setMerchantRefId(merchantRefId);
        doNothing().when(requestValidator).validateSoapHeader(header);
        doNothing().when(requestValidator).validateBatchDetokenizeRequest(request);
        doNothing().when(spy).handleExceptionsAndDelay(merchantRefId);
        spy.validateRequest(request, header);
        verify(requestValidator).validateSoapHeader(header);
        verify(requestValidator).validateBatchDetokenizeRequest(request);
        verify(spy).handleExceptionsAndDelay(merchantRefId);
    }

    @Test
    public void testValidateRequestTokenInquiryRequest() throws Exception {
        TokenInquiryRequest request = new TokenInquiryRequest();
        String merchantRefId = "merchantRefId";
        request.setMerchantRefId(merchantRefId);
        doNothing().when(requestValidator).validateSoapHeader(header);
        doNothing().when(requestValidator).validateTokenInquiryRequest(request);
        doNothing().when(spy).handleExceptionsAndDelay(merchantRefId);
        spy.validateRequest(request, header);
        verify(requestValidator).validateSoapHeader(header);
        verify(requestValidator).validateTokenInquiryRequest(request);
        verify(spy).handleExceptionsAndDelay(merchantRefId);
    }

    @Test
    public void testValidateRequestRegistrationRequest() throws Exception {
        RegistrationRequest request = new RegistrationRequest();
        String merchantRefId = "merchantRefId";
        request.setMerchantRefId(merchantRefId);
        doNothing().when(requestValidator).validateSoapHeader(header);
        doNothing().when(requestValidator).validateRegistrationRequest(request);
        doNothing().when(spy).handleExceptionsAndDelay(merchantRefId);
        spy.validateRequest(request, header);
        verify(requestValidator).validateSoapHeader(header);
        verify(requestValidator).validateRegistrationRequest(request);
        verify(spy).handleExceptionsAndDelay(merchantRefId);
    }

    @Test
    public void testValidateRequestDeregistrationRequest() throws Exception {
        DeregistrationRequest request = new DeregistrationRequest();
        String merchantRefId = "merchantRefId";
        request.setMerchantRefId(merchantRefId);
        doNothing().when(requestValidator).validateSoapHeader(header);
        doNothing().when(requestValidator).validateDeregistrationRequest(request);
        doNothing().when(spy).handleExceptionsAndDelay(merchantRefId);
        spy.validateRequest(request, header);
        verify(requestValidator).validateSoapHeader(header);
        verify(requestValidator).validateDeregistrationRequest(request);
        verify(spy).handleExceptionsAndDelay(merchantRefId);
    }

    @Test
    public void testValidateRequestTokenRegistrationRequest() throws Exception {
        TokenRegistrationRequest request = new TokenRegistrationRequest();
        String merchantRefId = "merchantRefId";
        request.setMerchantRefId(merchantRefId);
        doNothing().when(requestValidator).validateSoapHeader(header);
        doNothing().when(requestValidator).validateTokenRegistrationRequest(request);
        doNothing().when(spy).handleExceptionsAndDelay(merchantRefId);
        spy.validateRequest(request, header);
        verify(requestValidator).validateSoapHeader(header);
        verify(requestValidator).validateTokenRegistrationRequest(request);
        verify(spy).handleExceptionsAndDelay(merchantRefId);
    }

    @Test
    public void testValidateRequestECheckTokenizeRequest() throws Exception {
        ECheckTokenizeRequest request = new ECheckTokenizeRequest();
        String merchantRefId = "merchantRefId";
        request.setMerchantRefId(merchantRefId);
        doNothing().when(requestValidator).validateSoapHeader(header);
        doNothing().when(requestValidator).validateECheckTokenizeRequest(request);
        doNothing().when(spy).handleExceptionsAndDelay(merchantRefId);
        spy.validateRequest(request, header);
        verify(requestValidator).validateSoapHeader(header);
        verify(requestValidator).validateECheckTokenizeRequest(request);
        verify(spy).handleExceptionsAndDelay(merchantRefId);
    }

    @Test
    public void testValidateRequestECheckDetokenizeRequest() throws Exception {
        ECheckDetokenizeRequest request = new ECheckDetokenizeRequest();
        String merchantRefId = "merchantRefId";
        request.setMerchantRefId(merchantRefId);
        doNothing().when(requestValidator).validateSoapHeader(header);
        doNothing().when(requestValidator).validateECheckDetokenizeRequest(request);
        doNothing().when(spy).handleExceptionsAndDelay(merchantRefId);
        spy.validateRequest(request, header);
        verify(requestValidator).validateSoapHeader(header);
        verify(requestValidator).validateECheckDetokenizeRequest(request);
        verify(spy).handleExceptionsAndDelay(merchantRefId);
    }

    @Test
    public void testValidateRequestOrderDeregistrationRequest() throws Exception {
        OrderDeregistrationRequest request = new OrderDeregistrationRequest();
        String merchantRefId = "merchantRefId";
        request.setMerchantRefId(merchantRefId);
        doNothing().when(requestValidator).validateSoapHeader(header);
        doNothing().when(requestValidator).validateOrderDeregistrationRequest(request);
        doNothing().when(spy).handleExceptionsAndDelay(merchantRefId);
        spy.validateRequest(request, header);
        verify(requestValidator).validateSoapHeader(header);
        verify(requestValidator).validateOrderDeregistrationRequest(request);
        verify(spy).handleExceptionsAndDelay(merchantRefId);
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
        spy.addDelayBasedOnMerchantRefId("0032222222333");
        final long duration = System.nanoTime() - startTime;
        assertTrue(300 <= (duration / 1000000));
    }
}




