package com.ofwiki.test;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Proxy;

/**
 * @author HuangJS
 * @date 2020-08-08 3:44 下午
 */
public class FactoryBeanJdkProxyTest {
    public static void main(String[] argss) throws Exception {
        MyFactoryBean factoryBean = new MyFactoryBean();

        //System.out.println(MyFactoryBean.class.isInterface());
        //System.out.println(FactoryBean.class.isInterface());
        //
        FactoryBean bean = (FactoryBean) Proxy.newProxyInstance(
                factoryBean.getClass().getClassLoader(), new Class<?>[]{FactoryBean.class},
                (proxy, method, args) -> {
                    if (method.getName().equals("getObject") && args == null) {
                        return factoryBean;
                    }
                    return ReflectionUtils.invokeMethod(method, factoryBean, args);
                });

        System.out.println(bean.getObject());
        System.out.println(bean.getObjectType());

    }

    public static class MyFactoryBean implements FactoryBean {
        @Override
        public Object getObject() throws Exception {
            return "this is object text";
        }

        @Override
        public Class<?> getObjectType() {
            return String.class;
        }

    }
}
