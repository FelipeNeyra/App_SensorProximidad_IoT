# Tareas — Generación conceptual de pantallas

Las tareas están ordenadas por dependencia y acotadas para completarse en aproximadamente 10–20 minutos cada una. No agregar Firebase, autenticación real, conexión Bluetooth/Wi-Fi ni simulación de sensores: están fuera del alcance de esta spec.

## Aclaraciones y preparación

- [x] **T1. Confirmar niveles de Android y permisos aplicables.** Revisar configuración del proyecto y acordar los niveles mínimo/objetivo; identificar únicamente los permisos de ejecución realmente necesarios. Si falta la decisión, detener implementación y pedirla. **RF:** RF-1, RF-3.  
  **Hecho cuando:** queda documentado el nivel objetivo y la lista de permisos aplicables, o queda registrada la pregunta pendiente sin modificar el manifiesto. **Resultado actualizado por aclaración de alcance:** `minSdk 24`, `targetSdk 37`; se requiere `NEARBY_WIFI_DEVICES` en API 33+ para acceso conceptual futuro a dispositivos Wi-Fi cercanos; no se pide permiso de ubicación ni otro permiso en API inferiores.
- [x] **T2. Confirmar la disposición visual mínima.** Acordar una composición sencilla para Login, Configuración, Valores y bloqueo, sin incorporar bibliotecas. Si se solicita algo que contradice spec/plan, actualizar la spec antes de implementar. **RF:** RF-1, RF-5, RF-6.  
  **Hecho cuando:** las cuatro composiciones están descritas en español y no requieren dependencias nuevas. **Resultado:** propuesta sencilla aprobada; decisión anotada en spec y plan.
- [x] **T3. Preparar recursos de texto en español.** Añadir al archivo de strings los títulos, etiquetas y mensajes de solicitud/bloqueo que se usarán en las pantallas. **RF:** RF-1, RF-5, RF-6.  
  **Hecho cuando:** `strings.xml` contiene todos los textos previstos y no quedan cadenas visibles nuevas sin localizar.
- [x] **T4. Declarar los permisos aprobados.** Actualizar el manifiesto con la lista confirmada en T1, sin permisos de Firebase, sensores o capacidades no requeridas. **RF:** RF-1, RF-3.  
  **Hecho cuando:** el manifiesto declara solo los permisos aprobados y el proyecto compila. **Resultado actualizado:** el manifiesto declara únicamente `NEARBY_WIFI_DEVICES` con `neverForLocation`; compilación verificada nuevamente durante T14–T18.

## Lógica pura de permisos

- [x] **T5. Definir estados del flujo de permisos.** Crear `PermissionFlow.kt` y definir los estados `Ready`, `Requesting`, `BlockedRetryable` y `BlockedIndefinitely`, sin dependencias Android. **RF:** RF-2–RF-5.  
  **Hecho cuando:** los estados y sus datos necesarios se representan en un archivo Kotlin independiente de Android.
- [x] **T6. Implementar cálculo de permisos faltantes.** Añadir `permissionsToRequest(required, granted)` como función pura. **RF:** RF-1, RF-3.  
  **Hecho cuando:** la función devuelve exactamente los requeridos no concedidos y no consulta APIs ni estado externo.
- [x] **T7. Implementar transiciones de estado.** Añadir `nextPermissionState(current, result)` para concesión, primera denegación y denegación tras reintento. **RF:** RF-2–RF-5.  
  **Hecho cuando:** una primera denegación produce bloqueo recuperable y una nueva denegación tras reintento produce bloqueo indefinido, de forma determinista.
- [x] **T8. Implementar guardián de navegación y mensajes.** Añadir `canNavigate(state)` y `messageFor(state)` sin lógica de interfaz. **RF:** RF-1, RF-2, RF-5, RF-6.  
  **Hecho cuando:** solo `Ready` permite navegar y cada estado bloqueado selecciona el identificador de mensaje esperado.
- [x] **T9. Probar transiciones de permisos.** Crear `PermissionFlowTest.kt` y cubrir permisos faltantes, concesión, denegación inicial, reintento, segunda denegación y acceso a navegación. **RF:** RF-2–RF-5.  
  **Hecho cuando:** todas las pruebas unitarias de la lógica pura pasan y comprueban los casos anteriores.

## Pantallas

- [x] **T10. Crear pantalla de Login.** Crear `LoginScreen.kt` con título, correo, contraseña y acción de continuar; no validar ni enviar credenciales. **RF:** RF-6.  
  **Hecho cuando:** la pantalla muestra ambos campos y el botón en español, sin llamadas de autenticación o red.
