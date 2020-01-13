package com.ofwiki.mapper;

/**
 * sql session 模拟类
 *
 * @author HuangJS
 * @date 2019-08-30 10:56 AM
 */
public class SqlSession {
    public Object getConnection() {
        System.out.println("get sql session connection");
        return null;
    }

    public void closeConnection() {
        System.out.println("close sql session connection");
    }
}
