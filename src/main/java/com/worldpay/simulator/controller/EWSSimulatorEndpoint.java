package com.worldpay.simulator.controller;

import static com.worldpay.simulator.utils.EWSUtils.getAccountType;
import static com.worldpay.simulator.utils.EWSUtils.getError;
import static com.worldpay.simulator.utils.EWSUtils.getRoutingNumber;
import static com.worldpay.simulator.utils.ValidatorUtils.isValidToken;

import java.net.Inet4Address;
import java.net.UnknownHostException;

import javax.xml.bind.JAXBException;
import javax.xml.transform.TransformerException;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import org.springframework.ws.soap.SoapHeaderElement;
import org.springframework.ws.soap.server.endpoint.annotation.SoapHeader;

import com.worldpay.simulator.Account;
import com.worldpay.simulator.AccountType;
import com.worldpay.simulator.BatchDetokenizeRequest;
import com.worldpay.simulator.BatchDetokenizeResponse;
import com.worldpay.simulator.BatchTokenizeRequest;
import com.worldpay.simulator.BatchTokenizeResponse;
import com.worldpay.simulator.Card;
import com.worldpay.simulator.DecryptRequest;
import com.worldpay.simulator.DecryptResponse;
import com.worldpay.simulator.DeregistrationRequest;
import com.worldpay.simulator.DeregistrationResponse;
import com.worldpay.simulator.DetokenizeRequest;
import com.worldpay.simulator.DetokenizeResponse;
import com.worldpay.simulator.ECheckDetokenizeRequest;
import com.worldpay.simulator.ECheckDetokenizeResponse;
import com.worldpay.simulator.ECheckToken;
import com.worldpay.simulator.ECheckTokenizeRequest;
import com.worldpay.simulator.ECheckTokenizeResponse;
import com.worldpay.simulator.EchoRequest;
import com.worldpay.simulator.EchoResponse;
import com.worldpay.simulator.EncryptionRequest;
import com.worldpay.simulator.EncryptionResponse;
import com.worldpay.simulator.OrderDeregistrationRequest;
import com.worldpay.simulator.OrderDeregistrationResponse;
import com.worldpay.simulator.OrderRegistrationRequest;
import com.worldpay.simulator.OrderRegistrationResponse;
import com.worldpay.simulator.pojo.OutputFields;
import com.worldpay.simulator.RegistrationRequest;
import com.worldpay.simulator.RegistrationResponse;
import com.worldpay.simulator.Token;
import com.worldpay.simulator.TokenInquiryRequest;
import com.worldpay.simulator.TokenInquiryResponse;
import com.worldpay.simulator.TokenRegistrationRequest;
import com.worldpay.simulator.TokenRegistrationResponse;
import com.worldpay.simulator.TokenizeRequest;
import com.worldpay.simulator.TokenizeResponse;
import com.worldpay.simulator.VError;
import com.worldpay.simulator.VerifoneCryptogram;
import com.worldpay.simulator.VoltageCryptogram;
import com.worldpay.simulator.WalletType;
import com.worldpay.simulator.service.SimulatorResponseService;
import com.worldpay.simulator.utils.EWSUtils;
import com.worldpay.simulator.utils.HttpHeaderUtils;
import com.worldpay.simulator.service.ValidatorService;

@RestController
@Endpoint
public class EWSSimulatorEndpoint {

    @Autowired
    HttpHeaderUtils httpHeaderUtils;

    @Autowired
    ValidatorService validatorService;

    @Autowired
    SimulatorResponseService simulatorResponseService;

    //TODO origins should be specific
    @CrossOrigin(origins = "*")
    @RequestMapping("/inputPAN")
    public OutputFields inputPAN(@RequestParam(value = "primaryAccountNumber") String primaryAccountNumber) {
        OutputFields response = new OutputFields();
        response.setToken(EWSUtils.getPANToken(primaryAccountNumber));
        response.setRegId(EWSUtils.getRegIdFromPAN(primaryAccountNumber));
        response.setTokenNewlyGenerated(EWSUtils.checkNewlyGenerated(primaryAccountNumber));
        return response;
    }

    @CrossOrigin(origins = "*")
    @RequestMapping("/inputToken")
    public OutputFields inputToken(@RequestParam(value = "token") String token) {
        OutputFields response = new OutputFields();

        response.setPAN(EWSUtils.getPAN(token));
        response.setRegId(EWSUtils.getRegIdFromToken(token));
        response.setExpDate(EWSUtils.getExpDate());
        response.setCVV(EWSUtils.getCVVThroughToken(token));

        return response;
    }

