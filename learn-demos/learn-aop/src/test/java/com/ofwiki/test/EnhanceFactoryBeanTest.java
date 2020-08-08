package com.ofwiki.test;

import com.ofwiki.ConfigurationClassEnhancerTest;
import org.junit.Assert;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.annotation.*;

import java.util.Random;

/**
 * @author HuangJS
 * @date 2020-08-08 4:32 下午
 */
@Import(EnhanceFactoryBeanTest.FactoryBeanConfig.class)
public class EnhanceFactoryBeanTest {
    public static void main(String[] args) throws Exception {
        /**
         * 添加了@Configuration注解的配置类本被代理增强
         * 如果@Bean创建是是FactoryBean，且在容器中已经存在该FactoryBean对象，则对该对象进行代理增强，使多次调用该bean的getObject()方法时得到同一个对象
         * @see ConfigurationClassEnhancer.BeanMethodInterceptor#intercept(java.lang.Object, java.lang.reflect.Method, java.lang.Object[], org.springframework.cglib.proxy.MethodProxy)
         */


        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(EnhanceFactoryBeanTest.class);
        FactoryBeanConfig bean = applicationContext.getBean(FactoryBeanConfig.class);
        FactoryBean myFactoryBean = bean.myFactoryBean();
        FactoryBean myFactoryBean1 = bean.myFactoryBean();
        // 与其它增强后的@Bean方法不一样，这里每次调用@Bean获取到的是不同的对象
        Assert.assertFalse(myFactoryBean == myFactoryBean1);
        // 即便是两个不同的myFactoryBean实例，调用getObject得到的是同一个对象，因为都是取容器中已存在的对象
        Assert.assertTrue(myFactoryBean.getObject() == myFactoryBean1.getObject());


        // 有一种情况与以上相反，每次调用@Bean方法得到相同的对象，调用getObject得到的是不同的对象
        // @Bean方法的返回值类型是FactoryBean实现类，且用final修饰FactoryBean实现类或getObject方法
        // Assert.assertTrue(myFactoryBean == myFactoryBean1);
        // Assert.assertFalse(myFactoryBean.getObject() == myFactoryBean1.getObject());
    }

    @Configuration
    public static class FactoryBeanConfig{
        @Bean
        public FactoryBean myFactoryBean() {
            System.out.println("FactoryBeanConfig.myFactoryBean这个方法每次调用方法体都会被执行");
            return new MyFactoryBean();
        }
    }

    public static class MyFactoryBean implements FactoryBean {
        @Override
        public Object getObject() throws Exception {
            System.out.println("MyFactoryBean.getObject这个方法只会被执行一次");
            return String.valueOf(new Random().nextDouble());
        }

        @Override
        public Class<?> getObjectType() {
            return String.class;
        }
    }
}
