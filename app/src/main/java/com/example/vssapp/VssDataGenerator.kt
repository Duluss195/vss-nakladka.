package com.example.vssapp

import kotlin.random.Random
import java.util.Locale

object VssDataGenerator {

    // Bazowa sekwencja dla Wzmocnionego Podłoża
    private val baseValues = listOf(
        0.00,       // 0.02 MPa
        0.19, 0.22, // 0.05 MPa
        0.38, 0.40, // 0.10 MPa
        0.66, 0.67, // 0.15 MPa
        0.92, 0.92, // 0.20 MPa
        1.20, 1.23, // 0.25 MPa
        1.39, 1.39, // 0.30 MPa
        1.57, 1.57, // 0.35 MPa
        1.55,       // odciążenie 0.25
        1.34,       // odciążenie 0.15
        1.12,       // odciążenie 0.05
        0.80,       // odciążenie 0.00
        0.89, 0.89, // II cykl 0.05
        0.99, 0.99, // II cykl 0.10
        1.16, 1.16, // II cykl 0.15
        1.28, 1.28, // II cykl 0.20
        1.42        // II cykl 0.25
    )

    // Generuje osiadania dodając losowo od 0.01 do 0.05 mm do każdego pomiaru
    fun generateRandomizedWzmocnionePodloze(): List<String> {
        return baseValues.map { base ->
            val randomOffset = Random.nextDouble(0.01, 0.05)
            val finalValue = base + randomOffset
            String.format(Locale.US, "%.2f", finalValue)
        }
    }
}