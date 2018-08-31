package com.worldpay.simulator.validator;

import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.worldpay.simulator.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ws.soap.SoapHeaderElement;
import org.springframework.ws.soap.saaj.SaajSoapMessage;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.worldpay.simulator.exceptions.SecurityErrorException;

import static com.worldpay.simulator.validator.ValidatorUtils.*;

/**
 * Validator class:-
 * 1) validates the request based on the specifications on the document (not XSD)
 * 2) simulates error responses based on specific fields (merchant reference id or PAN)
 */

@Service
public class RequestValidator {

    public final int INVALID_REQ = 4;
    public final int TOKEN_INQUIRY_CARD_LIMIT = 200;
    public final int BATCH_TOKENIZE_CARD_LIMIT = 500;
    public final int EMPTY = 0;

    public final String INVALID_TOKEN = "Error: token is invalid or Not present";
    public final String INVALID_ROLLUP_ID = "RollupId not found in chain";
    public final String INVALID_SECURITY_CODE = "Error: CardSecurityCode is invalid";
    public final String INVALID_CARD_DETAILS = "Error: Card is invalid or Not present ";
    public final String INVALID_PAN = "Error: PAN (PrimaryAccountNumber) is invalid";
    public final String INVALID_REG_ID = "Invalid field data length";

    public final String INVALID_LANE_ID = "Error: LaneId is invalid";
    public final String INVALID_CHAIN_CODE = "Error: ChainCode is invalid";
    public final String INVALID_MERCHANT_ID = "Error: MerchantId is invalid";
    public final String INVALID_EXPIRY_DATE = "Error: Expiry Date is invalid (YYMM)";

    public final String INVALID_VERIFONE_CARD = "Error: Card details is invalid (Must either contain PAN and Expiry Date (or) any one of Track Numbers)";
    public final String INVALID_VOLTAGE_CARD = "Error: Card details is invalid (Must either contain PAN and Security Code (or) any one of Track Numbers)";
    public final String INVALID_ROUTING_NUM = "Error: Routing Number is invalid";
    public final String INVALID_ACCOUNT_NUM = "Invalid field data length";

    public final String MERCHANT_NOT_FOUND = "Error: Merchant not found";
    public final String MERCHANT_KEY_NOT_FOUND = "Error: MerchantKeyType not found";
    public final String CARD_NOT_FOUND = "Error: Card not found";
    public final String TOKEN_NOT_FOUND = "Error: Token not found";
    public final String TERMINAL_NOT_FOUND = "Error: Terminal not found";
    public final String CRYPTO_NOT_FOUND = "Error: Both VerifoneCryptogram and VoltageCryptogram not found";
    public final String ACCOUNT_NOT_FOUND = "Error: Account not found";
    private final String SECURITY_ERROR = "Rejected by policy. (from client)";

    public final String INVALID_ECHECK_TOKEN = "Invalid field data length";

    @Value("${validate.schema}")
    private boolean validateHeader;

    @Autowired
    JAXBService jaxbService;

    public JAXBContext getContext() throws JAXBException {
        return jaxbService.getContext();
    }

