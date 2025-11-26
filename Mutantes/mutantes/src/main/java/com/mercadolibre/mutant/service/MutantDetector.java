package com.mercadolibre.mutant.service;

import org.springframework.stereotype.Component;

@Component
public class MutantDetector {

    // Longitud de la secuencia para ser mutante (4 letras iguales)
    private static final int SEQUENCE_LENGTH = 4;

    public boolean isMutant(String[] dna) {
        if (dna == null || dna.length == 0) return false;

        int n = dna.length;
        int sequenceCount = 0;

        // Convertimos el array de Strings a una matriz de caracteres para hacerlo más rápido
        char[][] matrix = new char[n][n];
        for (int i = 0; i < n; i++) {
            matrix[i] = dna[i].toCharArray();
        }

        // Recorremos la matriz celda por celda
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {

                // 1. Buscamos Horizontalmente (hacia la derecha)
                if (col <= n - SEQUENCE_LENGTH) {
                    if (checkLine(matrix, row, col, 0, 1)) {
                        sequenceCount++;
                        if (sequenceCount > 1) return true; // ¡Ya encontramos 2! Terminamos rápido.
                    }
                }

                // 2. Buscamos Verticalmente (hacia abajo)
                if (row <= n - SEQUENCE_LENGTH) {
                    if (checkLine(matrix, row, col, 1, 0)) {
                        sequenceCount++;
                        if (sequenceCount > 1) return true;
                    }
                }

                // 3. Buscamos Diagonal Descendente (↘)
                if (row <= n - SEQUENCE_LENGTH && col <= n - SEQUENCE_LENGTH) {
                    if (checkLine(matrix, row, col, 1, 1)) {
                        sequenceCount++;
                        if (sequenceCount > 1) return true;
                    }
                }

                // 4. Buscamos Diagonal Ascendente (↗)
                if (row >= SEQUENCE_LENGTH - 1 && col <= n - SEQUENCE_LENGTH) {
                    if (checkLine(matrix, row, col, -1, 1)) {
                        sequenceCount++;
                        if (sequenceCount > 1) return true;
                    }
                }
            }
        }
        return false; // Si llegamos aquí, es humano
    }

    // Esta función auxiliar revisa si hay 4 letras iguales en una dirección específica
    private boolean checkLine(char[][] matrix, int row, int col, int dRow, int dCol) {
        char first = matrix[row][col];
        // Solo nos importan A, T, C, G
        if (first != 'A' && first != 'T' && first != 'C' && first != 'G') return false;

        for (int i = 1; i < SEQUENCE_LENGTH; i++) {
            if (matrix[row + i * dRow][col + i * dCol] != first) {
                return false; // Encontró una letra diferente, rompe la secuencia
            }
        }
        return true; // ¡Encontró 4 iguales!
    }
}