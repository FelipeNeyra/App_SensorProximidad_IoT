# Plan — Generación conceptual de pantallas

## Alcance y trazabilidad

Este plan concreta únicamente las pantallas, el flujo de permisos y la navegación descritos en la spec activa. No incluye conexión a Firebase, autenticación real, conexión Bluetooth/Wi-Fi ni simulación de sensores. La pantalla de credenciales es una maqueta de ingreso y no verifica credenciales (RF-6; fuera de alcance de la spec).

Configuración confirmada: `minSdk 24` y `targetSdk 37`. Por aclaración aprobada, se declara y solicita `NEARBY_WIFI_DEVICES` en API 33+ para representar el permiso de acceso a dispositivos Wi-Fi cercanos; se usa `neverForLocation` y no se implementa descubrimiento ni conexión. En API inferiores la interacción Wi-Fi es conceptual y no se pide un permiso alternativo. Bluetooth sigue conceptual y no pide permisos. No se añaden dependencias externas.

## Archivos que se crean o modifican

| Archivo | Acción y responsabilidad | RF |
|---|---|---|
| `app/src/main/java/com/example/prototipoeva3/MainActivity.kt` | Modificar para ser el punto de entrada Android y anfitrión del flujo Compose. Inicializar estado de permisos y mostrar Login, Configuración, Valores o bloqueo. Enlazar el resultado de la API Android de permisos con la lógica pura, sin poner reglas de decisión en los composables. | RF-1–RF-6 |
| `app/src/main/java/com/example/prototipoeva3/permissions/PermissionFlow.kt` | Crear. Funciones puras y tipos de estado para decidir solicitud, navegación bloqueada, reintento y bloqueo indefinido según permisos concedidos/denegados. | RF-2–RF-5 |
| `app/src/main/java/com/example/prototipoeva3/ui/screens/LoginScreen.kt` | Crear. Pantalla de ingreso conceptual con campos de correo y contraseña; sin autenticación ni envío de datos. Acción de continuar sujeta al guardián de permisos. | RF-1, RF-2, RF-6 |
| `app/src/main/java/com/example/prototipoeva3/ui/screens/ConfigurationScreen.kt` | Crear. Presentar controles Bluetooth y Wi-Fi, acceso a Valores y vínculo conceptual de usuario de seguridad (correo/teléfono), conforme a los componentes conocidos. Los controles no implementan conexiones externas. | RF-2, RF-6 |
| `app/src/main/java/com/example/prototipoeva3/ui/screens/ValuesScreen.kt` | Crear. Pantalla conceptual de valores de sensores, sin simular ni inventar datos en esta spec; permitir volver a Configuración cuando la navegación esté habilitada. | RF-2, RF-6 |
| `app/src/main/java/com/example/prototipoeva3/ui/screens/PermissionBlockedScreen.kt` | Crear. Mostrar explicación en español del permiso denegado, su importancia y que la navegación está bloqueada; cuando el bloqueo sea indefinido, mostrarlo explícitamente. | RF-2–RF-5 |
| `app/src/main/AndroidManifest.xml` | Declarar `NEARBY_WIFI_DEVICES` con `neverForLocation` para el flujo aprobado en API 33+. No declarar capacidades o permisos de Firebase/sensores ajenos al alcance. | RF-1–RF-4 |
| `app/src/main/res/values/strings.xml` | Modificar para centralizar textos visibles en español: solicitud, explicación, bloqueo, formularios y controles de navegación. | RF-1, RF-5, RF-6 |
| `app/src/test/java/com/example/prototipoeva3/permissions/PermissionFlowTest.kt` | Crear. Pruebas unitarias de transiciones puras de permiso y de la habilitación/bloqueo de navegación. | RF-2–RF-5 |

