package be.kdg.programming5.onepiece.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/css/**", "/js/**", "/webjars/**", "/error").permitAll()
                        .requestMatchers("/login", "/register").permitAll()

                        .requestMatchers("/characters/add", "/battles/add").authenticated()
                        .requestMatchers(HttpMethod.POST, "/characters/*/sword").authenticated()
                        .requestMatchers(HttpMethod.POST, "/characters/*/delete").authenticated()
                        .requestMatchers(HttpMethod.POST, "/battles/*/delete").authenticated()

                        .requestMatchers("/", "/characters/**", "/battles/**", "/crews/**").permitAll()
                        .requestMatchers("/api/**").permitAll()

                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/characters", true)
                        .failureUrl("/login?error")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/characters?logout")
                        .permitAll()
                )
                // Temporarily disabled for week 4; CSRF protection is re-enabled in week 5.
                .csrf(csrf -> csrf.disable());

        return http.build();
    }
}