package com.ofwiki.dynamicproxy;

import java.lang.reflect.Method;

/**
 * @author HuangJS
 * @date 2020-01-03 12:31 下午
 */
public class ProxyDemo {
    public interface Animal {
        String eat(String food);
    }

    public static class Hunter {
        public boolean fire(){
            System.out.println("Hunter fire");
            return false;
        }
    }

    public static void main(String[] args) throws Exception {
        // 代理接口示例
        Animal animalProxy = (Animal) ProxyUtil.newProxyInstance(ProxyUtil.class.getClassLoader(), Animal.class, (proxy, method, args1) -> {
            Class<?> declaringClass = method.getDeclaringClass();
            if (Object.class.equals(declaringClass)) {
                try {
                    return method.invoke(proxy, args1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            System.out.println("proxy interface logs --------------------");
            return method.getName() + "," + args1[0];
        });
        System.out.println("animalProxy result:"+animalProxy.eat("cookie"));


        //代理对象示例，需要对象传到InvocationHandler中，如以下示例，如可通过InvocationHandler的构造方法传入
        Hunter hunterProxy = (Hunter) ProxyUtil.newProxyInstance(ProxyUtil.class.getClassLoader(), Hunter.class, new ProxyUtil.InvocationHandler() {
            Hunter hunter = new Hunter();
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) {
                try {
                    Class<?> declaringClass = method.getDeclaringClass();
                    if (Object.class.equals(declaringClass)) {
                        return method.invoke(proxy, args);
                    }

                    System.out.println("proxy class logs --------------------");
                    return method.invoke(hunter, args);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        System.out.println("hunterProxy result:"+hunterProxy.fire());
    }
}
