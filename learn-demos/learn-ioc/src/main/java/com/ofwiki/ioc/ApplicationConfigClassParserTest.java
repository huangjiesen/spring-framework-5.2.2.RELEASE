package com.ofwiki.ioc;

import com.ofwiki.ioc.config.AppConfig1;
import com.ofwiki.ioc.service.OrderService;
import com.ofwiki.ioc.service.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author HuangJS
 * @date 2020-03-24 4:03 下午
 */
public class ApplicationConfigClassParserTest {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig1.class);
        //UserService userService = (UserService) context.getBean("userService");
        //System.out.println(userService.hashCode());
        //Object orderService = context.getBean("com.ofwiki.ioc.service.OrderService");
        //System.out.println("hash code is : "+orderService.hashCode());
        //OrderService bean = context.getBean(OrderService.class);
        //System.out.println(bean);
    }
}
