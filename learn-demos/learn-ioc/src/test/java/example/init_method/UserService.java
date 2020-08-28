package example.init_method;

import org.springframework.beans.factory.InitializingBean;

/**
 * @author HuangJS
 * @date 2020-08-28 10:04 上午
 */
public class UserService implements InitializingBean {
    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("userService afterPropertiesSet for InitializingBean");
    }

    public void haha() {
        System.out.println("this is userService inti method for xml config");
    }
}
