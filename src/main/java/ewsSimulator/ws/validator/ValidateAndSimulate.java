package ewsSimulator.ws.validator;

import ewsSimulator.ws.*;

import static ewsSimulator.ws.validator.Validator.validate;
import static ewsSimulator.ws.validator.ValidatorUtils.isValidMerchantRefId;

public class ValidateAndSimulate {

    public static void simulate(String merchantRefId) throws InterruptedException {
        if(!isValidMerchantRefId(merchantRefId)){
            EWSUtils.delayInResponse(merchantRefId);
            EWSUtils.handleDesiredExceptions(merchantRefId);
        }
    }

    public static void validateAndSimulate(TokenizeRequest request) throws InterruptedException {
        validate(request);
        simulate(request.getMerchantRefId());
    }

    public static void validateAndSimulate(DetokenizeRequest request) throws InterruptedException {
        validate(request);
        simulate(request.getMerchantRefId());
    }

    public static void validateAndSimulate(BatchTokenizeRequest request) throws InterruptedException {
        validate(request);
        simulate(request.getMerchantRefId());
    }

    public static void validateAndSimulate(BatchDetokenizeRequest request) throws InterruptedException {
        validate(request);
        simulate(request.getMerchantRefId());
    }

    public static void validateAndSimulate(TokenInquiryRequest request) throws InterruptedException {
        validate(request);
        simulate(request.getMerchantRefId());
    }

    public static void validateAndSimulate(RegistrationRequest request) throws InterruptedException {
        validate(request);
        simulate(request.getMerchantRefId());
    }

    public static void validateAndSimulate(DeregistrationRequest request) throws InterruptedException {
        validate(request);
        simulate(request.getMerchantRefId());
    }

    public static void validateAndSimulate(DecryptRequest request) throws InterruptedException {
        validate(request);
        simulate(request.getMerchantRefId());
    }

    public static void validateAndSimulate(TokenRegistrationRequest request) throws InterruptedException {
        validate(request);
        simulate(request.getMerchantRefId());
    }

    public static void validateAndSimulate(ECheckTokenizeRequest request) throws InterruptedException {
        validate(request);
        simulate(request.getMerchantRefId());
    }

    public static void validateAndSimulate(ECheckDetokenizeRequest request) throws InterruptedException {
        validate(request);
        simulate(request.getMerchantRefId());
    }

    public static void validateAndSimulate(OrderRegistrationRequest request) throws InterruptedException {
        validate(request);
        simulate(request.getMerchantRefId());
    }

    public static void validateAndSimulate(OrderDeregistrationRequest request) throws InterruptedException {
        validate(request);
        simulate(request.getMerchantRefId());
    }


}
