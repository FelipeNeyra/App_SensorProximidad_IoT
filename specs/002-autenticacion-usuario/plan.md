# Plan — Autenticación de usuario

## Alcance, supuestos y trazabilidad

Este plan implementa el ingreso con Firebase Authentication, la comprobación de conectividad, la gestión de sesión y el flujo de permisos descritos por `spec.md`. Se conserva el flujo de pantallas y permisos de `001-generacion-pantallas`; el guardián impide acceder a pantallas protegidas mientras no se autentique. No incluye Firestore/Realtime Database, operaciones SQL Connect/Data Connect, alta de usuarios, segundo factor ni sensores. Se permite únicamente la dependencia de compilación para el conector Data Connect generado que ya existe en el repositorio; no se invoca desde esta funcionalidad.

**Permiso de Internet:** en Android `INTERNET` es un permiso normal de manifiesto, se concede al instalar y no se puede solicitar en tiempo de ejecución. `ACCESS_NETWORK_STATE` también es normal y permite observar conectividad, no garantiza acceso a Internet. Por tanto no existe un diálogo de autorización para Internet ni una denegación de ese permiso en ejecución. RF-1/RF-2 se implementan solicitando automáticamente solo permisos restringidos que efectivamente apliquen a la conexión; se mantiene el permiso de red declarado y se informa de restricciones/desconexión. No se bloqueará la aplicación por una solicitud Android imposible. Este matiz queda explícito al usuario.

Se conserva `minSdk 24`, `targetSdk 37` y la configuración existente de `NEARBY_WIFI_DEVICES` para la capacidad conceptual de Wi-Fi de la spec anterior. Firebase Authentication no necesita ese permiso de dispositivo cercano: no se solicita para autenticar. La aplicación continúa ejecutándose si faltan permisos o conectividad, pero no habilita acceso autenticado a pantallas protegidas (RF-5, RNF-6).

## Archivos que se crean o modifican

| Archivo | Acción y responsabilidad | RF/RNF |
|---|---|---|
| `app/src/main/AndroidManifest.xml` | Modificar: declarar `INTERNET` y `ACCESS_NETWORK_STATE` (permisos normales), conservar `NEARBY_WIFI_DEVICES` con su límite de API/uso existente. No inventar diálogos de permiso para permisos normales. | RF-1, RF-4, RF-5, RNF-2 |
| `app/build.gradle.kts`, `build.gradle.kts`, `gradle/libs.versions.toml` | Conservar Google Services/Firebase BoM y `firebase-auth`. Para el conector Data Connect ya generado: añadir Firebase Data Connect SDK y Kotlin serialization (plugin/runtime) y alinear Kotlin a 2.4.0, requerido por metadatos de dependencias Firebase actuales. No se usa el conector ni se añaden operaciones de datos a la funcionalidad de autenticación. | RF-3, RF-6, RF-7 (compilación) |
| `app/google-services.json` | Añadir como configuración local del proyecto Firebase Android, con `project_info` y cliente que corresponda al `applicationId`. No versionar credenciales privadas; este archivo contiene configuración cliente identificadora, no secreto de servidor. | RF-3 |
| `app/src/main/java/com/example/prototipoeva3/MainActivity.kt` | Modificar: integrar Login, estados de autenticación/conectividad y guardián central; iniciar la observación de estado al abrir. Adaptar APIs del sistema, no albergar reglas de decisión. Conectar callbacks de login/logout y ciclo de vida de la aplicación. | RF-1–RF-7, RNF-1–RNF-2, RNF-6 |
| `app/src/main/java/com/example/prototipoeva3/auth/AuthRepository.kt` | Crear: adaptador Firebase Authentication para iniciar sesión, restaurar/observar usuario autenticado y cerrar sesión. Recibe credenciales en memoria solo durante la petición; nunca las persiste ni registra. Devuelve resultados tipados sin exponer textos crudos del SDK a la UI. | RF-3–RF-7, RNF-1, RNF-3–RNF-5 |
| `app/src/main/java/com/example/prototipoeva3/auth/AuthFlow.kt` | Crear: tipos de estado y funciones puras de validación, transición, clasificación de resultado/errores y control de vencimiento de sesión. Sin Android, Firebase, I/O, DOM ni estado global. | RF-3–RF-7, RNF-3–RNF-6 |
| `app/src/main/java/com/example/prototipoeva3/auth/ConnectivityMonitor.kt` | Crear: adaptador Android `ConnectivityManager` para leer/observar conectividad; informar conectividad como dato al flujo. El estado conectado no se interpretará como garantía de que Firebase esté disponible. | RF-4–RF-5, RNF-5 |
| `app/src/main/java/com/example/prototipoeva3/auth/SessionLifecycle.kt` | Crear: coordinación Android del ciclo de vida y temporizador de cierre al pasar la app a segundo plano/cerrarse; cancelar vencimiento cuando vuelve a interactuar antes de cinco minutos. No guardar contraseña ni token manualmente. | RF-6–RF-7, RNF-3 |
| `app/src/main/java/com/example/prototipoeva3/ui/screens/LoginScreen.kt` | Modificar: formulario de correo/contraseña, envío automático al pulsar ingresar, estado de espera y mensajes genéricos en español; limpiar campos/estado sensible tras resultado. No revelar si correo existe ni mostrar qué campo es incorrecto. | RF-3–RF-5, RNF-1, RNF-3–RNF-5, RNF-7 |
| `app/src/main/java/com/example/prototipoeva3/ui/screens/PermissionBlockedScreen.kt` | Modificar: explicación comprensible de permisos restringidos realmente aplicables, estado de conectividad y acción de reintento/configuración cuando el SO lo permita; la app permanece operativa en esta vista. | RF-1–RF-2, RF-5, RNF-2, RNF-5–RNF-7 |
| `app/src/main/res/values/strings.xml` | Modificar: centralizar mensajes españoles genéricos para campos vacíos, credenciales no válidas, sin Internet/servicio, permisos y sesión expirada. No incluir valores de entrada del usuario en mensajes. | RF-1–RF-5, RF-7, RNF-4–RNF-5, RNF-7 |
| `app/src/test/java/com/example/prototipoeva3/auth/AuthFlowTest.kt` | Crear: pruebas unitarias deterministas para validación, transición de autenticación, clasificación de errores y límites de sesión. Firebase y Android se sustituyen por resultados/valores de entrada, no se simulan credenciales reales. | RF-3–RF-7, RNF-3–RNF-6 |
| `app/src/test/java/com/example/prototipoeva3/auth/ConnectivityFlowTest.kt` | Crear: pruebas de decisiones puras para conectividad/permisos y elegibilidad de petición, incluyendo estados offline y denegación; ampliar las pruebas de permisos existentes si se comparte su flujo. | RF-1–RF-5, RNF-2, RNF-5–RNF-6 |

