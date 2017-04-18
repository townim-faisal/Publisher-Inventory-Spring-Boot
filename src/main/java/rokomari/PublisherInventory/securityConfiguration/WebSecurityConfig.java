package rokomari.PublisherInventory.securityConfiguration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
/*@EnableWebSecurity*/
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception{

       /*httpSecurity.
               authorizeRequests()
               .antMatchers("/").permitAll();*/

        httpSecurity.
                authorizeRequests()
                    .antMatchers("/layout/headerinc").permitAll()
                    .antMatchers("/resource/**").permitAll()
                    .antMatchers("/supersecret/supersecret-user").permitAll()
                    .anyRequest().authenticated()
                    .and()
                .formLogin()
                    .loginPage("/login")
                .defaultSuccessUrl("/")
                    .permitAll()
                    .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login")
                    .permitAll()
        .deleteCookies();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder authentication) throws Exception {


        authentication
                .userDetailsService(userDetailsService);
                /*.passwordEncoder(new BCryptPasswordEncoder())*/;

//        authentication.inMemoryAuthentication()
//                .withUser("user").password("pass").roles("USER");
//        authentication.inMemoryAuthentication()
//                .withUser("tareq").password("pass").roles("ADMIN");
    }
}
