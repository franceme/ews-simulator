package com.worldpay.simulator.exceptions;


import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.springframework.ws.soap.SoapFault;
import org.springframework.ws.soap.server.endpoint.SoapFaultMappingExceptionResolver;

import com.worldpay.simulator.RequestValidationFault;
import com.worldpay.simulator.ServerFault;

public class DetailSoapFaultDefinitionExceptionResolver extends SoapFaultMappingExceptionResolver {

    @Override
    protected void customizeFault(Object endpoint, Exception ex, SoapFault fault) {
        if (ex instanceof ServerFaultException) {
            ServerFault serverFault = ((ServerFaultException) ex).getServerFault();
            addFaultDetail(serverFault, fault);
        } else if (ex instanceof ClientFaultException) {
            RequestValidationFault requestValidationFault = ((ClientFaultException) ex).getRequestValidationFault();
            addFaultDetail(requestValidationFault, fault);
        }
    }

    private void addFaultDetail(Object customFault, SoapFault fault) {
        fault.addFaultDetail();
        try {
            JAXBContext context = JAXBContext.newInstance("com.worldpay.simulator");
            Marshaller marshaller = context.createMarshaller();
            marshaller.marshal(customFault, fault.getFaultDetail().getResult());
        }
        catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }


}


