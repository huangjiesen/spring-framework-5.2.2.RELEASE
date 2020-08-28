package example.populate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author HuangJS
 * @date 2020-08-28 3:57 下午
 */
@Service
public class UserService {
    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductService productService;

    public void save() {
        orderService.save();
        productService.save();
        System.out.println("save info form " + this.getClass().getSimpleName());
    }
}
