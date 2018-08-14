package ewsSimulator.ws;
import java.security.Security;
import java.util.Iterator;
import java.util.List;

import javax.activation.DataHandler;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;

import ewsSimulator.ws.validator.Validator;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.ws.client.WebServiceFaultException;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import org.springframework.ws.soap.SoapHeaderElement;
import org.springframework.ws.soap.SoapMessage;
import org.springframework.ws.soap.saaj.SaajSoapMessage;
import org.springframework.ws.soap.server.endpoint.annotation.SoapHeader;
import org.springframework.ws.transport.context.TransportContext;
import org.springframework.ws.transport.context.TransportContextHolder;
import org.springframework.ws.transport.http.HttpServletConnection;

import com.sun.org.apache.xerces.internal.dom.ElementNSImpl;


import java.util.UUID;

import static ewsSimulator.ws.EWSUtils.*;
import static ewsSimulator.ws.validator.ValidateAndSimulate.validateAndSimulate;


@Endpoint
public class EWSSimulatorEndpoint {

    private static final String NAMESPACE_URI = "urn:com:vantiv:types:encryption:transactions:v1";
    private static final String HEADER_URI = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd";
    private static final String DEFAULTPAN= "4266841015771878";
    private static final String DEMOBYTE = "2wABBJQ1AgAAAAAgJDUCAAAAAAA=\n" +
            "                AAAAAAAA/COBt84dnIEcwAA3gAAGhgEDoLABAAhAgAABAAAALnNCLw==,";
  /*  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "BatchDetokenizeRequest")
    @ResponsePayload
    public BatchDetokenizeResponse batchDetokenize(@RequestPayload BatchDetokenizeRequest batchDetokenizeRequest) {
        return new BatchDetokenizeResponse();
    }*/


    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "EchoRequest")
    @ResponsePayload
    public EchoResponse echo(@RequestPayload EchoRequest echoRequest) {
        return new EchoResponse();
    }

    /**
     *
     * @param request
     * @return
     */
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "RegistrationRequest")
    @ResponsePayload
    public RegistrationResponse registration(@RequestPayload RegistrationRequest request,
                                             @SoapHeader("{" + HEADER_URI + "}Security") SoapHeaderElement auth) throws InterruptedException, JAXBException, TransformerException {

        customizeHttpResponseHeader();
        validateAndSimulate(request,auth);



        RegistrationResponse response = new RegistrationResponse();

        String primaryAccountNumber = request.getPrimaryAccountNumber();
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
    public TokenizeResponse tokenize(@RequestPayload TokenizeRequest tokenizeRequest,
                                     @SoapHeader("{" + HEADER_URI + "}Security") SoapHeaderElement auth) throws InterruptedException {
        customizeHttpResponseHeader();
        validateAndSimulate(tokenizeRequest,auth);


        TokenizeResponse tokenizeResponse = new TokenizeResponse();
        addMerchantRefId(tokenizeRequest, tokenizeResponse);

        String primaryAccountNumber = tokenizeRequest.getPrimaryAccountNumber();
        int lengthPAN = primaryAccountNumber.length();
        tokenizeResponse.setToken(EWSUtils.getToken(primaryAccountNumber));

        if(lengthPAN >= 3 && ("000".equals(primaryAccountNumber.substring(lengthPAN - 3)))) {
            tokenizeResponse.setTokenNewlyGenerated(true);
        } else {
            tokenizeResponse.setTokenNewlyGenerated(false);
        }

        tokenizeResponse.setRequestId(EWSUtils.randomReqId());
        return tokenizeResponse;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "OrderRegistrationRequest")
    @ResponsePayload
    public OrderRegistrationResponse orderRegistration(@RequestPayload OrderRegistrationRequest request,
                                                       @SoapHeader("{" + HEADER_URI + "}Security") SoapHeaderElement auth) throws InterruptedException {

        //handle default validator based on the merchantID or PAN
        customizeHttpResponseHeader();
        validateAndSimulate(request,auth);

        OrderRegistrationResponse response = new OrderRegistrationResponse();
        String cvv = request.getCardSecurityCode();
        String orderLVT = "3";

        if(!EWSUtils.isSecurityCodeEmpty(cvv))
            response.setOrderLVT(orderLVT+EWSUtils.generateRandomNumber(17));

        response.setRequestId(EWSUtils.randomReqId());

        return response;

    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "BatchTokenizeRequest")
    @ResponsePayload
    public BatchTokenizeResponse batchTokenize(@RequestPayload BatchTokenizeRequest request,
                                               @SoapHeader("{" + HEADER_URI + "}Security") SoapHeaderElement auth) throws InterruptedException {

        //handle default validator based on the merchantID or PAN
        customizeHttpResponseHeader();
        validateAndSimulate(request,auth);

        BatchTokenizeResponse response = new BatchTokenizeResponse();

        for(Card card : request.getCard()){
            String PAN = card.getPrimaryAccountNumber();
            Token token = new Token();

            VError error = getError(PAN);
            if(error != null){
                token.setError(error);
                response.getToken().add(token);
                break;
            }else{
                token.setTokenValue(EWSUtils.getToken(PAN));
                token.setTokenNewlyGenerated(PAN.endsWith("000") ? true:false);
            }
            response.getToken().add(token);

        }
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "BatchDetokenizeRequest")
    @ResponsePayload
    public BatchDetokenizeResponse batchDetokenize(@RequestPayload BatchDetokenizeRequest request,
                                               @SoapHeader("{" + HEADER_URI + "}Security") SoapHeaderElement auth) throws InterruptedException {

        //handle default validator based on the merchantID or PAN
        customizeHttpResponseHeader();
        validateAndSimulate(request,auth);

        BatchDetokenizeResponse response = new BatchDetokenizeResponse();

        for(Token token : request.getToken()){
            String tokenValue = token.getTokenValue();
            Card card = new Card();

            VError error = getError(tokenValue);
            if(error != null){
                card.setError(error);
                response.getCard().add(card);
                break;
            }else{
                card.setPrimaryAccountNumber(EWSUtils.getPAN(tokenValue));
            }
            response.getCard().add(card);

        }

        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "ECheckTokenizeRequest")
    @ResponsePayload
    public ECheckTokenizeResponse echeckTokenize(@RequestPayload ECheckTokenizeRequest request,
                                                   @SoapHeader("{" + HEADER_URI + "}Security") SoapHeaderElement auth) throws InterruptedException {

        //handle default validator based on the merchantID or PAN
        customizeHttpResponseHeader();
        validateAndSimulate(request,auth);

        ECheckTokenizeResponse response = new ECheckTokenizeResponse();

        String AccNum = request.getAccount().getAccountNumber();

        ECheckToken token = new ECheckToken();

        VError error = getError(AccNum);
        if(error != null){
            token.setError(error);
            response.setToken(token);
        }else{
            token.setTokenValue(EWSUtils.getToken(AccNum));
            token.setTokenNewlyGenerated(AccNum.endsWith("000")?true:false);
        }

        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "ECheckDetokenizeRequest")
    @ResponsePayload
    public ECheckDetokenizeResponse eCheckDetokenizeResponse(@RequestPayload ECheckDetokenizeRequest request,
                                                 @SoapHeader("{" + HEADER_URI + "}Security") SoapHeaderElement auth) throws InterruptedException {

        //handle default validator based on the merchantID or PAN
        customizeHttpResponseHeader();
        validateAndSimulate(request,auth);

        ECheckDetokenizeResponse response = new ECheckDetokenizeResponse();
        String token = request.getToken().getTokenValue();
        String PAN = EWSUtils.getPAN(token);

        Account account = new Account();

        account.setAccountNumber(PAN);
        account.setAccountType(getAccountType(PAN));
        account.setRoutingNumber(getRoutingNumber(PAN));

        response.setAccount(account);

        return response;
    }


    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "TokenInquiryRequest")
    @ResponsePayload
    public TokenInquiryResponse tokenInquiry(@RequestPayload TokenInquiryRequest tokenInquiryRequest,
                                             @SoapHeader("{" + HEADER_URI + "}Security") SoapHeaderElement auth) throws InterruptedException {
        customizeHttpResponseHeader();
        validateAndSimulate(tokenInquiryRequest,auth);

        TokenInquiryResponse tokenInquiryResponse = new TokenInquiryResponse();
        String merchantRefId = tokenInquiryRequest.getMerchantRefId();
        if (merchantRefId != null)
            tokenInquiryResponse.setMerchantRefId(merchantRefId);

        for (Card card: tokenInquiryRequest.getCard()) {
            String primaryAccountNumber = card.getPrimaryAccountNumber();
            int lengthPAN = primaryAccountNumber.length();

            if ("000".equals(primaryAccountNumber.substring(lengthPAN - 3))) {
                tokenInquiryResponse.getToken().add(null);
            } else {
                Token token = new Token();
                token.setTokenValue(EWSUtils.getToken(primaryAccountNumber));
                tokenInquiryResponse.getToken().add(token);
            }
        }

        return tokenInquiryResponse;
    }




    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "DetokenizeRequest")
    @ResponsePayload
    public DetokenizeResponse detokenize(@RequestPayload DetokenizeRequest request,
                                         @SoapHeader("{" + HEADER_URI + "}Security") SoapHeaderElement auth) throws InterruptedException {
//        customizeHttpResponseHeader();

        validateAndSimulate(request,auth);

        DetokenizeResponse answer = new DetokenizeResponse();

        String requestId = EWSUtils.randomReqId();
        answer.setRequestId(requestId);

        String token = request.getToken();

        EWSUtils.handleDesiredExceptions(token);

        String primaryAccountNumber = EWSUtils.getPAN(token);

        EWSUtils.delayInResponse(request.merchantRefId);


        if (request.isCVV2Requested()){
            answer.setCardSecurityCode(EWSUtils.getCVVThroughToken(token));
        }
        if (request.isExpirationDateRequested()){
            answer.setExpirationDate("2308");
        }

        answer.setPrimaryAccountNumber(primaryAccountNumber);


        return answer;
    }



    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "OrderDeregistrationRequest")
    @ResponsePayload
    public OrderDeregistrationResponse orderDeregistration(@RequestPayload OrderDeregistrationRequest request,
                                                           @SoapHeader("{" + HEADER_URI + "}Security") SoapHeaderElement auth) throws InterruptedException {
//        customizeHttpResponseHeader();

        validateAndSimulate(request,auth);

        OrderDeregistrationResponse answer = new OrderDeregistrationResponse();

        answer.setRequestId(EWSUtils.randomReqId());

        String LVT = request.getOrderLVT();

        String token = request.getToken();

        String PAN = EWSUtils.getPAN(token);

        EWSUtils.handleDesiredExceptions(PAN);

        answer.setPrimaryAccountNumber(PAN);


        if(LVT.startsWith("3")) {
            // check position 2 for '6', '7', '8' or '9' to simulate error
            if (LVT.charAt(2) == '6') {
                VError error = new VError();
                error.setId(9999);
                error.setMessage("GENERIC CHECKOUT_ID ERROR");
                answer.getError().add(error);
            } else if (LVT.charAt(2) == '7') {
                VError error = new VError();
                error.setId(2);
                error.setMessage("GENERIC CHECKOUT_ID ERROR");
                answer.getError().add(error);
            } else if (LVT.charAt(2) == '8') {
                VError error = new VError();
                error.setId(4);
                error.setMessage("CHECKOUT_ID INVALID");
                answer.getError().add(error);
            } else if (LVT.charAt(2) == '9') {
                VError error = new VError();
                error.setId(6);
                error.setMessage("CHECKOUT_ID NOT_FOUND");
                answer.getError().add(error);
            } else {
                EWSUtils.delayInResponse(request.merchantRefId);
                answer.setCardSecurityCode(LVT);
            }
        }
        return answer;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "DeregistrationRequest")
    @ResponsePayload
    public DeregistrationResponse deregistration(@RequestPayload DeregistrationRequest request,
                                                 @SoapHeader("{" + HEADER_URI + "}Security") SoapHeaderElement auth) throws InterruptedException {
//        customizeHttpResponseHeader();

        validateAndSimulate(request,auth);
        DeregistrationResponse answer = new DeregistrationResponse();
        String regId = request.getRegId();
        String PAN = EWSUtils.getPANThroughRegId(regId);
        EWSUtils.delayInResponse(request.merchantRefId);

        // set requestId (mandatory)
        answer.setRequestId(EWSUtils.randomReqId());
        // set token (mandatory)
        String token = EWSUtils.getToken(PAN);
        EWSUtils.handleDesiredExceptions(token);
        answer.setToken(token);
        // set PAN (mandatory)
        answer.setPrimaryAccountNumber(PAN);
        // set expiration date (mandatory)
        // if card's cvv is odd, the expiration date would be 2308; otherwise empty
        String CVV = EWSUtils.getCVVThroughToken(token);
        answer.setExpirationDate("2308");
        // set CVV (optional)
        if (request.isCardSecurityCodeRequested()) {
            answer.setCardSecurityCode(CVV); }// set wallet type and ECI
        // take the last digit of CVV and module it by 3, the remaining would be indicator
        int indicator = (Integer.parseInt(regId.charAt(regId.length()-1)+"")) % 4;
        if(indicator == 1) {
            answer.setWalletType(WalletType.ANDROID);
            answer.setElectronicCommerceIndicator("01");
        } else if(indicator == 2) {
            answer.setWalletType(WalletType.APPLE);
            answer.setElectronicCommerceIndicator("02");
        } else if(indicator == 3) {
            answer.setWalletType(WalletType.SAMSUNG);
            answer.setElectronicCommerceIndicator("03"); }// set cryptogram
        // if indicator equals 0 it means it's not DPAN
        if(indicator != 0) {
            // the soap framework will re-encode this value on its own.
            byte[] cryptogramBytes = new Base64().decode(DEMOBYTE.getBytes());
            answer.setCryptogram(cryptogramBytes);
        }return answer;
    }


    public void addMerchantRefId(EncryptionRequest request, EncryptionResponse response) {
        String merchantRefId = request.getMerchantRefId();
        if (merchantRefId != null)
            response.setMerchantRefId(merchantRefId);
    }
}
