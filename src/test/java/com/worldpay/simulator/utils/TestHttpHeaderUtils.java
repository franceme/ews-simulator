package com.worldpay.simulator.utils;

import com.worldpay.simulator.SpringTestConfig;
import com.worldpay.simulator.validator.ValidateAndSimulate;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.BDDMockito.given;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ws.soap.SoapHeaderElement;
import org.springframework.ws.transport.WebServiceConnection;
import org.springframework.ws.transport.context.DefaultTransportContext;
import org.springframework.ws.transport.context.TransportContext;
import org.springframework.ws.transport.context.TransportContextHolder;
import org.springframework.ws.transport.http.CommonsHttpConnection;
import org.springframework.ws.transport.http.HttpComponentsConnection;
import org.springframework.ws.transport.http.HttpServletConnection;

import java.util.List;

import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import javax.servlet.http.HttpServletRequest;

@RunWith(PowerMockRunner.class)
@SpringBootTest(classes = SpringTestConfig.class)
@PowerMockRunnerDelegate(SpringRunner.class)
@PrepareForTest({TransportContextHolder.class})
public class TestHttpHeaderUtils {

    @Autowired
    @Qualifier("testHttpHeaders")
    HttpHeaderUtils httpHeaderUtils;

    private HttpHeaderUtils httpHeaderUtilsSpy;
    private String defaultCorrelationId;
    private String acceptInput;
    private String correlationHeader;
    private String acceptHeader;
    private TransportContext transportContextmock;
    private HttpServletConnection httpServletConnectionMock;

    @Mock
    HttpServletRequest httpServletRequestMock;

    @Before
    public void setup() {
        mockStatic(TransportContextHolder.class);
        httpHeaderUtilsSpy = spy(httpHeaderUtils);
        transportContextmock = mock(TransportContext.class);
        httpServletConnectionMock = mock(HttpServletConnection.class);
        defaultCorrelationId = "64f231d1-e122-4693-af76-5652d4e37441";
        acceptInput = "gzip,deflate";
        correlationHeader = "v_CorrelationId";
        acceptHeader = "Accept-Encoding";
    }


    @Test
    public void testCustomizeHttpResponseHeader() {

        willReturn(defaultCorrelationId).given(httpHeaderUtilsSpy).getHttpHeaderValue(correlationHeader);
        willDoNothing().given(httpHeaderUtilsSpy).addResponseHttpHeader(correlationHeader, defaultCorrelationId);
        willDoNothing().given(httpHeaderUtilsSpy).setResponseHttpHeaderValue(acceptHeader, acceptInput);

        httpHeaderUtilsSpy.customizeHttpResponseHeader();

        verify(httpHeaderUtilsSpy, times(1)).getHttpHeaderValue(correlationHeader);
        verify(httpHeaderUtilsSpy, times(1)).addResponseHttpHeader(correlationHeader, defaultCorrelationId);
        verify(httpHeaderUtilsSpy, times(1)).setResponseHttpHeaderValue(acceptHeader, acceptInput);
    }

    @Test
    public void testGetHttpServletRequest() throws Exception {

        PowerMockito.when(TransportContextHolder.class, "getTransportContext").thenReturn(transportContextmock);
        given(transportContextmock.getConnection()).willReturn(httpServletConnectionMock);
        given(httpServletConnectionMock.getHttpServletRequest()).willReturn(httpServletRequestMock);

        Assert.assertEquals(httpServletRequestMock,httpHeaderUtilsSpy.getHttpServletRequest());
    }
}
