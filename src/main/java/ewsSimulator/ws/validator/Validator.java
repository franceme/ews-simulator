package ewsSimulator.ws.validator;

import ewsSimulator.ws.*;

import java.util.List;

import static ewsSimulator.ws.validator.ValidatorUtils.*;

/**
 * Validator class:-
 * 1) validates the request based on the specifications on the document (not XSD)
 * 2) simulates error responses based on specific fields (merchant reference id or PAN)
 */
public class Validator {

    public static final int INVALID_REQ = 4;
    public static final int TOKEN_INQUIRY_CARD_LIMIT = 200;
    public static final int BATCH_TOKENIZE_CARD_LIMIT = 500;

    public static final String INVALID_TOKEN = "Error: token is invalid or Not present";
    public static final String INVALID_ROLLUP_ID = "Error: RollUpId is invalid";
    public static final String INVALID_SECURITY_CODE = "Error: CardSecurityCode is invalid";
    public static final String INVALID_CARD_DETAILS = "Error: Card is invalid or Not present ";
    public static final String INVALID_PAN = "Error: PAN (PermanentAccountNumber) is invalid";

    public static final String MERCHANT_NOT_FOUND = "Error: Merchant not found";
    public static final String TOKEN_NOT_FOUND = "Error: Token not found";
    public static final String CARD_NOT_FOUND = "Error: Card not found";



    public static boolean validateMerchantRefId(String merchantRefId){
//        if(merchantRefId == null || merchantRefId.length() > 16 || isStringEmpty(merchantRefId))
//            //EWSUtils.throwDesiredException(INVALID_REQ,INVALID_MERCHANT_REF_ID);
//            return false;
        return true;
    }

    public static void validateCard(List<Card> cards){
//        for(Card card : cards)
//            validateCard(card);
    }

    public static void validateCard(Card card){
//        if(!isValidPAN(card.getPrimaryAccountNumber()))
//            EWSUtils.throwDesiredException(INVALID_REQ,INVALID_PAN);
    }

    public static void validateToken(List<Token> tokens){
//        for(Token token: tokens)
//            validateToken(token);
    }

    public static void validateToken(Token token){
//        if(!isValidToken(token.getTokenValue()))
//            EWSUtils.throwDesiredException(INVALID_REQ,INVALID_TOKEN);
    }

    public static void validate(TokenizeRequest request){

        //mantory field check that is not mentioned in the xsd but in the document
//        if(request.getMerchant() == null)
//            EWSUtils.throwDesiredException(INVALID_REQ, MERCHANT_NOT_FOUND);
//
//        if(!isValidRollupId(request.getMerchant().getRollupId()))
//            EWSUtils.throwDesiredException(INVALID_REQ, INVALID_ROLLUP_ID);
//
//        if(!isValidPAN(request.getPrimaryAccountNumber()))
//            EWSUtils.throwDesiredException(INVALID_REQ,INVALID_PAN);
//
//        if(!isStringEmpty(request.getCardSecurityCode()))
//            if(isValidCVV(request.getCardSecurityCode()))
//                EWSUtils.throwDesiredException(INVALID_REQ,INVALID_SECURITY_CODE);
//
//        //validator to simulate based on the last few digits of the merchant-ref-id
//        if(validateMerchantRefId(request.getMerchantRefId()))
//            EWSUtils.handleDesiredExceptions(request.getMerchantRefId());

    }

    public static void validate(DetokenizeRequest request){

//        //mantory field check that is not mentioned in the xsd but in the document
//        if(request.getMerchant() == null)
//            EWSUtils.throwDesiredException(INVALID_REQ, MERCHANT_NOT_FOUND);
//
//        if(!isValidRollupId(request.getMerchant().getRollupId()))
//            EWSUtils.throwDesiredException(INVALID_REQ, INVALID_ROLLUP_ID);
//
//        if(isValidToken(request.getToken()))
//            EWSUtils.throwDesiredException(INVALID_REQ,INVALID_TOKEN);
//
//        //validator to simulate based on the last few digits of the merchant-ref-id
//        if(validateMerchantRefId(request.getMerchantRefId()))
//            EWSUtils.handleDesiredExceptions(request.getMerchantRefId());

    }


