package com.yahoo.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.servlet.DispatcherType;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {

    /*
     * 记住我
     * */

    /**
     * 自定义一个cookie对象。后续用于记住我功能。如果不设置则采用默认的。
     * cookie对象的key默认为jsessionid，但与SERVLET容器名冲突，
     * 所以将cookie的key设置为sid或rememberMe
     */
    @Bean
    public SimpleCookie rememberMeCookie() {
        //这个参数是cookie的名称（key）
        SimpleCookie simpleCookie = new SimpleCookie("remember--Me");
        //httponly属性如果设为true的话，只能通过http访问，javascript无法访问，会增加对xss防护的安全系数。防止xss读取cookie
        simpleCookie.setHttpOnly(true);
        simpleCookie.setPath("/");
        //记住我cookie有效时间30天 ,单位是秒
        simpleCookie.setMaxAge(60*60*24*30);
//        simpleCookie.setValue("abcdefghijklmnopq");
        return simpleCookie;
    }


    /**
     * rememberMe管理器：实现记住我功能
     */
    @Bean
    public CookieRememberMeManager rememberMeManager() {
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(rememberMeCookie());
        //rememberMe cookie加密的密钥 建议每个项目都不一样 默认AES算法 密钥长度(128 256 512 位)
        cookieRememberMeManager.setCipherKey(Base64.decode("4AvVhmFLUs0KTA3Kprsdag=="));
        return cookieRememberMeManager;
    }


    /**
     * 过滤器：
     */
//    @Bean
//    public FormAuthenticationFilter formAuthenticationFilter() {
//        FormAuthenticationFilter formAuthenticationFilter = new FormAuthenticationFilter();
//        //对应前端的checkbox的name = rememberMe
//        formAuthenticationFilter.setRememberMeParam("rememberMe");
//        return formAuthenticationFilter;
//    }


    /**
     * 解决报错：No SecurityManager accessible to the calling code,
     * 只要是有记住我功能都需要这个bean
     * */
    @Bean
    public FilterRegistrationBean shiroFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new DelegatingFilterProxy("shiroFilterFactoryBean"));
        registration.addInitParameter("targetFilterLifecycle", "true");
        registration.setEnabled(true);
        registration.setOrder(Integer.MAX_VALUE - 1);
        registration.addUrlPatterns("/*");

        //支持异步
        registration.setAsyncSupported(true);
        registration.setDispatcherTypes(DispatcherType.REQUEST, DispatcherType.ASYNC);
        return registration;
    }


//    记住我共呢需要的上面三个bean：SimpleCookie CookieRememberMeManager FilterRegistrationBean


    /**
     * 过滤器工厂bean：设置接口的访问权限等
     * */
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(DefaultWebSecurityManager securityManager) {
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();

//        Map<String, String> filterMap = new HashMap<>();
//        必须是link，因为要保证有序（按先后顺序匹配），一般把**放最后面
        LinkedHashMap<String, String> filterMap = new LinkedHashMap<>();
        filterMap.put("/song/allMusic", "perms[admin]");
        filterMap.put("/user/vip", "anon");
//        一个星代表一级，两个*代表任意多级
        filterMap.put("/user/**", "user");
        filterMap.put("/song/crud/**", "user");
//        前面的优先级高？
//        **的优先级最低？（不区分前后）


        bean.setFilterChainDefinitionMap(filterMap);
        bean.setSecurityManager(securityManager);

//        使用authc过滤器时没有认证会跳转
        bean.setLoginUrl("/toLogin");
//        使用perms过滤器时如果没有设置此，会跳转错误页面，报401
        bean.setUnauthorizedUrl("/user/noauthz");
        return bean;
    }


    /**
     * 安全管理器：设置userRealm，rememberMeManager，sessionManager
     * */
    @Bean
    public DefaultWebSecurityManager securityManager(UserRealm userRealm, DefaultWebSessionManager sessionManager, CookieRememberMeManager rememberMeManager) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(userRealm);
//        记住我。可以直接方法调用吗？
        securityManager.setRememberMeManager(rememberMeManager);

//        jsessionid问题
        securityManager.setSessionManager(sessionManager);
        return securityManager;
    }


    /**
     * session管理器：让jsessionid不出现
     * */
    @Bean
    public DefaultWebSessionManager sessionManager() {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setSessionIdUrlRewritingEnabled(false);
        sessionManager.setGlobalSessionTimeout(1000 * 60 * 60);
        return sessionManager;
    }


    /**
     * UserRealm
     * */
    @Bean
    public UserRealm userRealm() {
        return new UserRealm();
    }


    /**
     * ShiroDialect：thymeleaf整合shiro
     * */
    @Bean
    public ShiroDialect shiroDialect() {
        return new ShiroDialect();
    }



}
