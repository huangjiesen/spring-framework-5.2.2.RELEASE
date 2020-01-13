package com.ofwiki;

import com.ofwiki.config.AppConfig;
import com.ofwiki.dao.CatDao;
import com.ofwiki.dao.DogDao;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author HuangJS
 * @date 2020-01-13 2:36 下午
 */
public class Application {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        DogDao dogDao = context.getBean(DogDao.class);
        dogDao.query("二哈", 10);
        System.out.println();
        dogDao.query("小哈", 10);

        System.out.println();
        CatDao catDao = context.getBean(CatDao.class);
        catDao.select("tomo", 22);
    }
}
