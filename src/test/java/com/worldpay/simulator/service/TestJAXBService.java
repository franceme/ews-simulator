package com.worldpay.simulator.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import static junit.framework.TestCase.assertEquals;

import com.worldpay.simulator.RequestValidationFault;
import com.worldpay.simulator.SecurityHeaderType;
import com.worldpay.simulator.ServerFault;
import com.worldpay.simulator.SpringTestConfig;
import com.worldpay.simulator.service.JAXBService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringTestConfig.class)
public class TestJAXBService {

    @Autowired
    JAXBService jaxbService;

    @Before
    public void setUp(){
        jaxbService = new JAXBService();
    }

    @Test
    public void testgetJAXBContext() throws JAXBException {
        JAXBContext expectedContext = JAXBContext.newInstance(SecurityHeaderType.class, RequestValidationFault.class, ServerFault.class);

        JAXBContext context = jaxbService.getContext();
        assertEquals(expectedContext.toString(), context.toString());
    }

}
