package com.worldpay.simulator.validator;

import static com.worldpay.simulator.validator.ValidatorUtils.isValidAccount;
import static com.worldpay.simulator.validator.ValidatorUtils.isValidCVV;
import static com.worldpay.simulator.validator.ValidatorUtils.isValidMerchantRefId;
import static com.worldpay.simulator.validator.ValidatorUtils.isValidPAN;
import static com.worldpay.simulator.validator.ValidatorUtils.isValidRegId;
import static com.worldpay.simulator.validator.ValidatorUtils.isValidToken;
import static java.lang.Thread.sleep;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ws.soap.SoapHeaderElement;

import com.worldpay.simulator.BatchDetokenizeRequest;
import com.worldpay.simulator.BatchTokenizeRequest;
import com.worldpay.simulator.Card;
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
import com.worldpay.simulator.VerifoneCryptogram;
import com.worldpay.simulator.utils.EWSUtils;

@Service
public class ValidatorService {

    @Autowired
    private RequestValidator requestValidator;

    @Value("${simulate.delay}")
    private int delay = 0;

    public void handleExceptionsAndDelayForPAN(String input) throws InterruptedException {
        if (delay > 0) { // Delay is set in properties to be fixed in every request
            sleep(delay);
        }
        if (isValidPAN(input)) {
            if (delay < 0) { // Delay is not set, use PAN, reg_id, or token as per availability to calculate
                addDelay(input);
            }
            EWSUtils.handleDesiredExceptions(input);
        }
    }

    public void handleExceptionsAndDelayForToken(String input) throws InterruptedException {
        if (delay > 0) { // Delay is set in properties to be fixed in every request
            sleep(delay);
        }
        if (isValidToken(input)) {
            if (delay < 0) { // Delay is not set, use PAN, reg_id, or token as per availability to calculate
                addDelay(input);
            }
            EWSUtils.handleDesiredExceptions(input);
        }
    }

    public void handleExceptionsAndDelayForRegId(String input) throws InterruptedException {
        if (delay > 0) { // Delay is set in properties to be fixed in every request
            sleep(delay);
        }
        if (isValidRegId(input)) {
            if (delay < 0) { // Delay is not set, use PAN, reg_id, or token as per availability to calculate
                addDelay(input);
            }
            EWSUtils.handleDesiredExceptions(input);
        }
    }

    public void handleExceptionsAndDelayForCVV(String input) throws InterruptedException {
        if (delay > 0) { // Delay is set in properties to be fixed in every request
            sleep(delay);
        }
        if (isValidCVV(input)) {
            if (delay < 0) { // Delay is not set, use PAN, reg_id, or token as per availability to calculate
                addDelayForCVV(input);
            }
            EWSUtils.handleDesiredExceptionsForCVV(input);
        }
    }

    public void handleExceptionsAndDelayForAccountNumber(String input) throws InterruptedException {
        if (delay > 0) { // Delay is set in properties to be fixed in every request
            sleep(delay);
        }
        if (isValidAccount(input)) {
            if (delay < 0) { // Delay is not set, use PAN, reg_id, or token as per availability to calculate
                addDelay(input);
            }
            EWSUtils.handleDesiredExceptions(input);
        }
    }

    public void validateRequest(TokenizeRequest request, SoapHeaderElement header) throws InterruptedException {
        requestValidator.validateSoapHeader(header);
        requestValidator.validateTokenizeRequest(request);
        handleExceptionsAndDelayForPAN(request.getPrimaryAccountNumber());
    }

    public void validateRequest(DetokenizeRequest request, SoapHeaderElement header) throws InterruptedException {
        requestValidator.validateSoapHeader(header);
        requestValidator.validateDetokenizeRequest(request);
        handleExceptionsAndDelayForToken(request.getToken());
    }

    public void validateRequest(BatchTokenizeRequest request, SoapHeaderElement header) throws InterruptedException {
        String input = request.getMerchantRefId();
        requestValidator.validateSoapHeader(header);
        requestValidator.validateBatchTokenizeRequest(request);
        if (delay > 0) { // Delay is set in properties to be fixed in every request
            sleep(delay);
        }
        if (!isValidMerchantRefId(input)) {
            if (delay < 0) { // Delay is not set, use PAN, reg_id, or token as per availability to calculate
                addDelay(input);
            }
        }
    }

