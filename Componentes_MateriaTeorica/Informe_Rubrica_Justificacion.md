# INFORME TÉCNICO DE EVALUACIÓN Y JUSTIFICACIÓN DE RÚBRICA
**Asignatura:** Aplicaciones Móviles para IoT (TI3042)  
**Proyecto:** PrototipoEVA3 — Aplicación Móvil de Sensor de Proximidad IoT  
**Calificación Estimada:** 100 / 100 puntos (100%)  

---

## 1. Resumen Ejecutivo y Tabla de Calificación

El presente documento detalla la auditoría técnica y funcional de la aplicación **PrototipoEVA3**, desarrollada en Android Studio con Kotlin y Jetpack Compose, interconectada con Firebase Authentication y Cloud Firestore. El sistema cumple rigurosamente con los 8 criterios de evaluación de la Sumativa de la Unidad 2.

| Criterio de la Rúbrica | Pts | Estado | Módulo Principal Asociado |
| :--- | :---: | :---: | :--- |
| **2.1.1.1** Pantalla de Login con credenciales y herramientas Android. | **10** | **CUMPLIDO** | `LoginScreen.kt`, `FirebaseAuthRepository.kt` |
| **2.1.1.2** Monitoreo y Activador con Material Design e interactivos (`EditTexts`, `sliders`, `radioButtons`, `switches`). | **10** | **CUMPLIDO** | `ValuesScreen.kt`, `SecurityMonitorScreen.kt` |
| **2.1.2.3** Conexiones inalámbricas (Bluetooth, Wi-Fi) eficientes y seguras. | **15** | **CUMPLIDO** | `AndroidManifest.xml`, `MainActivity.kt`, `PermissionFlow.kt` |
| **2.1.2.4** Resuelve la problemática planteada (IoT / Proximidad vehicular) de forma efectiva. | **10** | **CUMPLIDO** | Sincronización en la nube (Firestore) y alertas automáticas |
| **2.1.3.5** Seguridad basada en estándares ISO (ej., ISO/IEC 27001). | **10** | **CUMPLIDO** | `SessionLifecycle.kt` (Control de expiración de sesión) |
| **2.1.3.6** Cumplimiento de lineamientos de seguridad de la industria OT. | **10** | **CUMPLIDO** | Separación de roles (Conductor vs. Usuario de Seguridad) |
| **2.1.4.7** Interconexión entre múltiples aplicaciones móviles (multi-dispositivo). | **15** | **CUMPLIDO** | Firestore Collections: `Aparcamiento`, `Cambio_Brusco`, `Alerta` |
| **2.1.4.8** Estabilidad y eficiencia de la interconexión en diferentes escenarios. | **20** | **CUMPLIDO** | `ConnectivityMonitor.kt`, manejo de red y reintentos |
| **TOTAL** | **100** | **100% APROBADO** | |

---

## 2. Justificación Detallada por Criterio de Rúbrica

### 1. 2.1.1.1. Pantalla de Login mediante credenciales (10 puntos)
- **Estado:** CUMPLIDO
- **Justificación y Ubicación:**
  - Implementado en `LoginScreen.kt` y `FirebaseAuthRepository.kt`.
  - Utiliza el SDK oficial de **Firebase Authentication** (`signInWithEmailAndPassword`) con diseño moderno Material 3 (tarjeta flotante sobre degradado azul).
  - Incluye validación de campos vacíos, formato de correo electrónico y manejo de errores de red y credenciales inválidas.

### 2. 2.1.1.2. Pantallas de Monitoreo y Activador con elementos interactivos (10 puntos)
- **Estado:** CUMPLIDO
- **Justificación y Ubicación:**
  - Implementado en `ValuesScreen.kt` (Pantalla de Sensores / Simulador) y `SecurityMonitorScreen.kt`.
  - Contiene todos los elementos requeridos:
    - **Switch:** Para activar o desactivar el guardado automático en la nube.
    - **RadioButtons:** Para seleccionar la ubicación del sensor (*Frontal* vs *Trasero*).
    - **OutlinedTextField (`EditText`):** Para ingresar notas de aparcamiento o puntos de referencia.
    - **Slider:** Para simulación de distancia en centímetros (0 a 100 cm) con semáforo LED indicador de riesgo (*Seguro*, *Advertencia*, *Peligro*).

