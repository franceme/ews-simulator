import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.Assert.assertEquals;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import ewsSimulator.ws.DetokenizeRequest;
import ewsSimulator.ws.DetokenizeResponse;
import ewsSimulator.ws.EWSSimulatorEndpoint;
import ewsSimulator.ws.EWSUtils;
import ewsSimulator.ws.MerchantType;

public class TestEWSSimulatorEndpoint {

    EWSSimulatorEndpoint ewsSimulatorEndpoint = new EWSSimulatorEndpoint();
    EWSSimulatorEndpoint ewsSimulatorEndpointSpy;

    @Before
    public void setup() {
        ewsSimulatorEndpointSpy = spy(ewsSimulatorEndpoint);
    }

    @Test
    public void testDetokenize_happyPath() throws InterruptedException {
        System.out.println("");
//        DetokenizeRequest detokenizeRequest = new DetokenizeRequest();
//        MerchantType merchant = new MerchantType();
//        merchant.setRollupId("1123");
//        detokenizeRequest.setMerchant(merchant);
//        detokenizeRequest.setToken("468498435168468");
//        detokenizeRequest.setCVV2Requested(true);
//        detokenizeRequest.setExpirationDateRequested(true);
//        DetokenizeResponse testResponse = ewsSimulatorEndpointSpy.detokenize(detokenizeRequest);

//        assertEquals("615348948648468",testResponse.getPrimaryAccountNumber());
//        assertEquals("468",testResponse.getCardSecurityCode());
//        assertEquals("2308",testResponse.getExpirationDate());

//        SimulatorResponses mockResponses = mock(SimulatorResponses.class);
//        when(simulators.getSimulatorResponses()).thenReturn(mockResponses);
//
//        SimulatorResponseHashEntry hashEntry = new SimulatorResponseHashEntry();
//        hashEntry.setToken("0000000000004444");
//        hashEntry.accountNumber = "4100000000000001";
//        hashEntry.setCvv2Response("123");
//        when(mockResponses.getDetokenizeResponseFromToken(eq("0000000000004444"))).thenReturn(hashEntry);
//
//        DetokenizeRequest detokenizationRequest = new DetokenizeRequest();
//        detokenizationRequest.setToken("0000000000004444");
//        detokenizationRequest.setCVV2Requested(false);
//        detokenizationRequest.setExpirationDateRequested(false);
//
//        DetokenizeResponse actualResponse = objectUnderTest.detokenize(detokenizationRequest);
//
//        assertEquals("4100000000000001", actualResponse.getPrimaryAccountNumber());
//        assertNull(actualResponse.getCardSecurityCode());
    }

}
