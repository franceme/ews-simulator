package com.worldpay.simulator;

import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;

import com.worldpay.simulator.utils.HttpHeaderUtils;
import com.worldpay.simulator.validator.RequestValidator;
import com.worldpay.simulator.validator.ValidatorService;

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
    public FilterConfiguration filterConfiguration() {
        FilterConfiguration testFilterConfiguration = new FilterConfiguration();
        return testFilterConfiguration;
    }

    @Bean(name = "testValidatorService")
    public ValidatorService validatorService() {
        ValidatorService validatorService = new ValidatorService();
        return validatorService;
    }

    @Bean(name = "testRequestValidator")
    public RequestValidator requestValidator() {
        RequestValidator requestValidator = new RequestValidator();
        return requestValidator;
    }
}
