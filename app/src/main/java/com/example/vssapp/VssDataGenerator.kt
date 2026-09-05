package com.example.vssapp

import kotlin.random.Random
import kotlin.math.roundToInt

object VssDataGenerator {

    data class VssResult(
        val deltaUsed: Double,
        val settlements: List<Double>
    )

    // Twoja gotowa, sztywna sekwencja ugięć (23 punkty pomiarowe: naciski, odciążenie, II cykl)
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

    fun generateRandomizedWzmocnionePodloze(): VssResult {
        // 1. Losujemy JEDNO wspólne przesunięcie dla całej serii (9 wariantów: 0.00, 0.01, ..., 0.08 mm)
        val deltaSteps = Random.nextInt(0, 9) // zwraca losową liczbę od 0 do 8
        val delta = deltaSteps * 0.01

        // 2. Dodajemy dokładnie tę samą wartość delta do KAŻDEJ z 23 pozycji
        val settlements = baseValues.map { base ->
            val finalValue = base + delta
            ((finalValue * 100.0).roundToInt()) / 100.0 // Zaokrąglenie do 2 miejsc po przecinku
        }

        return VssResult(
            deltaUsed = delta,
            settlements = settlements
        )
    }
}