Si los archivos reales de UI o el patrón de navegación difieren de la spec 001, se adapta la integración sin duplicar pantallas ni reemplazar componentes existentes. No se modifica `local.properties` ni se incluye una clave de cuenta de servicio.

## Funciones puras de lógica necesarias

En `AuthFlow.kt` (tipos sencillos Kotlin; lógica aislada de SDK/Android):

- `validateCredentials(email, password)`: resultado `Empty`, `InvalidFormat` o `Valid`; solo validación local básica, sin afirmar que un usuario existe (casos vacíos/invalidos, RNF-4; mensajes RNF-5).
- `authStateAfter(result)`: transforma un resultado abstracto de autenticación a `Authenticated`, `InvalidCredentials`, `Unavailable` o `UnexpectedFailure`; nunca incluye los valores de correo/contraseña ni mensajes técnicos del proveedor (RF-3–RF-5, RNF-4–RNF-5).
- `canSubmit(credentialsState, connectivityState, permissionState)`: habilita el envío cuando los campos tienen formato aceptable y los permisos aplicables permiten operar. Si no hay conectividad, explicar estado sin cerrar la aplicación; ante un intento explícito se puede efectuar la petición para que el proveedor confirme el resultado, si el usuario pide ingresar (RF-3–RF-5, RNF-1).
- `messageForAuthState(state)`: produce un identificador de recurso localizado en español; errores de credenciales se mantienen genéricos y no se indica qué credencial está mal (RNF-4–RNF-5, RNF-7).
- `sessionAction(state, event, now)`: produce renovar/cancelar/iniciar temporizador o cerrar sesión. Al volver a haber interacción dentro de cinco minutos se conserva la sesión y se cancela la expiración; al cumplirse el límite sin interacción se pide sign-out (RF-6–RF-7).
- `canEnterProtectedScreens(authState, permissionState)`: verdadero solo para usuario autenticado y permisos requeridos concedidos; jamás permite el acceso si no se validó la identidad (RF-5, RNF-6).

El adaptador puede reportar resultados concretos del SDK, pero las funciones puras no consultan Firebase, reloj del sistema ni conectividad. La solicitud Android de permisos permanece en el adaptador de plataforma, y su resultado se introduce como dato puro al flujo (RF-1–RF-2, RNF-2).

## Permisos de aplicación y credenciales/configuración Firebase

### Permisos

