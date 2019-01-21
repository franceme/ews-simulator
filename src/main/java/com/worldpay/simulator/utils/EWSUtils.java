package com.worldpay.simulator.utils;


import static com.worldpay.simulator.validator.ValidatorUtils.*;
import static com.worldpay.simulator.validator.ValidatorUtils.isValidPAN;


import java.util.Random;
import java.util.UUID;

import com.worldpay.simulator.AccountType;
import com.worldpay.simulator.errors.EWSError;
import com.worldpay.simulator.errors.ErrorIdMap;
import com.worldpay.simulator.VError;
import com.worldpay.simulator.exceptions.ClientFaultException;
import com.worldpay.simulator.exceptions.ServerFaultException;

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
        int inputLength = input.length();

        Long l = 99999999999999999l;
        Long inputValue = Long.parseLong(input);
        Long middleValue = (l-inputValue);

        StringBuilder middle = new StringBuilder(middleValue.toString());

        while(middle.length() < 17)
            middle.insert(0,"0");

        return middle.toString();
    }

    public static String generateEcheckAccount(String input){
        if(input.length() != 19 && !input.startsWith("2"))
            throw new ClientFaultException(6,"Token Not Found");

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
        } else if (isClientFaultError(errorId)) {
            throw new ClientFaultException(errorId);
        }
    }

    public static boolean isServerFaultError(int errorId) {
        return errorId == 101 || errorId == 102 || errorId == 103 || errorId == 108;
    }

    public static boolean isClientFaultError(int errorId) {
        return ErrorIdMap.containsErrorId(errorId) && !isServerFaultError(errorId);
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
        StringBuilder lastFour = new StringBuilder(regId.substring(lengthInput - 4));
        StringBuilder firstSix = new StringBuilder(regId.substring(0,6));

        Long l = 999999999l;
        Long inputValue = Long.parseLong(middle);
        Long middleValue = (l-inputValue);

        StringBuilder middleSb = new StringBuilder(middleValue.toString());

        while(middleSb.length() < 6)
            middleSb.insert(0,"0");


        return firstSix.reverse().toString() + middleSb.toString() + lastFour.reverse().toString();
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
        if(token.length() < 4){
            return "566";
        }
        return token.substring(token.length() - 4, token.length() - 1);
    }


    public static AccountType getAccountType(String AccNum){
        int lastDigit = Integer.parseInt(AccNum.substring(AccNum.length()-2,AccNum.length() - 1));
        switch (lastDigit%4){
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
}
