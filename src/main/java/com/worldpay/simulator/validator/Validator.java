package com.worldpay.simulator.validator;

import com.sun.org.apache.xerces.internal.dom.ElementNSImpl;
import com.worldpay.simulator.*;
import com.worldpay.simulator.exceptions.SecurityErrorException;

import org.springframework.ws.soap.SoapHeaderElement;
import org.w3c.dom.Node;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.util.List;

import static com.worldpay.simulator.validator.ValidatorUtils.*;

/**
 * Validator class:-
 * 1) validates the request based on the specifications on the document (not XSD)
 * 2) simulates error responses based on specific fields (merchant reference id or PAN)
 */
public class Validator {

    public static final int INVALID_REQ = 4;
    public static final int TOKEN_INQUIRY_CARD_LIMIT = 200;
    public static final int BATCH_TOKENIZE_CARD_LIMIT = 500;
    public static final int EMPTY = 0;

    public static final String INVALID_TOKEN = "Error: token is invalid or Not present";
    public static final String INVALID_ROLLUP_ID = "Error: RollUpId is invalid";
    public static final String INVALID_SECURITY_CODE = "Error: CardSecurityCode is invalid";
    public static final String INVALID_CARD_DETAILS = "Error: Card is invalid or Not present ";
    public static final String INVALID_PAN = "Error: PAN (PermanentAccountNumber) is invalid";
    public static final String INVALID_REG_ID = "Error: RegId is invalid";
    public static final String INVALID_MERCHANT_KEY_TYPE = "Error: MerchantKeyType is invalid";
    public static final String INVALID_LANE_ID = "Error: LaneId is invalid";
    public static final String INVALID_CHAIN_CODE = "Error: ChainCode is invalid";
    public static final String INVALID_MERCHANT_ID = "Error: MerchantId is invalid";
    public static final String INVALID_EXPIRY_DATE = "Error: Expiry Date is invalid (YYMM)";

    public static final String INVALID_VERIFONE_CARD = "Error: Card details is invalid (Must either contain PAN and Expiry Date (or) any one of Track Numbers)";
    public static final String INVALID_VOLTAGE_CARD = "Error: Card details is invalid (Must either contain PAN and Security Code (or) any one of Track Numbers)";
    public static final String INVALID_ROUTING_NUM = "Error: Routing Number is invalid";
    public static final String INVALID_ACCOUNT_NUM = "Error: Account Number is invalid";

    public static final String MERCHANT_NOT_FOUND = "Error: Merchant not found";
    public static final String MERCHANT_KEY_NOT_FOUND = "Error: MerchantKeyType not found";
    public static final String CARD_NOT_FOUND = "Error: Card not found";
    public static final String TOKEN_NOT_FOUND = "Error: Token not found";
    public static final String TERMINAL_NOT_FOUND = "Error: Terminal not found";
    public static final String CRYPTO_NOT_FOUND = "Error: Both VerifoneCryptogram and VoltageCryptogram not found";
    public static final String ACCOUNT_NOT_FOUND = "Error: Account not found";

