package com.anhnht.warehouse.service.modules.container.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class ContainerResponse {

    private String        containerId;
    private Integer       manifestId;
    private String        containerTypeName;
    private String        statusName;
    private String        cargoTypeName;
    private String        attributeName;
    private BigDecimal    grossWeight;
    private String        sealNumber;
    private String        note;
    private LocalDateTime createdAt;
}
