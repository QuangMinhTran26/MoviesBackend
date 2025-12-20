package dev.tran.movies;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import static org.springframework.security.config.Customizer.withDefaults;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity

public class SecurityConfig {

    public SecurityConfig() {
    }

    @Bean
    public UserDetailsService userDetailsService() {
        System.out.println("========================================");
        System.out.println("CREATING USERS");
        System.out.println("========================================");

        PasswordEncoder encoder = new BCryptPasswordEncoder();

        String encodedPassword = encoder.encode("123");
        System.out.println("Raw password: 123");
        System.out.println("Encoded password: " + encodedPassword);

        UserDetails admin = User.withUsername("Test1")
                .password(encodedPassword)
                .roles("ADMIN", "USER")
                .build();

        UserDetails user = User.withUsername("Test2")
                .password(encodedPassword)
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(admin, user);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/welcome").permitAll()
                        .requestMatchers("/auth/user/**").hasRole("USER")  // Changed to hasRole
                        .requestMatchers("/auth/admin/**").hasRole("ADMIN")  // Changed to hasRole
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .successHandler((request, response, authentication) -> {

                            boolean isAdmin = authentication.getAuthorities().stream()
                                    .anyMatch(grantedAuthority ->
                                            grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

                            if (isAdmin) {
                                response.sendRedirect("/auth/admin/adminProfile");
                            } else {
                                response.sendRedirect("/auth/user/userProfile");
                            }
                        })
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/auth/welcome")
                        .permitAll()
                );

        return http.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}