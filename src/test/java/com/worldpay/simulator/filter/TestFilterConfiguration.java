package com.worldpay.simulator.filter;


import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.worldpay.simulator.SpringTestConfig;
import com.worldpay.simulator.filter.FilterConfiguration;
import com.worldpay.simulator.filter.RequestLogFilter;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringTestConfig.class)
public class TestFilterConfiguration {

    @Spy
    FilterConfiguration filterConfigurationSpy;

    @Mock
    RequestLogFilter requestLogFilterMock;

    @Test
    public void testRequestLogFilter() {

        willReturn(requestLogFilterMock).given(filterConfigurationSpy).loggingFilterInstance();
        willDoNothing().given(requestLogFilterMock).setIncludeClientInfo(true);
        willDoNothing().given(requestLogFilterMock).setIncludeQueryString(true);
        willDoNothing().given(requestLogFilterMock).setIncludeHeaders(true);

        filterConfigurationSpy.requestLoggingFilter();

        verify(filterConfigurationSpy, times(1)).loggingFilterInstance();
        verify(requestLogFilterMock, times(1)).setIncludeClientInfo(true);
        verify(requestLogFilterMock, times(1)).setIncludeQueryString(true);
        verify(requestLogFilterMock, times(1)).setIncludeHeaders(true);
    }

    @Test
    public void testLoggingFilterInstance() {
        filterConfigurationSpy.loggingFilterInstance();
    }

}

//
//    RequestLogFilter loggingFilter = loggingFilter();
//        loggingFilter.setIncludeClientInfo(true);
//        loggingFilter.setIncludeQueryString(true);
//        loggingFilter.setIncludeHeaders(true);
//        return loggingFilter;
//}
