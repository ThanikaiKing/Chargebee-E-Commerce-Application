package com.thanikai.authentication;

import com.thanikai.databaseconfig.User;
import com.thanikai.databaseconfig.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserRepository userRepository;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/", "/login", "/oauth/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .oauth2Login()
                .loginPage("/login")
                .userInfoEndpoint()
                .and()
                .successHandler((request, response, authentication) -> {
                    System.out.println("AuthenticationSuccessHandler invoked");
                    System.out.println("Authentication name: " + authentication.getName());

                    OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
                    User existingUser = userRepository.getUserByEmailID(oauthUser.getAttribute("email"));
                    if (existingUser == null) {
                        User newUser = new User();
                        newUser.setUsername(oauthUser.getAttribute("name"));
                        newUser.setEmail(oauthUser.getAttribute("email"));
                        userRepository.save(newUser);
                        response.sendRedirect("/editUser/" + newUser.getId());
                    } else {
                        response.sendRedirect("/welcome/" + existingUser.getId());
                    }
                })
                .and()
                .logout().logoutSuccessUrl("/login").permitAll()
                .and()
                .exceptionHandling().accessDeniedPage("/403")
        ;
    }
}
