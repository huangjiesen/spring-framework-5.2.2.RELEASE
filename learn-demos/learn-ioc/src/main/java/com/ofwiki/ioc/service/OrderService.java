package com.ofwiki.ioc.service;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * @author HuangJS
 * @date 2020-03-24 4:35 下午
 */
public class OrderService {
    public void sayHi(){
        System.out.println("hello order");
    }

}
