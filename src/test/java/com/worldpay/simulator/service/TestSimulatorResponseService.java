package com.worldpay.simulator.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.worldpay.simulator.Account;
import com.worldpay.simulator.DeregistrationResponse;
import com.worldpay.simulator.DetokenizeResponse;
import com.worldpay.simulator.OrderDeregistrationResponse;
import com.worldpay.simulator.OrderRegistrationResponse;
import com.worldpay.simulator.RegistrationResponse;
import com.worldpay.simulator.SpringTestConfig;
import com.worldpay.simulator.Token;
import com.worldpay.simulator.TokenRegistrationResponse;
import com.worldpay.simulator.TokenizeResponse;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringTestConfig.class)
public class TestSimulatorResponseService {

    @Autowired
    private SimulatorResponseService service;

    @Test
    public void getDeregistrationResponseMap() {
        assertNull(service.getDeregistrationResponseMap());
    }

    @Test
    public void getOrderDeregistrationResponseMap() {
        assertNull(service.getOrderDeregistrationResponseMap());
    }

    @Test
    public void getDetokenizeResponseMap() {
        assertNull(service.getDetokenizeResponseMap());
    }

    @Test
    public void getTokenInquiryPanToTokenResponseMap() {
        assertNull(service.getTokenInquiryPanToTokenResponseMap());
    }

    @Test
    public void getECheckDetokenizeTokenToAccountResponseMap() {
        assertNull(service.getECheckDetokenizeTokenToAccountResponseMap());
    }

    @Test
    public void getECheckTokenizePanToTokenResponseMap() {
        assertNull(service.getECheckTokenizePanToTokenResponseMap());
    }

    @Test
    public void getBatchDetokenizeTokenToPanResponseMap() {
        assertNull(service.getBatchDetokenizeTokenToPanResponseMap());
    }

    @Test
    public void getBatchTokenizePANToTokenResponseMap() {
        assertNull(service.getBatchTokenizePanToTokenResponseMap());
    }

    @Test
    public void getTokenRegistrationResponseMap() {
        assertNull(service.getTokenRegistrationResponseMap());
    }

    @Test
    public void getOrderRegistrationResponseMap() {
        assertNull(service.getOrderRegistrationResponseMap());
    }

    @Test
    public void getTokenizeResponseMap() {
        assertNull(service.getTokenizeResponseMap());
    }

    @Test
    public void getRegistrationResponseMap() {
        assertNull(service.getRegistrationResponseMap());
    }

    @Test
    public void getDeregistrationExceptionMap() {
        assertNull(service.getDeregistrationExceptionMap());
    }

    @Test
    public void getOrderDeregistrationExceptionMap() {
        assertNull(service.getOrderDeregistrationExceptionMap());
    }

    @Test
    public void getDetokenizeExceptionMap() {
        assertNull(service.getDetokenizeExceptionMap());
    }

    @Test
    public void getTokenInquiryExceptionMap() {
        assertNull(service.getTokenInquiryExceptionMap());
    }

    @Test
    public void getECheckDetokenizeExceptionMap() {
        assertNull(service.getECheckDetokenizeExceptionMap());
    }

    @Test
    public void getECheckTokenizeExceptionMap() {
        assertNull(service.getECheckTokenizeExceptionMap());
    }

    @Test
    public void getBatchDetokenizeExceptionMap() {
        assertNull(service.getBatchDetokenizeExceptionMap());
    }

    @Test
    public void getBatchTokenizeExceptionMap() {
        assertNull(service.getBatchTokenizeExceptionMap());
    }

    @Test
    public void getTokenRegistrationExceptionMap() {
        assertNull(service.getTokenRegistrationExceptionMap());
    }

    @Test
    public void getOrderRegistrationExceptionMap() {
        assertNull(service.getOrderRegistrationExceptionMap());
    }

    @Test
    public void getTokenizeExceptionMap() {
        assertNull(service.getTokenizeExceptionMap());
    }

    @Test
    public void getRegistrationExceptionMap() {
        assertNull(service.getRegistrationExceptionMap());
    }