    @CrossOrigin(origins = "*")
    @RequestMapping("/inputRegId")
    public OutputFields inputRegId(@RequestParam(value = "regId") String regId) {
        OutputFields response = new OutputFields();

        response.setPAN(EWSUtils.convertRegIdToPAN(regId));
        response.setToken(EWSUtils.getPANToken(response.getPAN()));
        response.setExpDate(EWSUtils.getExpDate());
        int indicator = EWSUtils.getIndicator(regId);
        response.setECI(EWSUtils.getEci(indicator));
        response.setWalletType(EWSUtils.getWalletType(indicator));

        return response;
    }

    @CrossOrigin(origins = "*")
    @RequestMapping("/inputCVV")
    public OutputFields inputCVV(@RequestParam(value = "cvv") String cvv) {
        OutputFields response = new OutputFields();
        response.setOrderLVT(EWSUtils.getOrderLVT(cvv));

        return response;
    }

    @GetMapping("/exceptionsOff")
    @ResponseBody
    public ResponseEntity turnOffExceptions() {
        validatorService.turnOffExceptions();
        return new ResponseEntity("EWS Simulator - Exception simulations are turned off", HttpStatus.OK);
    }

    @GetMapping("/exceptionsOn")
    @ResponseBody
    public ResponseEntity turnOnExceptions() {
        validatorService.turnOnExceptions();
        return new ResponseEntity("EWS Simulator - Exception simulations are turned on", HttpStatus.OK);
    }

    @PostMapping("/clearAllResponses")
    @ResponseBody
    public ResponseEntity clearAllResponses() {
        simulatorResponseService.clearAllResponses();
        return new ResponseEntity("EWS Simulator -  All responses cleared", HttpStatus.OK);
    }

    @PostMapping("/addRegistrationResponse")
    @ResponseBody
    public ResponseEntity addRegistrationResponse(@RequestParam(value = "inputPan") String inputPan,
                                                  @RequestParam(value = "outputRegId") String outputRegId,
                                                  @RequestParam(value = "outputToken") String outputToken,
                                                  @RequestParam(value = "outputTokenNewlyGen") String outputTokenNewlyGen) {
        RegistrationResponse response = new RegistrationResponse();
        response.setRequestId(EWSUtils.randomReqId());
        response.setRegId(outputRegId);
        response.setToken(outputToken);
        response.setTokenNewlyGenerated("true".equalsIgnoreCase(outputTokenNewlyGen));
        simulatorResponseService.addRegistrationResponseToMap(inputPan, response);
        return new ResponseEntity("EWS Simulator - RegistrationResponse added", HttpStatus.OK);
    }

    @PostMapping("/addTokenizeResponse")
    @ResponseBody
    public ResponseEntity addTokenizeResponse(@RequestParam(value = "inputPan") String inputPan,
                                              @RequestParam(value = "outputToken") String outputToken,
                                              @RequestParam(value = "outputTokenNewlyGen") String outputTokenNewlyGen) {
        TokenizeResponse tokenizeResponse = new TokenizeResponse();
        tokenizeResponse.setToken(outputToken);
        tokenizeResponse.setTokenNewlyGenerated("true".equalsIgnoreCase(outputTokenNewlyGen));
        tokenizeResponse.setRequestId(EWSUtils.randomReqId());
        simulatorResponseService.addTokenizeResponseToMap(inputPan, tokenizeResponse);
        return new ResponseEntity("EWS Simulator - TokenizeResponse added", HttpStatus.OK);
    }

    @PostMapping("/addOrderRegistrationResponse")
    @ResponseBody
    public ResponseEntity addOrderRegistrationResponse(@RequestParam(value = "inputCvv") String inputCvv,
                                                       @RequestParam(value = "outputOrderLVT") String outputOrderLVT) {
        OrderRegistrationResponse orderRegistrationResponse = new OrderRegistrationResponse();
        orderRegistrationResponse.setOrderLVT(outputOrderLVT);
        orderRegistrationResponse.setRequestId(EWSUtils.randomReqId());
        simulatorResponseService.addOrderRegistrationResponseToMap(inputCvv, orderRegistrationResponse);
        return new ResponseEntity("EWS Simulator - OrderRegistrationResponse added", HttpStatus.OK);
    }

    @PostMapping("/addTokenRegistrationResponse")
    @ResponseBody
    public ResponseEntity addTokenRegistrationResponse(@RequestParam(value = "inputToken") String inputToken,
                                                       @RequestParam(value = "outputRegId") String outputRegId) {
        TokenRegistrationResponse tokenRegistrationResponse = new TokenRegistrationResponse();
        tokenRegistrationResponse.setRegId(outputRegId);
        tokenRegistrationResponse.setRequestId(EWSUtils.randomReqId());
        simulatorResponseService.addTokenRegistrationResponseToMap(inputToken, tokenRegistrationResponse);
        return new ResponseEntity("EWS Simulator - TokenregistrationResponse added", HttpStatus.OK);
    }

