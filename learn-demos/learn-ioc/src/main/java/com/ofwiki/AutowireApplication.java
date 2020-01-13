package com.ofwiki;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.Assert;

/**
 * 基于xml配置文件自动装配示例
 * @author HuangJS
 * @date 2020-01-10 4:16 下午
 */
public class AutowireApplication {
    public static void main(String[] args) {
        new ClassPathXmlApplicationContext("spring-autowire.xml")
                .getBean(UserService.class).save();
    }

    public static class UserService {
        private UserDao dbDao;
        public void setDbDao(UserDao dbDao) {
            this.dbDao = dbDao;
        }
        public void save() {
            Assert.notNull(dbDao, "dbDao field is null");
            System.out.println("UserService:save user info");
            dbDao.save();
        }
    }
    public static class UserDao {
        public void save() {
            System.out.println("UserDao:save user info");
        }
    }
}