- [x] **T11. Crear pantalla de Configuración.** Crear `ConfigurationScreen.kt` con acciones rotuladas Bluetooth y Wi-Fi, acceso a Valores y sección conceptual de vinculación por correo/teléfono con icono. **RF:** RF-6.  
  **Hecho cuando:** todos esos elementos se muestran en español y sus controles no afirman ni realizan conexiones externas.
- [x] **T12. Crear pantalla de Valores.** Crear `ValuesScreen.kt` con área identificada para valores de proximidad y acción de regreso; no generar valores ficticios ni controles de simulación. **RF:** RF-6.  
  **Hecho cuando:** la pantalla se muestra sin valores inventados y el control de regreso invoca la acción recibida.
- [x] **T13. Crear pantalla de permisos bloqueados.** Crear `PermissionBlockedScreen.kt` para explicar permisos denegados, su importancia y el bloqueo; distinguir estado recuperable de indefinido y ofrecer reintento solo en el recuperable. **RF:** RF-2–RF-5.  
  **Hecho cuando:** el bloqueo indefinido comunica explícitamente que la navegación está bloqueada y no muestra una acción de reintento incompatible con ese estado.

## Integración del flujo y navegación

- [x] **T14. Conectar solicitud inicial de permisos.** En `MainActivity.kt`, consultar el estado al iniciar, mostrar el mensaje y lanzar el mecanismo Android de solicitud cuando falten permisos. **RF:** RF-1.  
  **Hecho cuando:** con permisos faltantes se informa al usuario y, al confirmar la acción, se inicia la solicitud en tiempo de ejecución correspondiente a T1.
- [x] **T15. Conectar resultados y reintento de permisos.** Traducir el resultado del mecanismo Android a entradas de `nextPermissionState`; conectar la acción explícita de reintento mientras el estado sea recuperable. **RF:** RF-2–RF-4.  
  **Hecho cuando:** la primera denegación muestra bloqueo recuperable y el reintento solicita de nuevo sin crear un bucle automático.
- [x] **T16. Aplicar bloqueo central de navegación.** En `MainActivity.kt`, usar `canNavigate` para impedir acceso a Login, Configuración y Valores cuando los permisos no estén concedidos. **RF:** RF-2, RF-6.  
  **Hecho cuando:** ninguna acción ni ruta permite cambiar de pantalla en un estado bloqueado.
- [x] **T17. Integrar navegación de las tres pantallas.** Con permisos concedidos, conectar Login → Configuración → Valores y el regreso de Valores a Configuración. **RF:** RF-6.  
  **Hecho cuando:** las tres pantallas son archivos separados y cada ruta prevista puede recorrerse desde la interfaz.
- [x] **T18. Mantener visible el bloqueo indefinido.** Integrar `PermissionBlockedScreen` para el estado indefinido sin finalizar la aplicación. **RF:** RF-4, RF-5.  
  **Hecho cuando:** tras la segunda denegación la aplicación permanece ejecutándose, muestra el mensaje de bloqueo y no permite navegar.

## Verificación

- [x] **T19. Revisar idioma y límites de alcance.** Recorrer las pantallas y comprobar idioma español, ausencia de autenticación real, conexiones externas y valores simulados. **RF:** RF-1, RF-5, RF-6.  
  **Hecho cuando:** todas las etiquetas visibles están en español y no hay comportamiento fuera del alcance declarado. **Resultado:** inspección de pantallas, recursos y búsqueda de llamadas de Firebase/autenticación, red, Bluetooth/Wi-Fi o sensores; textos visibles centralizados en español, sin integración real ni valores simulados.
- [x] **T20. Ejecutar pruebas y verificar compilación.** Ejecutar pruebas unitarias y compilación del proyecto; corregir cualquier fallo antes de dar la tarea por terminada. **RF:** RF-1–RF-6.  
  **Hecho cuando:** las pruebas terminan en verde y el proyecto compila sin errores. **Resultado:** `:app:testDebugUnitTest` y `:app:assembleDebug` terminaron correctamente (BUILD SUCCESSFUL; tareas up-to-date).

**Limitación de verificación:** no se realizó recorrido interactivo en emulador/dispositivo; en particular, no se comprobó visualmente el diálogo real de `NEARBY_WIFI_DEVICES` ni las transiciones de denegación en el sistema operativo.
