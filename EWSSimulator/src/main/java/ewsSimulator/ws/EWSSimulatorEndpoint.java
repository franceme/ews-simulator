package ewsSimulator.ws;

import ewsSimulator.ws.generated.BatchDetokenizeRequest;
import ewsSimulator.ws.generated.BatchDetokenizeResponse;
import ewsSimulator.ws.generated.EchoRequest;
import ewsSimulator.ws.generated.EchoResponse;
import ewsSimulator.ws.generated.RegistrationRequest;
import ewsSimulator.ws.generated.RegistrationResponse;
import ewsSimulator.ws.generated.TokenizeRequest;
import ewsSimulator.ws.generated.TokenizeResponse;

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

    /**
     *
     * @param registrationRequest
     * @return
     */
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "RegistrationRequest")
    @ResponsePayload
    public RegistrationResponse registration(@RequestPayload RegistrationRequest registrationRequest) {
        RegistrationResponse response = new RegistrationResponse();
        String primaryAccountNumber = registrationRequest.getPrimaryAccountNumber();
        int lengthPAN = primaryAccountNumber.length();
        response.setRequestId(EWSUtils.randomReqId());
        response.setRegId(EWSUtils.getRegId(primaryAccountNumber));
        response.setToken(EWSUtils.getToken(primaryAccountNumber));

        if(lengthPAN >= 3 && (primaryAccountNumber.substring(lengthPAN - 3).equals("000"))) {
            response.setTokenNewlyGenerated(true);
        }else {
            response.setTokenNewlyGenerated(false);
        }

        return response;
    }


    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "TokenizeRequest")
    @ResponsePayload
    public TokenizeResponse tokenize(@RequestPayload TokenizeRequest tokenizeRequest) {
        TokenizeResponse tokenizeResponse = new TokenizeResponse();
        String primaryAccountNumber = tokenizeRequest.getPrimaryAccountNumber();
        int lengthPAN = primaryAccountNumber.length();
        tokenizeResponse.setToken(EWSUtils.getToken(primaryAccountNumber));

        if(lengthPAN >= 3 && (primaryAccountNumber.substring(lengthPAN - 3).equals("000"))) {
            tokenizeResponse.setTokenNewlyGenerated(true);
        } else {
            tokenizeResponse.setTokenNewlyGenerated(false);
        }

        tokenizeResponse.setRequestId(EWSUtils.randomReqId());
        return tokenizeResponse;
    }
}
