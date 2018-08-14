package com.worldpay.simulator.exceptions;


import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.springframework.ws.soap.SoapFault;
import org.springframework.ws.soap.server.endpoint.SoapFaultMappingExceptionResolver;

import com.worldpay.simulator.RequestValidationFault;
import com.worldpay.simulator.ServerFault;
import com.worldpay.simulator.errors.EWSError;
import com.worldpay.simulator.errors.ErrorIdMap;
import com.worldpay.simulator.utils.EWSUtils;

public class DetailSoapFaultDefinitionExceptionResolver extends SoapFaultMappingExceptionResolver {

    @Override
    protected void customizeFault(Object endpoint, Exception ex, SoapFault fault) {
        if (ex instanceof ServerFaultException) {
            ServerFault serverFault = ((ServerFaultException) ex).getServerFault();
            addFaultDetail(serverFault, fault);
        } else if (ex instanceof ClientFaultException) {
            RequestValidationFault requestValidationFault = ((ClientFaultException) ex).getRequestValidationFault();
            addFaultDetail(requestValidationFault, fault);
        } else {
            ServerFault serverFault = new ServerFault();
            serverFault.setRequestId(EWSUtils.randomReqId());
            serverFault.setId(2);
            EWSError error = ErrorIdMap.getError(2);
            serverFault.setCode(error.getErrorCode());
            serverFault.setMessage(error.getErrorMessage());
            addFaultDetail(serverFault, fault);
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


