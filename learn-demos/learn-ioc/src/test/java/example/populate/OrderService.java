package example.populate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author HuangJS
 * @date 2020-08-28 3:58 下午
 */
@Service
public class OrderService {
    @Autowired
    ProductService productService;
    @Autowired
    UserService userService;

    public void save() {
        System.out.println("save info form " + this.getClass().getSimpleName());
    }

}
