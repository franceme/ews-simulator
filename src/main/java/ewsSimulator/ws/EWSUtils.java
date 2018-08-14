package ewsSimulator.ws;

import static java.lang.Thread.sleep;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.springframework.ws.soap.SoapHeaderElement;
import org.springframework.ws.transport.context.TransportContext;
import org.springframework.ws.transport.context.TransportContextHolder;
import org.springframework.ws.transport.http.HttpServletConnection;
import org.w3c.dom.Node;

import com.sun.org.apache.xerces.internal.dom.ElementNSImpl;

public class EWSUtils {

    public static String defaultCorrelationId = "64f231d1-e122-4693-af76-5652d4e37441";
    public static String acceptInput = "gzip,deflate";
    public static String correlationHeader = "v_CorrelationId";
    public static String acceptHeader = "Accept-Encoding";

    public static String randomReqId() {
        return UUID.randomUUID().toString();
    }

    public static String getRegId(String primaryAccountNumber) {
        int lengthInput = primaryAccountNumber.length();

        if(lengthInput > 3) {
            StringBuilder sb = new StringBuilder(primaryAccountNumber.substring(0, lengthInput - 4));
            StringBuilder lastFour = new StringBuilder(primaryAccountNumber.substring(lengthInput - 4));
            return sb.append(lastFour.reverse().toString()).toString();
        } else {
            return primaryAccountNumber;
        }
    }

    public static void customizeHttpResponseHeader(){
        String headerValue = getHttpHeaderValue(correlationHeader);

        if(headerValue == null) {
            addResponseHttpHeader(correlationHeader,defaultCorrelationId);
        } else {
            addResponseHttpHeader(correlationHeader,headerValue);
        }
        setResponseHttpHeaderValue(acceptHeader,acceptInput);
    }

    public static String generateProperty(String input) {

        int lengthInput = input.length();

        try{
            Long temp = Long.parseLong(input);
        }catch (Exception e){
            throw new NumberFormatException();
        }

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
        if(primaryAccountNumber != null) {
            int lengthPAM = primaryAccountNumber.length();

            if (lengthPAM > 2 && primaryAccountNumber.substring(0, 2).equals("00")) {
                int time = Integer.parseInt(primaryAccountNumber.substring(2, 3));
                sleep(time * 1000);
            }
        }
    }

    private static HttpServletRequest getHttpServletRequest() {
        TransportContext ctx = TransportContextHolder.getTransportContext();
        return ( null != ctx ) ? ((HttpServletConnection) ctx.getConnection()).getHttpServletRequest() : null;
    }

    private static String getHttpHeaderValue( String headerName ) {
        HttpServletRequest httpServletRequest = getHttpServletRequest();
        return ( null != httpServletRequest ) ? httpServletRequest.getHeader( headerName ) : null;
    }

    private static HttpServletResponse getHttpServletResponse() {
        TransportContext ctx = TransportContextHolder.getTransportContext();
        return ( null != ctx ) ? ((HttpServletConnection) ctx.getConnection()).getHttpServletResponse() : null;
    }

    private static void addResponseHttpHeader(String headerName,String headerValue) {

        HttpServletResponse httpServletResponse = getHttpServletResponse();
        httpServletResponse.addHeader(headerName, headerValue);
    }

    private static void setResponseHttpHeaderValue(String headerName,String headerValue) {

        HttpServletResponse httpServletResponse = getHttpServletResponse();
        httpServletResponse.setHeader(headerName, headerValue);

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

        if(cvv.length() == 1 && (cvv.charAt(0) == '?' || cvv.charAt(0) == ' ') )
            return true;

        if(cvv.equalsIgnoreCase(""))
            return true;

        return false;
    }

    public static boolean isSecurityCodeValid(String cvv){
        if(cvv == null || cvv.length() < 3)
            return false;

        if(cvv.charAt(0) == '?' || cvv.charAt(0) == ' ')
            return true;



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
        int lengthInput = regId.length();

        if(lengthInput > 3) {
            StringBuilder sb = new StringBuilder(regId.substring(0, lengthInput - 4));
            StringBuilder lastFour = new StringBuilder(regId.substring(lengthInput - 4));
            return sb.append(lastFour.reverse().toString()).toString();
        } else {
            return regId;
        }
    }

    public static String getCVVThroughToken(String token) {
        return token.substring(token.length() - 3, token.length());
    }


}
