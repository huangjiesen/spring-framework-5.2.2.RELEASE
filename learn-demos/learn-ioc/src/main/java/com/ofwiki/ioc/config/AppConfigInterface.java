package com.ofwiki.ioc.config;

import com.ofwiki.ioc.service.UserService;
import org.springframework.context.annotation.Bean;

/**
 * @author HuangJS
 * @date 2020-03-26 6:20 下午
 */
public interface AppConfigInterface {
    @Bean
    default UserService userService() {
        return new UserService(555);
    }
}
