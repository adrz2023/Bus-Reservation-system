package org.befikreyatra.dto;



import lombok.Data;

import java.util.List;

@Data
public class BusSeatTemplateSaveRequest {
    private List<BusSeatTemplateEntryRequest> seats;
}