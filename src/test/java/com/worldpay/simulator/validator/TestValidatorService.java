package com.worldpay.simulator.validator;

import static org.powermock.api.mockito.PowerMockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.ws.soap.SoapHeaderElement;

import com.worldpay.simulator.Account;
import com.worldpay.simulator.AccountType;
import com.worldpay.simulator.BatchDetokenizeRequest;
import com.worldpay.simulator.BatchTokenizeRequest;
import com.worldpay.simulator.Card;
import com.worldpay.simulator.DecryptRequest;
import com.worldpay.simulator.DeregistrationRequest;
import com.worldpay.simulator.DetokenizeRequest;
import com.worldpay.simulator.ECheckDetokenizeRequest;
import com.worldpay.simulator.ECheckToken;
import com.worldpay.simulator.ECheckTokenizeRequest;
import com.worldpay.simulator.EchoRequest;
import com.worldpay.simulator.MerchantType;
import com.worldpay.simulator.OrderDeregistrationRequest;
import com.worldpay.simulator.OrderRegistrationRequest;
import com.worldpay.simulator.RegistrationRequest;
import com.worldpay.simulator.Token;
import com.worldpay.simulator.TokenInquiryRequest;
import com.worldpay.simulator.TokenRegistrationRequest;
import com.worldpay.simulator.TokenizeRequest;
import com.worldpay.simulator.VerifoneCryptogram;
import com.worldpay.simulator.VerifoneMerchantKeyType;
import com.worldpay.simulator.VerifoneTerminal;
import com.worldpay.simulator.WalletType;


@RunWith(PowerMockRunner.class)
@PrepareForTest({Validator.class})
public class TestValidatorService {


    private String validMerchantRfId;
    private String invalidMerchantRfId;
    private String rollupId;
    private String token;
    private SoapHeaderElement header;
    private String PAN;
    private String ANDROID;
    private String CRYPTOGRAM;
    private String CVV;
    private String registrationId;

    @Before
    public void setUp(){
        validMerchantRfId = "123456789123456789";
        invalidMerchantRfId = "123";
        token = "468498435168468";
        rollupId = "1123";
        header = null;
        PAN = "615348948648468";
        ANDROID = "ANDROID";
        CRYPTOGRAM = "2wABBJQ1AgAAAAAgJDUCAAAAAAA=";
        CVV = "468";
        registrationId = "615348948648648";
        ValidatorService temp = new ValidatorService();
        mockStatic(Validator.class);
    }


    @Test
    public void testValidateRequestDecryptRequest() throws Exception {
        DecryptRequest request = new DecryptRequest();
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);
        request.setMerchant(merchant);
        request.setMerchantRefId(validMerchantRfId);

        VerifoneCryptogram cryptogram = new VerifoneCryptogram();
        Card encryptedCard = new Card();
        encryptedCard.setPrimaryAccountNumber("123456789101112");
        encryptedCard.setExpirationDate("0818");
        cryptogram.setEncryptedCard(encryptedCard);
        cryptogram.setMerchantKeyType(VerifoneMerchantKeyType.SHARED);

        VerifoneTerminal terminal = new VerifoneTerminal();
        terminal.setRegisterId("1234");
        terminal.setLaneId("1234");
        terminal.setChainCode("1234");
        terminal.setMerchantId("1234");
        cryptogram.setTerminal(terminal);