    @PostMapping("/addBatchTokenizePanToTokenResponse")
    @ResponseBody
    public ResponseEntity addBatchTokenizePanToTokenResponse(@RequestParam(value = "inputPan") String inputPan,
                                                             @RequestParam(value = "outputToken") String outputToken,
                                                             @RequestParam(value = "outputTokenNewlyGen") String outputTokenNewlyGen) {
        Token token = new Token();
        token.setTokenValue(outputToken);
        token.setTokenNewlyGenerated("true".equalsIgnoreCase(outputTokenNewlyGen));
        simulatorResponseService.addBatchTokenizePanToTokenResponseToMap(inputPan, token);
        return new ResponseEntity("EWS Simulator - BatchTokenizePANToTokenResponse added", HttpStatus.OK);
    }

    @PostMapping("/addBatchDetokenizeTokenToPanResponseToMap")
    @ResponseBody
    public ResponseEntity addBatchDetokenizeTokenToPanResponseToMap(@RequestParam(value = "inputToken") String inputToken,
                                                                    @RequestParam(value = "outputPan") String outputPan) {
        simulatorResponseService.addBatchDetokenizeTokenToPanResponseToMap(inputToken, outputPan);
        return new ResponseEntity("EWS Simulator - BatchDetokenizeTokenToPANResponse added", HttpStatus.OK);
    }

    @PostMapping("/addECheckTokenizePanToTokenResponse")
    @ResponseBody
    public ResponseEntity addECheckTokenizePanToTokenResponse(@RequestParam(value = "inputPan") String inputPan,
                                                              @RequestParam(value = "outputToken") String outputToken,
                                                              @RequestParam(value = "outputTokenNewlyGen") String outputTokenNewlyGen) {
        Token token = new Token();
        token.setTokenValue(outputToken);
        token.setTokenNewlyGenerated("true".equalsIgnoreCase(outputTokenNewlyGen));
        simulatorResponseService.addECheckTokenizePanToTokenResponseToMap(inputPan, token);
        return new ResponseEntity("EWS Simulator - EcheckTokenizePanToTokenResponse added", HttpStatus.OK);
    }

    @PostMapping("/addECheckDetokenizeTokenToAccountResponse")
    @ResponseBody
    public ResponseEntity addECheckDetokenizeTokenToAccountResponse(@RequestParam(value = "inputToken") String inputToken,
                                                                    @RequestParam(value = "outputAccNum") String outputAccNum,
                                                                    @RequestParam(value = "outputAccType") String outputAccType,
                                                                    @RequestParam(value = "outputAccRoutingNum") String outputAccRoutingNum) {
        Account account = new Account();

        account.setAccountNumber(outputAccNum);
        account.setAccountType(AccountType.fromValue(outputAccType));
        account.setRoutingNumber(outputAccRoutingNum);
        simulatorResponseService.addECheckDetokenizeTokenToAccountResponseToMap(inputToken, account);
        return new ResponseEntity("EWS Simulator - ECheckDetokenizeTokenToAccountResponse added", HttpStatus.OK);
    }

    @PostMapping("/addTokenInquiryPanToTokenResponse")
    @ResponseBody
    public ResponseEntity addTokenInquiryPanToTokenResponse(@RequestParam(value = "inputPan") String inputPan,
                                                            @RequestParam(value = "outputToken") String outputToken,
                                                            @RequestParam(value = "outputTokenNewlyGen") String outputTokenNewlyGen) {
        boolean tokenNewlyGenerated = "true".equalsIgnoreCase(outputTokenNewlyGen);
        if (tokenNewlyGenerated) {
            simulatorResponseService.addTokenInquiryPanToTokenResponseToMap(inputPan, null);
            return new ResponseEntity("EWS Simulator - TokenInquiryPanToTokenResponse added", HttpStatus.OK);
        }
        Token token = new Token();
        token.setTokenValue(outputToken);
        token.setTokenNewlyGenerated(false);
        simulatorResponseService.addTokenInquiryPanToTokenResponseToMap(inputPan, token);
        return new ResponseEntity("EWS Simulator - TokenInquiryPanToTokenResponse added", HttpStatus.OK);
    }

    @PostMapping("/addDetokenizeResponse")
    @ResponseBody
    public ResponseEntity addDetokenizeResponse(@RequestParam(value = "inputToken") String inputToken,
                                                @RequestParam(value = "outputPan") String outputPan,
                                                @RequestParam(value = "outputCvv") String outputCvv,
                                                @RequestParam(value = "outputExpDate") String outputExpDate) {
        DetokenizeResponse detokenizeResponse = new DetokenizeResponse();
        detokenizeResponse.setRequestId(EWSUtils.randomReqId());
        if (outputCvv != null && outputCvv != "") {
            detokenizeResponse.setCardSecurityCode(outputCvv);
        }
        if (outputExpDate != null && outputExpDate != "") {
            detokenizeResponse.setExpirationDate(outputExpDate);
        }
        detokenizeResponse.setPrimaryAccountNumber(outputPan);
        simulatorResponseService.addDetokenizeResponseToMap(inputToken, detokenizeResponse);
        return new ResponseEntity("EWS Simulator - DetokenizeResponse added", HttpStatus.OK);
    }

