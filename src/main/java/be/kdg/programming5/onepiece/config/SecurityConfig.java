package be.kdg.programming5.onepiece.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.AnyRequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import org.springframework.http.MediaType;

import java.io.IOException;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
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

                        .requestMatchers(HttpMethod.GET, "/api/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/characters").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/api/characters/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/characters/**").authenticated()

                        .requestMatchers("/", "/characters/**", "/battles/**", "/crews/**").permitAll()

                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> {
                    AuthenticationEntryPoint apiEntryPoint = (request, response, authException) -> {
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                        response.getWriter().write(
                                "{\"message\":\"Authentication is required to perform this action\"}");
                    };
                    // Two entries so /api/** always gets JSON and every other path always
                    // redirects to /login, regardless of insertion order or Accept header:
                    // with only one matcher registered, Spring uses it as the entry point
                    // for ALL unmatched requests too, not just the ones it matches.
                    ex.defaultAuthenticationEntryPointFor(apiEntryPoint, new AntPathRequestMatcher("/api/**"));
                    ex.defaultAuthenticationEntryPointFor(
                            new LoginUrlAuthenticationEntryPoint("/login"), AnyRequestMatcher.INSTANCE);
                })

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
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
                )
                .addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class);

        return http.build();
    }

    private static final class CsrfCookieFilter extends OncePerRequestFilter {
        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                        FilterChain filterChain) throws ServletException, IOException {
            CsrfToken csrfToken = (CsrfToken) request.getAttribute("_csrf");
            if (csrfToken != null) {
                csrfToken.getToken();
            }
            filterChain.doFilter(request, response);
        }
    }
}