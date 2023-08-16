package com.hanzhisoft.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ClientWebsecurityConfigurer extends WebSecurityConfigurerAdapter {

    @Value("${client.http.security.authorize.matchers:/,/*/**}")
    private String[] authorizeMatchers = {};

    @Value("${client.web.security.ignoring.matchers:/css/**,/js/**,/favicon.ico,/static/**,/error}")
    private String[] ignoringMatchers = {};

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    /**
     * 访问静态资源
     *
     * @param web
     */
    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers(ignoringMatchers);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(authorizeMatchers).permitAll()
                .anyRequest().authenticated()
                .withObjectPostProcessor(
                        new ObjectPostProcessor<FilterSecurityInterceptor>() {
                            @Override
                            public <O extends FilterSecurityInterceptor> O postProcess(O o) {
                                return o;
                            }
                        }
                );
        // 不加会导致退出 不支持GET方式
        http.csrf().disable();
    }
}
