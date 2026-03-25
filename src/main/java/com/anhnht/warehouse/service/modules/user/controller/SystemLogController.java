package com.anhnht.warehouse.service.modules.user.controller;

import com.anhnht.warehouse.service.common.dto.response.ApiResponse;
import com.anhnht.warehouse.service.common.dto.response.PageResponse;
import com.anhnht.warehouse.service.modules.user.dto.response.SystemLogResponse;
import com.anhnht.warehouse.service.modules.user.mapper.UserMapper;
import com.anhnht.warehouse.service.modules.user.repository.SystemLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/system-logs")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class SystemLogController {

    private final SystemLogRepository systemLogRepository;
    private final UserMapper          userMapper;

    /**
     * GET /admin/system-logs
     * Paginated list of all system activity logs.
     */
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<SystemLogResponse>>> findAll(
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(
                PageResponse.from(systemLogRepository.findAll(pageable)
                        .map(userMapper::toSystemLogResponse))));
    }

    /**
     * GET /admin/system-logs/user/{userId}
     * Logs for a specific user.
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<PageResponse<SystemLogResponse>>> findByUser(
            @PathVariable Integer userId,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(
                PageResponse.from(systemLogRepository.findAllByUserUserId(userId, pageable)
                        .map(userMapper::toSystemLogResponse))));
    }
}
