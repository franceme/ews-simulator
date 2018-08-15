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
import org.junit.Test;
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

import jdk.nashorn.internal.runtime.arrays.ContinuousArrayData;


@RunWith(PowerMockRunner.class)
@PrepareForTest({Validator.class,JAXBContext.class,Unmarshaller.class,JAXBElement.class,ElementNSImpl.class})
public class TestValidator {
    private SoapHeaderElement header;
    private  JAXBContext context;
    private Unmarshaller unmarshaller;
    private JAXBElement<SecurityHeaderType> root;
    private ElementNSImpl userNameTokenAttribute;
    private Source source;
    private List<Object> securityAttributeList;
    private Card card1;
    private Card card2;
    private Token token1;
    private Token token2;
    private MerchantType merchant;
    private VerifoneTerminal terminal;
    private VerifoneCryptogram verifone;
    private Account account;
    private VoltageCryptogram voltage;

    @Before
    public void setUp(){
        mockStatic(Validator.class);
        mockStatic(JAXBContext.class);
        mockStatic(Unmarshaller.class);
        mockStatic(JAXBElement.class);
        header = null;
        context = null;
        unmarshaller = null;
        root = null;
        userNameTokenAttribute = null;
        source = null;
        securityAttributeList = new LinkedList<>();
        ((LinkedList<Object>) securityAttributeList).addFirst(new Object());
        card1 = new Card();
        card2 = new Card();
        token1 = new Token();
        token2 = new Token();
        merchant = new MerchantType();
        terminal = new VerifoneTerminal();
        verifone = new VerifoneCryptogram();
        account = new Account();
        voltage = new VoltageCryptogram();

    }
//
//    @Test
//    public void testValidateSoapHeader() throws Exception {
//        doReturn(context).when(JAXBContext.class, "newInstance", any());
//        doNothing().when(context.createMarshaller());
//        when(header.getSource()).thenReturn(source);
//        when(unmarshaller.unmarshal(source,SecurityHeaderType.class)).thenReturn(root);
//        doNothing().when(root.getValue());
//        when(root.getValue().getAny()).thenReturn(securityAttributeList);
//        when(securityAttributeList.get(any())).thenReturn(userNameTokenAttribute);
//        when(userNameTokenAttribute.getFirstChild()).thenReturn(null);
//
//        Validator.validateSoapHeader(header);
//
//    }


    @Test
    public void testValidateCards(){
        List<Card> cards = new ArrayList<>();
        card1.setPrimaryAccountNumber("1234567891011123");
        cards.add(card1);

        card2.setPrimaryAccountNumber("1234567891011000");
        cards.add(card2);
        Validator.validateCard(cards,1);
        Validator.validateCard(cards,2);
        Validator.validateCard(new ArrayList<Card>(),1);
    }

    @Test
    public void testValidateTokens(){
        List<Token> tokens = new ArrayList<>();
        tokens.add(token1);
        tokens.add(token2);
        Validator.validateToken(tokens,1);
        Validator.validateToken(tokens,2);
        Validator.validateCard(new ArrayList<Card>(),1);
    }

    @Test
    public void testValidateCard(){
        card1.setPrimaryAccountNumber("");
        Validator.validateCard(card1);
        card1.setPrimaryAccountNumber("1234567891011123");
        Validator.validateCard(card1);
    }

    @Test
    public void testValidateToken(){
        token1.setTokenValue("");
        Validator.validateToken(token1);
        token1.setTokenValue("12312312");
        Validator.validateToken(token1);
    }

    @Test
    public void testValidateVerifoneCard(){
        card1.setTrack1("");
        card1.setTrack2("");
        card1.setPrimaryAccountNumber("");
        card1.setExpirationDate("");
        Validator.validateVerifoneCard(card1);

        card1.setTrack1("1");
        Validator.validateVerifoneCard(card1);
        card1.setTrack2("2");
        Validator.validateVerifoneCard(card1);
        card1.setExpirationDate("2308");
        Validator.validateVerifoneCard(card1);
        card1.setPrimaryAccountNumber("1234567891011123");
        Validator.validateVerifoneCard(card1);

        card1.setTrack1("");
        Validator.validateVerifoneCard(card1);
        card1.setPrimaryAccountNumber("");
        Validator.validateVerifoneCard(card1);
        card1.setExpirationDate("");
        Validator.validateVerifoneCard(card1);

        card1.setTrack2("");
        card1.setPrimaryAccountNumber("1234567891011123");
        Validator.validateVerifoneCard(card1);
        card1.setExpirationDate("2308");
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
    public void testValidateVoltageCard(){
        card1.setTrack1("");
        card1.setTrack2("");
        card1.setPrimaryAccountNumber("");
        card1.setExpirationDate("");
        Validator.validateVerifoneCard(card1);

        card1.setTrack1("1");
        Validator.validateVerifoneCard(card1);
        card1.setTrack2("2");
        Validator.validateVerifoneCard(card1);
        card1.setSecurityCode("2308");
        Validator.validateVerifoneCard(card1);
        card1.setPrimaryAccountNumber("1234567891011123");
        Validator.validateVerifoneCard(card1);

        card1.setTrack1("");
        Validator.validateVerifoneCard(card1);
        card1.setPrimaryAccountNumber("");
        Validator.validateVerifoneCard(card1);
        card1.setSecurityCode("");
        Validator.validateVerifoneCard(card1);

        card1.setTrack2("");
        card1.setPrimaryAccountNumber("1234567891011123");
        Validator.validateVerifoneCard(card1);
        card1.setSecurityCode("2308");
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
    public void testValidateECheckToken(){
        Validator.validateToken((ECheckToken) null);
        ECheckToken tempToken = new ECheckToken();
        tempToken.setTokenValue("");
        Validator.validateToken(tempToken);
        tempToken.setTokenValue("123");
        Validator.validateToken(tempToken);
    }

    @Test
    public void testValidateMerchant(){
        Validator.validateMerchant((MerchantType)null);
        merchant.setRollupId("");
        Validator.validateMerchant(merchant);
        merchant.setRollupId("1123");
        Validator.validateMerchant(merchant);
    }

    @Test
    public void testValidateMerchantKeyType(){
        Validator.validateMerchantKeyType(null);
        Validator.validateMerchantKeyType(VerifoneMerchantKeyType.SHARED);
    }

    @Test
    public void testValidateVerifoneTerminal(){
        Validator.validateVerifoneTerminal(null);
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

    @Test
    public void testValidateVerifoneCryptogram(){
        verifone.setEncryptedCard(null);
        Validator.validateVerifoneCryptogram(verifone);
    }

    @Test
    public void testValidateAccount(){
        Validator.validateAccount(null);
        account.setAccountNumber("");
        account.setRoutingNumber("");
        Validator.validateAccount(account);
        account.setAccountNumber("12345");
        Validator.validateAccount(account);
        account.setRoutingNumber("123456789");
        Validator.validateAccount(account);
    }

    @Test
    public void testValidateVoltageCryptogram(){
        voltage.setEncryptedCard(null);
        Validator.validateVoltageCryptogram(voltage);
        voltage.setEncryptedCard(new Card());
        Validator.validateVoltageCryptogram(voltage);
    }

    @Test
    public void testValidateCryptogram(){
        Validator.validateCryptogram(null,null);
        Validator.validateCryptogram(verifone,null);
        Validator.validateCryptogram(null,voltage);
    }
    
}
