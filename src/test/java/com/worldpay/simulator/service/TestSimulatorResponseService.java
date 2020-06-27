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
import com.worldpay.simulator.ECheckToken;
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

        service.addECheckTokenizePanToTokenResponseToMap("key", new ECheckToken());
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

        service.addECheckTokenizePanToErrorTokenResponseToMap("key", new ECheckToken());
        assertEquals(1, service.geteCheckTokenizePanToErrorTokenResponseMap().size());

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
        assertEquals(0, service.getECheckTokenizeExceptionMap().size());
        assertEquals(0, service.geteCheckTokenizePanToErrorTokenResponseMap().size());
        assertEquals(0, service.getBatchTokenizeExceptionMap().size());
        assertEquals(0, service.getTokenRegistrationExceptionMap().size());
        assertEquals(0, service.getOrderRegistrationExceptionMap().size());

    }

    @Test
    public void getRegistrationExceptionSavedIfAny() {
        assertNull(service.getRegistrationExceptionSavedIfAny("123"));
        service.addRegistrationExceptionToMap("123", 101);
        assertEquals(new Integer(101), service.getRegistrationExceptionSavedIfAny("123"));
        service.clearRegistrationMap();
        assertNull(service.getRegistrationExceptionSavedIfAny("123"));
        service.addRegistrationExceptionToMap("*", 111);
        assertEquals(new Integer(111), service.getRegistrationExceptionSavedIfAny("978"));
    }

    @Test
    public void getRegistrationResponseSavedIfAny() {
        assertNull(service.getRegistrationResponseSavedIfAny("123"));
        RegistrationResponse response = new RegistrationResponse();
        service.addRegistrationResponseToMap("123", response);
        assertEquals(response, service.getRegistrationResponseSavedIfAny("123"));
        service.clearRegistrationMap();
        assertNull(service.getRegistrationResponseSavedIfAny("123"));
        service.addRegistrationResponseToMap("*", response);
        assertEquals(response, service.getRegistrationResponseSavedIfAny("978"));
        service.clearRegistrationMap();
    }

    @Test
    public void getTokenizeExceptionSavedIfAny() {
        assertNull(service.getTokenizeExceptionSavedIfAny("123"));
        service.addTokenizeExceptionToMap("123", 101);
        assertEquals(new Integer(101), service.getTokenizeExceptionSavedIfAny("123"));
        service.clearTokenizeMap();
        assertNull(service.getTokenizeExceptionSavedIfAny("123"));
        service.addTokenizeExceptionToMap("*", 111);
        assertEquals(new Integer(111), service.getTokenizeExceptionSavedIfAny("978"));
        service.clearTokenizeMap();
    }

    @Test
    public void getTokenizeResponseSavedIfAny() {
        assertNull(service.getTokenizeResponseSavedIfAny("123"));
        TokenizeResponse response = new TokenizeResponse();
        service.addTokenizeResponseToMap("123", response);
        assertEquals(response, service.getTokenizeResponseSavedIfAny("123"));
        service.clearTokenizeMap();
        assertNull(service.getTokenizeResponseSavedIfAny("123"));
        service.addTokenizeResponseToMap("*", response);
        assertEquals(response, service.getTokenizeResponseSavedIfAny("978"));
        service.clearTokenizeMap();
    }

    @Test
    public void getOrderRegistrationExceptionSavedIfAny() {
        assertNull(service.getOrderRegistrationExceptionSavedIfAny("123"));
        service.addOrderRegistrationExceptionToMap("123", 101);
        assertEquals(new Integer(101), service.getOrderRegistrationExceptionSavedIfAny("123"));
        service.clearOrderRegistrationMap();
        assertNull(service.getOrderRegistrationExceptionSavedIfAny("123"));
        service.addOrderRegistrationExceptionToMap("*", 111);
        assertEquals(new Integer(111), service.getOrderRegistrationExceptionSavedIfAny("978"));
        service.clearOrderRegistrationMap();
    }

    @Test
    public void getOrderRegistrationResponseSavedIfAny() {
        assertNull(service.getOrderRegistrationResponseSavedIfAny("123"));
        OrderRegistrationResponse response = new OrderRegistrationResponse();
        service.addOrderRegistrationResponseToMap("123", response);
        assertEquals(response, service.getOrderRegistrationResponseSavedIfAny("123"));
        service.clearOrderRegistrationMap();
        assertNull(service.getOrderRegistrationResponseSavedIfAny("123"));
        service.addOrderRegistrationResponseToMap("*", response);
        assertEquals(response, service.getOrderRegistrationResponseSavedIfAny("978"));
        service.clearOrderRegistrationMap();
    }

    @Test
    public void getTokenRegistrationExceptionSavedIfAny() {
        assertNull(service.getTokenRegistrationExceptionSavedIfAny("123"));
        service.addTokenRegistrationExceptionToMap("123", 101);
        assertEquals(new Integer(101), service.getTokenRegistrationExceptionSavedIfAny("123"));
        service.clearTokenRegistrationMap();
        assertNull(service.getTokenRegistrationExceptionSavedIfAny("123"));
        service.addTokenRegistrationExceptionToMap("*", 111);
        assertEquals(new Integer(111), service.getTokenRegistrationExceptionSavedIfAny("978"));
        service.clearTokenRegistrationMap();
    }

    @Test
    public void getTokenRegistrationResponseSavedIfAny() {
        assertNull(service.getTokenRegistrationResponseSavedIfAny("123"));
        TokenRegistrationResponse response = new TokenRegistrationResponse();
        service.addTokenRegistrationResponseToMap("123", response);
        assertEquals(response, service.getTokenRegistrationResponseSavedIfAny("123"));
        service.clearTokenRegistrationMap();
        assertNull(service.getTokenRegistrationResponseSavedIfAny("123"));
        service.addTokenRegistrationResponseToMap("*", response);
        assertEquals(response, service.getTokenRegistrationResponseSavedIfAny("978"));
        service.clearTokenRegistrationMap();
    }

    @Test
    public void getDetokenizeExceptionSavedIfAny() {
        assertNull(service.getDetokenizeExceptionSavedIfAny("123"));
        service.addDetokenizeExceptionToMap("123", 101);
        assertEquals(new Integer(101), service.getDetokenizeExceptionSavedIfAny("123"));
        service.clearDetokenizeMap();
        assertNull(service.getDetokenizeExceptionSavedIfAny("123"));
        service.addDetokenizeExceptionToMap("*", 111);
        assertEquals(new Integer(111), service.getDetokenizeExceptionSavedIfAny("978"));
        service.clearDetokenizeMap();
    }

    @Test
    public void getDetokenizeResponseSavedIfAny() {
        assertNull(service.getDetokenizeResponseSavedIfAny("123"));
        DetokenizeResponse response = new DetokenizeResponse();
        service.addDetokenizeResponseToMap("123", response);
        assertEquals(response, service.getDetokenizeResponseSavedIfAny("123"));
        service.clearDetokenizeMap();
        assertNull(service.getDetokenizeResponseSavedIfAny("123"));
        service.addDetokenizeResponseToMap("*", response);
        assertEquals(response, service.getDetokenizeResponseSavedIfAny("978"));
        service.clearDetokenizeMap();
    }

    @Test
    public void getDeregistrationExceptionSavedIfAny() {
        assertNull(service.getDeregistrationExceptionSavedIfAny("123"));
        service.addDeregistrationExceptionToMap("123", 101);
        assertEquals(new Integer(101), service.getDeregistrationExceptionSavedIfAny("123"));
        service.clearDeregistrationMap();
        assertNull(service.getDeregistrationExceptionSavedIfAny("123"));
        service.addDeregistrationExceptionToMap("*", 111);
        assertEquals(new Integer(111), service.getDeregistrationExceptionSavedIfAny("978"));
        service.clearDeregistrationMap();
    }

    @Test
    public void getDeregistrationResponseSavedIfAny() {
        assertNull(service.getDeregistrationResponseSavedIfAny("123"));
        DeregistrationResponse response = new DeregistrationResponse();
        service.addDeregistrationResponseToMap("123", response);
        assertEquals(response, service.getDeregistrationResponseSavedIfAny("123"));
        service.clearDeregistrationMap();
        assertNull(service.getDeregistrationResponseSavedIfAny("123"));
        service.addDeregistrationResponseToMap("*", response);
        assertEquals(response, service.getDeregistrationResponseSavedIfAny("978"));
        service.clearDeregistrationMap();
    }

    @Test
    public void getEcheckTokenizeErrorTokenSavedIfAny() {
        assertNull(service.getEcheckTokenizeErrorTokenSavedIfAny("123"));
        ECheckToken response = new ECheckToken();
        service.addECheckTokenizePanToErrorTokenResponseToMap("123", response);
        assertEquals(response, service.getEcheckTokenizeErrorTokenSavedIfAny("123"));
        service.clearECheckTokenizeMap();
        assertNull(service.getEcheckTokenizeErrorTokenSavedIfAny("123"));
        service.addECheckTokenizePanToErrorTokenResponseToMap("*", response);
        assertEquals(response, service.getEcheckTokenizeErrorTokenSavedIfAny("978"));
        service.clearECheckTokenizeMap();
    }
}
