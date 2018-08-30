package com.worldpay.simulator;

import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

@Service
public class JAXBService {

    private static final JAXBContext context;

    static {
        try {
            context = JAXBContext.newInstance(SecurityHeaderType.class, RequestValidationFault.class, ServerFault.class);
        } catch (JAXBException e) {
            throw new Error("Fatal Error: Cannot create JAXBContext");
        }
    }

    public JAXBContext getContext(){
        return context;
    }
}