    public static void validate(BatchTokenizeRequest request){

//        //mantory field check that is not mentioned in the xsd but in the document
//        if(request.getMerchant() == null)
//            EWSUtils.throwDesiredException(INVALID_REQ, MERCHANT_NOT_FOUND);
//
//        if(!isValidRollupId(request.getMerchant().getRollupId()))
//            EWSUtils.throwDesiredException(INVALID_REQ, INVALID_ROLLUP_ID);
//
//        if(request.getCard() == null || request.getCard().size() > 500 || request.getCard().size() == 0)
//            EWSUtils.throwDesiredException(INVALID_REQ,INVALID_CARD_DETAILS);
//
//        validateCard(request.getCard());
//
//        //validator to simulate based on the last few digits of the merchant-ref-id
//        if(validateMerchantRefId(request.getMerchantRefId()))
//        EWSUtils.handleDesiredExceptions(request.getMerchantRefId());

    }


    public static void validate(BatchDetokenizeRequest request){

//        //mantory field check that is not mentioned in the xsd but in the document
//        if(request.getMerchant() == null)
//            EWSUtils.throwDesiredException(INVALID_REQ, MERCHANT_NOT_FOUND);
//
//        if(!isValidRollupId(request.getMerchant().getRollupId()))
//            EWSUtils.throwDesiredException(INVALID_REQ, INVALID_ROLLUP_ID);
//
//        if(request.getToken() == null || request.getToken().size() > BATCH_TOKENIZE_CARD_LIMIT || request.getToken().size() == 0)
//            EWSUtils.throwDesiredException(INVALID_REQ,INVALID_TOKEN);
//
//        validateToken(request.getToken());
//
//        //validator to simulate based on the last few digits of the merchant-ref-id
//        if(validateMerchantRefId(request.getMerchantRefId()))
//            EWSUtils.handleDesiredExceptions(request.getMerchantRefId());

    }

    public static void validate(TokenInquiryRequest request){

//        //mandatory field check that is not mentioned in the xsd but in document
//        if(request.getMerchant() == null)
//            EWSUtils.throwDesiredException(INVALID_REQ, MERCHANT_NOT_FOUND);
//
//        if(!isValidRollupId(request.getMerchant().getRollupId()))
//            EWSUtils.throwDesiredException(INVALID_REQ,INVALID_ROLLUP_ID);
//
//        if(request.getCard() == null || request.getCard().size() > TOKEN_INQUIRY_CARD_LIMIT || request.getCard().size() == 0)
//            EWSUtils.throwDesiredException(INVALID_REQ,INVALID_CARD_DETAILS);
//
//        validateCard(request.getCard());
//
//        //exceptions to simulate based on the last few digits of merchant-ref-id or PAN
//        if(validateMerchantRefId(request.getMerchantRefId()))
//            EWSUtils.handleDesiredExceptions(request.getMerchantRefId());
    }

    public static void validate(OrderRegistrationRequest request){

//        //mandatory field check that is not mentioned in the xsd but in document
//        if(request.getMerchant() == null)
//            EWSUtils.throwDesiredException(INVALID_REQ, MERCHANT_NOT_FOUND);
//
//        if(!isValidRollupId(request.getMerchant().getRollupId()))
//            EWSUtils.throwDesiredException(INVALID_REQ,INVALID_ROLLUP_ID);
//
//        if(!isValidCVV(request.getCardSecurityCode()) )
//            EWSUtils.throwDesiredException(INVALID_REQ,INVALID_SECURITY_CODE);
//
//        //exceptions to simulate based on the last few digits of merchant-ref-id or PAN
//        if(validateMerchantRefId(request.getMerchantRefId()))
//            EWSUtils.handleDesiredExceptions(request.getMerchantRefId());
    }







}
