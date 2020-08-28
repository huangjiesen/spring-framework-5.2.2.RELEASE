package example.init_method;


import org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;

/**
 * 测试初始化方法调用处理
 * @see AbstractAutowireCapableBeanFactory#invokeInitMethods
 * @author HuangJS
 * @date 2020-08-28 9:58 上午
 */
@ComponentScan("example.init_method")
@ImportResource("example/init_method/context.xml")
public class IntiMethodTest {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(IntiMethodTest.class);
        System.out.println(applicationContext.getBean(UserService.class));
    }


    @Bean(initMethod = "initMethodForConfig")
    public UserDao userDao() {
        return new UserDao();
    }
}
