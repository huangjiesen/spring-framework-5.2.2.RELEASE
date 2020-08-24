package com.ofwiki;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author HuangJS
 * @date 2020-08-07 4:58 下午
 */
@Import(ConfigurationClassEnhancerTest1.EnhancerConfig.class)
public class ConfigurationClassEnhancerTest1 {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(ConfigurationClassEnhancerTest1.class);
        // 添加了@Configuration注解的配置类会被cglib增强，@Bean方法在方法内部直接调用其它@Bean方法，得到的是同一个对象
        // 如未添加@Configuration，@Bean方法在方法内部直接调用@Bean方法，会产生不同的对象
        EnhancerConfig bean = applicationContext.getBean(EnhancerConfig.class);
        System.out.println(bean);

        BeanB beanB = applicationContext.getBean(BeanB.class);
        BeanA beanA = applicationContext.getBean(BeanA.class);


        System.out.println(beanB.a == beanA);
    }

    @Configuration
    public static class EnhancerConfig {
        AtomicInteger integer = new AtomicInteger();

        @Bean
        public BeanB beanB() {
            BeanA a = beanA();

            return new BeanB(a);
        }


        @Bean
        public BeanA beanA() {
            System.out.println("第" + integer.incrementAndGet() + "初始化BeanA");
            return new BeanA();
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