    @PostMapping("/addOrderDeregistrationResponse")
    @ResponseBody
    public ResponseEntity addOrderDeregistrationResponse(@RequestParam(value = "inputToken") String inputToken,
                                                         @RequestParam(value = "outputPan") String outputPan,
                                                         @RequestParam(value = "outputCvv") String outputCvv) {
        OrderDeregistrationResponse orderDeregistrationResponse = new OrderDeregistrationResponse();

        orderDeregistrationResponse.setRequestId(EWSUtils.randomReqId());
        orderDeregistrationResponse.setPrimaryAccountNumber(outputPan);
        orderDeregistrationResponse.setCardSecurityCode(outputCvv);
        simulatorResponseService.addOrderDeregistrationResponseToMap(inputToken, orderDeregistrationResponse);
        return new ResponseEntity("EWS Simulator - OrderDeregistrationResponse added", HttpStatus.OK);
    }

    @PostMapping("/addDeregistrationResponse")
    @ResponseBody
    public ResponseEntity addDeregistrationResponse(@RequestParam(value = "inputRegId") String inputRegId,
                                                    @RequestParam(value = "outputToken") String outputToken,
                                                    @RequestParam(value = "outputPan") String outputPan,
                                                    @RequestParam(value = "outputCvv") String outputCvv,
                                                    @RequestParam(value = "outputExpDate") String outputExpDate,
                                                    @RequestParam(value = "outputWalletType") String outputWalletType,
                                                    @RequestParam(value = "outputEci") String outputEci,
                                                    @RequestParam(value = "outputCryptogram") String outputCryptogram) {
        DeregistrationResponse deregistrationResponse = new DeregistrationResponse();
        deregistrationResponse.setRequestId(EWSUtils.randomReqId());
        deregistrationResponse.setToken(outputToken);
        deregistrationResponse.setPrimaryAccountNumber(outputPan);
        deregistrationResponse.setExpirationDate(outputExpDate);
        if (outputCvv != null && outputCvv != "") {
            deregistrationResponse.setCardSecurityCode(outputCvv);
        }
        if (outputEci != null && outputEci != "") {
            deregistrationResponse.setElectronicCommerceIndicator(outputEci);
        }
        if (outputCryptogram != null && outputCryptogram != "") {
            deregistrationResponse.setCryptogram(new Base64().decode(outputCryptogram.getBytes()));
        }
        if (outputWalletType != null && outputWalletType != "") {
            deregistrationResponse.setWalletType(WalletType.valueOf(outputWalletType));
        }
        simulatorResponseService.addDeregistrationResponseToMap(inputRegId, deregistrationResponse);
        return new ResponseEntity("EWS Simulator - DeregistrationResponse added", HttpStatus.OK);
    }

    @PostMapping("/addRegistrationException")
    @ResponseBody
    public ResponseEntity addRegistrationException(@RequestParam(value = "inputPan") String inputPan,
                                                   @RequestParam(value = "outputErrorId") Integer outputErrorId) {
        simulatorResponseService.addRegistrationExceptionToMap(inputPan, outputErrorId);
        return new ResponseEntity("EWS Simulator - RegistrationException added", HttpStatus.OK);
    }

    @PostMapping("/addTokenizeException")
    @ResponseBody
    public ResponseEntity addTokenizeException(@RequestParam(value = "inputPan") String inputPan,
                                               @RequestParam(value = "outputErrorId") Integer outputErrorId) {
        simulatorResponseService.addTokenizeExceptionToMap(inputPan, outputErrorId);
        return new ResponseEntity("EWS Simulator - TokenizeException added", HttpStatus.OK);
    }

    @PostMapping("/addOrderRegistrationException")
    @ResponseBody
    public ResponseEntity addOrderRegistrationException(@RequestParam(value = "inputCvv") String inputCvv,
                                                        @RequestParam(value = "outputErrorId") Integer outputErrorId) {
        simulatorResponseService.addOrderRegistrationExceptionToMap(inputCvv, outputErrorId);
        return new ResponseEntity("EWS Simulator - OrderRegistrationException added", HttpStatus.OK);
    }

    @PostMapping("/addTokenRegistrationException")
    @ResponseBody
    public ResponseEntity addTokenRegistrationException(@RequestParam(value = "inputToken") String inputToken,
                                                        @RequestParam(value = "outputErrorId") Integer outputErrorId) {
        simulatorResponseService.addTokenRegistrationExceptionToMap(inputToken, outputErrorId);
        return new ResponseEntity("EWS Simulator - TokenregistrationException added", HttpStatus.OK);
    }

