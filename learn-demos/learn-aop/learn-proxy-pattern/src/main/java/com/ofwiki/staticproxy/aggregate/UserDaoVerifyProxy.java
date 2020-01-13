package com.ofwiki.staticproxy.aggregate;

/**
 * @author HuangJS
 * @date 2019-12-31 5:49 下午
 */
public class UserDaoVerifyProxy implements UserDao {
    UserDao target;

    public UserDaoVerifyProxy(UserDao target) {
        this.target = target;
    }

    @Override
    public void save(String user) {
        System.out.println(this.getClass().getSimpleName()+": verify logic");
        target.save(user);
    }
}