No se modifican Gradle ni se incorporan bibliotecas por esta planificación: la spec no resuelve la duda sobre dependencias externas. Compose y los componentes Android ya configurados se reutilizan. Si se requiere una dependencia o permiso nuevo no cubierto por el material conocido, se detiene el trabajo y se solicita aprobación/documentación según la constitución.

## Lógica pura necesaria

En `PermissionFlow.kt`, funciones sin Android, DOM, I/O ni estado global:

- `permissionsToRequest(required, granted)`: devuelve los permisos faltantes que corresponde solicitar (RF-1, RF-3).
- `nextPermissionState(current, result)`: transforma de manera determinista el resultado de la solicitud en estado `Ready`, `Requesting`, `BlockedRetryable` o `BlockedIndefinitely`. `Requesting` distingue la solicitud inicial del reintento; el resultado recibido es un mapa permiso/concedido. Una denegación inicial bloquea y habilita reintento; si tras volver a solicitar se deniega de nuevo, fija el bloqueo indefinido, sin bucle de diálogos automático (RF-2–RF-5).
- `canNavigate(state)`: solo devuelve verdadero en `Ready` (RF-2, RF-6).
- `messageFor(state)`: selecciona el identificador de mensaje español apropiado para explicar solicitud, denegación o bloqueo indefinido (RF-1, RF-5).

La API Android realiza la solicitud real y proporciona sus resultados; no se simula dentro de estas funciones. Si Android no permite volver a mostrar el diálogo del sistema tras una denegación permanente, la pantalla de bloqueo conserva el estado y comunica la situación; no se promete un diálogo que el sistema operativo no pueda presentar (RF-3–RF-5). El plan no introduce salida por Ajustes ni un permiso alternativo no especificado.

La recepción/envío de datos externos no forma parte de esta spec. En cualquier fase que sí se incorpore, deberá permanecer en funciones puras separadas de interfaz y conectarse mediante API externa/credenciales, de acuerdo con la constitución.

## Presentación e interacción

- **Inicio/permisos:** al abrir, evaluar los permisos requeridos. En API 33+, si falta `NEARBY_WIFI_DEVICES`, mostrar una explicación y acción explícita para lanzar la solicitud del sistema; no habilitar rutas mientras el estado no sea `Ready` (RF-1, RF-2). En API inferiores, la lista requerida por esta capacidad conceptual está vacía.
- **Denegación recuperable:** mostrar una vista de bloqueo con explicación clara y acción explícita para volver a solicitar. Si se conceden, continuar al Login; si se vuelven a denegar, pasar al bloqueo indefinido (RF-2–RF-4).
- **Bloqueo indefinido:** mantener la aplicación ejecutándose, mostrar el mensaje persistente de bloqueo y explicar por qué se necesitan los permisos. No permitir navegar a las pantallas protegidas (RF-4, RF-5; disponibilidad de la spec).
- **Login:** título, campo de correo, campo de contraseña y botón de continuar. El botón no valida contra Firebase ni autentica; sirve solo para avanzar en el prototipo si permisos están concedidos (RF-6; credenciales reales fuera de alcance).
- **Configuración:** botones/acciones rotulados Bluetooth y Wi-Fi, acceso a Valores, y sección de vinculación conceptual del usuario de seguridad con correo/teléfono e icono gráfico. Bluetooth informa que es conceptual; Wi-Fi informa permiso otorgado/conexión futura. No afirmar que se estableció conexión o vínculo externo (RF-6).
- **Valores:** área identificada para valores de proximidad y acción de regreso a Configuración. No mostrar valores inventados ni controles de simulación, pues la spec excluye simulación de sensores (RF-6, dentro de los límites de alcance).
- Todas las pantallas y mensajes estarán en español. La navegación visible usa controles explícitos y queda inhabilitada/guardada por el estado central de permisos (RF-2, RF-6).

### Disposición aprobada (T2)

