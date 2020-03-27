package com.ofwiki.ioc.config;

import com.ofwiki.ioc.service.OrderService;
import com.ofwiki.ioc.service.UserService;
import org.springframework.context.annotation.Import;

/**
 * @author HuangJS
 * @date 2020-03-25 11:18 上午
 */
@Import(UserService.class)
public class AppConfig4 {

}
