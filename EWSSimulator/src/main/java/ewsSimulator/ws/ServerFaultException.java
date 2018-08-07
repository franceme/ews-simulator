package ewsSimulator.ws;

public class ServerFaultException extends RuntimeException {

    private ServerFault serverFault;

    public ServerFaultException(String message, ServerFault serverFault) {
        super(message);
        this.serverFault = serverFault;
    }

    public ServerFaultException(String message, Throwable e, ServerFault  serverFault) {
        super(message, e);
        this.serverFault = serverFault;
    }

    public ServerFault getServerFault() {
        return serverFault;
    }

    public void setServerFault(ServerFault serverFault) {
        this.serverFault = serverFault;
    }
}
