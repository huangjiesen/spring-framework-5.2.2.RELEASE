package com.ofwiki.staticproxy.extend;

/**
 * @author HuangJS
 * @date 2019-12-31 5:13 下午
 */
public class UserDaoLogProxy extends UserDao {
    @Override
    public void save(String user) {
        System.out.println("com.sen.sp.DogProxy: dog proxy logic");
        super.save(user);
    }


}
