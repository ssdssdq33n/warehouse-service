package com.anhnht.warehouse.service.modules.yard.service;

import com.anhnht.warehouse.service.modules.yard.dto.request.RelocationRequest;
import com.anhnht.warehouse.service.modules.yard.dto.response.RelocationResponse;

public interface RelocationService {

    /**
     * Move a container from its current slot position to a new slot/tier.
     * Validates that the target slot and tier are available before moving.
     */
    RelocationResponse relocate(RelocationRequest request);
}
