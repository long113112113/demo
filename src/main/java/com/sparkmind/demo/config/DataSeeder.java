// package com.sparkmind.demo.config;

// import com.sparkmind.demo.entity.Role;
// import com.sparkmind.demo.entity.User;
// import com.sparkmind.demo.repository.RoleRepository;
// import com.sparkmind.demo.repository.UserRepository;
// import lombok.RequiredArgsConstructor;
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.boot.CommandLineRunner;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.stereotype.Component;
// import org.springframework.transaction.annotation.Transactional;

// import java.util.Optional;
// @Component
// @RequiredArgsConstructor
// @Slf4j
// public class DataSeeder implements CommandLineRunner {
//     private final UserRepository userRepository;
//     private final RoleRepository roleRepository;
//     private final PasswordEncoder passwordEncoder;

//     @Override
//     @Transactional // Quan trọng
//     public void run(String... args) throws Exception {

//         //Set roles
//         seedRoleIfNotExists("ADMIN", "Quan tri vien");
//         seedRoleIfNotExists("USER", "Nguoi dung thu vien");

//         //set admin
//         seedAdminUserIfNotExists();
//         log.info("roles and admin account seeded");
//     }

//     private void seedRoleIfNotExists(String roleName, String description) {
//         if (!roleRepository.existsByRoleName(roleName)) {
//             Role role = Role.builder()
//                     .roleName(roleName)
//                     .description(description)
//                     .build();
//             roleRepository.save(role);
//             log.info("Role {} created", roleName);
//         } else {
//             log.info("Role {} already exists", roleName);
            
//         }
//     }

//     private void seedAdminUserIfNotExists(){
//         String adminEmail = "admin@sparkmind.com";
//         String adminPassword = "admin";
//         if(!userRepository.existsByEmail(adminEmail)){
//             Optional<Role> adminRoleOpt = roleRepository.findByRoleName("ADMIN");
//             if (adminRoleOpt.isEmpty()) {
//                 log.error("ADMIN Role not found during seeding.");
//                 throw new RuntimeException("ADMIN Role not found during seeding.");
//             }
//             User adminUser = User.builder()
//                     .email(adminEmail)
//                     .passwordHash(passwordEncoder.encode(adminPassword))
//                     .fullName("Admin")
//                     .role(adminRoleOpt.get())
//                     .isActive(true)
//                     .isVerified(true)
//                     .build();
//             userRepository.save(adminUser);
//             log.info("User '{}' seeded.", adminEmail);
//         } else {
//             log.info("User '{}' already exists.", adminEmail);
//         }
//     }
// }