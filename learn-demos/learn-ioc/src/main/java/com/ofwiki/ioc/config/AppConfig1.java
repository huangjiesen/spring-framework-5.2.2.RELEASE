package com.ofwiki.ioc.config;

import com.ofwiki.ioc.service.OrderService;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author HuangJS
 * @date 2020-03-24 4:05 下午
 */
//@Import(OrderService.class)
@ComponentScan("com.ofwiki.ioc")
public class AppConfig1 extends AppConfig2{

}
