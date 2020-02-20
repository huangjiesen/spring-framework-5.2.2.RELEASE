package com.ofwiki.postprocessor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @author HuangJS
 * @date 2020-02-20 11:48 上午
 */
@Component
public class UserService implements BeanFactoryAware, InitializingBean, DisposableBean {
    private ApplicationContext applicationContext;
    public UserService() {
        System.out.println("1.bean的实例化");
    }
    @Autowired
    public void setApplicationContext(ApplicationContext applicationContext) {
        System.out.println("2.bean的依赖注入 ApplicationContext");
        this.applicationContext = applicationContext;
    }
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        System.out.println("3.Aware接口调用");
    }
    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("6.InitializingBean.afterPropertiesSet");
    }
    @PostConstruct
    public void initMethod() {
        System.out.println("5.init-method for @PostConstruct");
    }
    @Override
    public void destroy() throws Exception {
        System.out.println("9.DisposableBean.destroy");
    }
    @PreDestroy
    public void destroyMethod() {
        System.out.println("8.destroy-method for @PreDestroy");
    }
}
