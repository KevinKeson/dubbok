package org.kevink.dubbok.demo.impl;

import org.kevink.dubbok.demo.Service;

public class ServiceImpl implements Service {

    @Override
    public String hello(String name) {
        return "Hello, I am " + name + "!";
    }

}
