package com.worldpay.simulator.utils;


import static com.worldpay.simulator.utils.ValidatorUtils.isStringValidInteger;
import static com.worldpay.simulator.utils.ValidatorUtils.isValidPAN;

import java.util.Random;
import java.util.UUID;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPFault;
import javax.xml.ws.soap.SOAPFaultException;

import org.springframework.stereotype.Component;

import com.worldpay.simulator.AccountType;
import com.worldpay.simulator.VError;
import com.worldpay.simulator.WalletType;
import com.worldpay.simulator.errors.EWSError;
import com.worldpay.simulator.errors.ErrorIdMap;
import com.worldpay.simulator.exceptions.ClientFaultException;
import com.worldpay.simulator.exceptions.ServerFaultException;

@Component
public class EWSUtils {
    private static String defaultPan = "3000100011118566";

    public static String randomReqId() {
        return UUID.randomUUID().toString();
    }



    public static String generatePropertyStrategyPAN(String input){
        int lengthInput = input.length();
        String firstSix = input.substring(0,6);
        String lastFour = input.substring(lengthInput-4,lengthInput);
        StringBuffer sb = new StringBuffer(input.substring(6, lengthInput - 4));

        return firstSix+sb.reverse().toString()+lastFour;
    }

    public static String generateEcheckToken(String input){
        Long _17_9s = 99999999999999999l;
        Long inputValue = Long.parseLong(input);
        Long middleValue = (_17_9s-inputValue);

        StringBuilder middle = new StringBuilder(middleValue.toString());

        while(middle.length() < 17)
            middle.insert(0,"0");

        return middle.toString();
    }

    public static String generateEcheckAccount(String input){
        if(input.length() != 19 && !input.startsWith("2"))
            throw new ClientFaultException(106,"Token Not Found");

        Long l = 99999999999999999l;
        String middleValue = input.substring(1,18);
        Long accountNumber = l-Long.parseLong(middleValue);
        return accountNumber.toString();
    }

    public static String generateProperty(String input) {

        if(!isStringValidInteger(input))
            throw new ClientFaultException(104,"Value data must be numeric");

        if(isValidPAN(input))
            return generatePropertyStrategyPAN(input);

        return input;
    }

    public static String getPANToken(String primaryAccountNumber) {
        return generateProperty(primaryAccountNumber);
    }

    public static int getAccountType(AccountType accountType) {

        switch (accountType) {
            case CHECKING:
                return 0;
            case SAVINGS:
                return 1;
            case CORPORATE_CHECKING:
                return 2;
            default:
                return 3;
        }

    }

    //generating tokens based on cert environment
    //the length of the token varies between both simulator and cert (19 digits, 17 digits respectively)
    //Also the token always starts with "2" in cert
    public static String generateEcheckToken(String accountNumber, AccountType accountType) {
        return  "2"+generateEcheckToken(accountNumber)+String.valueOf(getAccountType(accountType));
    }

    public static String getPAN(String token) {

        if(!isValidPAN(generateProperty(token)))
            return defaultPan;
        return generateProperty(token);
    }

    public static String generateRandomNumber(int length){
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(length);
        for(int i=0;i<length;i++)
            sb.append((char)('0'+rnd.nextInt(10)));
        return sb.toString();
    }

    public static VError getError(String PAN){
        int last3DigitsButOne = Integer.parseInt(PAN.substring(PAN.length()-4,PAN.length() - 1));

        if(isServerFaultError(last3DigitsButOne))
            return null;

        EWSError validError = ErrorIdMap.getError(last3DigitsButOne);

        if(validError != null){
            VError error = new VError();
            error.setId(last3DigitsButOne);
            error.setCode(validError.getErrorCode());
            error.setMessage(validError.getErrorMessage());

            return error;
        }

        return null;

    }

    public static void handleDesiredExceptions(String input) {
        String inputLast3ButOne;
        int inputLength = input.length();
        if (inputLength >= 4) {
            int errorId = 0;
            inputLast3ButOne = input.substring(inputLength - 4,inputLength - 1);
            try {
                errorId = Integer.parseInt(inputLast3ButOne);
            } catch (NumberFormatException ex){
                // Do Nothing
            }

            if (errorId > 0)
                throwDesiredException(errorId);
        }
    }

    public static void handleDesiredExceptionsForCVV(String input) {
        String inputLast3;
        int inputLength = input.length();
        if (inputLength >= 3) {
            int errorId = 0;
            inputLast3 = input.substring(inputLength - 3,inputLength);
            try {
                errorId = Integer.parseInt(inputLast3);
            } catch (NumberFormatException ex){
                // Do Nothing
            }

            if (errorId > 0)
                throwDesiredException(errorId);
        }
    }

    public static void throwDesiredException(int errorId) {
        if (isServerFaultError(errorId)) {
            throw new ServerFaultException(errorId);
        } else if (isSOAPFaultError(errorId)) {
            throw new SOAPFaultException(getSoapFaultException(errorId));
        } else if (isClientFaultError(errorId)) {
            throw new ClientFaultException(errorId);
        }
    }

