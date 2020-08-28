package example.init_method;


import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;

/**
 * @author HuangJS
 * @date 2020-08-28 10:17 上午
 */
public class UserDao {
    @PostConstruct
    public void initMethodForAnnotation() {
        System.out.println("UserDao:@PostConstruct method");
    }

    public void initMethodForConfig() {
        System.out.println("UserDao:init method for config");
    }
}
