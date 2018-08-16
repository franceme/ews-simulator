package com.worldpay.simulator.exceptions;

import com.worldpay.simulator.RequestValidationFault;
import com.worldpay.simulator.ServerFault;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.ws.soap.SoapFault;
import org.springframework.ws.soap.SoapFaultDetail;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.transform.Result;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.fail;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TestDetailSoapFaultDefinitionExceptionResolver {

    private SoapFault fault;
    private DetailSoapFaultDefinitionExceptionResolver exceptionResolver;
    private DetailSoapFaultDefinitionExceptionResolver exceptionResolverSpy;

    @Before
    public void setUp() {
        fault = mock(SoapFault.class);
        exceptionResolver = new DetailSoapFaultDefinitionExceptionResolver();
        exceptionResolverSpy = spy(exceptionResolver);
    }

    @Test
    public void testAddFaultDetail() throws JAXBException {
        Marshaller marshaller = mock(Marshaller.class);
        SoapFaultDetail faultDetail = mock(SoapFaultDetail.class);
        Result result = mock(Result.class);
        JAXBContext context = JAXBContext.newInstance("com.worldpay.simulator");

        String customFault = "An Object";
        doReturn(faultDetail).when(fault).addFaultDetail();
        doReturn(context).when(exceptionResolverSpy).createJAXBContext();
        doReturn(marshaller).when(exceptionResolverSpy).createMarshaller(context);
        doReturn(faultDetail).when(exceptionResolverSpy).getFaultDetail(fault);
        doReturn(result).when(faultDetail).getResult();
        doNothing().when(marshaller).marshal(customFault, result);

        exceptionResolverSpy.addFaultDetail(customFault, fault);

        verify(fault, times(1)).addFaultDetail();
        verify(exceptionResolverSpy, times(1)).createMarshaller(context);
        verify(exceptionResolverSpy, times(1)).getFaultDetail(fault);
        verify(faultDetail, times(1)).getResult();
        verify(marshaller, times(1)).marshal(customFault, result);
    }

    @Test
    public void testAddDetail_Exception() throws JAXBException {
        Marshaller marshaller = mock(Marshaller.class);
        SoapFaultDetail faultDetail = mock(SoapFaultDetail.class);
        Result result = mock(Result.class);
        JAXBContext context = JAXBContext.newInstance("com.worldpay.simulator");

        String customFault = "An Object";
        doReturn(faultDetail).when(fault).addFaultDetail();
        doReturn(context).when(exceptionResolverSpy).createJAXBContext();
        doReturn(marshaller).when(exceptionResolverSpy).createMarshaller(context);
        doReturn(faultDetail).when(exceptionResolverSpy).getFaultDetail(fault);
        doReturn(result).when(faultDetail).getResult();
        doThrow(new JAXBException("")).when(marshaller).marshal(customFault, result);

        try{
            exceptionResolverSpy.addFaultDetail(customFault, fault);
            fail("RuntimeException expected. None thrown");
        } catch (RuntimeException ex) {
            String cause = ex.getCause().toString();
            assertTrue(cause.contains("javax.xml.bind.JAXBException"));
        }
    }

    @Test
    public void testCustomizeFault_RuntimeException() {
        Object endpoint = "endpoint";
        Exception ex = new RuntimeException();

        ServerFault serverFault = new ServerFault();

        doNothing().when(exceptionResolverSpy).logRuntimeError(ex);
        doNothing().when(exceptionResolverSpy).addFaultDetail(serverFault, fault);
        doReturn(serverFault).when(exceptionResolverSpy).createServerFault();

        exceptionResolverSpy.customizeFault(endpoint, ex, fault);

        verify(exceptionResolverSpy, times(1)).logRuntimeError(ex);
        verify(exceptionResolverSpy, times(1)).createServerFault();
        verify(exceptionResolverSpy, times(1)).addFaultDetail(serverFault, fault);
    }

    @Test
    public void testCustomizeFault_ClientFaultException() {
        Object endpoint = "endpoint";
        ClientFaultException ex = mock(ClientFaultException.class);
        RequestValidationFault requestValidationFault = new RequestValidationFault();

        doReturn(requestValidationFault).when(ex).getRequestValidationFault();
        doNothing().when(exceptionResolverSpy).addFaultDetail(requestValidationFault, fault);

        exceptionResolverSpy.customizeFault(endpoint, ex, fault);

        verify(ex, times(1)).getRequestValidationFault();
        verify(exceptionResolverSpy, times(1)).addFaultDetail(requestValidationFault, fault);
    }

    @Test
    public void testCustomizeFault_ServerFaultException() {
        Object endpoint = "endpoint";
        ServerFaultException ex = mock(ServerFaultException.class);
        ServerFault serverFault = new ServerFault();

        doReturn(serverFault).when(ex).getServerFault();
        doNothing().when(exceptionResolverSpy).addFaultDetail(serverFault, fault);

        exceptionResolverSpy.customizeFault(endpoint, ex, fault);

        verify(ex, times(1)).getServerFault();
        verify(exceptionResolverSpy, times(1)).addFaultDetail(serverFault, fault);
    }


    @Test
    public void testCreateServerFault() {
        ServerFault serverFault = exceptionResolverSpy.createServerFault();
        assertEquals(2, (int) serverFault.getId());
        assertEquals("UNKNOWN_ERROR", serverFault.getCode());
        assertEquals("an unspecified error occurred.", serverFault.getMessage());
    }

    @Test
    public void testGetFaultDetail() {
        SoapFaultDetail faultDetail = mock(SoapFaultDetail.class);
        doReturn(faultDetail).when(fault).getFaultDetail();

        exceptionResolverSpy.getFaultDetail(fault);

        verify(fault, times(1)).getFaultDetail();
    }

    @Test
    public void testCreateMarshaller() throws JAXBException {
        Marshaller marshaller = mock(Marshaller.class);
        JAXBContext context = mock(JAXBContext.class);
        doReturn(marshaller).when(context).createMarshaller();

        exceptionResolverSpy.createMarshaller(context);

        verify(context, times(1)).createMarshaller();
    }

    @Test
    public void testCreateJAXBContext() throws JAXBException {
        JAXBContext expectedContext = JAXBContext.newInstance("com.worldpay.simulator");
        JAXBContext context = exceptionResolverSpy.createJAXBContext();

        assertEquals(expectedContext.toString(), context.toString());
    }
}
