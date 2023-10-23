package cacamical.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@EnableWebSecurity
@Configuration
public class SecurityConfig {


    UserDetailsService myUserDetailsService() {
        return new MyUserDetailsService();
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http, HandlerMappingIntrospector introspector) throws Exception {
		MvcRequestMatcher.Builder mvcMatcherBuilder = new MvcRequestMatcher.Builder(introspector);


        RequestMatcher antPathMatcher = new AntPathRequestMatcher("/h2-console/**");

        // A supprimer 
        http.csrf().disable();
        http.headers().frameOptions().disable();


        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(antPathMatcher).permitAll()
                .requestMatchers(mvcMatcherBuilder.pattern("/connexion"),mvcMatcherBuilder.pattern("/inscription")).permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(loginConfigurer -> {
                loginConfigurer
                    .loginPage("/connexion")
                    .defaultSuccessUrl("/") 
                    .permitAll();
            });

        
        
        return http.build();
	}


}
