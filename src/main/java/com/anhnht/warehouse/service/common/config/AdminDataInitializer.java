package com.anhnht.warehouse.service.common.config;

import com.anhnht.warehouse.service.common.constant.RoleCode;
import com.anhnht.warehouse.service.modules.user.dto.request.CreateUserRequest;
import com.anhnht.warehouse.service.modules.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AdminDataInitializer implements CommandLineRunner {

    private final UserService userService;

    @Override
    public void run(String... args) {
        if (userService.existsByUsername("admin")) {
            log.debug("Admin user already exists — skipping initialization.");
            return;
        }

        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("admin");
        request.setPassword("admin123");
        request.setFullName("System Administrator");
        request.setEmail("admin@warehouse.local");
        request.setPhone("0988344168");
        request.setRoleName(RoleCode.ADMIN);

        userService.createUser(request);
        log.info("Admin user created successfully via AdminDataInitializer.");
    }
}
