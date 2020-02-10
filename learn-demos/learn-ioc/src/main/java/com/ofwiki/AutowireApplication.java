package com.ofwiki;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.Assert;

/**
 * 基于xml配置文件自动装配示例
 * @author HuangJS
 * @date 2020-01-10 4:16 下午
 */
public class AutowireApplication {
    public static void main(String[] args) {
        new ClassPathXmlApplicationContext("spring-autowire.xml")
                .getBean(UserService.class).save();
    }

    public static class UserService {
        private UserDao dbDao;
        public void setDbDao(UserDao dbDao) {
            this.dbDao = dbDao;
        }
        public void save() {
            Assert.notNull(dbDao, "dbDao field is null");
            System.out.println("UserService:save user info");
            dbDao.save();
        }
    }
    public static class UserDao {
        public void save() {
            System.out.println("UserDao:save user info");
        }
    }
    //
    // 以下为 spring-autowire.xml 文件内容
    //
    //<?xml version="1.0" encoding="UTF-8"?>
    //<beans xmlns="http://www.springframework.org/schema/beans"
    //    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    //    xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd"
    //    default-autowire="byType" >
    //    <!--
    //    在beans节点通过 default-autowire="byType" 开启自动装配，且指定全局默认按类型自动装配
    //    也可以在bean节点通过 autowire 指定，bean的装配方式高于beans
    //
    //    装配装配方式：
    //    no: 无需自动装配
    //    byName: 按属性名称自动属性。Spring会找与属性同名的bean，然后通过set方法注入
    //    byType: 按类型自动装配属性。Spring会找该属性类型的bean，然后通过set方法注入
    //    constructor: 类似于byType,但只适用于构造函数参数。如果容器中不存在造函数参数类型的bean会引发异常
    //
    //    参考文件：https://docs.spring.io/spring/docs/5.2.2.RELEASE/spring-framework-reference/core.html#beans-factory-autowire
    //            -->
    //
    //    <bean class="com.ofwiki.AutowireApplication.UserService" primary="true" autowire="byName" />
    //    <bean id="userService1" class="com.ofwiki.AutowireApplication.UserService"  />
    //    <bean id="dbDao" class="com.ofwiki.AutowireApplication.UserDao" />
    //</beans>
}
