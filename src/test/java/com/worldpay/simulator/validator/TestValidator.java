package com.worldpay.simulator.validator;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.spy;
import static org.powermock.api.mockito.PowerMockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.ws.transport.context.TransportContextHolder;

import com.worldpay.simulator.Account;
import com.worldpay.simulator.BatchDetokenizeRequest;
import com.worldpay.simulator.BatchTokenizeRequest;
import com.worldpay.simulator.Card;
import com.worldpay.simulator.DecryptRequest;
import com.worldpay.simulator.DeregistrationRequest;
import com.worldpay.simulator.DetokenizeRequest;
import com.worldpay.simulator.ECheckDetokenizeRequest;
import com.worldpay.simulator.ECheckToken;
import com.worldpay.simulator.ECheckTokenizeRequest;
import com.worldpay.simulator.MerchantType;
import com.worldpay.simulator.OrderDeregistrationRequest;
import com.worldpay.simulator.OrderRegistrationRequest;
import com.worldpay.simulator.RegistrationRequest;
import com.worldpay.simulator.SecurityHeaderType;
import com.worldpay.simulator.Token;
import com.worldpay.simulator.TokenInquiryRequest;
import com.worldpay.simulator.TokenRegistrationRequest;
import com.worldpay.simulator.TokenizeRequest;
import com.worldpay.simulator.VerifoneCryptogram;
import com.worldpay.simulator.VerifoneMerchantKeyType;
import com.worldpay.simulator.VerifoneTerminal;
import com.worldpay.simulator.VoltageCryptogram;
import com.worldpay.simulator.exceptions.DetailSoapFaultDefinitionExceptionResolver;


@RunWith(PowerMockRunner.class)
@PrepareForTest({ValidatorUtils.class})
public class TestValidator {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Mock
    JAXBContext jaxbContextMock;

    @Mock
    Unmarshaller unmarshallerMock;

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
    private static String INVALID_TOKEN;
    private static String TOKEN_NOT_FOUND;
    private String rollupId;
    private Validator validatorSpy;
    private Validator validator;


    @Before
    public void setUp() {

        mockStatic(ValidatorUtils.class);
        validator = new Validator();
        validatorSpy = spy(validator);
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
        rollupId = "1123";

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
        tokens = new ArrayList<>();
        Validator.validateToken(tokens,1);
    }

    @Test
    public void testValidateCard() throws Exception {
        doNothing().when(ValidatorUtils.class,"handleException",INVALID_REQ,INVALID_CARD_DETAILS);
        doReturn(true).when(ValidatorUtils.class,"isValidPAN","123456789011123");
        card1.setPrimaryAccountNumber("");
        Validator.validateCard(card1);
        card1.setPrimaryAccountNumber("123456789011123");
        Validator.validateCard(card1);
    }

