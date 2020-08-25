package com.ofwiki.test.aspect.service;

import org.springframework.stereotype.Service;

/**
 * @author HuangJS
 * @date 2020-08-25 10:09 上午
 */
@Service
public class UserService {

    public void save() {
        System.out.println("user service save method...");
    }
}
