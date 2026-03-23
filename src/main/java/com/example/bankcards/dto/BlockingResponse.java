package com.example.bankcards.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;
import java.time.LocalDateTime;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Relation(collectionRelation = "blockings", itemRelation = "block")
@Setter
@Getter
@AllArgsConstructor
public class BlockingResponse extends RepresentationModel<BlockingResponse> {
    private Long id;
    private Integer cardId;
    private LocalDateTime dateTime;
}
