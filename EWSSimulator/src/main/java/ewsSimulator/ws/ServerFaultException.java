package ewsSimulator.ws;

public class ServerFaultException extends RuntimeException {

    private ServerFault serverFault;
    private static final String FAULT_STRING = "Server Fault Exception";

    public ServerFaultException(int errorId) {
        super(FAULT_STRING);
        serverFault = new ServerFault();
        EWSError error = ErrorIdMap.getError(errorId);
        serverFault.setId(errorId);
        serverFault.setCode(error.getErrorCode());
        serverFault.setMessage(error.getErrorMessage());
    }

    public ServerFault getServerFault() {
        return serverFault;
    }

    public void setServerFault(ServerFault serverFault) {
        this.serverFault = serverFault;
    }
}
