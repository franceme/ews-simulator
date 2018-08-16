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
import javax.servlet.http.HttpServletResponse;

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

    @Mock
    TransportContext transportContextmock;

    @Mock
    HttpServletConnection httpServletConnectionMock;

    @Mock
    HttpServletRequest httpServletRequestMock;

    @Mock
    HttpServletResponse httpServletResponseMock;

    @Before
    public void setup() {
        mockStatic(TransportContextHolder.class);
        httpHeaderUtilsSpy = spy(httpHeaderUtils);
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
    public void testCustomizeHttpResponseHeaderNullHeaderValue() {

        willReturn(null).given(httpHeaderUtilsSpy).getHttpHeaderValue(correlationHeader);
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

    @Test
    public void testGetHttpServletRequestNullContext() throws Exception {

        PowerMockito.when(TransportContextHolder.class, "getTransportContext").thenReturn(null);

        Assert.assertEquals(null,httpHeaderUtilsSpy.getHttpServletRequest());
    }

    @Test
    public void testGetHttpHeaderValue() {

        willReturn(httpServletRequestMock).given(httpHeaderUtilsSpy).getHttpServletRequest();
        given(httpServletRequestMock.getHeader(correlationHeader)).willReturn(defaultCorrelationId);

        Assert.assertEquals(defaultCorrelationId,httpHeaderUtilsSpy.getHttpHeaderValue(correlationHeader));

    }

    @Test
    public void testGetHttpHeaderValueNullRequest() {

        willReturn(null).given(httpHeaderUtilsSpy).getHttpServletRequest();

        Assert.assertEquals(null,httpHeaderUtilsSpy.getHttpHeaderValue(correlationHeader));

    }

    @Test
    public void testGetHttpServletResponse() throws Exception {
        PowerMockito.when(TransportContextHolder.class, "getTransportContext").thenReturn(transportContextmock);

        given(transportContextmock.getConnection()).willReturn(httpServletConnectionMock);
        given(httpServletConnectionMock.getHttpServletResponse()).willReturn(httpServletResponseMock);

        Assert.assertEquals(httpServletResponseMock,httpHeaderUtilsSpy.getHttpServletResponse());
    }

    @Test
    public void testGetHttpServletResponseNullContext() throws Exception {
        PowerMockito.when(TransportContextHolder.class, "getTransportContext").thenReturn(null);

        Assert.assertEquals(null,httpHeaderUtilsSpy.getHttpServletResponse());
    }

    @Test
    public void testAddResponseHttpHeader() {
        willReturn(httpServletResponseMock).given(httpHeaderUtilsSpy).getHttpServletResponse();
        willDoNothing().given(httpServletResponseMock).addHeader(acceptHeader, acceptInput);

        httpHeaderUtilsSpy.addResponseHttpHeader(acceptHeader, acceptInput);
        verify(httpHeaderUtilsSpy, times(1)).getHttpServletResponse();
        verify(httpServletResponseMock, times(1)).addHeader(acceptHeader, acceptInput);
    }

    @Test
    public void testSetResponseHttpHeaderValue() {
        willReturn(httpServletResponseMock).given(httpHeaderUtilsSpy).getHttpServletResponse();
        willDoNothing().given(httpServletResponseMock).setHeader(acceptHeader, acceptInput);

        httpHeaderUtilsSpy.setResponseHttpHeaderValue(acceptHeader, acceptInput);
        verify(httpHeaderUtilsSpy, times(1)).getHttpServletResponse();
        verify(httpServletResponseMock, times(1)).setHeader(acceptHeader, acceptInput);
    }
}
