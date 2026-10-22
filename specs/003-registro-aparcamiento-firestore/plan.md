# Plan — Registro de Seguimiento de Aparcamiento en Firestore

## Alcance y diseño técnico

Este plan implementa la captura y persistencia en Cloud Firestore de los eventos de variación de proximidad en la pantalla `ValuesScreen.kt`.

### ID de Documentos en Firestore
La composición del ID de documento para la colección `'Aparcamiento'` se estructurará como:
`aparcamiento_<userId>_<timestampMs>`

En caso de no haber sesión activa (o usuario anónimo), el fallback será:
`aparcamiento_anonimo_<timestampMs>`

### Esquema del Documento `'Aparcamiento'`
- `id`: String (identificador compuesto del documento)
- `userId`: String (UID de Firebase Auth)
- `userEmail`: String (Correo del usuario o "no_email")
- `distancia`: Int (distancia medida en cm, 0-100)
- `estado`: String ("PELIGRO", "ADVERTENCIA", "SEGURO")
- `ubicacionModo`: String ("Frontal", "Trasero")
- `nota`: String (texto descriptivo opcional ingresado por el usuario)
- `timestamp`: Long (milisegundos Unix)

### Componentes de la Rúbrica (Punto 2.1.1.2)
En `ValuesScreen.kt` se incorporarán/integrarán:
1. **Slider**: Control interactivo de variación de distancia (0 a 100 cm).
2. **Switch**: Interruptor para activar/desactivar el modo de guardado automático en Firestore.
3. **RadioButtons / FilterChips**: Selección interactiva de ubicación ("Sensor Frontal" / "Sensor Trasero").
4. **OutlinedTextField / EditText**: Campo para especificar nota o punto de aparcamiento (ej. "Estacionamiento A-1").
5. **Indicador de Estado de Seguimiento**: Texto y badges visuales con animación de color que muestran en tiempo real si el seguimiento está en curso o el estado de guardado.

---

## Archivos a crear o modificar

| Archivo | Acción y responsabilidad |
|---|---|
| `app/build.gradle.kts` | Modificar: añadir dependencia `implementation("com.google.firebase:firebase-firestore")`. |
| `firestore.rules` | Modificar: habilitar reglas de lectura/escritura para la colección `Aparcamiento`. |
| `app/src/main/java/com/example/prototipoeva3/parking/ParkingRecord.kt` | Crear: modelo de datos para el registro de aparcamiento y función pura `generateParkingDocumentId`. |
| `app/src/main/java/com/example/prototipoeva3/parking/ParkingRepository.kt` | Crear: interfaz e implementación concreta de repositorio Firestore para guardar documentos en `'Aparcamiento'`. |
| `app/src/main/java/com/example/prototipoeva3/ui/screens/ValuesScreen.kt` | Modificar: agregar el estado de seguimiento (`isTracking`), el manejo de `onValueChangeFinished`, el Switch de guardado, RadioButtons de zona, TextField de notas e indicador visual de estado. |
| `app/src/main/res/values/strings.xml` | Modificar: añadir cadenas traducidas al español para los nuevos elementos UI. |
| `app/src/test/java/com/example/prototipoeva3/parking/ParkingRecordTest.kt` | Crear: pruebas unitarias para la composición del ID y categorización del estado de distancia. |
