package ewsSimulator.ws;

public class EWSError {

    private String errorCode;
    private String errorMessage;

    public EWSError(String errCode, String errMessage) {
        errorCode = errCode;
        errorMessage = errMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
