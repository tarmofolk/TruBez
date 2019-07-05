package com.tarmofolk.trubez.config;

import com.tarmofolk.trubez.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


//класс который конфигурирует веб секьюрити

@Configuration
@EnableWebSecurity


@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**кодировщик паролей.
     * би крипт с надежностью шифрования 8*/
    @Bean
    public PasswordEncoder getPasswordIncoder () {
        return new BCryptPasswordEncoder(8);
    }



    // отвечает за то, на какие страницы пускать без логина, а на какие нет

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()  //   включаем авторизацию
                // указываем, что на главный путь, на регистрацию  и на статику (css, js) даем полный доступ
                .antMatchers("/", "/registration", "/activate/*", "/static/**").permitAll()
                // для всех остальных  путей требуем авторизацию
                .anyRequest().authenticated()
                .and()
                // включаем форм логин
                .formLogin()
                // указываем что страница для логина /login
                .loginPage("/login")
                // разрешаем пользоваться всем
                .permitAll()
                .and()
                // включаем логаут
                .logout()
                // разрешаем пользоваться всем
                .permitAll();
    }

    // создает менеджер который обслуживает учетные записи


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService)
                .passwordEncoder(passwordEncoder);   // чтобы пароли не хранились в явном виде

    }

}