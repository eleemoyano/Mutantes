package com.mercadolibre.mutant.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotEmpty;

@Data
public class DnaRequest {
    @NotNull
    @NotEmpty
    private String[] dna;
}