    public static SOAPFault getSoapFaultException(int errorId) {
        EWSError error = ErrorIdMap.getError(errorId);
        SOAPFault soapFault = null;
        try {
            QName faultCode = new QName(SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE, error.getErrorCode());
            soapFault = SOAPFactory.newInstance(SOAPConstants.SOAP_1_1_PROTOCOL).createFault(error.getErrorMessage(), faultCode);
        }
        catch (SOAPException e) {
            e.printStackTrace();
        }
        return soapFault;
    }

    public static boolean isServerFaultError(int errorId) {
        return errorId == 101 || errorId == 102 || errorId == 103 || errorId == 108;
    }

    public static boolean isSOAPFaultError(int errorId) {
        return errorId == 999;
    }

    public static boolean isClientFaultError(int errorId) {
        return ErrorIdMap.containsErrorId(errorId) && !isServerFaultError(errorId) && !isSOAPFaultError(errorId);
    }


    public static String convertPanToRegId(String regId){
        int lengthInput = regId.length();
        String middle = regId.substring(6, lengthInput - 4);
        StringBuilder lastFour = new StringBuilder(regId.substring(lengthInput - 4));
        StringBuilder firstSix = new StringBuilder(regId.substring(0,6));

        Long l = 999999999l;
        Long inputValue = Long.parseLong(middle);
        Long middleValue = (l-inputValue);

        StringBuilder middleSb = new StringBuilder(middleValue.toString());

        while(middleSb.length() < 9)
            middleSb.insert(0,"0");

        return firstSix.reverse().toString() + middleSb.toString() + lastFour.reverse().toString();
    }

    public static String convertRegIdToPAN(String regId){
        int lengthInput = regId.length();
        String middle = regId.substring(6, lengthInput - 4);
        StringBuilder lastFour = new StringBuilder(regId.substring(lengthInput - 4)).reverse();
        StringBuilder firstSix = new StringBuilder(regId.substring(0,6)).reverse();
        char firstDigit = firstSix.charAt(0);
        Long l = 999999999l;

        Long inputValue = Long.parseLong(middle);
        Long middleValue = (l-inputValue);

        StringBuilder middleSb = new StringBuilder(middleValue.toString());

        if (firstDigit == '3') {
            while (middleSb.length() < 5)
                middleSb.insert(0, "0");
        } else {
            while (middleSb.length() < 6)
                middleSb.insert(0, "0");
        }


        return firstSix.toString() + middleSb.toString() + lastFour.toString();
    }


    public static String getPANThroughRegId(String regId) {
        return convertRegIdToPAN(regId);
    }


    public static String getRegIdFromPAN(String pan) {
        return convertPanToRegId(pan);
    }


    public static String getRegIdFromToken(String token) {
        String pan = getPAN(token);
        return getRegIdFromPAN(pan);
    }

    public static String getCVVThroughToken(String token) {
        if(token.length() < 4) {
            return "566";
        }
        return token.substring(token.length() - 4, token.length() - 1);
    }


    public static AccountType getAccountType(String AccNum){
        int lastButOneDigit = Integer.parseInt(AccNum.substring(AccNum.length() - 2, AccNum.length() - 1));
        switch (lastButOneDigit % 4) {
            case 0:
                return AccountType.CHECKING;
            case 1:
                return AccountType.SAVINGS;
            case 2:
                return AccountType.CORPORATE_CHECKING;
            default:
                return AccountType.CORPORATE_SAVINGS;
        }
    }

    public static String getRoutingNumber(String AccNum) {
        if(AccNum.length() < 9)
            return "123456789";
        return AccNum.substring(0, 9);
    }


    public static String decrypt(String input) {
        StringBuilder sb = new StringBuilder(input);
        return sb.reverse().toString();
    }

    //NICK's code
    public static boolean checkNewlyGenerated(String pan) {
        int length = pan.length();
        return (length >= 4 && pan.substring(length - 4, length - 1).matches("0\\d0"));
    }

    public static String getExpDate() {
        return "5001";
    }

    public static int getIndicator(String regId) {
        return Integer.parseInt(regId.charAt(regId.length() - 2) + "");
    }
    
    public static String getECI(int indicator) {
        switch (indicator) {
            case 1:
            case 3:
                return "07";
            case 2:
                return "05";
            default:
                return null;

        }
    }

    public static WalletType getWalletType(int indicator) {
        switch (indicator) {
            case 1:
                return WalletType.ANDROID;
            case 2:
                return WalletType.APPLE;
            case 3:
                return WalletType.SAMSUNG;
            default:
                return null;
        }
    }

    public static String getOrderLVT(String cvv) {
        String orderLVT = "3";

        //Generates the OrderLVT by repeating cvv until its at least 18 characters
        while(orderLVT.length() < 18) {
            if (!(cvv.equals(""))) {
                orderLVT += cvv;
            } else {
                orderLVT += "00000000000000000";
            }
        }

        return orderLVT.substring(0,18);
    }
}
