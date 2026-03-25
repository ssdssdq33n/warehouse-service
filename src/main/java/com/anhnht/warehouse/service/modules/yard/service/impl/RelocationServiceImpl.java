package com.anhnht.warehouse.service.modules.yard.service.impl;

import com.anhnht.warehouse.service.common.constant.ErrorCode;
import com.anhnht.warehouse.service.common.exception.BusinessException;
import com.anhnht.warehouse.service.common.exception.ResourceNotFoundException;
import com.anhnht.warehouse.service.modules.gatein.entity.ContainerPosition;
import com.anhnht.warehouse.service.modules.gatein.repository.ContainerPositionRepository;
import com.anhnht.warehouse.service.modules.yard.dto.request.RelocationRequest;
import com.anhnht.warehouse.service.modules.yard.dto.response.RelocationResponse;
import com.anhnht.warehouse.service.modules.yard.entity.Slot;
import com.anhnht.warehouse.service.modules.yard.repository.SlotRepository;
import com.anhnht.warehouse.service.modules.yard.service.RelocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RelocationServiceImpl implements RelocationService {

    private final ContainerPositionRepository positionRepository;
    private final SlotRepository              slotRepository;

    @Override
    @Transactional
    public RelocationResponse relocate(RelocationRequest request) {
        String  containerId    = request.getContainerId();
        Integer targetSlotId   = request.getTargetSlotId();
        Integer targetTier     = request.getTargetTier();

        // 1. Confirm current position exists
        ContainerPosition position = positionRepository.findByContainerContainerId(containerId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.NOT_FOUND,
                        "No active position found for container: " + containerId));

        Integer fromSlotId = position.getSlot().getSlotId();

        // No-op guard
        if (fromSlotId.equals(targetSlotId) && position.getTier().equals(targetTier)) {
            throw new BusinessException(ErrorCode.BAD_REQUEST,
                    "Container is already at the specified slot and tier");
        }

        // 2. Validate target slot exists
        Slot targetSlot = slotRepository.findById(targetSlotId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.SLOT_NOT_FOUND,
                        "Slot not found: " + targetSlotId));

        // 3. Validate tier within slot max
        if (targetTier > targetSlot.getMaxTier()) {
            throw new BusinessException(ErrorCode.BAD_REQUEST,
                    "Tier " + targetTier + " exceeds max tier " + targetSlot.getMaxTier()
                    + " for slot " + targetSlotId);
        }

        // 4. Validate target tier is not already occupied by a different container
        if (positionRepository.countBySlotAndTier(targetSlotId, targetTier) > 0) {
            // Check if it's the same container being relocated to same slot (different tier already checked above)
            throw new BusinessException(ErrorCode.SLOT_OCCUPIED,
                    "Tier " + targetTier + " in slot " + targetSlotId + " is already occupied");
        }

        // 5. Move the position
        position.setSlot(targetSlot);
        position.setTier(targetTier);
        ContainerPosition saved = positionRepository.save(position);

        return RelocationResponse.builder()
                .containerId(containerId)
                .fromSlotId(fromSlotId)
                .toSlotId(targetSlotId)
                .tier(targetTier)
                .updatedAt(saved.getUpdatedAt())
                .build();
    }
}