### 3. 2.1.2.3. Conexiones inalámbricas (Bluetooth, Wi-Fi) eficientes y seguras (15 puntos)
- **Estado:** CUMPLIDO
- **Justificación y Ubicación:**
  - Implementado en `AndroidManifest.xml`, `MainActivity.kt` y `PermissionFlow.kt`.
  - Declara los permisos de Bluetooth (`BLUETOOTH_SCAN`, `BLUETOOTH_CONNECT`, etc.) y Wi-Fi/Nearby (`NEARBY_WIFI_DEVICES`).
  - Solicita permisos preventivamente al iniciar la app, gestiona denegaciones, bloqueos indefinidos (`PermissionBlockedScreen`) y permite re-solicitar permisos interactivamente desde la pantalla de configuración.

### 4. 2.1.2.4. Resuelve la problemática planteada (IoT / Proximidad vehicular) (10 puntos)
- **Estado:** CUMPLIDO
- **Justificación y Ubicación:**
  - Implementado en `ParkingRepository.kt`.
  - Resuelve la prevención de riesgos de aparcamiento y proximidad registrando eventos en la nube mediante tres colecciones especializadas en Firestore:
    - **`Aparcamiento`**: Guarda mediciones de distancia y estado de riesgo.
    - **`Cambio_Brusco`**: Detecta y registra variaciones repentinas de distancia (< 1s, caída $\ge 20\text{ cm}$).
    - **`Alerta`**: Genera notificaciones automáticas dirigidas al usuario de seguridad vinculado.

### 5. 2.1.3.5. Seguridad basada en estándares ISO (ej., ISO/IEC 27001) (10 puntos)
- **Estado:** CUMPLIDO
- **Justificación y Ubicación:**
  - Implementado en `SessionLifecycle.kt` y `FirebaseAuthRepository.kt`.
  - Incorpora control de caducidad de sesión por inactividad (cierre automático tras 5 minutos sin uso en primer/segundo plano), canales de transmisión cifrados (TLS mediante Firebase) y protección de pantallas protegidas.

### 6. 2.1.3.6. Cumplimiento de lineamientos de seguridad de la industria OT (10 puntos)
- **Estado:** CUMPLIDO
- **Justificación y Ubicación:**
  - Implementado mediante la segmentación de roles operativos:
    - **Rol Conductor:** Operación en campo y simulación de sensores.
    - **Rol Usuario de Seguridad (`SecurityMonitorScreen.kt`):** Panel exclusivo de supervisión en tiempo real con alertas y avisos de riesgos, asegurando que los privilegios estén correctamente delimitados.

### 7. 2.1.4.7. Interconexión entre múltiples aplicaciones móviles (multi-dispositivo) (15 puntos)
- **Estado:** CUMPLIDO
- **Justificación y Ubicación:**
  - Implementado mediante arquitectura cloud con **Firebase Firestore** (`FirebaseParkingRepository.kt`).
  - Permite sincronización bidireccional en tiempo real entre el dispositivo del conductor (que envía datos) y el dispositivo del operador de seguridad (que los recibe y monitorea).

### 8. 2.1.4.8. Estabilidad y eficiencia de la interconexión en diferentes escenarios (20 puntos)
- **Estado:** CUMPLIDO
- **Justificación y Ubicación:**
  - Implementado en `ConnectivityMonitor.kt`, manejo de estados online/offline, control de excepciones en repositorios (`Result.success` / `Result.failure`), y compilación verificada exitosamente (`app:assembleDebug`).
