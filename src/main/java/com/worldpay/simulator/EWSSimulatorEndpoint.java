package com.worldpay.simulator;

import static com.worldpay.simulator.utils.EWSUtils.getAccountType;
import static com.worldpay.simulator.utils.EWSUtils.getError;
import static com.worldpay.simulator.utils.EWSUtils.getRoutingNumber;
import static com.worldpay.simulator.validator.ValidatorUtils.isValidToken;

import javax.xml.bind.JAXBException;
import javax.xml.transform.TransformerException;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import org.springframework.ws.soap.SoapHeaderElement;
import org.springframework.ws.soap.server.endpoint.annotation.SoapHeader;

import com.worldpay.simulator.utils.EWSUtils;
import com.worldpay.simulator.utils.HttpHeaderUtils;
import com.worldpay.simulator.validator.ValidatorService;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.UnknownHostException;


@Endpoint
public class EWSSimulatorEndpoint {

    @Autowired
    HttpHeaderUtils httpHeaderUtils;

    @Autowired
    ValidatorService validatorService;

    private static final String NAMESPACE_URI = "urn:com:vantiv:types:encryption:transactions:v1";
    private static final String HEADER_URI = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd";
    private static final String DEMOBYTE = "2wABBJQ1AgAAAAAgJDUCAAAAAAA=\n" +
            "                AAAAAAAA/COBt84dnIEcwAA3gAAGhgEDoLABAAhAgAABAAAALnNCLw==,";




    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "RegistrationRequest")
    @ResponsePayload
    public RegistrationResponse registration(@RequestPayload RegistrationRequest request,
                                             @SoapHeader("{" + HEADER_URI + "}Security") SoapHeaderElement auth) throws InterruptedException, JAXBException, TransformerException {

        httpHeaderUtils.customizeHttpResponseHeader();
        validatorService.validateRequest(request,auth);

        RegistrationResponse response = new RegistrationResponse();

        String primaryAccountNumber = request.getPrimaryAccountNumber();
        int lengthPAN = primaryAccountNumber.length();
        addMerchantRefId(request, response);
        response.setRequestId(EWSUtils.randomReqId());
        response.setRegId(EWSUtils.getRegIdFromPAN(primaryAccountNumber));
        response.setToken(EWSUtils.getPANToken(primaryAccountNumber));

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
        httpHeaderUtils.customizeHttpResponseHeader();
        validatorService.validateRequest(tokenizeRequest,auth);


        TokenizeResponse tokenizeResponse = new TokenizeResponse();
        addMerchantRefId(tokenizeRequest, tokenizeResponse);

        String primaryAccountNumber = tokenizeRequest.getPrimaryAccountNumber();
        int lengthPAN = primaryAccountNumber.length();
        tokenizeResponse.setToken(EWSUtils.getPANToken(primaryAccountNumber));

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
        httpHeaderUtils.customizeHttpResponseHeader();
        validatorService.validateRequest(request,auth);

        OrderRegistrationResponse response = new OrderRegistrationResponse();
        String cvv = request.getCardSecurityCode();
        String orderLVT = "3";

        response.setOrderLVT(orderLVT+EWSUtils.generateRandomNumber(17));
        response.setRequestId(EWSUtils.randomReqId());
        addMerchantRefId(request,response);
        return response;

    }


    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "TokenRegistrationRequest")
    @ResponsePayload
    public TokenRegistrationResponse tokenRegistration(@RequestPayload TokenRegistrationRequest tokenRegistrationRequest,
                                             @SoapHeader("{" + HEADER_URI + "}Security") SoapHeaderElement auth) throws InterruptedException {
        httpHeaderUtils.customizeHttpResponseHeader();
        validatorService.validateRequest(tokenRegistrationRequest, auth);

        TokenRegistrationResponse tokenRegistrationResponse = new TokenRegistrationResponse();
        addMerchantRefId(tokenRegistrationRequest, tokenRegistrationResponse);
        tokenRegistrationResponse.setRequestId(EWSUtils.randomReqId());

        String token = tokenRegistrationRequest.getToken();
        tokenRegistrationResponse.setRegId(EWSUtils.getRegIdFromToken(token));

        return tokenRegistrationResponse;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "BatchTokenizeRequest")
    @ResponsePayload
    public BatchTokenizeResponse batchTokenize(@RequestPayload BatchTokenizeRequest request,
                                               @SoapHeader("{" + HEADER_URI + "}Security") SoapHeaderElement auth) throws InterruptedException {

        //handle default validator based on the merchantID or PAN
        httpHeaderUtils.customizeHttpResponseHeader();
        validatorService.validateRequest(request,auth);

        BatchTokenizeResponse response = new BatchTokenizeResponse();

        for(Card card : request.getCard()){
            String PAN = card.getPrimaryAccountNumber();
            Token token = new Token();

            VError error = getError(PAN);
            if(error != null){
                token.setError(error);
                // break was specified in the document, but the cert environment behaves differently (So commenting out the below lines)
                // response.getToken().add(token);
                // break;
            }else{
                token.setTokenValue(EWSUtils.getPANToken(PAN));
                token.setTokenNewlyGenerated(PAN.endsWith("000") ? true:false);
            }
            response.getToken().add(token);

        }
        addMerchantRefId(request,response);
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "BatchDetokenizeRequest")
    @ResponsePayload
    public BatchDetokenizeResponse batchDetokenize(@RequestPayload BatchDetokenizeRequest request,
                                               @SoapHeader("{" + HEADER_URI + "}Security") SoapHeaderElement auth) throws InterruptedException {

        //handle default validator based on the merchantID or PAN
        httpHeaderUtils.customizeHttpResponseHeader();
        validatorService.validateRequest(request,auth);

        BatchDetokenizeResponse response = new BatchDetokenizeResponse();

        for(Token token : request.getToken()){
            String tokenValue = token.getTokenValue();
            Card card = new Card();

            VError error = getError(tokenValue);
            if(error != null){
                card.setError(error);
                // break was specified in the document, but the cert environment behaves differently (So commenting out the below lines)
                //response.getCard().add(card);
                //break;
            }else{
                card.setPrimaryAccountNumber(EWSUtils.getPAN(tokenValue));
            }
            response.getCard().add(card);

        }

        addMerchantRefId(request,response);
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "ECheckTokenizeRequest")
    @ResponsePayload
    public ECheckTokenizeResponse echeckTokenize(@RequestPayload ECheckTokenizeRequest request,
                                                   @SoapHeader("{" + HEADER_URI + "}Security") SoapHeaderElement auth) throws InterruptedException {

        //handle default validator based on the merchantID or PAN
        httpHeaderUtils.customizeHttpResponseHeader();
        validatorService.validateRequest(request,auth);

        ECheckTokenizeResponse response = new ECheckTokenizeResponse();

        String AccNum = request.getAccount().getAccountNumber();

        ECheckToken token = new ECheckToken();

        VError error = getError(AccNum);
        if(error != null){
            token.setError(error);
            response.setToken(token);
        }else{
            token.setTokenValue(EWSUtils.generateEcheckToken(AccNum,request.getAccount().getAccountType()));
            token.setTokenNewlyGenerated(AccNum.endsWith("000")?true:false);
        }
        response.setToken(token);

        addMerchantRefId(request,response);
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "ECheckDetokenizeRequest")
    @ResponsePayload
    public ECheckDetokenizeResponse eCheckDetokenizeResponse(@RequestPayload ECheckDetokenizeRequest request,
                                                 @SoapHeader("{" + HEADER_URI + "}Security") SoapHeaderElement auth) throws InterruptedException {

        //handle default validator based on the merchantID or PAN
        httpHeaderUtils.customizeHttpResponseHeader();
        validatorService.validateRequest(request,auth);

        ECheckDetokenizeResponse response = new ECheckDetokenizeResponse();
        String token = request.getToken().getTokenValue();
        String PAN = EWSUtils.generateEcheckAccount(token);
        String AccountTypeValue = token.substring(token.length()-1,token.length());

        Account account = new Account();

        account.setAccountNumber(PAN);
        account.setAccountType(getAccountType(token));
        account.setRoutingNumber(getRoutingNumber(PAN));

        response.setAccount(account);

        addMerchantRefId(request,response);
        return response;
    }


    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "TokenInquiryRequest")
    @ResponsePayload
    public TokenInquiryResponse tokenInquiry(@RequestPayload TokenInquiryRequest tokenInquiryRequest,
                                             @SoapHeader("{" + HEADER_URI + "}Security") SoapHeaderElement auth) throws InterruptedException {
        httpHeaderUtils.customizeHttpResponseHeader();
        validatorService.validateRequest(tokenInquiryRequest,auth);

        TokenInquiryResponse tokenInquiryResponse = new TokenInquiryResponse();
        addMerchantRefId(tokenInquiryRequest, tokenInquiryResponse);

        for (Card card: tokenInquiryRequest.getCard()) {
            String primaryAccountNumber = card.getPrimaryAccountNumber();
            int lengthPAN = primaryAccountNumber.length();

            if ("000".equals(primaryAccountNumber.substring(lengthPAN - 3))) {
                tokenInquiryResponse.getToken().add(null);
            } else {
                Token token = new Token();
                token.setTokenValue(EWSUtils.getPANToken(primaryAccountNumber));
                tokenInquiryResponse.getToken().add(token);
            }
        }

        return tokenInquiryResponse;
    }


    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "DetokenizeRequest")
    @ResponsePayload
    public DetokenizeResponse detokenize(@RequestPayload DetokenizeRequest request,
                                         @SoapHeader("{" + HEADER_URI + "}Security") SoapHeaderElement auth) throws InterruptedException {

        httpHeaderUtils.customizeHttpResponseHeader();

        validatorService.validateRequest(request,auth);

        DetokenizeResponse answer = new DetokenizeResponse();

        String requestId = EWSUtils.randomReqId();
        answer.setRequestId(requestId);

        String token = request.getToken();

        String primaryAccountNumber = EWSUtils.getPAN(token);

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

        httpHeaderUtils.customizeHttpResponseHeader();

        validatorService.validateRequest(request,auth);

        OrderDeregistrationResponse answer = new OrderDeregistrationResponse();

        answer.setRequestId(EWSUtils.randomReqId());

        String LVT = request.getOrderLVT();

        String token = request.getToken();

        if(!isValidToken(token) || token.length()<3) token = "3000100011118566";

        String PAN = EWSUtils.getPAN(token);

        EWSUtils.handleDesiredExceptions(PAN);

        answer.setPrimaryAccountNumber(PAN);


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
            answer.setCardSecurityCode(EWSUtils.getCVVThroughToken(token));
        }
        return answer;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "DeregistrationRequest")
    @ResponsePayload
    public DeregistrationResponse deregistration(@RequestPayload DeregistrationRequest request,
                                                 @SoapHeader("{" + HEADER_URI + "}Security") SoapHeaderElement auth) throws InterruptedException {
        httpHeaderUtils.customizeHttpResponseHeader();
        validatorService.validateRequest(request,auth);

        DeregistrationResponse answer = new DeregistrationResponse();
        String regId = request.getRegId();
        String PAN = EWSUtils.getPANThroughRegId(regId);

        // set requestId (mandatory)
        answer.setRequestId(EWSUtils.randomReqId());
        // set token (mandatory)
        String token = EWSUtils.getPANToken(PAN);
        EWSUtils.handleDesiredExceptions(token);
        answer.setToken(token);
        // set PAN (mandatory)
        answer.setPrimaryAccountNumber(PAN);
        // set expiration date (mandatory)
        // if card's cvv is odd, the expiration date would be 2308; otherwise empty
        String CVV = EWSUtils.getCVVThroughToken(token);
        answer.setExpirationDate("2308");
        // set CVV (optional)
        if (request.isCardSecurityCodeRequested() != null && request.isCardSecurityCodeRequested()) {
            answer.setCardSecurityCode(CVV); }// set wallet type and ECI
        // take the last digit of CVV and module it by 3, the remaining would be indicator
        int indicator = (Integer.parseInt(regId.charAt(regId.length()-1)+"")) % 4;
        if(indicator == 1) {
            answer.setWalletType(WalletType.ANDROID);
            answer.setElectronicCommerceIndicator("07");
        } else if(indicator == 2) {
            answer.setWalletType(WalletType.APPLE);
            answer.setElectronicCommerceIndicator("05");
        } else if(indicator == 3) {
            answer.setWalletType(WalletType.SAMSUNG);
            answer.setElectronicCommerceIndicator("07"); }// set cryptogram
        // if indicator equals 0 it means it's not DPAN
        if(indicator != 0) {
            // the soap framework will re-encode this value on its own.
            byte[] cryptogramBytes = new Base64().decode(DEMOBYTE.getBytes());
            answer.setCryptogram(cryptogramBytes);
        }return answer;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "DecryptRequest")
    @ResponsePayload
    public DecryptResponse decrypt(@RequestPayload DecryptRequest decryptRequest,
                                   @SoapHeader("{" + HEADER_URI + "}Security") SoapHeaderElement auth) throws InterruptedException {

        httpHeaderUtils.customizeHttpResponseHeader();
        validatorService.validateRequest(decryptRequest, auth);

        DecryptResponse decryptResponse = new DecryptResponse();
        addMerchantRefId(decryptRequest, decryptResponse);

        if (decryptRequest.getVerifoneCryptogram() != null) {
            handleVerifoneCryptogram(decryptRequest, decryptResponse);
        } else {
            handleVoltageCryptogram(decryptRequest, decryptResponse);
        }

        return decryptResponse;
    }


    public void addMerchantRefId(EncryptionRequest request, EncryptionResponse response) {
        String merchantRefId = request.getMerchantRefId();
        if (merchantRefId != null)
            response.setMerchantRefId(merchantRefId);
    }

    public void handleVerifoneCryptogram(DecryptRequest decryptRequest, DecryptResponse decryptResponse) {
        VerifoneCryptogram verifoneCryptogram = decryptRequest.getVerifoneCryptogram();
        Card encryptedCard = verifoneCryptogram.getEncryptedCard();
        Card decryptedCard = new Card();

        String encryptedPAN = encryptedCard.getPrimaryAccountNumber();
        if (encryptedPAN != null){
            decryptedCard.setPrimaryAccountNumber(EWSUtils.decrypt(encryptedPAN));
            decryptedCard.setExpirationDate(encryptedCard.getExpirationDate());
        } else {
            handleCardWithTrack(encryptedCard, decryptedCard);
        }
        decryptResponse.setDecryptedCard(decryptedCard);
    }

    public void handleVoltageCryptogram(DecryptRequest decryptRequest, DecryptResponse decryptResponse) {
        VoltageCryptogram voltageCryptogram = decryptRequest.getVoltageCryptogram();
        Card encryptedCard = voltageCryptogram.getEncryptedCard();
        Card decryptedCard = new Card();

        String encryptedPAN = encryptedCard.getPrimaryAccountNumber();
        if (encryptedPAN != null){
            decryptedCard.setPrimaryAccountNumber(EWSUtils.decrypt(encryptedPAN));
            decryptedCard.setSecurityCode(encryptedCard.getSecurityCode());
        } else {
            handleCardWithTrack(encryptedCard, decryptedCard);
        }
        decryptResponse.setDecryptedCard(decryptedCard);
    }

    public void handleCardWithTrack(Card encryptedCard, Card decryptedCard) {
        String encryptedTrack1;
        if ((encryptedTrack1 = encryptedCard.getTrack1()) != null) {
            decryptedCard.setTrack1(EWSUtils.decrypt(encryptedTrack1));
        } else {
            String encryptedTrack2 = encryptedCard.getTrack2();
            decryptedCard.setTrack2(EWSUtils.decrypt(encryptedTrack2));
        }
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "EchoRequest")
    @ResponsePayload
    public EchoResponse echo(@RequestPayload EchoRequest request,
                             @SoapHeader("{" + HEADER_URI + "}Security") SoapHeaderElement auth) throws UnknownHostException {

        httpHeaderUtils.customizeHttpResponseHeader();

        validatorService.validateRequest(request,auth);

        EchoResponse answer = new EchoResponse();
        String test = request.getTest();
        answer.setResponse(test);
        answer.setProjectVersion("4.3.0-RELEASE");
        answer.setBuildNumber("devBuild");
        answer.setRevisionNumber("devBuild");
        answer.setHostEnvironment(Inet4Address.getLocalHost().getHostAddress());
        return answer;
    }
}
