package com.example.prototipoeva3.parking

import org.junit.Assert.assertEquals
import org.junit.Test

class ParkingRecordTest {

    @Test
    fun generateParkingDocumentId_withValidUserId_composesIdCorrectly() {
        val id = generateParkingDocumentId("user123", 1700000000000L)
        assertEquals("aparcamiento_user123_1700000000000", id)
    }

    @Test
    fun generateParkingDocumentId_withBlankUserId_usesAnonimoFallback() {
        val id = generateParkingDocumentId("  ", 1700000000000L)
        assertEquals("aparcamiento_anonimo_1700000000000", id)
    }

    @Test
    fun generateParkingDocumentId_withSpecialChars_sanitizesUserId() {
        val id = generateParkingDocumentId("user.name@mail.com", 1700000000000L)
        assertEquals("aparcamiento_user_name_mail_com_1700000000000", id)
    }

    @Test
    fun categorizeDistanceState_returnsCorrectCategories() {
        assertEquals("PELIGRO", categorizeDistanceState(15f))
        assertEquals("PELIGRO", categorizeDistanceState(33f))
        assertEquals("ADVERTENCIA", categorizeDistanceState(34f))
        assertEquals("ADVERTENCIA", categorizeDistanceState(50f))
        assertEquals("ADVERTENCIA", categorizeDistanceState(66f))
        assertEquals("SEGURO", categorizeDistanceState(67f))
        assertEquals("SEGURO", categorizeDistanceState(90f))
    }

    @Test
    fun parkingRecord_toMap_containsAllExpectedFields() {
        val record = ParkingRecord(
            id = "aparcamiento_u1_1000",
            userId = "u1",
            userEmail = "u1@test.com",
            distancia = 25,
            estado = "PELIGRO",
            ubicacionModo = "Frontal",
            nota = "Plaza A1",
            timestamp = 1000L,
        )

        val map = record.toMap()

        assertEquals("aparcamiento_u1_1000", map["id"])
        assertEquals("u1", map["userId"])
        assertEquals("u1@test.com", map["userEmail"])
        assertEquals(25, map["distancia"])
        assertEquals("PELIGRO", map["estado"])
        assertEquals("Frontal", map["ubicacionModo"])
        assertEquals("Plaza A1", map["nota"])
        assertEquals(1000L, map["timestamp"])
    }
}
