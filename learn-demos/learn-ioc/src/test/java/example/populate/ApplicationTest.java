package example.populate;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author HuangJS
 * @date 2020-08-28 3:56 下午
 */
@ComponentScan("example.populate")
public class ApplicationTest {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ApplicationTest.class);


        UserService bean = context.getBean(UserService.class);

        bean.save();
    }
}
