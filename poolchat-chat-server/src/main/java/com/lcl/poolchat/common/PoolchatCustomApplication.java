package com.lcl.poolchat.common;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

/**
 * @author zhongzb
 * @date 2021/05/27
 */
@SpringBootApplication(scanBasePackages = {"com.lcl.poolchat"})
@MapperScan({"com.lcl.poolchat.common.**.mapper"})
@ServletComponentScan
public class PoolchatCustomApplication {

    public static void main(String[] args) {
        SpringApplication.run(PoolchatCustomApplication.class,args);
    }

}