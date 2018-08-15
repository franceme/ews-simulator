package com.worldpay.simulator.utils;

import com.worldpay.simulator.EWSSimulatorEndpoint;
import com.worldpay.simulator.utils.HttpHeaderUtils;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.worldpay.simulator.validator.ValidateAndSimulate;
import static org.powermock.api.mockito.PowerMockito.*;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.ws.soap.SoapHeaderElement;
import java.util.List;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ValidateAndSimulate.class, HttpHeaderUtils.class})
public class TestHttpHeaderUtils {

    @Before
    public void setup() {
    }


    @Test
    public void testCustomizeHttpResponseHeader() {



    }
}
