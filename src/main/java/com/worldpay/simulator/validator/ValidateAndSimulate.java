package com.worldpay.simulator.validator;

import com.worldpay.simulator.*;
import com.worldpay.simulator.utils.EWSUtils;

import org.springframework.ws.soap.SoapHeaderElement;

import static com.worldpay.simulator.validator.Validator.validate;
import static com.worldpay.simulator.validator.Validator.validateSoapHeader;
import static com.worldpay.simulator.validator.ValidatorUtils.isValidMerchantRefId;

public class ValidateAndSimulate {



    public static void simulate(String merchantRefId) throws InterruptedException {
        if(!isValidMerchantRefId(merchantRefId)){
            EWSUtils.delayInResponse(merchantRefId);
            EWSUtils.handleDesiredExceptions(merchantRefId);
        }
    }

    public static void validateAndSimulate(TokenizeRequest request,SoapHeaderElement header) throws InterruptedException {
        validateSoapHeader(header);
        validate(request);
        simulate(request.getMerchantRefId());
    }

    public static void validateAndSimulate(DetokenizeRequest request,SoapHeaderElement header) throws InterruptedException {
        validateSoapHeader(header);
        validate(request);
        simulate(request.getMerchantRefId());
    }

    public static void validateAndSimulate(BatchTokenizeRequest request,SoapHeaderElement header) throws InterruptedException {
        validateSoapHeader(header);
        validate(request);
        simulate(request.getMerchantRefId());
    }

    public static void validateAndSimulate(BatchDetokenizeRequest request,SoapHeaderElement header) throws InterruptedException {
        validateSoapHeader(header);
        validate(request);
        simulate(request.getMerchantRefId());
    }

    public static void validateAndSimulate(TokenInquiryRequest request,SoapHeaderElement header) throws InterruptedException {
        validateSoapHeader(header);
        validate(request);
        simulate(request.getMerchantRefId());
    }

    public static void validateAndSimulate(RegistrationRequest request,SoapHeaderElement header) throws InterruptedException {
        validateSoapHeader(header);
        validate(request);
        simulate(request.getMerchantRefId());
    }

    public static void validateAndSimulate(DeregistrationRequest request,SoapHeaderElement header) throws InterruptedException {
        validateSoapHeader(header);
        validate(request);
        simulate(request.getMerchantRefId());
    }

    public static void validateAndSimulate(DecryptRequest request,SoapHeaderElement header) throws InterruptedException {
        validateSoapHeader(header);
        validate(request);
        simulate(request.getMerchantRefId());
    }

    public static void validateAndSimulate(TokenRegistrationRequest request,SoapHeaderElement header) throws InterruptedException {
        validateSoapHeader(header);
        validate(request);
        simulate(request.getMerchantRefId());
    }

    public static void validateAndSimulate(ECheckTokenizeRequest request,SoapHeaderElement header) throws InterruptedException {
        validateSoapHeader(header);
        validate(request);
        simulate(request.getMerchantRefId());
    }

    public static void validateAndSimulate(ECheckDetokenizeRequest request,SoapHeaderElement header) throws InterruptedException {
        validateSoapHeader(header);
        validate(request);
        simulate(request.getMerchantRefId());
    }

    public static void validateAndSimulate(OrderRegistrationRequest request,SoapHeaderElement header) throws InterruptedException {
        validateSoapHeader(header);
        validate(request);
        simulate(request.getMerchantRefId());
    }

    public static void validateAndSimulate(OrderDeregistrationRequest request,SoapHeaderElement header) throws InterruptedException {
        validateSoapHeader(header);
        validate(request);
        simulate(request.getMerchantRefId());
    }

    public static void validateAndSimulate(EchoRequest request,SoapHeaderElement header) {
        validateSoapHeader(header);
    }


}
