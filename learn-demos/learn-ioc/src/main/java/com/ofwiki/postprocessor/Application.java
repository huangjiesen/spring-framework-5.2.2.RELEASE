package com.ofwiki.postprocessor;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author HuangJS
 * @date 2020-02-20 11:02 上午
 */
@ComponentScan("com.ofwiki.postprocessor")
public class Application {
    public static void main(String[] args) {
        new AnnotationConfigApplicationContext(Application.class)
                .close();
    }
}