    public static void validateSoapHeader(SoapHeaderElement header){

        if(header == null) {
            throw new SecurityErrorException("TID:20531165.Rejected by policy.");
        }

        try {
            JAXBContext context = JAXBContext.newInstance(SecurityHeaderType.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            JAXBElement<SecurityHeaderType> root = unmarshaller.unmarshal(header.getSource(), SecurityHeaderType.class);

            List<Object> securityAttributeList = root.getValue().getAny();

            if(securityAttributeList.size() == 1) {
                ElementNSImpl userNameTokenAttribute = (ElementNSImpl)securityAttributeList.get(0);

                if(userNameTokenAttribute.getFirstChild() == null) {
                    throw new SecurityErrorException("TID:20531165.Rejected by policy.");
                }

                Node userNameNode = userNameTokenAttribute.getFirstChild();

                if(!userNameNode.getLocalName().equals("Username")) {
                    throw new SecurityErrorException("TID:20531165.Rejected by policy.");
                }

                if(userNameNode.getNextSibling() == null || !userNameNode.getNextSibling().getLocalName().equals("Password")) {
                    throw new SecurityErrorException("TID:20531165.Rejected by policy.");
                }
            } else {
                throw new SecurityErrorException("TID:20531165.Rejected by policy.");
            }

        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    public static void validateCard(List<Card> cards, int LIMIT){

        if(cards.size() > LIMIT || cards.size() == EMPTY)
            handleException(INVALID_REQ,INVALID_CARD_DETAILS);

        for(Card card : cards)
            validateCard(card);
    }

    public static void validateCard(Card card){
        if(!isValidPAN(card.getPrimaryAccountNumber()))
            handleException(INVALID_REQ,INVALID_PAN);
    }

    public static void validateVerifoneCard(Card card){

        if(isStringEmpty(card.getTrack1()) && isStringEmpty(card.getTrack2()) && !isValidPAN(card.getPrimaryAccountNumber()) && !isValidExpiryDate(card.getExpirationDate()))
            handleException(INVALID_REQ,INVALID_VERIFONE_CARD);

        if(!isStringEmpty(card.getTrack1()))
            if(!isStringEmpty(card.getPrimaryAccountNumber()) || !isStringEmpty(card.getExpirationDate()) || !isStringEmpty(card.getTrack2()))
                handleException(INVALID_REQ,INVALID_VERIFONE_CARD);

        if(!isStringEmpty(card.getTrack2()))
            if(!isStringEmpty(card.getPrimaryAccountNumber()) || !isValidExpiryDate(card.getExpirationDate()) || !isStringEmpty(card.getTrack1()))
                handleException(INVALID_REQ,INVALID_VERIFONE_CARD);

        if(isValidPAN(card.getPrimaryAccountNumber()) && isValidExpiryDate(card.getExpirationDate()))
            if( !isStringEmpty(card.getTrack2()) || !isStringEmpty(card.getTrack1()))
                handleException(INVALID_REQ,INVALID_VERIFONE_CARD);

        if(!isValidPAN(card.getPrimaryAccountNumber()))
            handleException(INVALID_REQ,INVALID_PAN);

        if(!isValidExpiryDate(card.getExpirationDate()))
            handleException(INVALID_REQ,INVALID_EXPIRY_DATE);

    }

    public static void validateVoltageCard(Card card){

        if(isStringEmpty(card.getTrack1()) && isStringEmpty(card.getTrack2()) && !isValidPAN(card.getPrimaryAccountNumber()) && !isValidExpiryDate(card.getSecurityCode()))
            handleException(INVALID_REQ,INVALID_VOLTAGE_CARD);

        if(!isStringEmpty(card.getTrack1()))
            if(!isStringEmpty(card.getPrimaryAccountNumber()) || !isValidCVV(card.getSecurityCode()) || !isStringEmpty(card.getTrack2()))
                handleException(INVALID_REQ,INVALID_VOLTAGE_CARD);

        if(!isStringEmpty(card.getTrack2()))
            if(!isStringEmpty(card.getPrimaryAccountNumber()) || !isValidCVV(card.getSecurityCode()) || !isStringEmpty(card.getTrack1()))
                handleException(INVALID_REQ,INVALID_VOLTAGE_CARD);

        if(isValidPAN(card.getPrimaryAccountNumber()) && isValidCVV(card.getSecurityCode()))
            if( !isStringEmpty(card.getTrack2()) || !isStringEmpty(card.getTrack1()))
                handleException(INVALID_REQ,INVALID_VOLTAGE_CARD);

        if(!isValidPAN(card.getPrimaryAccountNumber()))
            handleException(INVALID_REQ,INVALID_PAN);

        if(!isValidCVV(card.getSecurityCode()))
            handleException(INVALID_REQ,INVALID_SECURITY_CODE);

    }

    public static void validateToken(List<Token> tokens, int LIMIT){

        if(tokens.size() > LIMIT || tokens.size() == EMPTY)
            handleException(INVALID_REQ,INVALID_TOKEN);

        for(Token token: tokens)
            validateToken(token);
    }

    public static void validateToken(Token token){
        if(!isValidToken(token.getTokenValue()))
            handleException(INVALID_REQ,INVALID_TOKEN);
    }

    public static void validateToken(ECheckToken token){

        if(token == null)
            handleException(INVALID_REQ,TOKEN_NOT_FOUND);

        if(!isValidToken(token.getTokenValue()))
            handleException(INVALID_REQ,INVALID_TOKEN);

    }

    public static void validateMerchant(MerchantType merchant){
        if(merchant == null)
            handleException(INVALID_REQ, MERCHANT_NOT_FOUND);

        if(!isValidRollupId(merchant.getRollupId()))
            handleException(INVALID_REQ, INVALID_ROLLUP_ID);
    }

    public static void validateMerchantKeyType(VerifoneMerchantKeyType merchantKeyType){
        if(merchantKeyType == null)
            handleException(INVALID_REQ,MERCHANT_KEY_NOT_FOUND);

        if(!merchantKeyType.value().equalsIgnoreCase("SHARED")
                || !merchantKeyType.value().equalsIgnoreCase("UNIQUE"))
            handleException(INVALID_REQ,INVALID_MERCHANT_KEY_TYPE);

    }

    public static void validateVerifoneTerminal(VerifoneTerminal terminal){

        if(terminal == null)
            handleException(INVALID_REQ,TERMINAL_NOT_FOUND);

        if(isStringEmpty(terminal.getRegisterId()))
            handleException(INVALID_REQ, INVALID_REG_ID);

        if(isStringEmpty(terminal.getLaneId()))
            handleException(INVALID_REQ, INVALID_LANE_ID);

        if(isStringEmpty(terminal.getChainCode()))
            handleException(INVALID_REQ, INVALID_CHAIN_CODE);

        if(isStringEmpty(terminal.getMerchantId()))
            handleException(INVALID_REQ, INVALID_MERCHANT_ID);
    }

    public static void validateVerifoneCryptogram(VerifoneCryptogram verifone){

        if(verifone.getEncryptedCard() == null)
            handleException(INVALID_REQ, CARD_NOT_FOUND);

        validateVerifoneCard(verifone.getEncryptedCard());

        validateMerchantKeyType(verifone.getMerchantKeyType());

        validateVerifoneTerminal(verifone.getTerminal());

    }

    public static void validateAccount(Account account){
        if(account == null)
            handleException(INVALID_REQ, ACCOUNT_NOT_FOUND);

        if(!isValidAccount(account.getAccountNumber()))
            handleException(INVALID_REQ, INVALID_ACCOUNT_NUM);

        if(!isValidRoutingNumber(account.getRoutingNumber()))
            handleException(INVALID_REQ, INVALID_ROUTING_NUM);

    }

    public static void validateVoltageCryptogram(VoltageCryptogram voltage){
        if(voltage.getEncryptedCard() == null)
            handleException(INVALID_REQ, CARD_NOT_FOUND);

        validateVoltageCard(voltage.getEncryptedCard());
    }

    public static void validateCryptogram(VerifoneCryptogram verifone, VoltageCryptogram voltage){
        if(verifone == null && voltage == null)
            handleException(INVALID_REQ, CRYPTO_NOT_FOUND);

        if(voltage == null)
            validateVerifoneCryptogram(verifone);

        if(verifone == null)
            validateVoltageCryptogram(voltage);

    }

    public static void validate(TokenizeRequest request){

        //mantory field check that is not mentioned in the xsd but in the document
        validateMerchant(request.getMerchant());

        if(!isValidPAN(request.getPrimaryAccountNumber()))
            handleException(INVALID_REQ,INVALID_PAN);

        if(!isStringEmpty(request.getCardSecurityCode()))
            if(!isValidCVV(request.getCardSecurityCode()))
                handleException(INVALID_REQ,INVALID_SECURITY_CODE);


    }

    public static void validate(DetokenizeRequest request){

        //mantory field check that is not mentioned in the xsd but in the document
        validateMerchant(request.getMerchant());

        if(!isValidToken(request.getToken()))
            handleException(INVALID_REQ,INVALID_TOKEN);

    }


    public static void validate(BatchTokenizeRequest request){

        //mantory field check that is not mentioned in the xsd but in the document
        validateMerchant(request.getMerchant());

        validateCard(request.getCard(),BATCH_TOKENIZE_CARD_LIMIT);

    }


    public static void validate(BatchDetokenizeRequest request){

        //mantory field check that is not mentioned in the xsd but in the document
        validateMerchant(request.getMerchant());

        validateToken(request.getToken(),BATCH_TOKENIZE_CARD_LIMIT);

    }

    public static void validate(TokenInquiryRequest request){

        //mandatory field check that is not mentioned in the xsd but in document
        validateMerchant(request.getMerchant());

        validateCard(request.getCard(),TOKEN_INQUIRY_CARD_LIMIT);
    }

    public static void validate(RegistrationRequest request){

        //mandatory field check that is not mentioned in the xsd but in document
        validateMerchant(request.getMerchant());
    }

    public static void validate(DeregistrationRequest request){

        //mandatory field check that is not mentioned in the xsd but in document
        validateMerchant(request.getMerchant());

        if(!isValidRegId(request.getRegId()))
            handleException(INVALID_REQ,INVALID_REG_ID);
    }

    public static void validate(DecryptRequest request){

        //mandatory field check that is not mentioned in the xsd but in document
        validateMerchant(request.getMerchant());

        validateCryptogram(request.getVerifoneCryptogram(),request.getVoltageCryptogram());
    }

    public static void validate(TokenRegistrationRequest request){

        //mandatory field check that is not mentioned in the xsd but in document
        validateMerchant(request.getMerchant());

        if(!isValidToken(request.getToken()))
            handleException(INVALID_REQ,INVALID_TOKEN);
    }

    public static void validate(ECheckTokenizeRequest request){

        //mandatory field check that is not mentioned in the xsd but in document
        validateMerchant(request.getMerchant());

        validateAccount(request.getAccount());
    }

    public static void validate(ECheckDetokenizeRequest request){

        //mandatory field check that is not mentioned in the xsd but in document
        validateMerchant(request.getMerchant());

        validateToken(request.getToken());
    }

    public static void validate(OrderRegistrationRequest request){

        //mandatory field check that is not mentioned in the xsd but in document
        validateMerchant(request.getMerchant());

        if(!isValidCVV(request.getCardSecurityCode()) )
            handleException(INVALID_REQ,INVALID_SECURITY_CODE);
    }

    public static void validate(OrderDeregistrationRequest request){

        //mandatory field check that is not mentioned in the xsd but in document
        validateMerchant(request.getMerchant());

        if(!isValidOrderLVT(request.getOrderLVT()) )
            handleException(INVALID_REQ,INVALID_SECURITY_CODE);
    }

}
