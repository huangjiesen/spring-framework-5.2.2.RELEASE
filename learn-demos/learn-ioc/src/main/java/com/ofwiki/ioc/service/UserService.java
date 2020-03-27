package com.ofwiki.ioc.service;

/**
 * @author HuangJS
 * @date 2020-03-24 4:34 下午
 */
//@Lazy(false)
//@Component
public class UserService {
    private int ss;

    public UserService() {
        super();
    }

    public UserService(int ss) {
        this.ss = ss;
    }

    public void sss() {

        System.out.println(ss);
    }
}
