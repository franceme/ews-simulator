package com.worldpay.simulator.exceptions;

import com.worldpay.simulator.RequestValidationFault;
import com.worldpay.simulator.ServerFault;
import com.worldpay.simulator.utils.HttpHeaderUtils;
import com.worldpay.simulator.service.ValidatorService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ValidatorService.class, HttpHeaderUtils.class})
public class TestExceptions {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    private ClientFaultException clientFaultException;
    private SecurityErrorException securityErrorException;
    private ServerFaultException serverFaultException;

    private int clientError;
    private int serverError;
    private String errorMessage;

    @Before
    public void setUp() {

        errorMessage = "error";
        clientError = 104;
        serverError = 101;
    }

    @Test
    public void testConstructors(){
        clientFaultException = new ClientFaultException(clientError);
        assertNotNull(clientFaultException);

        clientFaultException = new ClientFaultException(clientError,errorMessage);
        assertNotNull(clientFaultException);

        securityErrorException = new SecurityErrorException(errorMessage);
        assertNotNull(securityErrorException);

        exception.expect(ServerFaultException.class);
        clientFaultException = new ClientFaultException(serverError);

    }

    @Test
    public void testServerFaultException(){
        exception.expect(ServerFaultException.class);
        clientFaultException = new ClientFaultException(serverError,errorMessage);
    }

    @Test
    public void testGetRequestValidationFault(){
        clientFaultException = new ClientFaultException(clientError);
        assertNotNull(clientFaultException.getRequestValidationFault());

        RequestValidationFault fault = new RequestValidationFault();
        clientFaultException.setRequestValidationFault(fault);
    }

    @Test
    public void testGetServerFault(){
        serverFaultException = new ServerFaultException(serverError);
        ServerFault fault = new ServerFault();
        assertNotNull(serverFaultException.getServerFault());

        serverFaultException.setServerFault(fault);
        assertNotNull(serverFaultException.getServerFault());
    }
}
