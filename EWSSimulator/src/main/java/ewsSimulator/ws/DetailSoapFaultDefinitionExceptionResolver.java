package ewsSimulator.ws;

import java.io.StringWriter;

import javax.xml.namespace.QName;

import org.springframework.ws.soap.SoapFault;
import org.springframework.ws.soap.SoapFaultDetail;
import org.springframework.ws.soap.server.endpoint.SoapFaultMappingExceptionResolver;

public class DetailSoapFaultDefinitionExceptionResolver extends SoapFaultMappingExceptionResolver {

    private static final QName SERVER_FAULT = new QName("ServerFault");
    private static final QName REQUEST_VALIDATION_FAULT = new QName("RequestValidationFault");

    @Override
    protected void customizeFault(Object endpoint, Exception ex, SoapFault fault) {
        if (ex instanceof ServerFaultException) {
            ServerFault serverFault = ((ServerFaultException) ex).getServerFault();
            SoapFaultDetail detail = fault.addFaultDetail();
            String faultXml = "";
            detail.addFaultDetailElement(SERVER_FAULT).addText(faultXml);
        }
    }

//    private String getServerFaultXml(ServerFault serverFault) {
//        StringWriter sw = new StringWriter();
//        try {
//
//        }

}


