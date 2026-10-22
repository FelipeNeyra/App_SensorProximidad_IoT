# Tareas — Registro de Seguimiento de Aparcamiento en Firestore

## Configuración y Reglas de Firestore
- [x] 1. Añadir dependencia `firebase-firestore` a `app/build.gradle.kts`.
- [x] 2. Actualizar `firestore.rules` permitiendo acceso a la colección `Aparcamiento`.

## Lógica pura y Repositorio
- [x] 3. Crear `ParkingRecord.kt` con el modelo de datos y la función `generateParkingDocumentId`.
- [x] 4. Crear `ParkingRepository.kt` para interactuar con Cloud Firestore en la colección `'Aparcamiento'`.
- [x] 5. Crear pruebas unitarias `ParkingRecordTest.kt` para verificar la composición del ID y categorización del estado.

## Interfaz de Usuario y Rúbrica 2.1.1.2
- [x] 6. Añadir recursos de cadenas en español a `strings.xml`.
- [x] 7. Actualizar `ValuesScreen.kt` incorporando elementos interactivos (Slider, Switch, RadioButtons, OutlinedTextField, Indicadores de seguimiento) y la llamada a Firestore al soltar el Slider (`onValueChangeFinished`).
- [x] 8. Verificar la compilación del proyecto y ejecución de las pruebas unitarias.
