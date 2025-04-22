package sbs.getcry.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import sbs.getcry.exception.CustomAccessDeniedHandler;
import sbs.getcry.exception.JwtAuthenticationExceptionHandler;
import sbs.getcry.filter.JwtAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private JwtAuthenticationExceptionHandler jwtAuthenticationExceptionHandler;

    @Autowired
    private CustomAccessDeniedHandler customAccessDeniedHandler;

    /**
     * BCrypt加密器
     *
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // 直接返回 BCrypt 实例
    }

    /**
     * Spring Security 5.0 之后, AuthenticationManager 不能直接 @Bean 注入
     * 必须通过 AuthenticationConfiguration 提供 getAuthenticationManager() 获取
     *
     * @param authenticationConfiguration
     * @return
     * @throws Exception
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


    /**
     * 设置 Spring Security 的过滤器链配置
     *
     * @param http
     * @return
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // 设置不需要认证的路径
        var excludePath = new String[]{"/user/login", "/user/register", "/user/logout"};
        http.csrf(csrf -> csrf.disable());//关闭CSRF保护
        http.authorizeHttpRequests(authorizeRequests ->
                authorizeRequests
                        .requestMatchers(excludePath)      //接口进行配置
                        .permitAll()                       //任何人都能访问
                        .anyRequest()                      //其他所有接口
                        .authenticated()                   //需要身份认证
        );
        // 将自定义的过滤器 authenticationTokenFilter 插入到 ExceptionTranslationFilter 之后执行
        http.addFilterAfter(jwtAuthenticationFilter, ExceptionTranslationFilter.class);

        // 设置认证失败异常 和 权限不足异常的处理
        http.exceptionHandling(ex ->
                ex.authenticationEntryPoint(jwtAuthenticationExceptionHandler).accessDeniedHandler(customAccessDeniedHandler)
        );

        return http.build();
    }
}
