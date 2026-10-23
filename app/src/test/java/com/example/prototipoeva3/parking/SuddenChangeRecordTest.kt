package com.example.prototipoeva3.parking

import com.google.firebase.Timestamp
import java.util.Date
import org.junit.Assert.assertEquals
import org.junit.Test

class SuddenChangeRecordTest {

    @Test
    fun generateSuddenChangeDocumentId_withValidUserId_composesIdCorrectly() {
        val id = generateSuddenChangeDocumentId("user123", 1700000000000L)
        assertEquals("cambio_brusco_user123_1700000000000", id)
    }

    @Test
    fun generateSuddenChangeDocumentId_withBlankUserId_usesAnonimoFallback() {
        val id = generateSuddenChangeDocumentId("  ", 1700000000000L)
        assertEquals("cambio_brusco_anonimo_1700000000000", id)
    }

    @Test
    fun suddenChangeRecord_toMap_containsAllExpectedFieldsAndTypes() {
        val timestamp = Timestamp(Date(2000L))
        val record = SuddenChangeRecord(
            id = "cambio_brusco_u1_2000",
            userId = "u1",
            userEmail = "u1@test.com",
            distanciaInicial = 80,
            distanciaFinal = 20,
            variacionDistancia = 60,
            tiempoMs = 450L,
            estado = "PELIGRO",
            ubicacionModo = "Frontal",
            nota = "Impacto simulado",
            timestamp = timestamp,
        )

        val map = record.toMap()

        assertEquals("cambio_brusco_u1_2000", map["id"])
        assertEquals("u1", map["userId"])
        assertEquals("u1@test.com", map["userEmail"])
        assertEquals(80, map["distanciaInicial"])
        assertEquals(20, map["distanciaFinal"])
        assertEquals(60, map["variacionDistancia"])
        assertEquals(450L, map["tiempoMs"])
        assertEquals("PELIGRO", map["estado"])
        assertEquals("Frontal", map["ubicacionModo"])
        assertEquals("Impacto simulado", map["nota"])
        assertEquals(timestamp, map["timestamp"])
    }
}
