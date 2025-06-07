package kr.ac.hansung.cse.hellospringdatajpa.config;

import kr.ac.hansung.cse.hellospringdatajpa.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
//            .authorizeHttpRequests(authz -> authz
//                // 공개 접근 허용 (임시로 /products도 포함)
//                .requestMatchers("/", "/register", "/login", "/products", "/products/", "/css/**", "/js/**", "/images/**").permitAll()
//
//                // 상품 생성, 수정, 삭제는 관리자만 (임시로 주석)
//                .requestMatchers("/products/new", "/products/save", "/products/edit/**", "/products/delete/**").hasRole("ADMIN")
//
//                // 관리자 페이지는 관리자만
//                .requestMatchers("/admin/**").hasRole("ADMIN")
//
//                // 그 외 모든 요청은 인증 필요
//                .anyRequest().authenticated()
//            )
            .authorizeHttpRequests(authz -> authz
                // 공개 접근 허용
                .requestMatchers("/", "/register", "/login", "/css/**", "/js/**", "/images/**").permitAll()

                // 상품 목록 조회는 로그인한 사용자만
                .requestMatchers("/products", "/products/").hasAnyRole("USER", "ADMIN")

                // 상품 생성, 수정, 삭제는 관리자만
                .requestMatchers("/products/new", "/products/save", "/products/edit/**", "/products/delete/**").hasRole("ADMIN")

                // 관리자 페이지는 관리자만
                .requestMatchers("/admin/**").hasRole("ADMIN")

                // 그 외 모든 요청은 인증 필요
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .usernameParameter("email")
                .passwordParameter("password")
                .defaultSuccessUrl("/products", true)
                .failureUrl("/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            )
            .userDetailsService(userDetailsService);

        return http.build();
    }
}