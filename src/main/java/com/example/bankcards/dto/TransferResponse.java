package com.example.bankcards.dto;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class TransferResponse extends RepresentationModel<TransferResponse> {
    private Long id;
    private Integer cardFromId;
    private Integer cardToId;
    private BigDecimal money;
}
