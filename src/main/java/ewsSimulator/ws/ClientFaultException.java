package ewsSimulator.ws;

public class ClientFaultException extends RuntimeException{

    private static final String FAULT_STRING = "Client Fault Exception";
    private RequestValidationFault requestValidationFault;

    public ClientFaultException(int errorId) {
        super(FAULT_STRING);
        if (!EWSUtils.isClientFaultError(errorId)) {
            throw new ServerFaultException(3);
        }
        EWSError error = ErrorIdMap.getError(errorId);
        initRequestValidationfault(errorId, error, error.getErrorMessage());
    }

    public ClientFaultException(int errorId, String errorMessage) {
        super(FAULT_STRING);
        if (!EWSUtils.isClientFaultError(errorId)) {
            throw new ServerFaultException(3);
        }
        EWSError error = ErrorIdMap.getError(errorId);
        initRequestValidationfault(errorId, error, errorMessage);
    }

    public void initRequestValidationfault(int errorId, EWSError error, String errorMessage) {
        requestValidationFault = new RequestValidationFault();
        requestValidationFault.setId(errorId);
        requestValidationFault.setCode(error.getErrorCode());
        requestValidationFault.setMessage(errorMessage);
    }

    public RequestValidationFault getRequestValidationFault() {
        return requestValidationFault;
    }

    public void setRequestValidationFault(RequestValidationFault requestValidationFault) {
        this.requestValidationFault = requestValidationFault;
    }
}
