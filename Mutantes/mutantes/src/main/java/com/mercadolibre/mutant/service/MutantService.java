package com.mercadolibre.mutant.service;

import com.mercadolibre.mutant.entity.DnaRecord;
import com.mercadolibre.mutant.repository.DnaRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MutantService {

    private final DnaRecordRepository repository;
    private final MutantDetector detector;

    public boolean analyze(String[] dna) {
        // 1. Validamos que el ADN no venga roto
        validateDna(dna);

        // 2. Calculamos un "Hash" (ID único) del ADN para buscarlo rápido
        String hash = String.join("", dna);

        // 3. ¿Ya analizamos este ADN antes?
        Optional<DnaRecord> existing = repository.findByDnaHash(hash);
        if (existing.isPresent()) {
            // Si ya existe, devolvemos el resultado guardado (ahorramos tiempo)
            return existing.get().isMutant();
        }

        // 4. Si es nuevo, usamos el detector
        boolean isMutant = detector.isMutant(dna);

        // 5. Guardamos el resultado en la base de datos
        DnaRecord record = DnaRecord.builder()
                .dnaHash(hash)
                .isMutant(isMutant)
                .build();
        repository.save(record);

        return isMutant;
    }

    private void validateDna(String[] dna) {
        if (dna == null || dna.length == 0) throw new IllegalArgumentException("El ADN no puede estar vacío");
        int n = dna.length;
        if (n < 4) throw new IllegalArgumentException("La matriz debe ser al menos de 4x4");

        for (String row : dna) {
            if (row == null || row.length() != n) {
                throw new IllegalArgumentException("El ADN debe ser una matriz cuadrada (NxN)");
            }
            // Verifica que solo tenga letras válidas (A, T, C, G) usando una expresión regular
            if (!row.matches("[ATCG]+")) {
                throw new IllegalArgumentException("El ADN contiene caracteres inválidos");
            }
        }
    }
}