package com.worldpay.simulator.validator;

import com.worldpay.simulator.exceptions.ClientFaultException;

import java.text.NumberFormat;
import java.text.ParsePosition;

public class ValidatorUtils {

    public static final int PAN_LENGTH_1 = 13;
    public static final int PAN_LENGTH_2 = 19;
    public static final int CVV_LENGTH_1 = 3;
    public static final int CVV_LENGTH_2 = 4;
    public static final int REG_LENGTH_1 = 1;
    public static final int REG_LENGTH_2 = 19;
    public static final int ROUTING_LENGTH = 9;
    public static final int ACC_LENGTH_1 = 4;
    public static final int ACC_LENGTH_2 = 17;
    public static final int TOKEN_LENGTH = 50;
    public static final int ROLLUP_ID_LENGTH = 6;
    public static final int ORDER_LVT_LENGTH = 18;

    public static boolean isStringEmpty(String value) {
        return (value == null || value.isEmpty() || value.equalsIgnoreCase("?"));
    }

    public static boolean isStringValidInteger(String str) {
        if (isStringEmpty(str))
            return false;

        NumberFormat formatter = NumberFormat.getInstance();
        ParsePosition pos = new ParsePosition(0);
        formatter.parse(str, pos);
        return str.length() == pos.getIndex();
    }

    public static boolean isValidPAN(String PAN) {
        return (isStringValidInteger(PAN) && PAN_LENGTH_1 <= PAN.length() && PAN.length() <= PAN_LENGTH_2);
    }

    public static boolean isValidRegId(String regId) {
        return (isStringValidInteger(regId) && REG_LENGTH_1 <= regId.length() && regId.length() <= REG_LENGTH_2);
    }

    public static boolean isValidRollupId(String rollupId) {
        return (isStringValidInteger(rollupId) && rollupId.length() <= ROLLUP_ID_LENGTH);
    }

    public static boolean isValidCVV(String cvv) {
        return (isStringValidInteger(cvv) && (cvv.length() == CVV_LENGTH_1 || cvv.length() == CVV_LENGTH_2));
    }

    public static boolean isValidOrderLVT(String LVT) {
        return (isStringValidInteger(LVT) && LVT.length() == ORDER_LVT_LENGTH && LVT.startsWith("3"));
    }

    public static boolean isValidToken(String token) {
        return (!isStringEmpty(token) && token.length() <= TOKEN_LENGTH);
    }

    public static boolean isValidAccount(String account) {
        return (!isStringEmpty(account) && (ACC_LENGTH_1 <= account.length() && account.length() <= ACC_LENGTH_2));
    }

    public static boolean isValidRoutingNumber(String routingNum) {
        return (!isStringEmpty(routingNum) && routingNum.length() == ROUTING_LENGTH);
    }

    public static boolean isValidMerchantRefId(String merchantRefId) {
        return (merchantRefId == null || merchantRefId.length() > 16 || isStringEmpty(merchantRefId));
    }

    public static void handleException(int ERR_ID, String ERR_MSG) {
        throw new ClientFaultException(ERR_ID, ERR_MSG);
    }

}
