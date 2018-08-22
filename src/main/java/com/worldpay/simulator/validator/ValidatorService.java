package com.worldpay.simulator.validator;

import com.worldpay.simulator.*;
import com.worldpay.simulator.utils.EWSUtils;

import org.springframework.ws.soap.SoapHeaderElement;

import static com.worldpay.simulator.validator.Validator.validateOrderDeRegistrationRequest;
import static com.worldpay.simulator.validator.Validator.validateSoapHeader;
import static com.worldpay.simulator.validator.ValidatorUtils.isValidMerchantRefId;

public class ValidatorService {

    public static void handleExceptionsAndDelay(String merchantRefId) throws InterruptedException {
        if(!isValidMerchantRefId(merchantRefId)){
            EWSUtils.delayInResponse(merchantRefId);
            EWSUtils.handleDesiredExceptions(merchantRefId);
        }
    }

    public static void validateRequest(TokenizeRequest request,SoapHeaderElement header) throws InterruptedException {
        validateSoapHeader(header);
        Validator.validateTokenizeRequest(request);
        handleExceptionsAndDelay(request.getMerchantRefId());
    }

    public static void validateRequest(DetokenizeRequest request,SoapHeaderElement header) throws InterruptedException {
        validateSoapHeader(header);
        Validator.validateDeTokenizeRequest(request);
        handleExceptionsAndDelay(request.getMerchantRefId());
    }

    public static void validateRequest(BatchTokenizeRequest request,SoapHeaderElement header) throws InterruptedException {
        validateSoapHeader(header);
        Validator.validateBatchTokenizeRequest(request);
        handleExceptionsAndDelay(request.getMerchantRefId());
    }

    public static void validateRequest(BatchDetokenizeRequest request,SoapHeaderElement header) throws InterruptedException {
        validateSoapHeader(header);
        Validator.validateBatchDeTokenizeRequest(request);
        handleExceptionsAndDelay(request.getMerchantRefId());
    }

    public static void validateRequest(TokenInquiryRequest request,SoapHeaderElement header) throws InterruptedException {
        validateSoapHeader(header);
        Validator.validateTokenInquiryRequest(request);
        handleExceptionsAndDelay(request.getMerchantRefId());
    }

    public static void validateRequest(RegistrationRequest request,SoapHeaderElement header) throws InterruptedException {
        validateSoapHeader(header);
        Validator.validateRegistrationRequest(request);
        handleExceptionsAndDelay(request.getMerchantRefId());
    }

    public static void validateRequest(DeregistrationRequest request,SoapHeaderElement header) throws InterruptedException {
        validateSoapHeader(header);
        Validator.validateDeRegistrationRequest(request);
        handleExceptionsAndDelay(request.getMerchantRefId());
    }

    public static void validateRequest(DecryptRequest request,SoapHeaderElement header) throws InterruptedException {
        validateSoapHeader(header);
        Validator.validateDecryptRequest(request);
        handleExceptionsAndDelay(request.getMerchantRefId());
    }

    public static void validateRequest(TokenRegistrationRequest request,SoapHeaderElement header) throws InterruptedException {
        validateSoapHeader(header);
        Validator.validateTokenRegistrationRequest(request);
        handleExceptionsAndDelay(request.getMerchantRefId());
    }

    public static void validateRequest(ECheckTokenizeRequest request,SoapHeaderElement header) throws InterruptedException {
        validateSoapHeader(header);
        Validator.validateECheckTokenizeRequest(request);
        handleExceptionsAndDelay(request.getMerchantRefId());
    }

    public static void validateRequest(ECheckDetokenizeRequest request,SoapHeaderElement header) throws InterruptedException {
        validateSoapHeader(header);
        Validator.validateECheckDeTokenizeRequest(request);
        handleExceptionsAndDelay(request.getMerchantRefId());
    }

    public static void validateRequest(OrderRegistrationRequest request,SoapHeaderElement header) throws InterruptedException {
        validateSoapHeader(header);
        Validator.validateOrderRegistrationRequest(request);
        handleExceptionsAndDelay(request.getMerchantRefId());
    }

    public static void validateRequest(OrderDeregistrationRequest request,SoapHeaderElement header) throws InterruptedException {
        validateSoapHeader(header);
        validateOrderDeRegistrationRequest(request);
        handleExceptionsAndDelay(request.getMerchantRefId());
    }

    public static void validateRequest(EchoRequest request,SoapHeaderElement header) {
        validateSoapHeader(header);
    }


}
