package ewsSimulator.ws;

import org.springframework.ws.client.WebServiceFaultException;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.util.UUID;


@Endpoint
public class EWSSimulatorEndpoint {

    private static final String NAMESPACE_URI = "urn:com:vantiv:types:encryption:transactions:v1";
    private static final String DEFAULTPAN= "4266841015771878";

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
    public RegistrationResponse registration(@RequestPayload RegistrationRequest registrationRequest) throws InterruptedException {
        ServerFault serverFault = new ServerFault();
        serverFault.setMessage("An unspecified error occured");
        serverFault.setCode("UNKNOWN_ERROR");
        serverFault.setId(2);
        serverFault.setRequestId(EWSUtils.randomReqId());
        throw new ServerFaultException("Server Fault Exception", serverFault);

//        RegistrationResponse response = new RegistrationResponse();
//        String primaryAccountNumber = registrationRequest.getPrimaryAccountNumber();
//        EWSUtils.delayInResponse(primaryAccountNumber);
//        int lengthPAN = primaryAccountNumber.length();
//        response.setRequestId(EWSUtils.randomReqId());
//        response.setRegId(EWSUtils.getRegId(primaryAccountNumber));
//        response.setToken(EWSUtils.getToken(primaryAccountNumber));
//
//        if(lengthPAN >= 3 && (primaryAccountNumber.substring(lengthPAN - 3).equals("000"))) {
//            response.setTokenNewlyGenerated(true);
//        }else {
//            response.setTokenNewlyGenerated(false);
//        }
//
//        return response;
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

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "OrderRegistrationRequest")
    @ResponsePayload
    public OrderRegistrationResponse orderRegistration(@RequestPayload OrderRegistrationRequest orderRegistrationRequest){

        OrderRegistrationResponse response = new OrderRegistrationResponse();
        String cvv = "";
        String orderLVT = "3";

        if(orderRegistrationRequest.getCardSecurityCode() != null)
            cvv = orderRegistrationRequest.getCardSecurityCode();


        if(!EWSUtils.isSecurityCodeValid(cvv))
            throw new WebServiceFaultException("ERROR: invalid CardSecurityCode");


        if(!EWSUtils.isSecurityCodeEmpty(cvv))
            response.setOrderLVT(orderLVT+EWSUtils.generateRandomNumber(17));

        response.setRequestId(UUID.randomUUID().toString());


        return response;
    }


    /**
     *
     * @param detokenizationRequest
     * @return
     * @throws InterruptedException
     */
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "DetokenizeRequest")
    @ResponsePayload
    public DetokenizeResponse detokenize(@RequestPayload DetokenizeRequest detokenizationRequest) throws InterruptedException {
        DetokenizeResponse answer = new DetokenizeResponse();

        String token = detokenizationRequest.getToken();

        MerchantType merchant = detokenizationRequest.getMerchant();

        if(!EWSUtils.validToken(token)|| !EWSUtils.validRollupId(merchant.getRollupId())) throw new WebServiceFaultException("Fault occurred while processing.");


        String primaryAccountNumber = EWSUtils.getPAN(token);


        EWSUtils.delayInResponse(primaryAccountNumber);


        if (detokenizationRequest.isCVV2Requested()){
            answer.setCardSecurityCode(EWSUtils.getCVVThroughToken(token));
        }
        if (detokenizationRequest.isExpirationDateRequested()){
            answer.setExpirationDate("2308");
        }

        answer.setPrimaryAccountNumber(primaryAccountNumber);
        String requestId = EWSUtils.randomReqId();
        answer.setRequestId(requestId);

        return answer;
    }


    /**
     * 
     * @param orderDeregistrationRequest
     * @return
     * @throws InterruptedException
     */
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "OrderDeregistrationRequest")
    @ResponsePayload
    public OrderDeregistrationResponse orderDeregistration(@RequestPayload OrderDeregistrationRequest orderDeregistrationRequest) throws InterruptedException {
        OrderDeregistrationResponse answer = new OrderDeregistrationResponse();

        answer.setRequestId(EWSUtils.randomReqId());

        String cvv = orderDeregistrationRequest.getOrderLVT();

        String token = orderDeregistrationRequest.getToken();

        MerchantType merchant = orderDeregistrationRequest.getMerchant();

        if(!EWSUtils.validCVV(cvv)||!EWSUtils.validRollupId(merchant.getRollupId())) throw new WebServiceFaultException("Fault occurred while processing.");

        if(EWSUtils.validToken(token)) {
            answer.setPrimaryAccountNumber(EWSUtils.getPAN(token));
        }
        else answer.setPrimaryAccountNumber(DEFAULTPAN);

        if(cvv.startsWith("3")) {
            // check position 2 for '6', '7', '8' or '9' to simulate error
            if (cvv.charAt(2) == '6') {
                VError error = new VError();
                error.setId(9999);
                error.setMessage("GENERIC CHECKOUT_ID ERROR");
                answer.getError().add(error);
            } else if (cvv.charAt(2) == '7') {
                VError error = new VError();
                error.setId(2);
                error.setMessage("GENERIC CHECKOUT_ID ERROR");
                answer.getError().add(error);
            } else if (cvv.charAt(2) == '8') {
                VError error = new VError();
                error.setId(4);
                error.setMessage("CHECKOUT_ID INVALID");
                answer.getError().add(error);
            } else if (cvv.charAt(2) == '9') {
                VError error = new VError();
                error.setId(6);
                error.setMessage("CHECKOUT_ID NOT_FOUND");
                answer.getError().add(error);
            } else {
                EWSUtils.delayInResponse(EWSUtils.getPAN(token));
                answer.setCardSecurityCode(cvv);
            }
        }

        return answer;
    }

}
