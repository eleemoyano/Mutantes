package com.mercadolibre.mutant;

import com.mercadolibre.mutant.controller.MutantController;
import com.mercadolibre.mutant.dto.DnaRequest;
import com.mercadolibre.mutant.service.MutantDetector;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MutantDetectorApplicationTests {

    @Autowired
    private MutantDetector detector;

    @Autowired
    private MutantController controller;

    @Test
    void testMutantHorizontal() {
        String[] dna = {"AAAA", "CCCC", "TCAG", "GGTC"};
        assertTrue(detector.isMutant(dna));
    }

    @Test
    void testMutantVertical() {
        String[] dna = {"ACCT", "ACCT", "ACCT", "ACCT"};
        assertTrue(detector.isMutant(dna));
    }

    @Test
    void testMutantDiagonal() {
        String[] dna = {"ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG"};
        assertTrue(detector.isMutant(dna));
    }

    @Test
    void testMutantDiagonalReverse() {
        String[] dna = {"ATGCGA", "CAGTAC", "TTAAGT", "AGAAGG", "CCCCTA", "TCACTG"};
        assertTrue(detector.isMutant(dna));
    }

    @Test
    void testHuman() {
        String[] dna = {"ATGC", "CAGT", "TTAT", "AGAC"};
        assertFalse(detector.isMutant(dna));
    }

    @Test
    void testInvalidDnaNull() {
        assertFalse(detector.isMutant(null));
    }

    @Test
    void testInvalidDnaEmpty() {
        assertFalse(detector.isMutant(new String[]{}));
    }

    @Test
    void testControllerMutant() {
        DnaRequest request = new DnaRequest();
        request.setDna(new String[]{"AAAA", "CCCC", "TCAG", "GGTC"});
        ResponseEntity<Void> response = controller.checkMutant(request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testControllerHuman() {
        DnaRequest request = new DnaRequest();
        request.setDna(new String[]{"ATGC", "CAGT", "TTAT", "AGAC"});
        ResponseEntity<Void> response = controller.checkMutant(request);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void testInvalidCharacters() {
        DnaRequest request = new DnaRequest();
        request.setDna(new String[]{"AXGC", "CAGT", "TTAT", "AGAC"}); // Tiene una X
        ResponseEntity<Void> response = controller.checkMutant(request);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testStats() {
        assertNotNull(controller.getStats().getBody());
    }
}