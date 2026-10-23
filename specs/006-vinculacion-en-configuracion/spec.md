# Spec 00-6 — Traslado de Vinculación de Usuario a ConfigurationScreen

## Contexto y objetivo
Trasladar la sección e interfaz gráfica de vinculación de Usuario de Seguridad desde `LoginScreen.kt` hacia `ConfigurationScreen.kt`. Mantener la funcionalidad establecida: validación de campos obligatorios (correo y teléfono), verificación de existencia en Firebase Authentication antes de permitir la vinculación, estado condicional de botones ("Vincular Usuario" / "Desvincular Usuario") e inserción de alertas en Firestore asociadas al usuario de seguridad cuando ocurra un evento con estado `'ADVERTENCIA'`.

## Historias de usuario
- **HU-1**: Como usuario quiero administrar la vinculación y desvinculación de mi usuario de seguridad desde la pantalla de Configuración (`ConfigurationScreen.kt`).
- **HU-2**: Como usuario quiero que `LoginScreen.kt` se mantenga enfocada únicamente en el inicio de sesión.
- **HU-3**: Como usuario quiero que se valide la existencia del usuario de seguridad en Firebase Auth desde `ConfigurationScreen.kt`.

## Requisitos funcionales
- **RF-1**: LA INTERFAZ DE VINCULACIÓN DE USUARIO DE SEGURIDAD SE UBICARÁ EN `ConfigurationScreen.kt`.
- **RF-2**: `LoginScreen.kt` ELIMINARÁ LOS CAMPOS Y BOTONES DE VINCULACIÓN DE USUARIO DE SEGURIDAD.
- **RF-3**: SE CONSERVARÁ LA VALIDACIÓN DE CAMPOS OBLIGATORIOS (CORREO Y TELÉFONO) Y LA VERIFICACIÓN EN FIREBASE AUTHENTICATION ANTES DE REALIZAR LA VINCULACIÓN.
- **RF-4**: SE MANTENDRÁ LA GENERACIÓN DE DOCUMENTOS EN LA COLECCIÓN `'Alerta'` EN FIRESTORE CON EL CORREO DEL USUARIO VINCULADO PARA ESTADOS DE `'ADVERTENCIA'`.
