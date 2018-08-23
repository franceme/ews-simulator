package com.worldpay.simulator.validator;

import static com.worldpay.simulator.validator.ValidatorUtils.isValidMerchantRefId;
import static java.lang.Thread.sleep;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ws.soap.SoapHeaderElement;

import com.worldpay.simulator.BatchDetokenizeRequest;
import com.worldpay.simulator.BatchTokenizeRequest;
import com.worldpay.simulator.DecryptRequest;
import com.worldpay.simulator.DeregistrationRequest;
import com.worldpay.simulator.DetokenizeRequest;
import com.worldpay.simulator.ECheckDetokenizeRequest;
import com.worldpay.simulator.ECheckTokenizeRequest;
import com.worldpay.simulator.EchoRequest;
import com.worldpay.simulator.OrderDeregistrationRequest;
import com.worldpay.simulator.OrderRegistrationRequest;
import com.worldpay.simulator.RegistrationRequest;
import com.worldpay.simulator.TokenInquiryRequest;
import com.worldpay.simulator.TokenRegistrationRequest;
import com.worldpay.simulator.TokenizeRequest;
import com.worldpay.simulator.utils.EWSUtils;

@Service
public class ValidatorService {

    @Autowired
    private RequestValidator requestValidator;

    @Value("${simulate.delay}")
    private int delay = 0;

    public void handleExceptionsAndDelay(String merchantRefId) throws InterruptedException {
        if (delay > 0) { // Delay is set in properties to be fixed in every request
            sleep(delay);
        }
        if (!isValidMerchantRefId(merchantRefId)) {
            if (delay < 0) { // Delay is not set, use merchant ref id to calculate
                addDelayBasedOnMerchantRefId(merchantRefId);
            }
            EWSUtils.handleDesiredExceptions(merchantRefId);
        }
    }

    public void validateRequest(TokenizeRequest request, SoapHeaderElement header) throws InterruptedException {
        requestValidator.validateSoapHeader(header);
        requestValidator.validateTokenizeRequest(request);
        handleExceptionsAndDelay(request.getMerchantRefId());
    }

    public void validateRequest(DetokenizeRequest request, SoapHeaderElement header) throws InterruptedException {
        requestValidator.validateSoapHeader(header);
        requestValidator.validateDetokenizeRequest(request);
        handleExceptionsAndDelay(request.getMerchantRefId());
    }

    public void validateRequest(BatchTokenizeRequest request, SoapHeaderElement header) throws InterruptedException {
        requestValidator.validateSoapHeader(header);
        requestValidator.validateBatchTokenizeRequest(request);
        handleExceptionsAndDelay(request.getMerchantRefId());
    }

    public void validateRequest(BatchDetokenizeRequest request, SoapHeaderElement header) throws InterruptedException {
        requestValidator.validateSoapHeader(header);
        requestValidator.validateBatchDetokenizeRequest(request);
        handleExceptionsAndDelay(request.getMerchantRefId());
    }

    public void validateRequest(TokenInquiryRequest request, SoapHeaderElement header) throws InterruptedException {
        requestValidator.validateSoapHeader(header);
        requestValidator.validateTokenInquiryRequest(request);
        handleExceptionsAndDelay(request.getMerchantRefId());
    }

    public void validateRequest(RegistrationRequest request, SoapHeaderElement header) throws InterruptedException {
        requestValidator.validateSoapHeader(header);
        requestValidator.validateRegistrationRequest(request);
        handleExceptionsAndDelay(request.getMerchantRefId());
    }

    public void validateRequest(DeregistrationRequest request, SoapHeaderElement header) throws InterruptedException {
        requestValidator.validateSoapHeader(header);
        requestValidator.validateDeregistrationRequest(request);
        handleExceptionsAndDelay(request.getMerchantRefId());
    }

    public void validateRequest(DecryptRequest request, SoapHeaderElement header) throws InterruptedException {
        requestValidator.validateSoapHeader(header);
        requestValidator.validateDecryptRequest(request);
        handleExceptionsAndDelay(request.getMerchantRefId());
    }

    public void validateRequest(TokenRegistrationRequest request, SoapHeaderElement header) throws InterruptedException {
        requestValidator.validateSoapHeader(header);
        requestValidator.validateTokenRegistrationRequest(request);
        handleExceptionsAndDelay(request.getMerchantRefId());
    }

    public void validateRequest(ECheckTokenizeRequest request, SoapHeaderElement header) throws InterruptedException {
        requestValidator.validateSoapHeader(header);
        requestValidator.validateECheckTokenizeRequest(request);
        handleExceptionsAndDelay(request.getMerchantRefId());
    }

    public void validateRequest(ECheckDetokenizeRequest request, SoapHeaderElement header) throws InterruptedException {
        requestValidator.validateSoapHeader(header);
        requestValidator.validateECheckDetokenizeRequest(request);
        handleExceptionsAndDelay(request.getMerchantRefId());
    }

    public void validateRequest(OrderRegistrationRequest request, SoapHeaderElement header) throws InterruptedException {
        requestValidator.validateSoapHeader(header);
        requestValidator.validateOrderRegistrationRequest(request);
        handleExceptionsAndDelay(request.getMerchantRefId());
    }

    public void validateRequest(OrderDeregistrationRequest request, SoapHeaderElement header) throws InterruptedException {
        requestValidator.validateSoapHeader(header);
        requestValidator.validateOrderDeregistrationRequest(request);
        handleExceptionsAndDelay(request.getMerchantRefId());
    }

    public void validateRequest(EchoRequest request, SoapHeaderElement header) {
        requestValidator.validateSoapHeader(header);
    }


    public void addDelayBasedOnMerchantRefId(String merchantRefId) throws InterruptedException {
        if (merchantRefId != null) {
            if (merchantRefId.length() > 2 && merchantRefId.substring(0, 2).equals("00")) {
                int time = Integer.parseInt(merchantRefId.substring(2, 3));
                sleep(time * 100);
            }
        }
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }
}