- `android.permission.INTERNET`: declarar en manifiesto. Es permiso normal, concedido durante instalación; **no** lanzar solicitud runtime (RF-1, RNF-2).
- `android.permission.ACCESS_NETWORK_STATE`: declarar para observar cambios de red y mostrar estado; normal, no requiere diálogo runtime (RF-4–RF-5).
- `NEARBY_WIFI_DEVICES`: conservar solo para el flujo de Wi-Fi cercano descrito en la spec 001, con solicitud runtime donde aplica (API 33+ y uso correspondiente); no es requisito de Firebase Authentication. Si está denegado, mostrar por qué afecta la capacidad Wi-Fi próxima sin terminar la app (RF-1–RF-2, RF-5).
- No pedir ubicación, contactos ni Bluetooth para autenticar: son innecesarios para los RF de esta spec.

### Configuración y credenciales

- Proyecto Firebase existente y activo, con aplicación Android registrada exactamente como `com.example.prototipoeva3` (verificar `applicationId` antes de descargar configuración).
- Habilitar el proveedor **Email/Password** en Firebase Authentication. La aplicación no crea usuarios, según fuera de alcance; se requieren cuentas de prueba/usuario creadas por una vía administrativa autorizada.
- `app/google-services.json` correspondiente a ese registro y plugin Google Services (ya declarado). Contiene identificadores/configuración pública de cliente; protegerlo según política del repositorio, sin confundirlo con secreto.
- No se requiere clave API secreta, contraseña de proyecto ni cuenta de servicio dentro de la app para Firebase Authentication cliente. Las cuentas de usuario se introducen en pantalla y no se almacenan.
- Para validar configuración/proveedor en consola/MCP se necesita acceso de operador autorizado al proyecto Firebase; el MCP puede inspeccionar proyecto/configuración/rules si tiene permisos, pero no sustituye una prueba de inicio de sesión de cliente. No incluir tokens de MCP, claves privadas ni service account en Git.

## Conexión y flujo de Firebase

1. Al iniciar, crear/obtener la instancia cliente de Firebase Authentication usando la configuración del proyecto incluida por Google Services y observar el usuario actual restaurado por Firebase. Sesión existente válida lleva a la ruta protegida; no se vuelve a pedir contraseña (RF-6).
2. Ante acción de ingreso, comprobar campos vacíos/formato y estado de permisos aplicables. Enviar solicitud automáticamente a `signInWithEmailAndPassword(email, password)`; no hay endpoint propio ni escritura en Firestore (RF-3, RNF-1).
3. Mantener correo/contraseña solo en memoria durante la solicitud; no usar preferencias, base local, logs, analytics ni argumentos persistidos. Limpiar contraseña en éxito/error y al abandonar el formulario (RNF-3).
4. Mapear respuesta exitosa a autenticado; Firebase conserva la sesión mediante su mecanismo SDK. Mapear credenciales erróneas a mensaje genérico. Mapear timeout, ausencia de red, indisponibilidad/problema de Firebase y fallos inesperados a mensajes diferenciados de conectividad/servicio sin revelar valores o detalles sensibles (RF-3–RF-5, RNF-4–RNF-5).
5. Si conectividad indica offline, mantener UI en ejecución y explicar que no se puede validar; no permitir pantallas protegidas. Si la conexión cae durante la petición, resolver por resultado de Firebase y mostrar error genérico. ConnectivityManager es una señal informativa, no sustituye la respuesta de Authentication (RF-4–RF-5, RNF-6).
6. En sign-out explícito invocar `FirebaseAuth.signOut()` y volver a Login. En segundo plano/salida, registrar un plazo de cinco minutos; cancelar si el usuario reanuda/interactúa antes del límite; si se alcanza sin interacción, invocar sign-out y exigir ingreso siguiente vez. Debe persistir solo el instante/estado mínimo de vencimiento, no credenciales. Android puede terminar el proceso; al reabrir comparar vencimiento antes de admitir la sesión, sin depender de que un temporizador del proceso sobreviva (RF-6–RF-7).

## Decisiones técnicas y alternativas descartadas