        request.setVerifoneCryptogram(cryptogram);
        doNothing().when(Validator.class, "validateSoapHeader", header);
        ValidatorService.validateRequest(request,header);
    }

    @Test
    public void testValidateRequestOrderRegistrationRequest() throws Exception {
        OrderRegistrationRequest request = new OrderRegistrationRequest();
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);
        request.setMerchant(merchant);
        request.setCardSecurityCode("123");

        request.setMerchantRefId(validMerchantRfId);
        doNothing().when(Validator.class, "validateSoapHeader", header);
        ValidatorService.validateRequest(request,header);
    }

    @Test
    public void testSimulate() throws InterruptedException {
        ValidatorService.handleExceptionsAndDelay(validMerchantRfId);
        ValidatorService.handleExceptionsAndDelay(invalidMerchantRfId);
    }

    @Test
    public void testValidateRequestTokenizeRequest() throws Exception {
        TokenizeRequest tokenizeRequest = new TokenizeRequest();
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);
        tokenizeRequest.setMerchant(merchant);
        tokenizeRequest.setPrimaryAccountNumber(PAN);
        tokenizeRequest.setMerchantRefId(validMerchantRfId);
        doNothing().when(Validator.class, "validateSoapHeader", header);
        ValidatorService.validateRequest(tokenizeRequest,header);
    }

    @Test
    public void testValidateRequestDetokenizeRequest() throws Exception {
        DetokenizeRequest detokenizeRequest = new DetokenizeRequest();
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);
        detokenizeRequest.setMerchant(merchant);
        detokenizeRequest.setToken(token);
        detokenizeRequest.setCVV2Requested(true);
        detokenizeRequest.setExpirationDateRequested(true);
        detokenizeRequest.setMerchantRefId(validMerchantRfId);
        doNothing().when(Validator.class, "validateSoapHeader", header);
        ValidatorService.validateRequest(detokenizeRequest,header);
    }

    @Test
    public void testValidateRequestBatchTokenizeRequest() throws Exception {
        BatchTokenizeRequest request = new BatchTokenizeRequest();
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);
        request.setMerchant(merchant);

        Card card1 = new Card();
        card1.setPrimaryAccountNumber(PAN);

        Card card2 = new Card();
        card2.setPrimaryAccountNumber("615348948648000");

        Card card3 = new Card();
        card3.setPrimaryAccountNumber("615348948648004");

        request.getCard().add(card1);
        request.getCard().add(card2);
        request.getCard().add(card3);

        request.setMerchantRefId(validMerchantRfId);
        doNothing().when(Validator.class, "validateSoapHeader", header);
        ValidatorService.validateRequest(request,header);
    }

    @Test
    public void testValidateRequestBatchDetokenizeRequest() throws Exception {
        BatchDetokenizeRequest request = new BatchDetokenizeRequest();

        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);


        Token token1 = new Token();
        Token token2 = new Token();
        Token token3 = new Token();

        token1.setTokenValue(token);
        token2.setTokenValue("468498435168000");
        token3.setTokenValue("468498435168004");

        request.setMerchant(merchant);
        request.getToken().add(token1);
        request.getToken().add(token2);
        request.getToken().add(token3);

        request.setMerchantRefId(validMerchantRfId);
        doNothing().when(Validator.class, "validateSoapHeader", header);
        ValidatorService.validateRequest(request,header);
    }

    @Test
    public void testValidateRequestTokenInquiryRequest() throws Exception {
        TokenInquiryRequest request = new TokenInquiryRequest();
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);
        request.setMerchant(merchant);
        List<Card> cards = request.getCard();

        Card card1 = new Card();
        card1.setPrimaryAccountNumber("1234567891011123");
        cards.add(card1);

        Card card2 = new Card();
        card2.setPrimaryAccountNumber("1234567891011000");
        cards.add(card2);

        request.setMerchantRefId(validMerchantRfId);
        doNothing().when(Validator.class, "validateSoapHeader", header);
        ValidatorService.validateRequest(request,header);
    }

    @Test
    public void testValidateRequestRegistrationRequest() throws Exception {
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);

        RegistrationRequest request = new RegistrationRequest();
        request.setMerchantRefId(validMerchantRfId);
        request.setPrimaryAccountNumber(PAN);
        request.setWalletType(WalletType.fromValue(ANDROID));
        request.setCryptogram(CRYPTOGRAM.getBytes());
        request.setMerchant(merchant);

        request.setMerchantRefId(validMerchantRfId);
        doNothing().when(Validator.class, "validateSoapHeader", header);
        ValidatorService.validateRequest(request,header);
    }

    @Test
    public void testValidateRequestDeregistrationRequest() throws Exception {
        DeregistrationRequest deregistrationRequest = new DeregistrationRequest();
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);
        deregistrationRequest.setMerchant(merchant);
        deregistrationRequest.setRegId(registrationId);
        deregistrationRequest.setCardSecurityCodeRequested(true);

        deregistrationRequest.setMerchantRefId(validMerchantRfId);
        doNothing().when(Validator.class, "validateSoapHeader", header);
        ValidatorService.validateRequest(deregistrationRequest,header);
    }

    @Test
    public void testValidateRequestTokenRegistrationRequest() throws Exception {
        TokenRegistrationRequest request = new TokenRegistrationRequest();
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);
        request.setMerchant(merchant);
        request.setToken(token);

        request.setMerchantRefId(validMerchantRfId);
        doNothing().when(Validator.class, "validateSoapHeader", header);
        ValidatorService.validateRequest(request,header);
    }

    @Test
    public void testValidateRequestECheckTokenizeRequest() throws Exception {
        ECheckTokenizeRequest request = new ECheckTokenizeRequest();
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);
        request.setMerchant(merchant);
        Account account = new Account();
        account.setAccountNumber("1234567890123");
        account.setAccountType(AccountType.CHECKING);
        account.setRoutingNumber("123456");
        request.setAccount(account);

        request.setMerchantRefId(validMerchantRfId);
        doNothing().when(Validator.class, "validateSoapHeader", header);
        ValidatorService.validateRequest(request,header);
    }

    @Test
    public void testValidateRequestECheckDetokenizeRequest() throws Exception {
        ECheckDetokenizeRequest request = new ECheckDetokenizeRequest();
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);
        request.setMerchant(merchant);
        ECheckToken batchtoken = new ECheckToken();
        batchtoken.setTokenValue(token);
        request.setToken(batchtoken);

        request.setMerchantRefId(validMerchantRfId);
        doNothing().when(Validator.class, "validateSoapHeader", header);
        ValidatorService.validateRequest(request,header);
    }

    @Test
    public void testValidateRequestOrderDeregistrationRequest() throws Exception {
        OrderDeregistrationRequest orderDeregistrationRequest = new OrderDeregistrationRequest();
        MerchantType merchant = new MerchantType();
        merchant.setRollupId(rollupId);
        orderDeregistrationRequest.setMerchant(merchant);
        orderDeregistrationRequest.setOrderLVT(CVV);
        orderDeregistrationRequest.setToken(token);

        orderDeregistrationRequest.setMerchantRefId(validMerchantRfId);
        doNothing().when(Validator.class, "validateSoapHeader", header);
        ValidatorService.validateRequest(orderDeregistrationRequest,header);
    }

    @Test
    public void testValidateRequestEchoRequest() throws Exception {
        EchoRequest echoRequest = new EchoRequest();
        String test = "asdahfiuahofo";
        echoRequest.setTest(test);

        doNothing().when(Validator.class, "validateSoapHeader", header);
        ValidatorService.validateRequest(echoRequest,header);

    }
}




