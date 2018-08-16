package com.worldpay.simulator.exceptions;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.ws.soap.SoapFault;
import org.springframework.ws.soap.SoapFaultDetail;
import org.springframework.ws.soap.server.endpoint.SoapFaultMappingExceptionResolver;

import com.worldpay.simulator.RequestValidationFault;
import com.worldpay.simulator.ServerFault;
import com.worldpay.simulator.errors.EWSError;
import com.worldpay.simulator.errors.ErrorIdMap;
import com.worldpay.simulator.utils.EWSUtils;

public class DetailSoapFaultDefinitionExceptionResolver extends SoapFaultMappingExceptionResolver {

    private static final Logger logger = LoggerFactory.getLogger(DetailSoapFaultDefinitionExceptionResolver.class);

    @Override
    public void customizeFault(Object endpoint, Exception ex, SoapFault fault) {
        if (ex instanceof ServerFaultException) {
            ServerFault serverFault = ((ServerFaultException) ex).getServerFault();
            addFaultDetail(serverFault, fault);
        } else if (ex instanceof ClientFaultException) {
            RequestValidationFault requestValidationFault = ((ClientFaultException) ex).getRequestValidationFault();
            addFaultDetail(requestValidationFault, fault);
        } else {
            logRuntimeError(ex);
            ServerFault serverFault = createServerFault();
            addFaultDetail(serverFault, fault);
        }
    }

    public void addFaultDetail(Object customFault, SoapFault fault) {
        fault.addFaultDetail();
        try {
            JAXBContext context = createJAXBContext();
            Marshaller marshaller = createMarshaller(context);
            marshaller.marshal(customFault, getFaultDetail(fault).getResult());
        }
        catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    public ServerFault createServerFault() {
        ServerFault serverFault = new ServerFault();
        serverFault.setRequestId(EWSUtils.randomReqId());
        serverFault.setId(2);
        EWSError error = ErrorIdMap.getError(2);
        serverFault.setCode(error.getErrorCode());
        serverFault.setMessage(error.getErrorMessage());

        return serverFault;
    }

    public SoapFaultDetail getFaultDetail(SoapFault fault) {
        return fault.getFaultDetail();
    }

    public Marshaller createMarshaller(JAXBContext context) throws JAXBException {
        return context.createMarshaller();
    }

    public JAXBContext createJAXBContext() throws JAXBException {
        return JAXBContext.newInstance("com.worldpay.simulator");
    }

    public void logRuntimeError(Exception ex) {
        Logger logger = getLogger();
        logger.error("Runtime exception:\n" + ex);
    }

    public Logger getLogger() {
        return logger;
    }
}