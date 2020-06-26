package com.ofwiki.ioc;

import com.ofwiki.ioc.config.AppConfig3;
import com.ofwiki.ioc.config.AppConfig4;
import com.ofwiki.ioc.config.AppConfig5;
import com.ofwiki.ioc.service.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author HuangJS
 * @date 2020-03-24 4:03 下午
 */
public class ApplicationConfigClassParserTest {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig5.class);

        //
        // MyTODO 2020/3/26 6:23 下午 HuangJS 测试org.springframework.context.annotation.ConfigurationClassParser.processInterfaces
        //
        UserService userService =  context.getBean(UserService.class);
        userService.sss();


        AppConfig5 bean = context.getBean(AppConfig5.class);
        System.out.println(bean);

        //System.out.println(userService.hashCode());
        //Object orderService = context.getBean("com.ofwiki.ioc.service.OrderService");
        //System.out.println("hash code is : "+orderService.hashCode());
        //OrderService bean = context.getBean(OrderService.class);
        //System.out.println(bean);
    }
}
