package com.worldpay.simulator.validator;

import static org.mockito.ArgumentMatchers.any;
import static org.powermock.api.mockito.PowerMockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.ws.soap.SoapHeaderElement;

import com.sun.org.apache.xerces.internal.dom.ElementNSImpl;
import com.worldpay.simulator.Account;
import com.worldpay.simulator.Card;
import com.worldpay.simulator.ECheckToken;
import com.worldpay.simulator.MerchantType;
import com.worldpay.simulator.SecurityHeaderType;
import com.worldpay.simulator.Token;
import com.worldpay.simulator.VerifoneCryptogram;
import com.worldpay.simulator.VerifoneMerchantKeyType;
import com.worldpay.simulator.VerifoneTerminal;
import com.worldpay.simulator.VoltageCryptogram;
import com.worldpay.simulator.exceptions.ClientFaultException;
import com.worldpay.simulator.utils.EWSUtils;

import jdk.nashorn.internal.runtime.arrays.ContinuousArrayData;


@RunWith(PowerMockRunner.class)
@PrepareForTest({ValidatorUtils.class})
public class TestValidator {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    private Card card1;
    private Card card2;
    private Token token1;
    private Token token2;
    private MerchantType merchant;
    private VerifoneTerminal terminal;
    private VerifoneCryptogram verifone;
    private Account account;
    private VoltageCryptogram voltage;
    private int INVALID_REQ;
    private String INVALID_CARD_DETAILS;
    private String PAN1;
    private String PAN2;
    private String expDate;
    public static String INVALID_TOKEN;
    public static String TOKEN_NOT_FOUND;


    @Before
    public void setUp(){

        mockStatic(ValidatorUtils.class);

        card1 = new Card();
        card2 = new Card();
        token1 = new Token();
        token2 = new Token();
        merchant = new MerchantType();
        terminal = new VerifoneTerminal();
        verifone = new VerifoneCryptogram();
        account = new Account();
        voltage = new VoltageCryptogram();
        Validator temp = new Validator();
        INVALID_REQ = 4;
        INVALID_CARD_DETAILS = "Error: Card is invalid or Not present ";
        PAN1 = "1234567891011123";
        PAN2= "1234567891011000";
        expDate = "2308";
        INVALID_TOKEN = "Error: Token not found";
        TOKEN_NOT_FOUND = "Error: Token not found";

    }

    @Test
    public void testValidateCards_1() throws Exception {

        //case 1 : when size = 0 || card size > limit
        List<Card> cards = new ArrayList<>();
        card1.setPrimaryAccountNumber(PAN1);
        cards.add(card1);

        card2.setPrimaryAccountNumber(PAN2);
        cards.add(card2);

        doNothing().when(ValidatorUtils.class,"handleException",INVALID_REQ,INVALID_CARD_DETAILS);

        Validator.validateCard(cards,1);

    }

    @Test
    public void testValidateCards_2() throws Exception {

        doNothing().when(ValidatorUtils.class,"handleException",INVALID_REQ,INVALID_CARD_DETAILS);
        Validator.validateCard(new ArrayList<Card>(),1);
    }

    @Test
    public void testValidateTokens() throws Exception {
        doNothing().when(ValidatorUtils.class,"handleException",INVALID_REQ,INVALID_CARD_DETAILS);
        List<Token> tokens = new ArrayList<>();
        tokens.add(token1);
        tokens.add(token2);
        Validator.validateToken(tokens,1);
        Validator.validateToken(tokens,2);
        Validator.validateCard(new ArrayList<Card>(),1);
    }

    @Test
    public void testValidateCard() throws Exception {
        doNothing().when(ValidatorUtils.class,"handleException",INVALID_REQ,INVALID_CARD_DETAILS);
        card1.setPrimaryAccountNumber("");
        Validator.validateCard(card1);
        card1.setPrimaryAccountNumber("1234567891011123");
        Validator.validateCard(card1);
    }

    @Test
    public void testValidateToken() throws Exception {
        doNothing().when(ValidatorUtils.class,"handleException",INVALID_REQ,INVALID_CARD_DETAILS);
        token1.setTokenValue("");
        Validator.validateToken(token1);
        token1.setTokenValue("12312312");
        Validator.validateToken(token1);
    }

    @Test
    public void testValidateVerifoneCard() throws Exception {
        doNothing().when(ValidatorUtils.class,"handleException",INVALID_REQ,INVALID_CARD_DETAILS);
        doReturn(true).when(ValidatorUtils.class,"isStringEmpty",null);
        doReturn(true).when(ValidatorUtils.class,"isStringEmpty","");
        doReturn(true).when(ValidatorUtils.class,"isValidExpiryDate",expDate);
        doReturn(true).when(ValidatorUtils.class,"isValidPAN",PAN1);

        card1.setTrack1("");
        card1.setTrack2("");
        card1.setPrimaryAccountNumber("");
        card1.setExpirationDate("");
        Validator.validateVerifoneCard(card1);

        card1.setTrack1("1");
        Validator.validateVerifoneCard(card1);
        card1.setTrack2("2");
        Validator.validateVerifoneCard(card1);
        card1.setExpirationDate(expDate);
        Validator.validateVerifoneCard(card1);
        card1.setPrimaryAccountNumber(PAN1);
        Validator.validateVerifoneCard(card1);

        card1.setTrack1("");
        Validator.validateVerifoneCard(card1);
        card1.setPrimaryAccountNumber("");
        Validator.validateVerifoneCard(card1);
        card1.setExpirationDate("");
        Validator.validateVerifoneCard(card1);

        card1.setTrack2("");
        card1.setPrimaryAccountNumber(PAN1);
        Validator.validateVerifoneCard(card1);
        card1.setExpirationDate(expDate);
        Validator.validateVerifoneCard(card1);
        card1.setTrack1("1");
        Validator.validateVerifoneCard(card1);
        card1.setTrack2("2");
        Validator.validateVerifoneCard(card1);

        card1.setTrack1("");
        card1.setTrack2("");
        card1.setPrimaryAccountNumber("");
        Validator.validateVerifoneCard(card1);
    }