| Decisión | Justificación | Alternativa descartada |
|---|---|---|
| Firebase Authentication Android SDK directamente desde la app mediante `firebase-auth` y configuración Google Services existente. | Es el proveedor explícito de la spec y el material del proyecto lo selecciona para acceso seguro; evita backend no especificado (RF-3). | Backend propio/REST implementado en esta etapa: agrega componente y manejo de secretos fuera de alcance. |
| Declarar `INTERNET`/`ACCESS_NETWORK_STATE` y no fingir solicitud runtime para ellos. Solicitar solo permisos realmente restringidos y aplicables. | Son permisos normales Android y el sistema no presenta prompt runtime; respeta plataforma y evita un flujo engañoso (RF-1–RF-2, RNF-2). | Mostrar un diálogo propio como si fuera permiso de sistema o pedir permisos innecesarios. |
| Separar reglas puras (`AuthFlow`) del adaptador Firebase, conectividad Android y UI. | Respeta la constitución y permite probar decisiones sin DOM/I/O; los SDK quedan en bordes (RF-3–RF-7). | Colocar llamadas Firebase y reglas de negocio directamente en composables. |
| Delegar persistencia de sesión al SDK y controlar explícitamente expiración por inactividad de cinco minutos. | Cumple sesión persistente hasta logout y excepción temporal al abandonar app; tras muerte de proceso se valida plazo antes de permitir acceso (RF-6–RF-7). | Guardar contraseñas/tokens manualmente o confiar solo en un temporizador en memoria que se pierde al morir el proceso. |
| Usar conectividad como diagnóstico, pero usar resultado de Firebase como autoridad sobre disponibilidad y credenciales. | Una red conectada puede no tener Internet/Firebase; el flujo distingue ambos fallos sin bloquear la app (RF-4–RF-5). | Considerar que `NET_CAPABILITY_INTERNET` garantiza el servicio o mantener una conexión persistente no requerida. |
| Mensaje genérico común para credenciales incorrectas, sin revelar el campo erróneo o existencia de cuenta. | Reduce filtración de información y cumple RNF-4/RNF-5; la UI mantiene los campos corregibles. | Mostrar literalmente códigos/excepciones del SDK o confirmar que un correo existe. |
| No añadir Firebase Analytics, Firestore ni Realtime Database al flujo de autenticación. | El alcance excluye persistencia de datos; se minimiza recopilación y superficie de permisos. | Enviar credenciales/eventos a otros servicios o implementar base de datos en esta spec. |
| Incluir dependencias Firebase Data Connect y Kotlin serialization solo para compilar el conector Kotlin generado ya presente, sin invocarlo en el flujo de autenticación. | El código generado existente referenciaba tipos del SDK, serializadores y metadatos Kotlin que no estaban disponibles; las pruebas no podían compilar. Se usa `firebase-dataconnect:18.0.1`, Kotlin/serialization plugin `2.4.0` y serialization JSON `1.9.0`; se mantiene Data Connect fuera del comportamiento funcional. | Borrar o reescribir fuentes generadas (cambio destructivo y ajeno a la necesidad), o habilitar operaciones SQL Connect en autenticación (fuera de alcance). |

## Cobertura de requisitos

- **RF-1:** permisos normales de Internet declarados; la solicitud runtime solo se hace para permisos restringidos aplicables. Manifiesto, adaptador de permisos y explicación al usuario.
- **RF-2:** reintento de permisos restringidos cuando aplique; tras denegación permanente se informa cómo habilitarlo en Ajustes, sin prometer prompt imposible. Manifiesto, `PermissionBlockedScreen`, recursos.
- **RF-3:** envío al Firebase Authentication al intentar ingreso; `AuthRepository`, `AuthFlow`, Login.
- **RF-4:** clasificación y mensaje para falta de conexión/servicio Firebase; `ConnectivityMonitor`, `AuthRepository`, recursos.
- **RF-5:** ninguna caída/denegación cierra la app; estados de error permanecen en UI, pero las pantallas protegidas requieren identidad validada; flujo central.
- **RF-6:** SDK conserva sesión hasta logout explícito; restauración al inicio y acción de cerrar sesión en flujo autenticado.
- **RF-7:** plazo de cinco minutos sin interacción tras cierre/segundo plano; vencimiento comprobado al reanudar/reabrir y sign-out al cumplirse.
- **RNF-1–RNF-2:** solicitud automática al enviar credenciales; solicitud de permisos del sistema automática solo si se necesitan permisos runtime aplicables.
- **RNF-3–RNF-4:** no persistir credenciales; errores no revelan campo erróneo ni valores.
- **RNF-5–RNF-7:** mensajes objetivos, aplicación disponible pero rutas protegidas denegadas sin autenticación; recursos españoles.

## Condiciones de validación previas a implementación

Confirmar que el proyecto Firebase indicado por el usuario es el correcto, que Email/Password está habilitado, que `google-services.json` corresponde al `applicationId`, y que existe una cuenta de prueba creada por canal autorizado. No se debe fabricar configuración ni secretos si falta alguno de estos datos; solicitarlo antes de implementar/probar la conexión real. La prueba automática de lógica debe pasar antes de avanzar; pruebas de integración usan proyecto/cuenta de prueba, nunca credenciales reales de usuario.
