package ewsSimulator.ws;

import ewsSimulator.ws.generated.BatchDetokenizeRequest;
import ewsSimulator.ws.generated.BatchDetokenizeResponse;
import ewsSimulator.ws.generated.EchoRequest;
import ewsSimulator.ws.generated.EchoResponse;
import ewsSimulator.ws.generated.RegistrationRequest;
import ewsSimulator.ws.generated.RegistrationResponse;

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

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "RegistrationRequest")
    @ResponsePayload
    public RegistrationResponse registration(@RequestPayload RegistrationRequest registrationRequest) {
        RegistrationResponse response = new RegistrationResponse();
        response.setRequestId("417d3d96-0675-4306-be5b-652c8c6ba9f7");
        response.setRegId("1479321767965480923");
        response.setToken("1111000100038566");
        response.setTokenNewlyGenerated(false);

        return response;
    }
}
