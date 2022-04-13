package org.kevink;

public class ServiceImpl implements Service {

    @Override
    public String hello(String name) {
        return "Hello, I am " + name + "!";
    }

}
