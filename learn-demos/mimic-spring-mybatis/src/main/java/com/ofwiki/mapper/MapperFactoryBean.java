package com.ofwiki.mapper;

import org.springframework.beans.factory.FactoryBean;

/**
 * 实现了FactoryBean的类，spring会调用其getObject、getObjectType创建bean
 * 可以在getObject方法自定义Bean的创建过程
 *
 * @author HuangJS
 * @date 2019-08-29 10:43 AM
 */
public class MapperFactoryBean<T> implements FactoryBean<T> {
    private Class<T> t;
    private String proxyTargetName;
    private SqlSession sqlSession;


    public MapperFactoryBean(Class<T> t) {
        this.t = t;
    }

    public SqlSession getSqlSession() {
        return sqlSession;
    }

    // GenericBeanDefinition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
    // 注册BeanDefinition时通过以上设置,容器会自动set方法注入可用依赖
    public void setSqlSession(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    public void setProxyTargetName(String proxyTargetName) {
        this.proxyTargetName = proxyTargetName;
    }

    @Override
    public T getObject() throws Exception {
        System.out.println("create proxy instance " + proxyTargetName);
        return MapperProxyUtil.getProxy(t, sqlSession);
    }

    @Override
    public Class<?> getObjectType() {
        return t;
    }
}