    @PostMapping("/addBatchTokenizeException")
    @ResponseBody
    public ResponseEntity addBatchTokenizeException(@RequestParam(value = "inputPan") String inputPan,
                                                    @RequestParam(value = "outputErrorId") Integer outputErrorId) {
        simulatorResponseService.addBatchTokenizeExceptionToMap(inputPan, outputErrorId);
        return new ResponseEntity("EWS Simulator - BatchTokenizeException added", HttpStatus.OK);
    }

    @PostMapping("/addBatchDetokenizeException")
    @ResponseBody
    public ResponseEntity addBatchDetokenizeException(@RequestParam(value = "inputToken") String inputToken,
                                                      @RequestParam(value = "outputErrorId") Integer outputErrorId) {
        simulatorResponseService.addBatchDetokenizeExceptionToMap(inputToken, outputErrorId);
        return new ResponseEntity("EWS Simulator - BatchDetokenizeException added", HttpStatus.OK);
    }

    @PostMapping("/addEcheckTokenizeException")
    @ResponseBody
    public ResponseEntity addEcheckTokenizeException(@RequestParam(value = "inputPan") String inputPan,
                                                     @RequestParam(value = "outputErrorId") Integer outputErrorId) {
        simulatorResponseService.addECheckTokenizeExceptionToMap(inputPan, outputErrorId);
        return new ResponseEntity("EWS Simulator - EcheckTokenizeException added", HttpStatus.OK);
    }

    @PostMapping("/addECheckDetokenizeException")
    @ResponseBody
    public ResponseEntity addECheckDetokenizeException(@RequestParam(value = "inputToken") String inputToken,
                                                       @RequestParam(value = "outputErrorId") Integer outputErrorId) {
        simulatorResponseService.addECheckDetokenizeExceptionToMap(inputToken, outputErrorId);
        return new ResponseEntity("EWS Simulator - ECheckDetokenizeResponseException added", HttpStatus.OK);
    }

    /*@PostMapping("/addTokenInquiryException")
    @ResponseBody
    public ResponseEntity addTokenInquiryException() {
        return new ResponseEntity("EWS Simulator - TokenInquiryException added", HttpStatus.OK);
    }*/

    @PostMapping("/addDetokenizeException")
    @ResponseBody
    public ResponseEntity addDetokenizeException(@RequestParam(value = "inputToken") String inputToken,
                                                 @RequestParam(value = "outputErrorId") Integer outputErrorId) {
        simulatorResponseService.addDetokenizeExceptionToMap(inputToken, outputErrorId);
        return new ResponseEntity("EWS Simulator - DetokenizeException added", HttpStatus.OK);
    }

    @PostMapping("/addOrderDeregistrationException")
    @ResponseBody
    public ResponseEntity addOrderDeregistrationException(@RequestParam(value = "inputToken") String inputToken,
                                                          @RequestParam(value = "outputErrorId") Integer outputErrorId) {
        simulatorResponseService.addOrderDeregistrationExceptionToMap(inputToken, outputErrorId);
        return new ResponseEntity("EWS Simulator - OrderDeregistrationException added", HttpStatus.OK);
    }

    @PostMapping("/addDeregistrationException")
    @ResponseBody
    public ResponseEntity addDeregistrationException(@RequestParam(value = "inputRegId") String inputRegId,
                                                     @RequestParam(value = "outputErrorId") Integer outputErrorId) {
        simulatorResponseService.addDeregistrationExceptionToMap(inputRegId, outputErrorId);
        return new ResponseEntity("EWS Simulator - DeregistrationException added", HttpStatus.OK);
    }


