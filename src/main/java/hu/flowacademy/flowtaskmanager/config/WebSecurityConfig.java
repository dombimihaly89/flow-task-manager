package hu.flowacademy.flowtaskmanager.config;

import hu.flowacademy.flowtaskmanager.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

@EnableWebSecurity
@EnableResourceServer
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter implements ResourceServerConfigurer {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {}

    @Override
    public void configure(final HttpSecurity http) throws Exception {
        // @formatter:off
        http.authorizeRequests()
                .antMatchers("/", "/login", "/oauth/**", "/oauth/token/revokeById/**", "/tokens/**").permitAll()
                .antMatchers("/api/users/register").permitAll()
                .antMatchers("/api/users/**").permitAll()
                .antMatchers("/api/tasks/findall").hasAnyAuthority(User.Role.MENTOR.name(), User.Role.STUDENT.name())
                .antMatchers("/api/tasks/rate{id}{rating}").hasAnyAuthority(User.Role.STUDENT.name())
                .antMatchers("/api/tasks", "/api/tasks/{id}", "/api/tasks/update").permitAll()
                .antMatchers("/api/posts", "/api/posts/findall", "/api/posts/{id}").hasAnyAuthority(User.Role.MENTOR.name(), User.Role.STUDENT.name())
                .anyRequest().authenticated()
                .and().formLogin().permitAll()
                .and().csrf().disable();
        // @formatter:on
    }

}
