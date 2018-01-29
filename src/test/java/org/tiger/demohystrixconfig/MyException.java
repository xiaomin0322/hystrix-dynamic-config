package org.tiger.demohystrixconfig;

/**
 * PACKAGE_NAME: com.netflix.hystrix.strategy.exception
 * USER :  Administrator
 * DATE :  2018/1/29
 */

public class MyException extends Exception {

    public String config = Configer.val;

}