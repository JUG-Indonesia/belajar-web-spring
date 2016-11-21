package id.jug.spring.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Created by galih.lasahido@gmail.com
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Secured(value = "enabled")
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    
    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Autowired
    private DataSource dataSource;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        auth.userDetailsService(jdbcDao()).passwordEncoder(encoder);
    }

    public JdbcDaoImpl jdbcDao() {
        JdbcDaoImpl jdbcDao = new JdbcDaoImpl();
        jdbcDao.setUsersByUsernameQuery("SELECT USERNAME, PASSWORD, ACTIVE FROM USERS WHERE UPPER(USERNAME) = UPPER(?) ");
        jdbcDao.setAuthoritiesByUsernameQuery("SELECT USERNAME,ROLES FROM USERS WHERE UPPER(USERNAME) = UPPER(?) ");
        jdbcDao.setDataSource(dataSource);
        return jdbcDao;
    }
    
    
    @Override
    public void configure(final WebSecurity web) throws Exception {
        web.ignoring()
        .antMatchers("/styles/**")
        .antMatchers("/css/**")
        .antMatchers("/js/**")
        .antMatchers("/fonts/**")
        .antMatchers("/img/**")        
        .antMatchers("/resources/**");
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        final HttpSessionCsrfTokenRepository tokenRepository = new HttpSessionCsrfTokenRepository();
        tokenRepository.setParameterName("csrf");

    	http
    		.headers().frameOptions().disable().and()
        	.csrf()
        	.csrfTokenRepository(tokenRepository)
        	.and()
	        .authorizeRequests()
	        .antMatchers("/user**").hasRole("admin ")
	        .antMatchers("/dashboard**").hasAnyRole("admin")
	        .antMatchers("/login**").permitAll()
            .antMatchers("/error**").permitAll()
            .antMatchers("/logout**").permitAll()
            .antMatchers("/denied**").permitAll()
            .antMatchers("/user**").hasRole("admin")
            .antMatchers("/**").authenticated()
            .anyRequest()
            .authenticated()
            .and()
            .exceptionHandling()
            .accessDeniedPage("/denied")
            .and()
            .formLogin()
            .loginPage("/login")
            .defaultSuccessUrl("/dashboard")
            .failureUrl("/error-login")
            .usernameParameter("username")
			.passwordParameter("password")
            .and()
            .logout()
            .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
            .and()
            .sessionManagement()
            .maximumSessions(1);
    }
    
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}