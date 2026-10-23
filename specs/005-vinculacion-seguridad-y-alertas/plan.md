# Plan — Vinculación de Usuario de Seguridad y Registro de Alertas en Firestore

## Alcance y diseño técnico

Este plan implementa la vinculación de usuario de seguridad con validación en Firebase Auth y la creación automática de documentos en la colección `'Alerta'` de Cloud Firestore.

### Reglas de Firestore
Añadir regla para `/Alerta/{documentId}` en `firestore.rules`.

### Verificación de Usuario en Firebase Auth
En `AuthRepository.kt`:
```kotlin
suspend fun checkUserExistsInAuth(email: String): Result<Boolean>
```
Utiliza `fetchSignInMethodsForEmail(email)` de Firebase Auth. Si retorna métodos de acceso no vacíos, el usuario está registrado.

### Registro en Colección `'Alerta'`
- **Estructura del documento en `'Alerta'`:**
  - `id`: String (patrón `alerta_<userId>_<timestampMs>`)
  - `userId`: String
  - `securityUserEmail`: String
  - `referenceDocumentId`: String
  - `referenceCollection`: String ("Aparcamiento" / "Cambio_Brusco")
  - `estado`: String ("ADVERTENCIA")
  - `mensaje`: String ("Prueba de mensaje de alerta - App Arduino.")
  - `timestamp`: `com.google.firebase.Timestamp`

En `ParkingRepository.kt`:
Al guardar un registro en `'Aparcamiento'` o `'Cambio_Brusco'`, si `estado == "ADVERTENCIA"`, se ejecuta automáticamente `saveAlertRecord(...)`.

### Interfaz de Usuario
- **`LoginScreen.kt` & `ConfigurationScreen.kt`:**
  - Sección de "Usuario de Seguridad" con campos para correo y teléfono.
  - Muestra botón "Vincular Usuario" (si no está vinculado) o "Desvincular Usuario" (si está vinculado).
  - Al hacer clic en "Vincular Usuario":
    1. Revisa que correo y teléfono no estén vacíos.
    2. Comprueba existencia en Firebase Auth.
    3. Si es válido, vincula y guarda en sesión/preferencias.

---

## Archivos a crear o modificar

| Archivo | Acción y responsabilidad |
|---|---|
| `firestore.rules` | Modificar: añadir regla de lectura/escritura para la colección `Alerta`. |
| `app/src/main/java/com/example/prototipoeva3/parking/AlertRecord.kt` | Crear: modelo de datos y función `generateAlertDocumentId`. |
| `app/src/main/java/com/example/prototipoeva3/auth/AuthRepository.kt` & `FirebaseAuthRepository.kt` | Modificar: añadir función `checkUserExistsInAuth(email)`. |
| `app/src/main/java/com/example/prototipoeva3/parking/ParkingRepository.kt` | Modificar: incluir creación automática de alerta cuando `estado == "ADVERTENCIA"`. |
| `app/src/main/java/com/example/prototipoeva3/ui/screens/LoginScreen.kt` | Modificar: incorporar sección de vinculación/desvinculación con botones condicionales. |
| `app/src/main/res/values/strings.xml` | Modificar: agregar textos para vinculación de usuario de seguridad y alertas. |
| `app/src/test/java/com/example/prototipoeva3/parking/AlertRecordTest.kt` | Crear: pruebas unitarias para el modelo `AlertRecord`. |
