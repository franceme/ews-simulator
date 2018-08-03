package ewsSimulator.ws;

import java.rmi.RemoteException;

import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;


@Endpoint
public class EWSSimulatorEndpoint {

    /*private static final String NAMESPACE_URI = "urn:com:vantiv:types:encryption:transactions:v1";

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "BatchDetokenizeRequest")
    @ResponsePayload
    public BatchDetokenizeResponse batchDetokenize(@RequestPayload BatchDetokenizeRequest batchDetokenizeRequest) throws RemoteException, ServerFault, RequestValidationFault {
        System.out.println("reached here");
        return new BatchDetokenizeResponse();
    }*/

}
