package com.example.prototipoeva3.parking

import com.google.firebase.Timestamp
import java.util.Date
import org.junit.Assert.assertEquals
import org.junit.Test

class AlertRecordTest {

    @Test
    fun generateAlertDocumentId_withValidUserId_composesIdCorrectly() {
        val id = generateAlertDocumentId("usr_777", 1700000000000L)
        assertEquals("alerta_usr_777_1700000000000", id)
    }

    @Test
    fun generateAlertDocumentId_withBlankUserId_usesAnonimoFallback() {
        val id = generateAlertDocumentId("  ", 1700000000000L)
        assertEquals("alerta_anonimo_1700000000000", id)
    }

    @Test
    fun alertRecord_toMap_containsAllExpectedFields() {
        val timestamp = Timestamp(Date(3000L))
        val record = AlertRecord(
            id = "alerta_u1_3000",
            userId = "u1",
            securityUserEmail = "seguridad@test.com",
            referenceDocumentId = "aparcamiento_u1_2900",
            referenceCollection = "Aparcamiento",
            estado = "ADVERTENCIA",
            mensaje = DEFAULT_ALERT_MESSAGE,
            timestamp = timestamp,
        )

        val map = record.toMap()

        assertEquals("alerta_u1_3000", map["id"])
        assertEquals("u1", map["userId"])
        assertEquals("seguridad@test.com", map["securityUserEmail"])
        assertEquals("aparcamiento_u1_2900", map["referenceDocumentId"])
        assertEquals("Aparcamiento", map["referenceCollection"])
        assertEquals("ADVERTENCIA", map["estado"])
        assertEquals("Prueba de mensaje de alerta - App Arduino.", map["mensaje"])
        assertEquals(timestamp, map["timestamp"])
    }
}
