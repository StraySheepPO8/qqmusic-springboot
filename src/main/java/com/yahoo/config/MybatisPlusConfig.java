package com.yahoo.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
//@EnableTransactionManagement
public class MybatisPlusConfig {

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

        PaginationInnerInterceptor innerInterceptor = new PaginationInnerInterceptor();
//        数据库类型
        innerInterceptor.setDbType(DbType.MYSQL);
//        允许超页
        innerInterceptor.setOverflow(true);

        interceptor.addInnerInterceptor(innerInterceptor);
        return interceptor;
    }


    @Bean
    public ConfigurationCustomizer configurationCustomizer() {
        return mybatisConfiguration -> mybatisConfiguration.setUseGeneratedShortKey(false);
    }


}