    public void validateSoapHeader(SoapHeaderElement header){
        if (!validateHeader) {
            return;
        }

        if(header == null) {
            throw new SecurityErrorException(SECURITY_ERROR);
        }


        try {
            JAXBContext context = getContext();
            Unmarshaller unmarshaller = context.createUnmarshaller();
            JAXBElement<SecurityHeaderType> root = unmarshaller.unmarshal(header.getSource(), SecurityHeaderType.class);

            List<Object> securityAttributeList = root.getValue().getAny();

            if(securityAttributeList.size() == 1) {
                Element userNameTokenAttribute = (Element)securityAttributeList.get(0);

                if(userNameTokenAttribute.getFirstChild() == null) {
                    throw new SecurityErrorException(SECURITY_ERROR);
                }

                Node userNameNode = userNameTokenAttribute.getFirstChild();

                if(!userNameNode.getLocalName().equals("Username")) {
                    throw new SecurityErrorException(SECURITY_ERROR);
                }

                if(userNameNode.getNextSibling() == null || userNameNode.getNextSibling().getLocalName() == null || !userNameNode.getNextSibling().getLocalName().equals("Password")) {
                    throw new SecurityErrorException(SECURITY_ERROR);
                }
            } else {
                throw new SecurityErrorException(SECURITY_ERROR);
            }

        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    public void validateCard(List<Card> cards, int LIMIT){

        if(cards.size() > LIMIT || cards.size() == EMPTY)
            handleException(INVALID_REQ,INVALID_CARD_DETAILS);

        for(Card card : cards)
            validateCard(card);
    }

    public void validateCard(Card card){
        if(!isValidPAN(card.getPrimaryAccountNumber()))
            handleException(INVALID_REQ,INVALID_PAN);
    }


    public void validateVerifoneCard(Card card){
        if(isStringEmpty(card.getTrack1()) && isStringEmpty(card.getTrack2()) && !isValidPAN(card.getPrimaryAccountNumber()) && !isValidExpiryDate(card.getExpirationDate()))
            handleException(INVALID_REQ,INVALID_VERIFONE_CARD);

        if(!isStringEmpty(card.getTrack1())) {
            if (!isStringEmpty(card.getPrimaryAccountNumber()) || !isStringEmpty(card.getExpirationDate()) || !isStringEmpty(card.getTrack2()))
                handleException(INVALID_REQ, INVALID_VERIFONE_CARD);
            return;
        }

        if(!isStringEmpty(card.getTrack2())) {
            if (!isStringEmpty(card.getPrimaryAccountNumber()) || !isStringEmpty(card.getExpirationDate()))
                handleException(INVALID_REQ, INVALID_VERIFONE_CARD);
            return;
        }


        if(!isValidPAN(card.getPrimaryAccountNumber()))
            handleException(INVALID_REQ,INVALID_PAN);

        if(!isValidExpiryDate(card.getExpirationDate()))
            handleException(INVALID_REQ,INVALID_EXPIRY_DATE);

    }

    public void validateVoltageCard(Card card){

        if(isStringEmpty(card.getTrack1()) && isStringEmpty(card.getTrack2()) && !isValidPAN(card.getPrimaryAccountNumber()) && !isValidExpiryDate(card.getSecurityCode()))
            handleException(INVALID_REQ,INVALID_VOLTAGE_CARD);

        if(!isStringEmpty(card.getTrack1())) {
            if (!isStringEmpty(card.getPrimaryAccountNumber()) || !isStringEmpty(card.getSecurityCode()) || !isStringEmpty(card.getTrack2()))
                handleException(INVALID_REQ, INVALID_VOLTAGE_CARD);
            return;
        }

        if(!isStringEmpty(card.getTrack2())) {
            if (!isStringEmpty(card.getPrimaryAccountNumber()) || !isStringEmpty(card.getSecurityCode()))
                handleException(INVALID_REQ, INVALID_VOLTAGE_CARD);
            return;
        }

        if(!isValidPAN(card.getPrimaryAccountNumber()))
            handleException(INVALID_REQ,INVALID_PAN);

        if(!isValidCVV(card.getSecurityCode()))
            handleException(INVALID_REQ,INVALID_SECURITY_CODE);

    }


    public void validateToken(List<Token> tokens, int LIMIT){

        if(tokens.size() > LIMIT || tokens.size() == EMPTY)
            handleException(INVALID_REQ,INVALID_TOKEN);

        for(Token token: tokens)
            validateToken(token);
    }

    public void validateToken(Token token){
        if(!isValidToken(token.getTokenValue()))
            handleException(INVALID_REQ,INVALID_TOKEN);
    }

    public void validateToken(ECheckToken token){

        if(token == null)
            handleException(INVALID_REQ,TOKEN_NOT_FOUND);

        if(!isValidToken(token.getTokenValue()))
            handleException(INVALID_REQ,INVALID_ECHECK_TOKEN);

    }

    public void validateMerchant(MerchantType merchant){
        if(merchant == null)
            handleException(INVALID_REQ, MERCHANT_NOT_FOUND);

        if(!isValidRollupId(merchant.getRollupId()))
            handleException(INVALID_REQ, INVALID_ROLLUP_ID);
    }

    public void validateMerchantKeyType(VerifoneMerchantKeyType merchantKeyType){
        if(merchantKeyType == null)
            handleException(INVALID_REQ,MERCHANT_KEY_NOT_FOUND);
    }

    public void validateVerifoneTerminal(VerifoneTerminal terminal){

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

    public void validateVerifoneCryptogram(VerifoneCryptogram verifone){

        if(verifone.getEncryptedCard() == null)
            handleException(INVALID_REQ, CARD_NOT_FOUND);

        validateVerifoneCard(verifone.getEncryptedCard());

        validateMerchantKeyType(verifone.getMerchantKeyType());

        validateVerifoneTerminal(verifone.getTerminal());

    }

    public void validateAccount(Account account){
        if(account == null)
            handleException(INVALID_REQ, ACCOUNT_NOT_FOUND);

        if(!isValidAccount(account.getAccountNumber()))
            handleException(INVALID_REQ, INVALID_ACCOUNT_NUM);

        if(!isValidRoutingNumber(account.getRoutingNumber()))
            handleException(INVALID_REQ, INVALID_ROUTING_NUM);

    }

    public void validateVoltageCryptogram(VoltageCryptogram voltage){
        if(voltage.getEncryptedCard() == null)
            handleException(INVALID_REQ, CARD_NOT_FOUND);

        validateVoltageCard(voltage.getEncryptedCard());
    }

    public void validateCryptogram(VerifoneCryptogram verifone, VoltageCryptogram voltage){
        if(verifone == null && voltage == null)
            handleException(INVALID_REQ, CRYPTO_NOT_FOUND);

        if(voltage == null)
            validateVerifoneCryptogram(verifone);

        if(verifone == null)
            validateVoltageCryptogram(voltage);

    }

    public void validateTokenizeRequest(TokenizeRequest request){

        //mantory field check that is not mentioned in the xsd but in the document
        validateMerchant(request.getMerchant());

        if(!isValidPAN(request.getPrimaryAccountNumber()))
            handleException(INVALID_REQ,INVALID_PAN);

        if(!isStringEmpty(request.getCardSecurityCode()))
            if(!isValidCVV(request.getCardSecurityCode()))
                handleException(INVALID_REQ,INVALID_SECURITY_CODE);


    }

    public void validateDetokenizeRequest(DetokenizeRequest request){

        //mantory field check that is not mentioned in the xsd but in the document
        validateMerchant(request.getMerchant());

        if(!isValidToken(request.getToken()))
            handleException(INVALID_REQ,INVALID_TOKEN);

    }


    public void validateBatchTokenizeRequest(BatchTokenizeRequest request){

        //mantory field check that is not mentioned in the xsd but in the document
        validateMerchant(request.getMerchant());

        validateCard(request.getCard(),BATCH_TOKENIZE_CARD_LIMIT);

    }


    public void validateBatchDetokenizeRequest(BatchDetokenizeRequest request){

        //mantory field check that is not mentioned in the xsd but in the document
        validateMerchant(request.getMerchant());

        validateToken(request.getToken(),BATCH_TOKENIZE_CARD_LIMIT);

    }

    public void validateTokenInquiryRequest(TokenInquiryRequest request){

        //mandatory field check that is not mentioned in the xsd but in document
        validateMerchant(request.getMerchant());

        validateCard(request.getCard(),TOKEN_INQUIRY_CARD_LIMIT);
    }

    public void validateRegistrationRequest(RegistrationRequest request){

        //mandatory field check that is not mentioned in the xsd but in document
        validateMerchant(request.getMerchant());

        if(!isValidPAN(request.getPrimaryAccountNumber()))
            handleException(INVALID_REQ,INVALID_PAN);
    }

    public void validateDeregistrationRequest(DeregistrationRequest request){

        //mandatory field check that is not mentioned in the xsd but in document
        validateMerchant(request.getMerchant());

        if(!isValidRegId(request.getRegId()))
            handleException(INVALID_REQ,INVALID_REG_ID);
    }

    public void validateDecryptRequest(DecryptRequest request){

        //mandatory field check that is not mentioned in the xsd but in document
        validateMerchant(request.getMerchant());

        validateCryptogram(request.getVerifoneCryptogram(),request.getVoltageCryptogram());
    }

    public void validateTokenRegistrationRequest(TokenRegistrationRequest request){

        //mandatory field check that is not mentioned in the xsd but in document
        validateMerchant(request.getMerchant());

        if(!isValidToken(request.getToken()))
            handleException(INVALID_REQ,INVALID_TOKEN);
    }

    public void validateECheckTokenizeRequest(ECheckTokenizeRequest request){

        //mandatory field check that is not mentioned in the xsd but in document
        validateMerchant(request.getMerchant());

        validateAccount(request.getAccount());
    }

    public void validateECheckDetokenizeRequest(ECheckDetokenizeRequest request){

        //mandatory field check that is not mentioned in the xsd but in document
        validateMerchant(request.getMerchant());

        validateToken(request.getToken());
    }

    public void validateOrderRegistrationRequest(OrderRegistrationRequest request){

        //mandatory field check that is not mentioned in the xsd but in document
        validateMerchant(request.getMerchant());

        if(!isValidCVV(request.getCardSecurityCode()) )
            handleException(INVALID_REQ,INVALID_SECURITY_CODE);
    }

    public void validateOrderDeregistrationRequest(OrderDeregistrationRequest request){

        //mandatory field check that is not mentioned in the xsd but in document
        validateMerchant(request.getMerchant());

        if(!isValidOrderLVT(request.getOrderLVT()) )
            handleException(INVALID_REQ,INVALID_SECURITY_CODE);

        if(isValidToken(request.getToken()))
            handleException(INVALID_REQ,INVALID_TOKEN);
    }

}
