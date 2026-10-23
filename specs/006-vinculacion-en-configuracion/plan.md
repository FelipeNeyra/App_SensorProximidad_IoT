# Plan — Traslado de Vinculación de Usuario a ConfigurationScreen

## Alcance y diseño técnico

### Cambios en UI
1. **`LoginScreen.kt`:**
   - Eliminar sección de vinculación de usuario de seguridad, simplificando el formulario de inicio de sesión.
2. **`ConfigurationScreen.kt`:**
   - Incorporar parámetros `onCheckSecurityUserExists`, `onLinkSecurityUser`, `onUnlinkSecurityUser`, `linkedSecurityEmail`.
   - Agregar los botones "Vincular Usuario" / "Desvincular Usuario" bajo los campos de correo y teléfono.
   - Aplicar lógica de validación de campos vacíos y verificación en Firebase Auth.
3. **`MainActivity.kt`:**
   - Conectar los callbacks de vinculación de usuario de seguridad hacia `ConfigurationScreen`.

---

## Archivos a modificar

| Archivo | Acción y responsabilidad |
|---|---|
| `app/src/main/java/com/example/prototipoeva3/ui/screens/LoginScreen.kt` | Modificar: eliminar componentes UI de vinculación de usuario de seguridad. |
| `app/src/main/java/com/example/prototipoeva3/ui/screens/ConfigurationScreen.kt` | Modificar: integrar botones de "Vincular Usuario" / "Desvincular Usuario", validación de campos y callbacks de verificación. |
| `app/src/main/java/com/example/prototipoeva3/MainActivity.kt` | Modificar: pasar los handlers de vinculación a `ConfigurationScreen`. |
