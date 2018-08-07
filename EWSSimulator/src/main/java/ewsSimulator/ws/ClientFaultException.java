package ewsSimulator.ws;

public class ClientFaultException extends RuntimeException{

    private RequestValidationFault requestValidationFault;

    public ClientFaultException(String message, RequestValidationFault requestValidationFault) {
        super(message);
        this.requestValidationFault = requestValidationFault;
    }

    public ClientFaultException(String message, Throwable e, RequestValidationFault requestValidationFault) {
        super(message, e);
        this.requestValidationFault = requestValidationFault;
    }

    public RequestValidationFault getRequestValidationFault() {
        return requestValidationFault;
    }

    public void setRequestValidationFault(RequestValidationFault requestValidationFault) {
        this.requestValidationFault = requestValidationFault;
    }
}
