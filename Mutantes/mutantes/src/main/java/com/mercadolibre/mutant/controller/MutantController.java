package com.mercadolibre.mutant.controller;

import com.mercadolibre.mutant.dto.DnaRequest;
import com.mercadolibre.mutant.dto.StatsResponse;
import com.mercadolibre.mutant.service.MutantService;
import com.mercadolibre.mutant.service.StatsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MutantController {

    private final MutantService mutantService;
    private final StatsService statsService;

    @PostMapping("/mutant")
    public ResponseEntity<Void> checkMutant(@Valid @RequestBody DnaRequest request) {
        try {
            boolean isMutant = mutantService.analyze(request.getDna());
            if (isMutant) {
                return ResponseEntity.ok().build(); // 200 OK (Es mutante)
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // 403 Forbidden (Es humano)
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build(); // 400 Bad Request (ADN inválido)
        }
    }

    @GetMapping("/stats")
    public ResponseEntity<StatsResponse> getStats() {
        return ResponseEntity.ok(statsService.getStats());
    }
}
