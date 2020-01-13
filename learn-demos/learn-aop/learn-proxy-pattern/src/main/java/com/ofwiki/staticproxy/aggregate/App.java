package com.ofwiki.staticproxy.aggregate;

/**
 * @author HuangJS
 * @date 2019-12-31 5:50 下午
 */
public class App {
    public static void main(String[] args) {
        UserDao target = new UserDaoImpl();
        // 目标对象作为代理对象的属性存在，相对继承代理代理顺序更灵活，不存在链路继承产生更多类的问题
        //UserDao verifyProxy = new UserDaoVerifyProxy(new UserDaoLogProxy(target));
        UserDao verifyProxy = new UserDaoLogProxy(new UserDaoVerifyProxy(target));
        verifyProxy.save("lishi");
    }
}
