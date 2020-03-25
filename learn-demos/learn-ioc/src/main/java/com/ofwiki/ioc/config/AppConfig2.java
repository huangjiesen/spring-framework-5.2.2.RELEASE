package com.ofwiki.ioc.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author HuangJS
 * @date 2020-03-24 4:04 下午
 */
@Configuration
@Import({AppConfig3.class})
public class AppConfig2 {

}