    @Test
    public void clearAllResponses() {
        service.addDeregistrationResponseToMap("key", new DeregistrationResponse());
        assertEquals(1, service.getDeregistrationResponseMap().size());

        service.addOrderDeregistrationResponseToMap("key", new OrderDeregistrationResponse());
        assertEquals(1, service.getOrderDeregistrationResponseMap().size());

        service.addDetokenizeResponseToMap("key", new DetokenizeResponse());
        assertEquals(1, service.getDetokenizeResponseMap().size());

        service.addTokenInquiryPanToTokenResponseToMap("key", new Token());
        assertEquals(1, service.getTokenInquiryPanToTokenResponseMap().size());

        service.addECheckDetokenizeTokenToAccountResponseToMap("key", new Account());
        assertEquals(1, service.getECheckDetokenizeTokenToAccountResponseMap().size());

        service.addECheckTokenizePanToTokenResponseToMap("key", new Token());
        assertEquals(1, service.getECheckTokenizePanToTokenResponseMap().size());

        service.addBatchDetokenizeTokenToPanResponseToMap("key", "pan");
        assertEquals(1, service.getBatchDetokenizeTokenToPanResponseMap().size());

        service.addBatchTokenizePanToTokenResponseToMap("key", new Token());
        assertEquals(1, service.getBatchTokenizePanToTokenResponseMap().size());

        service.addTokenRegistrationResponseToMap("key", new TokenRegistrationResponse());
        assertEquals(1, service.getTokenRegistrationResponseMap().size());

        service.addOrderRegistrationResponseToMap("key", new OrderRegistrationResponse());
        assertEquals(1, service.getOrderRegistrationResponseMap().size());

        service.addTokenizeResponseToMap("key", new TokenizeResponse());
        assertEquals(1, service.getTokenizeResponseMap().size());

        service.addRegistrationResponseToMap("key", new RegistrationResponse());
        assertEquals(1, service.getRegistrationResponseMap().size());

        service.addDeregistrationExceptionToMap("key", 101);
        assertEquals(1, service.getDeregistrationExceptionMap().size());

        service.addOrderDeregistrationExceptionToMap("key", 101);
        assertEquals(1, service.getOrderDeregistrationExceptionMap().size());

        service.addDetokenizeExceptionToMap("key", 101);
        assertEquals(1, service.getDetokenizeExceptionMap().size());

        service.addTokenInquiryExceptionToMap("key", 101);
        assertEquals(1, service.getTokenInquiryExceptionMap().size());

        service.addECheckDetokenizeExceptionToMap("key", 101);
        assertEquals(1, service.getECheckDetokenizeExceptionMap().size());

        service.addECheckTokenizeExceptionToMap("key", 101);
        assertEquals(1, service.getECheckDetokenizeExceptionMap().size());

        service.addBatchDetokenizeExceptionToMap("key", 101);
        assertEquals(1, service.getBatchDetokenizeExceptionMap().size());

        service.addBatchTokenizeExceptionToMap("key", 101);
        assertEquals(1, service.getBatchTokenizeExceptionMap().size());

        service.addTokenRegistrationExceptionToMap("key", 101);
        assertEquals(1, service.getTokenRegistrationExceptionMap().size());

        service.addOrderRegistrationExceptionToMap("key", 101);
        assertEquals(1, service.getOrderRegistrationExceptionMap().size());

        service.clearAllResponses();

        assertEquals(0, service.getDeregistrationResponseMap().size());
        assertEquals(0, service.getOrderDeregistrationResponseMap().size());
        assertEquals(0, service.getDetokenizeResponseMap().size());
        assertEquals(0, service.getTokenInquiryPanToTokenResponseMap().size());
        assertEquals(0, service.getECheckDetokenizeTokenToAccountResponseMap().size());
        assertEquals(0, service.getECheckTokenizePanToTokenResponseMap().size());
        assertEquals(0, service.getBatchDetokenizeTokenToPanResponseMap().size());
        assertEquals(0, service.getBatchTokenizePanToTokenResponseMap().size());
        assertEquals(0, service.getTokenRegistrationResponseMap().size());
        assertEquals(0, service.getOrderRegistrationResponseMap().size());
        assertEquals(0, service.getTokenizeResponseMap().size());
        assertEquals(0, service.getRegistrationResponseMap().size());
        assertEquals(0, service.getDeregistrationExceptionMap().size());
        assertEquals(0, service.getOrderDeregistrationExceptionMap().size());
        assertEquals(0, service.getDetokenizeExceptionMap().size());
        assertEquals(0, service.getTokenInquiryExceptionMap().size());
        assertEquals(0, service.getECheckDetokenizeExceptionMap().size());
        assertEquals(0, service.getECheckDetokenizeExceptionMap().size());
        assertEquals(0, service.getBatchDetokenizeExceptionMap().size());
        assertEquals(0, service.getBatchTokenizeExceptionMap().size());
        assertEquals(0, service.getTokenRegistrationExceptionMap().size());
        assertEquals(0, service.getOrderRegistrationExceptionMap().size());

    }
}
