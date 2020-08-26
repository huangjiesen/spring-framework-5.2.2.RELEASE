package com.ofwiki.determine.candidate.constructors;

import org.springframework.stereotype.Service;

/**
 * @author HuangJS
 * @date 2020-08-26 8:46 下午
 */
@Service
public class UserService {
    private UserDao size;


    public UserService(UserDao beanA) {
        this.size = beanA;
    }


    public void save() {
        System.out.println("save user info");
    }

}