    @Test
    public void testValidateToken() throws Exception {
        doNothing().when(ValidatorUtils.class,"handleException",INVALID_REQ,INVALID_CARD_DETAILS);
        doReturn(true).when(ValidatorUtils.class,"isValidToken","12312312");
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
        doReturn(true).when(ValidatorUtils.class,"isValidCVV",expDate);

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

    @Test(expected = NullPointerException.class)
    public void testValidateECheckToken_null() throws Exception {
        doNothing().when(ValidatorUtils.class,"handleException",INVALID_REQ,TOKEN_NOT_FOUND);
        Validator.validateToken((ECheckToken) null);
        ECheckToken tempToken = new ECheckToken();
        tempToken.setTokenValue("");
        Validator.validateToken((ECheckToken)tempToken);
        tempToken.setTokenValue("123");
        Validator.validateToken((ECheckToken)tempToken);
    }

    @Test
    public void testValidateECheckToken() throws Exception {
        doNothing().when(ValidatorUtils.class,"handleException",INVALID_REQ,TOKEN_NOT_FOUND);
        doReturn(true).when(ValidatorUtils.class,"isValidToken","123");
        ECheckToken tempToken = new ECheckToken();
        tempToken.setTokenValue("");
        Validator.validateToken((ECheckToken)tempToken);
        tempToken.setTokenValue("123");
        Validator.validateToken((ECheckToken)tempToken);
    }

    @Test(expected = NullPointerException.class)
    public void testValidateMerchant_null() throws Exception {
        doNothing().when(ValidatorUtils.class,"handleException",INVALID_REQ,INVALID_CARD_DETAILS);
        Validator.validateMerchant((MerchantType)null);
    }

    @Test
    public void testValidateMerchant() throws Exception {
        doNothing().when(ValidatorUtils.class,"handleException",INVALID_REQ,INVALID_CARD_DETAILS);
        doReturn(true).when(ValidatorUtils.class,"isValidRollupId","1123");
        merchant.setRollupId("");
        Validator.validateMerchant(merchant);
        merchant.setRollupId("1123");
        Validator.validateMerchant(merchant);
    }

    @Test
    public void testValidateMerchantKeyType() throws Exception {
        doNothing().when(ValidatorUtils.class,"handleException",INVALID_REQ,INVALID_CARD_DETAILS);
        Validator.validateMerchantKeyType(null);
        Validator.validateMerchantKeyType(VerifoneMerchantKeyType.SHARED);
    }

    @Test(expected = Exception.class)
    public void testValidateVerifoneTerminal_null() throws Exception {
        doNothing().when(ValidatorUtils.class,"handleException",INVALID_REQ,INVALID_CARD_DETAILS);
        Validator.validateVerifoneTerminal(null);
    }

    @Test
    public void testValidateVerifoneTerminal() throws Exception {
        doNothing().when(ValidatorUtils.class,"handleException",INVALID_REQ,INVALID_CARD_DETAILS);
        doReturn(true).when(ValidatorUtils.class,"isStringEmpty",null);
        doReturn(true).when(ValidatorUtils.class,"isStringEmpty","");

        Validator.validateVerifoneTerminal(terminal);
        terminal.setRegisterId("123");
        Validator.validateVerifoneTerminal(terminal);
        terminal.setLaneId("1");
        Validator.validateVerifoneTerminal(terminal);
        terminal.setChainCode("123");
        Validator.validateVerifoneTerminal(terminal);
        terminal.setMerchantId("123");
        Validator.validateVerifoneTerminal(terminal);
    }

    @Test(expected = NullPointerException.class)
    public void testValidateVerifoneCryptogram_null() throws Exception {
        doNothing().when(ValidatorUtils.class,"handleException",INVALID_REQ,INVALID_CARD_DETAILS);
        verifone.setEncryptedCard(null);
        Validator.validateVerifoneCryptogram(verifone);
    }

    @Test
    public void testValidateVerifoneCryptogram() throws Exception {
        doNothing().when(ValidatorUtils.class,"handleException",INVALID_REQ,INVALID_CARD_DETAILS);
        card1.setTrack1("1");
        card1.setTrack2("2");
        card1.setExpirationDate(expDate);
        card1.setPrimaryAccountNumber(PAN1);
        terminal.setRegisterId("123");
        terminal.setLaneId("1");
        terminal.setChainCode("123");
        terminal.setMerchantId("123");
        verifone.setEncryptedCard(card1);
        verifone.setMerchantKeyType(VerifoneMerchantKeyType.SHARED);
        verifone.setTerminal(terminal);
        Validator.validateVerifoneCryptogram(verifone);
    }

    @Test(expected = Exception.class)
    public void testValidateAccount_null() throws Exception {
        doNothing().when(ValidatorUtils.class,"handleException",INVALID_REQ,INVALID_CARD_DETAILS);
        Validator.validateAccount(null);
    }

    @Test
    public void testValidateAccount() throws Exception {
        doNothing().when(ValidatorUtils.class,"handleException",INVALID_REQ,INVALID_CARD_DETAILS);
        doReturn(true).when(ValidatorUtils.class,"isValidAccount","12345");
        doReturn(true).when(ValidatorUtils.class,"isValidRoutingNumber","123456789");

        account.setAccountNumber("");
        account.setRoutingNumber("");
        Validator.validateAccount(account);
        account.setAccountNumber("12345");
        Validator.validateAccount(account);
        account.setRoutingNumber("123456789");
        Validator.validateAccount(account);
    }

    @Test(expected = Exception.class)
    public void testValidateVoltageCryptogram_null() throws Exception {
        doNothing().when(ValidatorUtils.class,"handleException",INVALID_REQ,INVALID_CARD_DETAILS);
        voltage.setEncryptedCard(null);
        Validator.validateVoltageCryptogram(voltage);
    }

    @Test
    public void testValidateVoltageCryptogram() throws Exception {
        doNothing().when(ValidatorUtils.class,"handleException",INVALID_REQ,INVALID_CARD_DETAILS);
        card1.setTrack1("1");
        card1.setTrack2("2");
        card1.setExpirationDate(expDate);
        card1.setPrimaryAccountNumber(PAN1);
        voltage.setEncryptedCard(card1);
        Validator.validateVoltageCryptogram(voltage);
    }

    @Test(expected =NullPointerException.class)
    public void testValidateCryptogram_allNull() throws Exception {
        doNothing().when(ValidatorUtils.class,"handleException",INVALID_REQ,INVALID_CARD_DETAILS);
        Validator.validateCryptogram(null,null);
    }

    @Test
    public void testValidateCryptogram_voltageNull() throws Exception {
        doNothing().when(ValidatorUtils.class,"handleException",INVALID_REQ,INVALID_CARD_DETAILS);
        card1.setTrack1("1");
        card1.setTrack2("2");
        card1.setExpirationDate(expDate);
        card1.setPrimaryAccountNumber(PAN1);
        terminal.setRegisterId("123");
        terminal.setLaneId("1");
        terminal.setChainCode("123");
        terminal.setMerchantId("123");
        verifone.setEncryptedCard(card1);
        verifone.setMerchantKeyType(VerifoneMerchantKeyType.SHARED);
        verifone.setTerminal(terminal);
        Validator.validateCryptogram(verifone,null);
    }

    @Test
    public void testValidateCryptogram_verifoneNull() throws Exception {
        doNothing().when(ValidatorUtils.class,"handleException",INVALID_REQ,INVALID_CARD_DETAILS);
        card1.setTrack1("1");
        card1.setTrack2("2");
        card1.setExpirationDate(expDate);
        card1.setPrimaryAccountNumber(PAN1);
        voltage.setEncryptedCard(card1);
        Validator.validateCryptogram(null,voltage);
    }

    @Test
    public void testValidateTokenizeRequest() throws Exception {
        doNothing().when(ValidatorUtils.class,"handleException",INVALID_REQ,INVALID_CARD_DETAILS);
        doReturn(true).when(ValidatorUtils.class,"isValidPAN",PAN1);
        doReturn(true).when(ValidatorUtils.class,"isStringEmpty","");
        doReturn(false).when(ValidatorUtils.class,"isStringEmpty","1");
        doReturn(false).when(ValidatorUtils.class,"isStringEmpty","123");
        doReturn(true).when(ValidatorUtils.class,"isValidCVV","123");
        TokenizeRequest tokenizeRequest = new TokenizeRequest();
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);
        tokenizeRequest.setMerchant(merchant);
        tokenizeRequest.setPrimaryAccountNumber("");
        tokenizeRequest.setCardSecurityCode("");
        Validator.validate(tokenizeRequest);
        tokenizeRequest.setPrimaryAccountNumber(PAN1);
        Validator.validate(tokenizeRequest);
        tokenizeRequest.setCardSecurityCode("1");
        Validator.validate(tokenizeRequest);
        tokenizeRequest.setCardSecurityCode("123");
        Validator.validate(tokenizeRequest);
    }

