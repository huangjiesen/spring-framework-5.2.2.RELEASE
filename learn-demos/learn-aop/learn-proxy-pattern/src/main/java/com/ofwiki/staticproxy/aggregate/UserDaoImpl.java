package com.ofwiki.staticproxy.aggregate;

/**
 * @author HuangJS
 * @date 2019-12-31 5:39 下午
 */
public class UserDaoImpl implements UserDao {

    @Override
    public void save(String user) {
        System.out.println("save user info:" + user);
    }
}