    public void validateRequest(BatchDetokenizeRequest request, SoapHeaderElement header) throws InterruptedException {
        String input = request.getMerchantRefId();
        requestValidator.validateSoapHeader(header);
        requestValidator.validateBatchDetokenizeRequest(request);
        if (delay > 0) { // Delay is set in properties to be fixed in every request
            sleep(delay);
        }
        if (!isValidMerchantRefId(input)) {
            if (delay < 0) { // Delay is not set, use PAN, reg_id, or token as per availability to calculate
                addDelay(input);
            }
        }
    }

    public void validateRequest(TokenInquiryRequest request, SoapHeaderElement header) throws InterruptedException {
        String input = request.getMerchantRefId();
        requestValidator.validateSoapHeader(header);
        requestValidator.validateTokenInquiryRequest(request);
        if (delay > 0) { // Delay is set in properties to be fixed in every request
            sleep(delay);
        }
        if (!isValidMerchantRefId(input)) {
            if (delay < 0) { // Delay is not set, use PAN, reg_id, or token as per availability to calculate
                addDelay(input);
            }
        }
    }

    public void validateRequest(RegistrationRequest request, SoapHeaderElement header) throws InterruptedException {
        requestValidator.validateSoapHeader(header);
        requestValidator.validateRegistrationRequest(request);
        handleExceptionsAndDelayForPAN(request.getPrimaryAccountNumber());
    }

    public void validateRequest(DeregistrationRequest request, SoapHeaderElement header) throws InterruptedException {
        requestValidator.validateSoapHeader(header);
        requestValidator.validateDeregistrationRequest(request);
        handleExceptionsAndDelayForRegId(request.getRegId());
    }

    public void validateRequest(DecryptRequest request, SoapHeaderElement header) throws InterruptedException {
        requestValidator.validateSoapHeader(header);
        requestValidator.validateDecryptRequest(request);
        VerifoneCryptogram verifoneCryptogram = request.getVerifoneCryptogram();
        Card encryptedCard = verifoneCryptogram.getEncryptedCard();

        String encryptedPAN = encryptedCard.getPrimaryAccountNumber();
        handleExceptionsAndDelayForPAN(encryptedPAN);
    }

    public void validateRequest(TokenRegistrationRequest request, SoapHeaderElement header) throws InterruptedException {
        requestValidator.validateSoapHeader(header);
        requestValidator.validateTokenRegistrationRequest(request);
        handleExceptionsAndDelayForToken(request.getToken());
    }

    public void validateRequest(ECheckTokenizeRequest request, SoapHeaderElement header) throws InterruptedException {
        requestValidator.validateSoapHeader(header);
        requestValidator.validateECheckTokenizeRequest(request);
        handleExceptionsAndDelayForAccountNumber(request.getAccount().getAccountNumber());
    }

    public void validateRequest(ECheckDetokenizeRequest request, SoapHeaderElement header) throws InterruptedException {
        requestValidator.validateSoapHeader(header);
        requestValidator.validateECheckDetokenizeRequest(request);
        handleExceptionsAndDelayForToken(request.getToken().getTokenValue());
    }

    public void validateRequest(OrderRegistrationRequest request, SoapHeaderElement header) throws InterruptedException {
        requestValidator.validateSoapHeader(header);
        requestValidator.validateOrderRegistrationRequest(request);
        handleExceptionsAndDelayForCVV(request.getCardSecurityCode());
    }

    public void validateRequest(OrderDeregistrationRequest request, SoapHeaderElement header) throws InterruptedException {
        requestValidator.validateSoapHeader(header);
        requestValidator.validateOrderDeregistrationRequest(request);
        handleExceptionsAndDelayForToken(request.getToken());
    }

    public void validateRequest(EchoRequest request, SoapHeaderElement header) {
        requestValidator.validateSoapHeader(header);
    }


    public void addDelay(String input) throws InterruptedException {
        if (input != null) {
            int inputLength = input.length();
            if (inputLength > 2 && input.substring(0, 2).equals("00")) {
                int time = Integer.parseInt(input.substring(2, 3));
                sleep(time * 100);
            }
        }
    }

    public void addDelayForCVV(String input) throws InterruptedException {
        if (input != null) {
            int inputLength = input.length();
            if (inputLength > 1 && input.charAt(1) == '0') {
                int time = Integer.parseInt(input.substring(2, 3));
                sleep(time * 100);
            }
        }
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }
}
