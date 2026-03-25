package com.anhnht.warehouse.service.modules.container.controller;

import com.anhnht.warehouse.service.common.dto.response.ApiResponse;
import com.anhnht.warehouse.service.common.dto.response.PageResponse;
import com.anhnht.warehouse.service.common.util.PageableUtils;
import com.anhnht.warehouse.service.modules.container.dto.request.ContainerRequest;
import com.anhnht.warehouse.service.modules.container.dto.request.ExportPriorityRequest;
import com.anhnht.warehouse.service.modules.container.dto.response.ContainerResponse;
import com.anhnht.warehouse.service.modules.container.dto.response.ContainerStatusHistoryResponse;
import com.anhnht.warehouse.service.modules.container.dto.response.ExportPriorityResponse;
import com.anhnht.warehouse.service.modules.container.mapper.ContainerMapper;
import com.anhnht.warehouse.service.modules.container.service.ContainerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/containers")
@RequiredArgsConstructor
public class ContainerController {

    private final ContainerService containerService;
    private final ContainerMapper  containerMapper;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','OPERATOR')")
    public ResponseEntity<ApiResponse<PageResponse<ContainerResponse>>> getContainers(
            @RequestParam(required = false)               String keyword,
            @RequestParam(defaultValue = "0")             int page,
            @RequestParam(defaultValue = "20")            int size,
            @RequestParam(defaultValue = "containerId")   String sortBy,
            @RequestParam(defaultValue = "asc")           String direction) {

        Pageable pageable = PageableUtils.of(page, size, sortBy, direction);
        return ResponseEntity.ok(ApiResponse.success(
                PageResponse.of(containerService.findAll(keyword, pageable)
                        .map(containerMapper::toContainerResponse))));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','OPERATOR')")
    public ResponseEntity<ApiResponse<ContainerResponse>> getContainer(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.success(
                containerMapper.toContainerResponse(containerService.findById(id))));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','OPERATOR')")
    public ResponseEntity<ApiResponse<ContainerResponse>> createContainer(
            @Valid @RequestBody ContainerRequest request) {
        return ResponseEntity.status(201).body(ApiResponse.created(
                containerMapper.toContainerResponse(containerService.create(request))));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','OPERATOR')")
    public ResponseEntity<ApiResponse<ContainerResponse>> updateContainer(
            @PathVariable String id,
            @Valid @RequestBody ContainerRequest request) {
        return ResponseEntity.ok(ApiResponse.success(
                containerMapper.toContainerResponse(containerService.update(id, request))));
    }

    @GetMapping("/{id}/status-history")
    @PreAuthorize("hasAnyRole('ADMIN','OPERATOR')")
    public ResponseEntity<ApiResponse<List<ContainerStatusHistoryResponse>>> getStatusHistory(
            @PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.success(
                containerMapper.toHistoryResponses(containerService.getStatusHistory(id))));
    }

    @PutMapping("/{id}/export-priority")
    @PreAuthorize("hasAnyRole('ADMIN','OPERATOR')")
    public ResponseEntity<ApiResponse<ExportPriorityResponse>> setExportPriority(
            @PathVariable String id,
            @Valid @RequestBody ExportPriorityRequest request) {
        return ResponseEntity.ok(ApiResponse.success(
                containerMapper.toExportPriorityResponse(containerService.setExportPriority(id, request))));
    }

    @GetMapping("/{id}/export-priority")
    @PreAuthorize("hasAnyRole('ADMIN','OPERATOR')")
    public ResponseEntity<ApiResponse<ExportPriorityResponse>> getExportPriority(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.success(
                containerMapper.toExportPriorityResponse(containerService.getExportPriority(id))));
    }
}
