package com.worldpay.simulator;

import com.worldpay.simulator.exceptions.ServerFaultException;
import com.worldpay.simulator.exceptions.ClientFaultException;
import com.worldpay.simulator.utils.EWSUtils;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TestEWSUtils {
  private String reqId1;
  private String reqId2;
  private String PAN1;
  private String PAN2;
  private String regId1;
  private String regId2;
  private String property1;
  private String property2;
  private String invalidProperty;
  private String defaultPAN;
  private String cvv;
  private String token;
  private AccountType CHECKING;
  private AccountType SAVINGS;
  private AccountType CORPORATE_CHECKING;
  private AccountType CORPORATE_SAVINGS;



  @Before
  public void setUp(){
    PAN1 = "123513521231";
    PAN2 = "251";
    regId1 = "123513521321";
    regId2 = "251";
    property1 = "253153211231";
    property2 = "251";
    invalidProperty = "asd21132";
    defaultPAN = "3000100011118566";
    cvv = "688";
    token = "1252645696785667688";
    SAVINGS = AccountType.SAVINGS;
    CHECKING = AccountType.CHECKING;
    CORPORATE_CHECKING = AccountType.CORPORATE_CHECKING;
    CORPORATE_SAVINGS = AccountType.CORPORATE_SAVINGS;

  }




  @Test
  public void testRandomReqId(){
    reqId1 = EWSUtils.randomReqId();
    reqId2 = EWSUtils.randomReqId();
    assertFalse(reqId1.equals(reqId2));
  }

  @Test
  public void testGetRegId(){
    String temp = EWSUtils.getRegIdFromPAN(PAN1);
    assertEquals(regId1,temp);
    temp = EWSUtils.getRegIdFromPAN(PAN2);
    assertEquals(regId2,temp);
  }

  @Test
  public void testGenerateProperty(){
    String temp = EWSUtils.generateProperty(PAN1);
    assertEquals(property1,temp);
    temp = EWSUtils.generateProperty(PAN2);
    assertEquals(property2,temp);
  }

  @Test
  public void testGetToken(){
    String temp = EWSUtils.getToken(PAN1);
    assertEquals(property1,temp);
    temp = EWSUtils.getToken(PAN2);
    assertEquals(property2,temp);


  }

  @Test
  public void testGetPAN(){
    String temp = EWSUtils.getPAN(property1);
    assertEquals(PAN1,temp);
    temp = EWSUtils.getPAN(property2);
    assertEquals(PAN2,temp);
    temp = EWSUtils.getPAN(invalidProperty);
    assertEquals(defaultPAN,temp);
  }

  @Test
  public void testGenerateRandomNumber(){
    String temp1 = EWSUtils.generateRandomNumber(5);
    String temp2 = EWSUtils.generateRandomNumber(5);
    assertNotEquals(temp1,temp2);
  }

  @Test
  public void testIsSecurityCodeEmpty(){
    assertTrue(EWSUtils.isSecurityCodeEmpty(null));
    assertTrue(EWSUtils.isSecurityCodeEmpty("?"));
    assertTrue(EWSUtils.isSecurityCodeEmpty(" "));
    assertTrue(EWSUtils.isSecurityCodeEmpty(""));
    assertFalse(EWSUtils.isSecurityCodeEmpty("123"));
  }

  @Test
  public void testIsSecurityCodeValid(){
    assertFalse(EWSUtils.isSecurityCodeValid(null));
    assertFalse(EWSUtils.isSecurityCodeValid("?"));
    assertFalse(EWSUtils.isSecurityCodeValid(" "));
    assertFalse(EWSUtils.isSecurityCodeValid(""));
    assertFalse(EWSUtils.isSecurityCodeValid("12"));
    assertTrue(EWSUtils.isSecurityCodeValid("123"));
  }

  @Test(expected = Exception.class)
  public void handleDesiredExceptions_throwException(){
    EWSUtils.handleDesiredExceptions("1003");
  }

  @Test
  public void handleDesiredExceptions_noException_inputTooShort(){
    EWSUtils.handleDesiredExceptions("13");
  }

  @Test
  public void handleDesiredExceptions_NoException(){
    EWSUtils.handleDesiredExceptions("1313131");
  }

  @Test(expected = ServerFaultException.class)
  public void testThrowDesiredException_ServerFaultException_1(){
    EWSUtils.throwDesiredException(1);
  }

  @Test(expected = ServerFaultException.class)
  public void testThrowDesiredException_ServerFaultException_2(){
    EWSUtils.throwDesiredException(2);
  }

  @Test(expected = ServerFaultException.class)
  public void testThrowDesiredException_ServerFaultException_3(){
    EWSUtils.throwDesiredException(3);
  }

  @Test(expected = ServerFaultException.class)
  public void testThrowDesiredException_ServerFaultException_8(){
    EWSUtils.throwDesiredException(8);
  }

  @Test(expected = ClientFaultException.class)
  public void testThrowDesiredException_ClientFaultException(){
    EWSUtils.throwDesiredException(7);
  }

  @Test
  public void testIsClientFaultError(){
    assertTrue(EWSUtils.isClientFaultError(4));
    assertFalse(EWSUtils.isClientFaultError(2));
    assertFalse(EWSUtils.isClientFaultError(99));
  }

  @Test
  public void testGetPANThroughRegId(){
    String temp = EWSUtils.getPANThroughRegId(regId1);
    assertEquals(PAN1,temp);
    temp = EWSUtils.getPANThroughRegId(regId2);
    assertEquals(PAN2,temp);
    assertEquals("12",EWSUtils.getPANThroughRegId("12"));
  }

  @Test
  public void testGetCVVThroughToken(){
    assertEquals(cvv,EWSUtils.getCVVThroughToken(token));
  }

  @Test
  public void testGetAccountType(){
    assertEquals(CHECKING,EWSUtils.getAccountType("120"));
    assertEquals(SAVINGS,EWSUtils.getAccountType("121"));
    assertEquals(CORPORATE_CHECKING,EWSUtils.getAccountType("122"));
    assertEquals(CORPORATE_SAVINGS,EWSUtils.getAccountType("123"));
  }

  @Test
  public void testGetRoutingNumber(){
    assertEquals("300010",EWSUtils.getRoutingNumber(defaultPAN));
  }

  @Test
  public void testDecrypt(){
    assertEquals("132125315321",EWSUtils.decrypt(PAN1));
  }


}
