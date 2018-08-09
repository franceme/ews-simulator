package ewsSimulator.ws;

import org.apache.tomcat.util.codec.binary.Base64;
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
    private static final String DEMOBYTE = "2wABBJQ1AgAAAAAgJDUCAAAAAAA=\n" +
            "                AAAAAAAA/COBt84dnIEcwAA3gAAGhgEDoLABAAhAgAABAAAALnNCLw==,";

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

        RegistrationResponse response = new RegistrationResponse();
        String primaryAccountNumber = registrationRequest.getPrimaryAccountNumber();
        EWSUtils.delayInResponse(primaryAccountNumber);
        EWSUtils.handleDesiredExceptions(primaryAccountNumber);
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
        EWSUtils.handleDesiredExceptions(primaryAccountNumber);
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



    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "DetokenizeRequest")
    @ResponsePayload
    public DetokenizeResponse detokenize(@RequestPayload DetokenizeRequest detokenizationRequest) throws InterruptedException {
        DetokenizeResponse answer = new DetokenizeResponse();

        String requestId = EWSUtils.randomReqId();
        answer.setRequestId(requestId);

        String token = detokenizationRequest.getToken();

        MerchantType merchant = detokenizationRequest.getMerchant();

//        if(!EWSUtils.validToken(token)|| !EWSUtils.validRollupId(merchant.getRollupId()))
//          throw new WebServiceFaultException("Fault occurred while processing.");


//-------
        EWSUtils.handleDesiredExceptions(token);
//-------


        String primaryAccountNumber = EWSUtils.getPAN(token);


        EWSUtils.delayInResponse(primaryAccountNumber);


        if (detokenizationRequest.isCVV2Requested()){
            answer.setCardSecurityCode(EWSUtils.getCVVThroughToken(token));
        }
        if (detokenizationRequest.isExpirationDateRequested()){
            answer.setExpirationDate("2308");
        }

        answer.setPrimaryAccountNumber(primaryAccountNumber);


        return answer;
    }



    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "OrderDeregistrationRequest")
    @ResponsePayload
    public OrderDeregistrationResponse orderDeregistration(@RequestPayload OrderDeregistrationRequest orderDeregistrationRequest) throws InterruptedException {
        OrderDeregistrationResponse answer = new OrderDeregistrationResponse();

        answer.setRequestId(EWSUtils.randomReqId());

        String cvv = orderDeregistrationRequest.getOrderLVT();

        String token = orderDeregistrationRequest.getToken();



        MerchantType merchant = orderDeregistrationRequest.getMerchant();

//        if(!EWSUtils.validCVV(cvv)||!EWSUtils.validRollupId(merchant.getRollupId())) throw new WebServiceFaultException("Fault occurred while processing.");

//        if(EWSUtils.validToken(token)) {
//            answer.setPrimaryAccountNumber(EWSUtils.getPAN(token));
//        }
//        else answer.setPrimaryAccountNumber(DEFAULTPAN);


        String PAN = EWSUtils.getPAN(token);
// ------
        EWSUtils.handleDesiredExceptions(PAN);
// ------

        answer.setPrimaryAccountNumber(PAN);


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

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "DeregistrationRequest")
    @ResponsePayload
    public DeregistrationResponse deregistration(@RequestPayload DeregistrationRequest deregistrationRequest) throws InterruptedException {
        DeregistrationResponse answer = new DeregistrationResponse();
        String regId = deregistrationRequest.getRegId();
        //check mandatory
        MerchantType merchant = deregistrationRequest.getMerchant();
//        if(!EWSUtils.validRegId(regId)||!EWSUtils.validRollupId(merchant.getRollupId()))
//            throw new WebServiceFaultException("Fault occurred while processing.");
        String PAN = EWSUtils.getPANThroughRegId(regId);
//        if(! EWSUtils.validPAN(PAN)) throw new WebServiceFaultException("Fault occurred while processing.");
        EWSUtils.delayInResponse(PAN);
        // set requestId (mandatory)
        answer.setRequestId(EWSUtils.randomReqId());
        // set token (mandatory)
        String token = EWSUtils.getToken(PAN);

// ----
        EWSUtils.handleDesiredExceptions(token);
// ----

//        if(EWSUtils.validToken(token)) answer.setToken(token);
//        else answer.setToken("");
        answer.setToken(token);
        // set PAN (mandatory)
        answer.setPrimaryAccountNumber(PAN);
        // set expiration date (mandatory)
        // if card's cvv is odd, the expiration date would be 2308; otherwise empty
        String CVV = EWSUtils.getCVVThroughToken(token);
        if(Integer.parseInt(regId)%2 ==1) {
            answer.setExpirationDate("2308"); }else answer.setExpirationDate("");
        // set CVV (optional)
        if (deregistrationRequest.isCardSecurityCodeRequested()) {
            answer.setCardSecurityCode(CVV); }// set wallet type and ECI
        // take the last digit of CVV and module it by 3, the remaining would be indicator
        int indicator = (EWSUtils.getWallet(CVV))%3;
        if(indicator == 1) {
            answer.setWalletType(WalletType.ANDROID);
            answer.setElectronicCommerceIndicator("01"); } else if(indicator == 2) {
            answer.setWalletType(WalletType.APPLE);
            answer.setElectronicCommerceIndicator("02"); } else if(indicator == 3) {
            answer.setWalletType(WalletType.SAMSUNG);
            answer.setElectronicCommerceIndicator("03"); }// set cryptogram
        // if indicator equals 0 it means it's not DPAN
        if(indicator != 0) {
            // the soap framework will re-encode this value on its own.
            byte[] cryptogramBytes = new Base64().decode(DEMOBYTE.getBytes());
            answer.setCryptogram(cryptogramBytes);
        }return answer;
    }
}
