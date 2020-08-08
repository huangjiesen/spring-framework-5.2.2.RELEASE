package com.ofwiki;

import org.springframework.context.annotation.*;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author HuangJS
 * @date 2020-08-07 4:58 下午
 */
@Import(ConfigurationClassEnhancerTest.EnhancerConfig.class)
public class ConfigurationClassEnhancerTest {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(ConfigurationClassEnhancerTest.class);
        // 添加了@Configuration注解的配置类会被cglib增强，@Bean方法在方法内部直接调用其它@Bean方法，得到的是同一个对象
        // 如未添加@Configuration，@Bean方法在方法内部直接调用@Bean方法，会产生不同的对象
        EnhancerConfig bean = applicationContext.getBean(EnhancerConfig.class);
        System.out.println(bean);

        applicationContext.getBean(BeanB.class);
        applicationContext.getBean(BeanA.class);
    }

    //@Configuration
    public static class EnhancerConfig {
        AtomicInteger integer = new AtomicInteger();
        @Bean
        public BeanA beanA() {
            // 如果本类加了@Configuration，会被cglib增强，增强实现：如果Bean是单例，则会判断bean工厂中是否已存在该bean
            // 因为：如果本类加了@Configuration注解，方法体只会被执行一次，否则每次调用都会被执行
            System.out.println("第" + integer.incrementAndGet() + "初始化BeanA");
            return new BeanA();
        }

        @Bean
        public BeanB myTest1() {
            return new BeanB(beanA());
        }
    }

    public static class BeanA {
    }
    public static class BeanB {
        BeanA a;
        public BeanB(BeanA a) {
            this.a = a;
        }
    }
}
