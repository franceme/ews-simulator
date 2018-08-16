package com.worldpay.simulator;

import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;

import com.worldpay.simulator.utils.HttpHeaderUtils;

@ContextConfiguration
public class SpringTestConfig {

    @Bean(name = "testHttpHeaders")
    public HttpHeaderUtils httpHeaderUtils(){
        HttpHeaderUtils httpHeaderUtils = new HttpHeaderUtils();
        return httpHeaderUtils;
    }

    @Bean(name = "testEWSSimulator")
    public EWSSimulatorEndpoint eWSSimulatorEndpoint(){
        EWSSimulatorEndpoint eWSSimulatorEndpoint = new EWSSimulatorEndpoint();
        return eWSSimulatorEndpoint;
    }

    @Bean(name = "testFilterConfig")
    public TestFilterConfiguration testFilterConfiguration() {
        TestFilterConfiguration testFilterConfiguration = new TestFilterConfiguration();
        return testFilterConfiguration;
    }
}