    @Test
    public void testValidateDetokenizeRequest() throws Exception {
        doNothing().when(ValidatorUtils.class,"handleException",INVALID_REQ,INVALID_CARD_DETAILS);
        doReturn(true).when(ValidatorUtils.class,"isValidToken","468498435168468");
        DetokenizeRequest detokenizeRequest = new DetokenizeRequest();
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);
        detokenizeRequest.setMerchant(merchant);
        detokenizeRequest.setToken("");
        Validator.validate(detokenizeRequest);
        detokenizeRequest.setToken("468498435168468");
        Validator.validate(detokenizeRequest);
    }

    @Test
    public void testValidateBatchTokenizeRequest() throws Exception {
        doNothing().when(ValidatorUtils.class,"handleException",INVALID_REQ,INVALID_CARD_DETAILS);
        BatchTokenizeRequest batchTokenizeRequest = new BatchTokenizeRequest();
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);
        batchTokenizeRequest.setMerchant(merchant);
        card1.setPrimaryAccountNumber(PAN1);
        card2.setPrimaryAccountNumber(PAN2);

        batchTokenizeRequest.getCard().add(card1);
        batchTokenizeRequest.getCard().add(card2);
        Validator.validate(batchTokenizeRequest);
    }

    @Test
    public void testValidateBatchDetokenizeRequest() throws Exception {
        doNothing().when(ValidatorUtils.class,"handleException",INVALID_REQ,INVALID_CARD_DETAILS);
        BatchDetokenizeRequest batchDetokenizeRequest = new BatchDetokenizeRequest();
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);
        batchDetokenizeRequest.setMerchant(merchant);
        token1.setTokenValue("12312312");
        token2.setTokenValue("12312312");

        batchDetokenizeRequest.getToken().add(token1);
        batchDetokenizeRequest.getToken().add(token2);
        Validator.validate(batchDetokenizeRequest);
    }

    @Test
    public void testValidateTokenInquiryRequest() throws Exception {
        doNothing().when(ValidatorUtils.class,"handleException",INVALID_REQ,INVALID_CARD_DETAILS);
        TokenInquiryRequest tokenInquiryRequest = new TokenInquiryRequest();
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);
        tokenInquiryRequest.setMerchant(merchant);
        card1.setPrimaryAccountNumber(PAN1);
        card2.setPrimaryAccountNumber(PAN2);

        tokenInquiryRequest.getCard().add(card1);
        tokenInquiryRequest.getCard().add(card2);
        Validator.validate(tokenInquiryRequest);
    }

    @Test
    public void testValidateRegistrationRequest() throws Exception {
        doNothing().when(ValidatorUtils.class,"handleException",INVALID_REQ,INVALID_CARD_DETAILS);
        RegistrationRequest registrationRequest = new RegistrationRequest();
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);
        registrationRequest.setMerchant(merchant);
        Validator.validate(registrationRequest);
    }

    @Test
    public void testValidateDeregistrationRequest() throws Exception {
        doNothing().when(ValidatorUtils.class,"handleException",INVALID_REQ,INVALID_CARD_DETAILS);
        doReturn(true).when(ValidatorUtils.class,"isValidRegId","123456");
        DeregistrationRequest deregistrationRequest = new DeregistrationRequest();
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);
        deregistrationRequest.setMerchant(merchant);
        deregistrationRequest.setRegId("");
        Validator.validate(deregistrationRequest);
        deregistrationRequest.setRegId("123456");
        Validator.validate(deregistrationRequest);
    }

    @Test
    public void testValidateDecryptRequest() throws Exception {
        doNothing().when(ValidatorUtils.class,"handleException",INVALID_REQ,INVALID_CARD_DETAILS);
        DecryptRequest decryptRequest = new DecryptRequest();
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);
        decryptRequest.setMerchant(merchant);
        card1.setTrack1("1");
        card1.setTrack2("2");
        card1.setExpirationDate(expDate);
        card1.setPrimaryAccountNumber(PAN1);
        terminal.setRegisterId("123");
        terminal.setLaneId("1");
        terminal.setChainCode("123");
        terminal.setMerchantId("123");
        verifone.setEncryptedCard(card1);
        verifone.setMerchantKeyType(VerifoneMerchantKeyType.SHARED);
        verifone.setTerminal(terminal);
        voltage.setEncryptedCard(card1);
        decryptRequest.setVerifoneCryptogram(verifone);
        decryptRequest.setVoltageCryptogram(voltage);
        Validator.validate(decryptRequest);
    }

    @Test
    public void testValidateTokenRegistrationRequest() throws Exception {
        doNothing().when(ValidatorUtils.class,"handleException",INVALID_REQ,INVALID_CARD_DETAILS);
        doReturn(true).when(ValidatorUtils.class,"isValidToken","468498435168468");
        TokenRegistrationRequest tokenRegistrationRequest = new TokenRegistrationRequest();
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);
        tokenRegistrationRequest.setMerchant(merchant);
        tokenRegistrationRequest.setToken("");
        Validator.validate(tokenRegistrationRequest);
        tokenRegistrationRequest.setToken("468498435168468");
        Validator.validate(tokenRegistrationRequest);
    }

    @Test
    public void testValidateECheckTokenizeRequest() throws Exception {
        doNothing().when(ValidatorUtils.class,"handleException",INVALID_REQ,INVALID_CARD_DETAILS);
        ECheckTokenizeRequest eCheckTokenizeRequest = new ECheckTokenizeRequest();
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);
        eCheckTokenizeRequest.setMerchant(merchant);
        account.setAccountNumber("12345");
        account.setRoutingNumber("123456789");
        eCheckTokenizeRequest.setAccount(new Account());
        Validator.validate(eCheckTokenizeRequest);
        eCheckTokenizeRequest.setAccount(account);
        Validator.validate(eCheckTokenizeRequest);
    }

    @Test
    public void testValidateECheckDetokenizeRequest() throws Exception {
        doNothing().when(ValidatorUtils.class,"handleException",INVALID_REQ,INVALID_CARD_DETAILS);
        ECheckDetokenizeRequest eCheckDetokenizeRequest = new ECheckDetokenizeRequest();
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);
        eCheckDetokenizeRequest.setMerchant(merchant);
        ECheckToken tempToken = new ECheckToken();
        tempToken.setTokenValue("123");
        eCheckDetokenizeRequest.setToken(new ECheckToken());
        Validator.validate(eCheckDetokenizeRequest);
        eCheckDetokenizeRequest.setToken(tempToken);
        Validator.validate(eCheckDetokenizeRequest);
    }

    @Test
    public void testValidateOrderRegistrationRequest() throws Exception {
        doNothing().when(ValidatorUtils.class,"handleException",INVALID_REQ,INVALID_CARD_DETAILS);
        doReturn(true).when(ValidatorUtils.class,"isValidCVV","123");
        OrderRegistrationRequest orderRegistrationRequest = new OrderRegistrationRequest();
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);
        orderRegistrationRequest.setMerchant(merchant);

        orderRegistrationRequest.setCardSecurityCode("");
        Validator.validate(orderRegistrationRequest);
        orderRegistrationRequest.setCardSecurityCode("123");
        Validator.validate(orderRegistrationRequest);
    }

    @Test
    public void testValidateOrderDeregistrationRequest() throws Exception {
        doNothing().when(ValidatorUtils.class,"handleException",INVALID_REQ,INVALID_CARD_DETAILS);
        doReturn(true).when(ValidatorUtils.class,"isValidOrderLVT","312456789123456789");
        OrderDeregistrationRequest orderDeregistrationRequest = new OrderDeregistrationRequest();
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);
        orderDeregistrationRequest.setMerchant(merchant);

        orderDeregistrationRequest.setOrderLVT("");
        Validator.validate(orderDeregistrationRequest);
        orderDeregistrationRequest.setOrderLVT("312456789123456789");
        Validator.validate(orderDeregistrationRequest);
    }


}
