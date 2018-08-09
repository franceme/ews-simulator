package ewsSimulator.ws.validator;

import java.text.NumberFormat;
import java.text.ParsePosition;

public class ValidatorUtils {

    public static final int PAN_LENGTH_1 = 13;
    public static final int PAN_LENGTH_2 = 19;
    public static final int CVV_LENGTH_1 = 3;
    public static final int CVV_LENGTH_2 = 4;
    public static final int TOKEN_LENGTH = 50;
    public static final int ROLLUP_ID_LENGTH = 6;

    public static boolean isStringEmpty(String value){
        if(value == null || value.isEmpty() || value.equalsIgnoreCase("?"))
            return true;
        /*if((value.equalsIgnoreCase(" ")
                || value.equalsIgnoreCase("")
                || value.equalsIgnoreCase("?"))
                && (value.length() == 1
        || value.length() == 0))
            return true;*/
        return false;
    }

    public static boolean isStringValidInteger(String str)
    {
        if(isStringEmpty(str))
            return false;

        NumberFormat formatter = NumberFormat.getInstance();
        ParsePosition pos = new ParsePosition(0);
        formatter.parse(str, pos);
        return str.length() == pos.getIndex();
    }

    public  static boolean isValidPAN(String PAN){
        if(isStringValidInteger(PAN))
            if(PAN.length() == PAN_LENGTH_1 || PAN.length() == PAN_LENGTH_2)
            return true;
        return false;
    }

    public  static boolean isValidRollupId(String rollupId){
        if(isStringValidInteger(rollupId) && rollupId.length() <= ROLLUP_ID_LENGTH)
            return true;
        return false;
    }

    public static boolean isValidCVV(String cvv){
        if(isStringValidInteger(cvv))
            if(cvv.length() == CVV_LENGTH_1 || cvv.length() == CVV_LENGTH_2)
                return true;
        return false;
    }

    public static boolean isValidToken(String token){
        if(!isStringEmpty(token) && token.length() <= TOKEN_LENGTH)
            return true;
        return false;
    }

}
