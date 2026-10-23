# Tareas — Vinculación de Usuario de Seguridad y Registro de Alertas en Firestore

- [ ] 1. Modificar `firestore.rules` añadiendo regla para la colección `Alerta`.
- [ ] 2. Crear `AlertRecord.kt` con modelo de datos y mensaje de alerta por defecto.
- [ ] 3. Actualizar `AuthRepository.kt` y `FirebaseAuthRepository.kt` añadiendo `checkUserExistsInAuth(email)`.
- [ ] 4. Actualizar `ParkingRepository.kt` para insertar automáticamente en `'Alerta'` cuando `estado == "ADVERTENCIA"`.
- [ ] 5. Crear pruebas unitarias `AlertRecordTest.kt`.
- [ ] 6. Añadir recursos de texto en `strings.xml`.
- [ ] 7. Actualizar `LoginScreen.kt` con los botones "Vincular Usuario" y "Desvincular Usuario", validación de campos y verificación en Firebase Auth.
- [ ] 8. Ejecutar compilación y pruebas unitarias.
