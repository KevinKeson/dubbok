package org.kevink.dubbok.demo.service.impl;

import org.kevink.dubbok.demo.service.Service;

public class ServiceImpl implements Service {

    @Override
    public String hello(String name) {
        return "Hello, I am " + name + "!";
    }

}