    private static final String NAMESPACE_URI = "urn:com:vantiv:types:encryption:transactions:v1";
    private static final String HEADER_URI = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd";
    private static final String DEMOBYTE = "2wABBJQ1AgAAAAAgJDUCAAAAAAA=\n" +
            "                AAAAAAAA/COBt84dnIEcwAA3gAAGhgEDoLABAAhAgAABAAAALnNCLw==,";


    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "RegistrationRequest")
    @ResponsePayload
    public RegistrationResponse registration(@RequestPayload RegistrationRequest request,
                                             @SoapHeader("{" + HEADER_URI + "}Security") SoapHeaderElement auth) throws InterruptedException, JAXBException, TransformerException {

        httpHeaderUtils.customizeHttpResponseHeader();
        String primaryAccountNumber = request.getPrimaryAccountNumber();
        // Check for saved exceptions
        Integer savedException = simulatorResponseService.getRegistrationExceptionSavedIfAny(primaryAccountNumber);
        if (savedException != null) {
            EWSUtils.throwDesiredException(savedException);
        }

        // Check for saved responses
        RegistrationResponse savedResponse = simulatorResponseService.getRegistrationResponseSavedIfAny(primaryAccountNumber);
        if (savedResponse != null) {
            addMerchantRefId(request, savedResponse);
            return savedResponse;
        }

        // Fallback
        validatorService.validateRequest(request, auth);
        RegistrationResponse response = new RegistrationResponse();
        addMerchantRefId(request, response);
        response.setRequestId(EWSUtils.randomReqId());
        response.setRegId(EWSUtils.getRegIdFromPAN(primaryAccountNumber));
        response.setToken(EWSUtils.getPANToken(primaryAccountNumber));
        response.setTokenNewlyGenerated(EWSUtils.checkNewlyGenerated(primaryAccountNumber));

        return response;
    }


    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "TokenizeRequest")
    @ResponsePayload
    public TokenizeResponse tokenize(@RequestPayload TokenizeRequest tokenizeRequest,
                                     @SoapHeader("{" + HEADER_URI + "}Security") SoapHeaderElement auth) throws InterruptedException {
        httpHeaderUtils.customizeHttpResponseHeader();
        String primaryAccountNumber = tokenizeRequest.getPrimaryAccountNumber();

        // Check for saved exceptions
        Integer savedException = simulatorResponseService.getTokenizeExceptionSavedIfAny(primaryAccountNumber);
        if (savedException != null) {
            EWSUtils.throwDesiredException(savedException);
        }

        // Check for saved responses
        TokenizeResponse savedResponse = simulatorResponseService.getTokenizeResponseSavedIfAny(primaryAccountNumber);
        if (savedResponse != null) {
            addMerchantRefId(tokenizeRequest, savedResponse);
            return savedResponse;
        }

        // Fallback
        validatorService.validateRequest(tokenizeRequest, auth);
        TokenizeResponse tokenizeResponse = new TokenizeResponse();
        addMerchantRefId(tokenizeRequest, tokenizeResponse);
        tokenizeResponse.setToken(EWSUtils.getPANToken(primaryAccountNumber));
        tokenizeResponse.setTokenNewlyGenerated(EWSUtils.checkNewlyGenerated(primaryAccountNumber));
        tokenizeResponse.setRequestId(EWSUtils.randomReqId());
        return tokenizeResponse;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "OrderRegistrationRequest")
    @ResponsePayload
    public OrderRegistrationResponse orderRegistration(@RequestPayload OrderRegistrationRequest request,
                                                       @SoapHeader("{" + HEADER_URI + "}Security") SoapHeaderElement auth) throws InterruptedException {

        //handle default validator based on the merchantID or PAN
        httpHeaderUtils.customizeHttpResponseHeader();
        String cvv = request.getCardSecurityCode();

        // Check for saved exceptions
        Integer savedException = simulatorResponseService.getOrderRegistrationExceptionSavedIfAny(cvv);
        if (savedException != null) {
            EWSUtils.throwDesiredException(savedException);
        }

        // Check for saved responses
        OrderRegistrationResponse savedResponse = simulatorResponseService.getOrderRegistrationResponseSavedIfAny(cvv);
        if (savedResponse != null) {
            addMerchantRefId(request, savedResponse);
            return savedResponse;
        }

        // Fallback
        validatorService.validateRequest(request, auth);

        OrderRegistrationResponse response = new OrderRegistrationResponse();
        String orderLVT = "3";

        //Generates the OrderLVT by repeating cvv until its at least 18 characters
        while (orderLVT.length() < 18) {
            if (!(cvv.equals(""))) {
                orderLVT += cvv;
            } else {
                orderLVT += "00000000000000000";
            }
        }
        response.setOrderLVT(orderLVT.substring(0, 18));

        response.setRequestId(EWSUtils.randomReqId());
        addMerchantRefId(request, response);
        return response;

    }


    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "TokenRegistrationRequest")
    @ResponsePayload
    public TokenRegistrationResponse tokenRegistration(@RequestPayload TokenRegistrationRequest request,
                                                       @SoapHeader("{" + HEADER_URI + "}Security") SoapHeaderElement auth) throws InterruptedException {
        httpHeaderUtils.customizeHttpResponseHeader();
        String token = request.getToken();

        // Check for saved exceptions
        Integer savedException = simulatorResponseService.getTokenRegistrationExceptionSavedIfAny(token);
        if (savedException != null) {
            EWSUtils.throwDesiredException(savedException);
        }

        // Check for saved responses
        TokenRegistrationResponse savedResponse = simulatorResponseService.getTokenRegistrationResponseSavedIfAny(token);
        if (savedResponse != null) {
            addMerchantRefId(request, savedResponse);
            return savedResponse;
        }

        // Fallback
        validatorService.validateRequest(request, auth);

        TokenRegistrationResponse tokenRegistrationResponse = new TokenRegistrationResponse();
        addMerchantRefId(request, tokenRegistrationResponse);
        tokenRegistrationResponse.setRequestId(EWSUtils.randomReqId());

        tokenRegistrationResponse.setRegId(EWSUtils.getRegIdFromToken(token));

        return tokenRegistrationResponse;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "BatchTokenizeRequest")
    @ResponsePayload
    public BatchTokenizeResponse batchTokenize(@RequestPayload BatchTokenizeRequest request,
                                               @SoapHeader("{" + HEADER_URI + "}Security") SoapHeaderElement auth) throws InterruptedException {

        //handle default validator based on the merchantID or PAN
        httpHeaderUtils.customizeHttpResponseHeader();
        validatorService.validateRequest(request, auth);

        BatchTokenizeResponse response = new BatchTokenizeResponse();

        for (Card card : request.getCard()) {
            String PAN = card.getPrimaryAccountNumber();
            Token token = new Token();

            VError error = getError(PAN);
            if (error != null) {
                token.setError(error);
                // break was specified in the document, but the cert environment behaves differently (So commenting out the below lines)
                // response.getToken().add(token);
                // break;
            } else {
                token.setTokenValue(EWSUtils.getPANToken(PAN));
                token.setTokenNewlyGenerated(EWSUtils.checkNewlyGenerated(PAN));
            }
            response.getToken().add(token);

        }
        addMerchantRefId(request, response);
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "BatchDetokenizeRequest")
    @ResponsePayload
    public BatchDetokenizeResponse batchDetokenize(@RequestPayload BatchDetokenizeRequest request,
                                                   @SoapHeader("{" + HEADER_URI + "}Security") SoapHeaderElement auth) throws InterruptedException {

        //handle default validator based on the merchantID or PAN
        httpHeaderUtils.customizeHttpResponseHeader();
        validatorService.validateRequest(request, auth);

        BatchDetokenizeResponse response = new BatchDetokenizeResponse();

        for (Token token : request.getToken()) {
            String tokenValue = token.getTokenValue();
            Card card = new Card();

            VError error = getError(tokenValue);
            if (error != null) {
                card.setError(error);
                // break was specified in the document, but the cert environment behaves differently (So commenting out the below lines)
                //response.getCard().add(card);
                //break;
            } else {
                card.setPrimaryAccountNumber(EWSUtils.getPAN(tokenValue));
            }
            response.getCard().add(card);

        }

        addMerchantRefId(request, response);
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "ECheckTokenizeRequest")
    @ResponsePayload
    public ECheckTokenizeResponse echeckTokenize(@RequestPayload ECheckTokenizeRequest request,
                                                 @SoapHeader("{" + HEADER_URI + "}Security") SoapHeaderElement auth) throws InterruptedException {

        //handle default validator based on the merchantID or PAN
        httpHeaderUtils.customizeHttpResponseHeader();
        validatorService.validateRequest(request, auth);

        ECheckTokenizeResponse response = new ECheckTokenizeResponse();

        String primaryAccountNumber = request.getAccount().getAccountNumber();

        ECheckToken token = new ECheckToken();

        VError error = getError(primaryAccountNumber);
        if (error != null) {
            token.setError(error);
            response.setToken(token);
        } else {
            token.setTokenValue(EWSUtils.generateEcheckToken(primaryAccountNumber, request.getAccount().getAccountType()));
            token.setTokenNewlyGenerated(EWSUtils.checkNewlyGenerated(primaryAccountNumber));
        }
        response.setToken(token);

        addMerchantRefId(request, response);
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "ECheckDetokenizeRequest")
    @ResponsePayload
    public ECheckDetokenizeResponse eCheckDetokenizeResponse(@RequestPayload ECheckDetokenizeRequest request,
                                                             @SoapHeader("{" + HEADER_URI + "}Security") SoapHeaderElement auth) throws InterruptedException {

        //handle default validator based on the merchantID or PAN
        httpHeaderUtils.customizeHttpResponseHeader();
        validatorService.validateRequest(request, auth);

        ECheckDetokenizeResponse response = new ECheckDetokenizeResponse();
        String token = request.getToken().getTokenValue();
        String PAN = EWSUtils.generateEcheckAccount(token);

        Account account = new Account();

        account.setAccountNumber(PAN);
        account.setAccountType(getAccountType(token));
        account.setRoutingNumber(getRoutingNumber(PAN));

        response.setAccount(account);

        addMerchantRefId(request, response);
        return response;
    }


    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "TokenInquiryRequest")
    @ResponsePayload
    public TokenInquiryResponse tokenInquiry(@RequestPayload TokenInquiryRequest tokenInquiryRequest,
                                             @SoapHeader("{" + HEADER_URI + "}Security") SoapHeaderElement auth) throws InterruptedException {
        httpHeaderUtils.customizeHttpResponseHeader();
        validatorService.validateRequest(tokenInquiryRequest, auth);

        TokenInquiryResponse tokenInquiryResponse = new TokenInquiryResponse();
        addMerchantRefId(tokenInquiryRequest, tokenInquiryResponse);

        for (Card card : tokenInquiryRequest.getCard()) {
            String primaryAccountNumber = card.getPrimaryAccountNumber();

            if (EWSUtils.checkNewlyGenerated(primaryAccountNumber)) {
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
        String token = request.getToken();

        // Check for saved exceptions
        Integer savedException = simulatorResponseService.getDetokenizeExceptionSavedIfAny(token);
        if (savedException != null) {
            EWSUtils.throwDesiredException(savedException);
        }

        // Check for saved responses
        DetokenizeResponse savedResponse = simulatorResponseService.getDetokenizeResponseSavedIfAny(token);
        if (savedResponse != null) {
            addMerchantRefId(request, savedResponse);
            return savedResponse;
        }

        // Fallback

        validatorService.validateRequest(request, auth);

        DetokenizeResponse answer = new DetokenizeResponse();

        String requestId = EWSUtils.randomReqId();
        answer.setRequestId(requestId);


        String primaryAccountNumber = EWSUtils.getPAN(token);
        if (request.isCVV2Requested() != null && request.isCVV2Requested()) {
            answer.setCardSecurityCode(EWSUtils.getCVVThroughToken(token));
        }
        if (request.isExpirationDateRequested() != null && request.isExpirationDateRequested()) {
            answer.setExpirationDate("5001");
        }

        answer.setPrimaryAccountNumber(primaryAccountNumber);


        return answer;
    }


    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "OrderDeregistrationRequest")
    @ResponsePayload
    public OrderDeregistrationResponse orderDeregistration(@RequestPayload OrderDeregistrationRequest request,
                                                           @SoapHeader("{" + HEADER_URI + "}Security") SoapHeaderElement auth) throws InterruptedException {

        httpHeaderUtils.customizeHttpResponseHeader();

        validatorService.validateRequest(request, auth);

        OrderDeregistrationResponse answer = new OrderDeregistrationResponse();

        answer.setRequestId(EWSUtils.randomReqId());

        String LVT = request.getOrderLVT();

        String token = request.getToken();

        if (!isValidToken(token) || token.length() < 3) token = "3000100011118566";

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
        String regId = request.getRegId();

        // Check for saved exceptions
        Integer savedException = simulatorResponseService.getDeregistrationExceptionSavedIfAny(regId);
        if (savedException != null) {
            EWSUtils.throwDesiredException(savedException);
        }

        // Check for saved responses
        DeregistrationResponse savedResponse = simulatorResponseService.getDeregistrationResponseSavedIfAny(regId);
        if (savedResponse != null) {
            addMerchantRefId(request, savedResponse);
            return savedResponse;
        }

        // Fallback
        validatorService.validateRequest(request, auth);

        DeregistrationResponse answer = new DeregistrationResponse();
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
        // if card's cvv is odd, the expiration date would be 5001; otherwise empty
        String CVV = EWSUtils.getCVVThroughToken(token);
        answer.setExpirationDate("5001");
        // set CVV (optional)
        if (request.isCardSecurityCodeRequested() != null
                && request.isCardSecurityCodeRequested() && !"000".equals(CVV)) {
            answer.setCardSecurityCode(CVV);
        }// set wallet type and ECI
        // take the last digit of CVV and module it by 3, the remaining would be indicator
        int indicator = (Integer.parseInt(regId.charAt(regId.length() - 2) + "")) % 4;
        if (indicator == 1) {
            answer.setWalletType(WalletType.ANDROID);
            answer.setElectronicCommerceIndicator("07");
        } else if (indicator == 2) {
            answer.setWalletType(WalletType.APPLE);
            answer.setElectronicCommerceIndicator("05");
        } else if (indicator == 3) {
            answer.setWalletType(WalletType.SAMSUNG);
            answer.setElectronicCommerceIndicator("07");
        }// set cryptogram
        // if indicator equals 0 it means it's not DPAN
        if (indicator != 0) {
            // the soap framework will re-encode this value on its own.
            byte[] cryptogramBytes = new Base64().decode(DEMOBYTE.getBytes());
            answer.setCryptogram(cryptogramBytes);
        }
        return answer;
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
        if (encryptedPAN != null) {
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
        if (encryptedPAN != null) {
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

        validatorService.validateRequest(request, auth);

        EchoResponse answer = new EchoResponse();
        String test = request.getTest();
        answer.setResponse(test);
        answer.setProjectVersion("4.3.10-RELEASE");
        answer.setBuildNumber("devBuild");
        answer.setRevisionNumber("devBuild");
        answer.setHostEnvironment(Inet4Address.getLocalHost().getHostAddress());
        return answer;
    }
}
