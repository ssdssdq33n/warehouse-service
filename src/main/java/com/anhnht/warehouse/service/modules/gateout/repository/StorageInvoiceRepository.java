package com.anhnht.warehouse.service.modules.gateout.repository;

import com.anhnht.warehouse.service.modules.gateout.entity.StorageInvoice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StorageInvoiceRepository extends JpaRepository<StorageInvoice, Integer> {

    @EntityGraph(attributePaths = {"container", "gateOutReceipt"})
    Optional<StorageInvoice> findByGateOutReceiptGateOutId(Integer gateOutId);

    boolean existsByContainerContainerId(String containerId);
}
