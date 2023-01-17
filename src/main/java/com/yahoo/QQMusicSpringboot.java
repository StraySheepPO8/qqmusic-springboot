package com.yahoo;

import com.yahoo.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.redis.core.RedisTemplate;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Set;

@EnableCaching
@SpringBootApplication
public class QQMusicSpringboot {

//    静态方法会比创建此类的对象先执行，所以完成了自动配置，存在RedisUtil这个Bean
    public static void main(String[] args) {
        SpringApplication.run(QQMusicSpringboot.class,args);
    }

//    public static void flushDB(){
//        Set<String> keys = redis.keys("*");
//        assert keys != null;
//        redis.delete(keys);
//    }
}
