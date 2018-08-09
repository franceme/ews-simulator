package ewsSimulator.ws;


import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.springframework.ws.soap.SoapFault;
import org.springframework.ws.soap.server.endpoint.SoapFaultMappingExceptionResolver;

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
            JAXBContext context = JAXBContext.newInstance("ewsSimulator.ws");
            Marshaller marshaller = context.createMarshaller();
            marshaller.marshal(customFault, fault.getFaultDetail().getResult());
        }
        catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }


}


