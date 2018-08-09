package ewsSimulator.ws;

import static java.lang.Thread.sleep;

import java.util.Random;
import java.util.UUID;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.springframework.ws.soap.SoapHeaderElement;

import com.sun.org.apache.xerces.internal.dom.ElementNSImpl;

public class EWSUtils {

    public static String randomReqId() {
        return UUID.randomUUID().toString();
    }

    public static String getRegId(String primaryAccountNumber) {

        return Long.toString(Long.parseLong(primaryAccountNumber) + 99999);
    }

    public static String generateProperty(String input) {

        int lengthInput = input.length();

        if(lengthInput > 3) {
            StringBuffer sb = new StringBuffer(input.substring(0, lengthInput - 4));
            String lastFour = input.substring(lengthInput - 4);
            return sb.reverse().append(lastFour).toString();
        } else {
            return input;
        }
    }

    public static String getToken(String primaryAccountNumber) {

        return generateProperty(primaryAccountNumber);

    }

    public static String getPAN(String token) {

        try {
            return generateProperty(token);
        } catch(NumberFormatException ex) {
            return "3000100011118566";
        }
    }



    public static void delayInResponse(String primaryAccountNumber) throws InterruptedException {
        int lengthPAM = primaryAccountNumber.length();

        if(lengthPAM > 2 && primaryAccountNumber.substring(0, 2).equals("00")) {
            int time = Integer.parseInt(primaryAccountNumber.substring(2,3));
            sleep(time * 1000);
        }
    }

    public static void getAuthentication(SoapHeaderElement header){
        try {

            JAXBContext context = JAXBContext.newInstance(SecurityHeaderType.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            JAXBElement<SecurityHeaderType> root = unmarshaller.unmarshal(header.getSource(), SecurityHeaderType.class);

            ElementNSImpl checkwhatever = (ElementNSImpl)root.getValue().getAny().get(0);
            String localName = checkwhatever.getFirstChild().getNextSibling().getLocalName();

            String hi = "";

        } catch (JAXBException e) {
            e.printStackTrace();
        }
        //return authentication;
    }

    public static String generateRandomNumber(int length){
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(length);
        for(int i=0;i<length;i++)
            sb.append((char)('0'+rnd.nextInt(10)));
        return sb.toString();
    }


    public static boolean isSecurityCodeEmpty(String cvv){

        if(cvv == null)
            return true;

        if((cvv.charAt(0) == '?' || cvv.charAt(0) == ' ') && cvv.length() == 1)
            return true;

        if(cvv.equalsIgnoreCase(""))
            return true;

        return false;
    }

    public static boolean isSecurityCodeValid(String cvv){

        if(cvv.charAt(0) == '?' || cvv.charAt(0) == ' ')
            return true;

        if(cvv.length() < 3)
            return false;

        return true;
    }

    public static void handleDesiredExceptions(String input) {
        String inputLast3;
        if (input.length() >= 3) {
            int errorId = 0;
            inputLast3 = input.substring(input.length() - 3);
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
        return errorId == 1 || errorId == 2 || errorId == 3 || errorId == 8;
    }

    public static boolean isClientFaultError(int errorId) {
        return ErrorIdMap.containsErrorId(errorId) && !isServerFaultError(errorId);
    }


    public static String getPANThroughRegId(String regId) {
        return Long.toString(Long.parseLong(regId) - 99999);
    }

    public static String getCVVThroughToken(String token) {
        return token.substring(token.length() - 3, token.length());
    }

    public static int getWallet(String CVV){
        return Integer.parseInt(CVV.charAt(2)+"");
    }

}