- Login: formulario centrado con correo, contraseña y continuar.
- Configuración: controles en columna para Bluetooth, Wi-Fi, Valores y vinculación del usuario de seguridad.
- Valores: área identificada para valores de proximidad y control para volver a Configuración.
- Bloqueo: explicación del permiso/estado, con reintento solo en bloqueo recuperable.
- Sin dependencias adicionales; los textos se centralizan en `strings.xml`.

Los composables de estas pantallas reciben acciones como callbacks, pero no realizan autenticación, conectividad ni navegación por sí mismos; la integración de callbacks corresponde a tareas posteriores.

## Decisiones técnicas y alternativas descartadas

| Decisión | Justificación | Alternativa descartada |
|---|---|---|
| Separar el flujo puro de permisos de la interfaz y de la API Android. | Cumple la constitución: reglas comprobables sin DOM/interfaz y el sistema operativo queda como adaptador de permisos (RF-1–RF-5). | Consultar permisos y decidir transiciones directamente dentro de cada pantalla; mezcla responsabilidades y dificulta probar la lógica. |
| Usar el mecanismo de permisos en tiempo de ejecución de Android solo para los permisos aplicables. | La spec exige solicitud en tiempo real, pero la plataforma y versión determinan cuáles aplican (RF-1, RF-3). | Suponer que Bluetooth/Wi-Fi siempre requieren los mismos permisos o solicitar permisos no necesarios. |
| Mantener las tres pantallas como archivos/pantallas separados y centralizar el guardián de navegación en `MainActivity`. | Satisface el criterio de finalización y permite bloquear todas las rutas de forma coherente (RF-2, RF-6). | Un único composable grande con navegación y permisos duplicados en cada botón. |
| Mantener Login y controles externos como prototipo visual, sin Firebase, verificación de credenciales ni conexión real. | Respeta expresamente el alcance excluido por la spec (RF-6 como navegación; integraciones fuera de alcance). | Conectar Firebase, validar credenciales o integrar dispositivos ahora; sería trabajo no autorizado por la spec. |
| No agregar dependencias externas hasta resolver la duda abierta y validar los materiales permitidos. | La constitución exige correspondencia con los conceptos conocidos y aprobación/documentación de incorporaciones (RF-1–RF-6). | Añadir una biblioteca de navegación/permisos sin aprobación. |
| Usar textos en recursos Android en español y mostrar bloqueo explicativo sin cerrar la app. | Cumple idioma, mensaje de denegación y disponibilidad solicitada (RF-1, RF-5). | Cerrar la aplicación o dejar una pantalla muda al denegar permisos. |

## Cobertura resumida de RF

- **RF-1:** evaluación inicial, mensaje en español y solicitud Android; `PermissionFlow`, `MainActivity`, `PermissionBlockedScreen`, manifiesto y recursos.
- **RF-2:** estado bloqueado impide cualquier navegación; `PermissionFlow.canNavigate` y guardián central en `MainActivity`.
- **RF-3:** acción explícita de reintento vuelve a pedir permisos mientras el bloqueo sea recuperable; `PermissionFlow` y pantalla de bloqueo.
- **RF-4:** segunda denegación lleva a estado indefinido, sin reintento automático de navegación; `PermissionFlow` y pruebas.
- **RF-5:** vista persistente explica el bloqueo indefinido sin terminar la aplicación; `PermissionBlockedScreen` y recursos.
- **RF-6:** navegación entre Login, Configuración y Valores con permisos concedidos; pantallas separadas y guardián central. Las funciones externas y valores simulados permanecen fuera de alcance.

## Condiciones previas / aclaraciones necesarias

Decisiones resueltas: `minSdk 24`/`targetSdk 37`; solicitud de `NEARBY_WIFI_DEVICES` en API 33+ para acceso conceptual a dispositivos Wi-Fi cercanos (sin descubrimiento/conexión); disposición sencilla aprobada como se describe arriba. La duda sobre dependencias externas sigue sin requerir resolución porque este plan no añade ninguna. Si cambia esta capacidad, actualizar primero la spec.
