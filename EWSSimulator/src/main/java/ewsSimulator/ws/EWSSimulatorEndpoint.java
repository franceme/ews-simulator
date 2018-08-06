package ewsSimulator.ws;

import ewsSimulator.ws.generated.BatchDetokenizeRequest;
import ewsSimulator.ws.generated.BatchDetokenizeResponse;
import ewsSimulator.ws.generated.EchoRequest;
import ewsSimulator.ws.generated.EchoResponse;

import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;


@Endpoint
public class EWSSimulatorEndpoint {

    private static final String NAMESPACE_URI = "urn:com:vantiv:types:encryption:transactions:v1";

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "BatchDetokenizeRequest")
    @ResponsePayload
    public BatchDetokenizeResponse batchDetokenize(@RequestPayload BatchDetokenizeRequest batchDetokenizeRequest) {
        return new BatchDetokenizeResponse();
    }


    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "EchoRequest")
    @ResponsePayload
    public EchoResponse echo(@RequestPayload EchoRequest echoRequest) {
        return new EchoResponse();
    }
}
