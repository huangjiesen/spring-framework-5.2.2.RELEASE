package com.ofwiki.config;

import com.ofwiki.mapper.MapperScan;
import com.ofwiki.mapper.SqlSession;
import com.ofwiki.service.DogService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * 在@MapperScan注解中import MapperBeanDefinitionRegistryPostProcessor.class
 *
 * @author HuangJS
 * @date 2019-08-29 9:44 AM
 */
@Configuration
@MapperScan("com.ofwiki.dao")
public class AppConfig {

    @Bean
    public DogService dogService() {
        return new DogService();
    }


    @Bean
    public SqlSession sqlSession() {
        // sql session 模拟类
        return new SqlSession();
    }

    //@Bean
    //public MapperBeanDefinitionRegistryPostProcessor daoBeanDefinitionRegistryPostProcessor() {
    //	return new MapperBeanDefinitionRegistryPostProcessor();
    //}

}
