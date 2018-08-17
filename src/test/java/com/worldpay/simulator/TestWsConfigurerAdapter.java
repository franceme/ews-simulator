package com.worldpay.simulator;


import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.server.EndpointInterceptor;
import org.springframework.ws.soap.server.endpoint.interceptor.PayloadValidatingInterceptor;
import org.springframework.xml.xsd.XsdSchema;

@RunWith(MockitoJUnitRunner.class)
public class TestWsConfigurerAdapter {

    @Spy
    WebServiceConfig webServiceConfigSpy;

    @Mock
    PayloadValidatingInterceptor payloadValidatingInterceptorMock;

    @Mock
    XsdSchema xsdSchemaMock;


    @Test
    public void testAddInterceptor(){

        List<EndpointInterceptor> testInterceptors = new ArrayList<>();

        doReturn(payloadValidatingInterceptorMock).when(webServiceConfigSpy).payloadValidatingInterceptorInstance();
        doNothing().when(payloadValidatingInterceptorMock).setValidateRequest(true);
        doNothing().when(payloadValidatingInterceptorMock).setValidateResponse(true);
        doReturn(xsdSchemaMock).when(webServiceConfigSpy).encryptionSchema();
        doNothing().when(payloadValidatingInterceptorMock).setXsdSchema(xsdSchemaMock);

        webServiceConfigSpy.addInterceptors(testInterceptors);

    }
}
