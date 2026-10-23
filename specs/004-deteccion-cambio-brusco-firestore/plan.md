# Plan — Detección de Cambio Brusco de Distancia en Firestore

## Alcance y diseño técnico

Este plan añade la capacidad de evaluar variaciones abruptas de distancia durante el movimiento del Slider en `ValuesScreen.kt` e insertar el evento en la colección `'Cambio_Brusco'` de Cloud Firestore.

### ID y Esquema de Documentos en `'Cambio_Brusco'`
- **ID:** `cambio_brusco_<userId>_<timestampMs>`
- **Campos:**
  - `id`: String
  - `userId`: String
  - `userEmail`: String
  - `distanciaInicial`: Int
  - `distanciaFinal`: Int
  - `variacionDistancia`: Int
  - `tiempoMs`: Long (tiempo transcurrido, < 1000 ms)
  - `estado`: String ("PELIGRO", "ADVERTENCIA", "SEGURO")
  - `ubicacionModo`: String ("Frontal", "Trasero")
  - `nota`: String
  - `timestamp`: `com.google.firebase.Timestamp`

### Regla de Detección
Durante la interacción con el Slider:
1. Al iniciar la variación o en cada muestra continua, se registra `(distanciaInicial, tiempoInicialMs)`.
2. Si en una muestra posterior la distancia disminuye (caída ≥ 20 cm) en un intervalo `deltaTiempo <= 1000 ms`, se confirma el evento de cambio brusco.
3. Se invoca `saveSuddenChangeRecord(...)` en `ParkingRepository` y se notifica en pantalla.

---

## Archivos a crear o modificar

| Archivo | Acción y responsabilidad |
|---|---|
| `firestore.rules` | Modificar: añadir regla de lectura/escritura para la colección `Cambio_Brusco`. |
| `app/src/main/java/com/example/prototipoeva3/parking/SuddenChangeRecord.kt` | Crear: modelo de datos y función de composición de ID `generateSuddenChangeDocumentId`. |
| `app/src/main/java/com/example/prototipoeva3/parking/ParkingRepository.kt` | Modificar: incluir método `saveSuddenChangeRecord(...)` en la interfaz y en `FirebaseParkingRepository`. |
| `app/src/main/java/com/example/prototipoeva3/ui/screens/ValuesScreen.kt` | Modificar: incorporar lógica de detección de reducción abrupta (< 1s, caída ≥ 20 cm) e inserción en Firestore. |
| `app/src/main/res/values/strings.xml` | Modificar: añadir textos para la notificación de cambio brusco. |
| `app/src/test/java/com/example/prototipoeva3/parking/SuddenChangeRecordTest.kt` | Crear: pruebas unitarias para el modelo `SuddenChangeRecord` y composición de IDs. |
