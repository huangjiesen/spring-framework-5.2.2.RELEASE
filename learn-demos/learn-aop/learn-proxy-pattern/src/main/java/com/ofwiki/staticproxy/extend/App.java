package com.ofwiki.staticproxy.extend;

/**
 * @author HuangJS
 * @date 2019-12-31 5:16 下午
 */
public class App {
    public static void main(String[] args) {
        //
        // 直接new做了log记录的代理对象
        // 缺点是再次代理扩展时会更多的类。如对UserDao添加权限控制时，添加一个UserDaoVerifyProxy类并且要继承UserDaoLogProxy类
        // 如果要验证权限再记录log，做成链路继承，则要将UserDaoLogProxy改为继承UserDaoVerifyProxy，会产生更多的类或改动
        //
        UserDao dog = new UserDaoLogProxy();
        dog.save("zhange shan");
    }
}
