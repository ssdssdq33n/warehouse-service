package com.anhnht.warehouse.service.modules.gatein.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class GateInReceiptResponse {

    private Integer       gateInId;
    private String        containerId;
    private Integer       voyageId;
    private String        voyageNo;
    private LocalDateTime gateInTime;
    private Integer       createdById;
    private String        createdByUsername;
    private String        note;
}