    @Test
    public void testValidateVoltageCard() throws Exception {
        doNothing().when(ValidatorUtils.class,"handleException",INVALID_REQ,INVALID_CARD_DETAILS);
        doReturn(true).when(ValidatorUtils.class,"isStringEmpty",null);
        doReturn(true).when(ValidatorUtils.class,"isStringEmpty","");
        doReturn(true).when(ValidatorUtils.class,"isValidExpiryDate",expDate);
        doReturn(true).when(ValidatorUtils.class,"isValidPAN",PAN1);

        card1.setTrack1("");
        card1.setTrack2("");
        card1.setPrimaryAccountNumber("");
        card1.setExpirationDate("");
        Validator.validateVoltageCard(card1);

        card1.setTrack1("1");
        Validator.validateVoltageCard(card1);
        card1.setTrack2("2");
        Validator.validateVoltageCard(card1);
        card1.setSecurityCode(expDate);
        Validator.validateVoltageCard(card1);
        card1.setPrimaryAccountNumber(PAN1);
        Validator.validateVoltageCard(card1);

        card1.setTrack1("");
        Validator.validateVoltageCard(card1);
        card1.setPrimaryAccountNumber("");
        Validator.validateVoltageCard(card1);
        card1.setSecurityCode("");
        Validator.validateVoltageCard(card1);

        card1.setTrack2("");
        card1.setPrimaryAccountNumber(PAN1);
        Validator.validateVoltageCard(card1);
        card1.setSecurityCode(expDate);
        Validator.validateVoltageCard(card1);
        card1.setTrack1("1");
        Validator.validateVoltageCard(card1);
        card1.setTrack2("2");
        Validator.validateVoltageCard(card1);

        card1.setTrack1("");
        card1.setTrack2("");
        card1.setPrimaryAccountNumber("");
        Validator.validateVoltageCard(card1);
    }

//    @Test
//    public void testValidateECheckToken_null() throws Exception {
//        doNothing().when(ValidatorUtils.class,"handleException",INVALID_REQ,TOKEN_NOT_FOUND);
//        Validator.validateToken((ECheckToken) null);
//        ECheckToken tempToken = new ECheckToken();
//        tempToken.setTokenValue("");
//        Validator.validateToken((ECheckToken)tempToken);
//        tempToken.setTokenValue("123");
//        Validator.validateToken((ECheckToken)tempToken);
//    }
//
//
//
//    @Test
//    public void testValidateMerchant() throws Exception {
//        doNothing().when(ValidatorUtils.class,"handleException",INVALID_REQ,INVALID_CARD_DETAILS);
//        Validator.validateMerchant((MerchantType)null);
//        merchant.setRollupId("");
//        Validator.validateMerchant(merchant);
//        merchant.setRollupId("1123");
//        Validator.validateMerchant(merchant);
//    }
//
//    @Test(expected = Exception.class)
//    public void testValidateMerchantKeyType(){
//        Validator.validateMerchantKeyType(null);
//        Validator.validateMerchantKeyType(VerifoneMerchantKeyType.SHARED);
//    }
//
//    @Test(expected = Exception.class)
//    public void testValidateVerifoneTerminal(){
//        Validator.validateVerifoneTerminal(null);
//        Validator.validateVerifoneTerminal(terminal);
//        terminal.setRegisterId("123");
//        Validator.validateVerifoneTerminal(terminal);
//        terminal.setLaneId("1");
//        Validator.validateVerifoneTerminal(terminal);
//        terminal.setChainCode("123");
//        Validator.validateVerifoneTerminal(terminal);
//        terminal.setMerchantId("123");
//        Validator.validateVerifoneTerminal(terminal);
//    }
//
//    @Test(expected = Exception.class)
//    public void testValidateVerifoneCryptogram(){
//        verifone.setEncryptedCard(null);
//        Validator.validateVerifoneCryptogram(verifone);
//    }
//
//    @Test(expected = Exception.class)
//    public void testValidateAccount(){
//        Validator.validateAccount(null);
//        account.setAccountNumber("");
//        account.setRoutingNumber("");
//        Validator.validateAccount(account);
//        account.setAccountNumber("12345");
//        Validator.validateAccount(account);
//        account.setRoutingNumber("123456789");
//        Validator.validateAccount(account);
//    }
//
//    @Test(expected = Exception.class)
//    public void testValidateVoltageCryptogram(){
//        voltage.setEncryptedCard(null);
//        Validator.validateVoltageCryptogram(voltage);
//        voltage.setEncryptedCard(new Card());
//        Validator.validateVoltageCryptogram(voltage);
//    }
//
//    @Test(expected = Exception.class)
//    public void testValidateCryptogram(){
//        Validator.validateCryptogram(null,null);
//        Validator.validateCryptogram(verifone,null);
//        Validator.validateCryptogram(null,voltage);
//    }

}
