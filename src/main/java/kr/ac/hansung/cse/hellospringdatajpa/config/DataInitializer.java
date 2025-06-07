package kr.ac.hansung.cse.hellospringdatajpa.config;

import kr.ac.hansung.cse.hellospringdatajpa.entity.Role;
import kr.ac.hansung.cse.hellospringdatajpa.entity.User;
import kr.ac.hansung.cse.hellospringdatajpa.repo.RoleRepository;
import kr.ac.hansung.cse.hellospringdatajpa.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // 역할이 이미 존재하는지 확인
        if (roleRepository.count() == 0) {
            // 기본 역할 생성
            Role userRole = new Role("ROLE_USER");
            Role adminRole = new Role("ROLE_ADMIN");

            roleRepository.save(userRole);
            roleRepository.save(adminRole);

            // 관리자 계정이 없으면 생성
            if (!userRepository.existsByEmail("2071328@hansung.ac.kr")) {
                User admin = new User();
                admin.setEmail("2071328@hansung.ac.kr");
                admin.setPassword(passwordEncoder.encode("soryong31")); // 실제 암호화
                admin.setName("관리자");
                admin.setEnabled(true);

                // 관리자에게 두 역할 모두 부여
                admin.getRoles().add(userRole);
                admin.getRoles().add(adminRole);

                userRepository.save(admin);

                System.out.println("관리자 계정이 생성되었습니다:");
                System.out.println("이메일: 2071328@hansung.ac.kr");
                System.out.println("비밀번호: soryong31");
            }
        }
    }
}