# Tareas — Autenticación de usuario

Cada tarea está acotada para completarse en un máximo aproximado de 10 minutos. Ejecutarlas en orden; una tarea bloqueada por configuración o decisión pendiente no debe sustituirse por datos inventados.

## Configuración y permisos

- [x] 1. Revisar el `applicationId` y `minSdk`/`targetSdk` existentes frente al plan, sin cambiarlos. **RF:** RF-3.
  - Hecho cuando: queda confirmado en la revisión que el identificador Android es `com.example.prototipoeva3` y se registra cualquier discrepancia antes de continuar.
- [x] 2. Confirmar con el responsable qué proyecto Firebase se usará y si Email/Password está habilitado. **RF:** RF-3.
  - Hecho cuando: el proyecto activo del MCP es `bdd-appsensoresiot` (`BDD-AppSensoresIoT`, estado `ACTIVE`) y su configuración Firebase indica `emailPassword: true`; comprobado con las herramientas Firebase del MCP el 2026-10-19.
- [x] 3. Obtener y verificar `app/google-services.json` para el identificador Android confirmado. **RF:** RF-3.
  - Hecho cuando: la configuración añadida por el usuario en la raíz se copió a `app/google-services.json`; coincide con el proyecto `bdd-appsensoresiot` y el `applicationId` `com.example.prototipoeva3`, y `:app:processDebugGoogleServices` terminó correctamente. Es configuración cliente, no credencial de servicio.
- [x] 4. Revisar el manifiesto actual y anotar permisos necesarios para Internet, estado de red y Wi-Fi cercano. **RF:** RF-1, RF-2, RF-4.
  - Hecho cuando: se distingue por escrito que `INTERNET`/`ACCESS_NETWORK_STATE` son normales y que solo permisos restringidos aplicables pueden requerir solicitud runtime.
- [x] 5. Declarar `INTERNET` y `ACCESS_NETWORK_STATE` en `AndroidManifest.xml`, conservando la declaración Wi-Fi cercana ya aprobada. **RF:** RF-1, RF-4.
  - Hecho cuando: el manifiesto contiene ambos permisos normales y no se agregó una solicitud runtime para ellos.
- [x] 6. Verificar el plugin Google Services y la dependencia `firebase-auth` en Gradle. **RF:** RF-3.
  - Hecho cuando: ambos están disponibles para la app y no se agregaron dependencias de productos Firebase fuera de alcance.

## Lógica pura y pruebas

> Se añadieron dependencias de compilación para el conector Firebase Data Connect generado preexistente, sin habilitar su uso funcional; tras alinear Kotlin/serialization, `:app:testDebugUnitTest` termina correctamente.

- [x] 7. Definir estados y resultados de autenticación en `AuthFlow.kt`. **RF:** RF-3, RF-4, RF-5.
  - Hecho cuando: los tipos distinguen autenticado, credenciales inválidas, indisponibilidad y fallo inesperado sin incluir credenciales ni excepciones crudas.
- [x] 8. Implementar validación pura de campos vacíos y formato básico en `AuthFlow.kt`. **RF:** RF-3, RF-5.
  - Hecho cuando: casos vacíos y formato inválido producen resultados deterministas sin llamadas Android/Firebase.
- [x] 9. Implementar clasificación pura de resultados y selección de identificadores de mensajes. **RF:** RF-3, RF-4, RF-5.
  - Hecho cuando: éxito, credenciales inválidas, falta de servicio y fallo inesperado se distinguen y cada uno selecciona un mensaje genérico.
- [x] 10. Implementar la regla pura de acceso a pantallas protegidas. **RF:** RF-5.
  - Hecho cuando: únicamente el estado autenticado con permisos requeridos concedidos permite el acceso.
- [x] 11. Implementar transiciones puras de sesión para inicio, logout, segundo plano y reanudación. **RF:** RF-6, RF-7.
  - Hecho cuando: el modelo conserva sesión hasta logout y cancela el vencimiento si hay interacción antes de cinco minutos.
- [x] 12. Crear pruebas unitarias de validación de credenciales. **RF:** RF-3, RF-5.
  - Hecho cuando: pruebas cubren entrada vacía, formato inválido y formato aceptable sin usar datos reales.
- [x] 13. Crear pruebas unitarias de clasificación de resultados y mensajes. **RF:** RF-3, RF-4, RF-5.
  - Hecho cuando: éxito, credenciales erróneas, offline y servicio no disponible producen estados/mensajes esperados, sin filtrar datos.
- [x] 14. Crear pruebas unitarias del guardián de rutas. **RF:** RF-5.
  - Hecho cuando: pruebas demuestran que todo estado no autenticado bloquea pantallas protegidas.
- [x] 15. Crear pruebas unitarias de límites de sesión. **RF:** RF-6, RF-7.
  - Hecho cuando: pruebas con tiempo de entrada controlado verifican interacción antes del límite, expiración a los cinco minutos y logout explícito.

## Adaptadores Firebase y conectividad

- [x] 16. Crear `AuthRepository.kt` con una interfaz/contrato pequeño para autenticación. **RF:** RF-3, RF-4.
  - Hecho cuando: el contrato representa éxito y fallos tipados y no depende de la interfaz de usuario.
- [x] 17. Implementar el inicio de sesión del repositorio con Firebase Authentication. **RF:** RF-3.
  - Hecho cuando: el adaptador invoca `signInWithEmailAndPassword` una vez por solicitud válida y transforma su resultado al contrato tipado.
- [x] 18. Implementar lectura/restauración del usuario autenticado y cierre de sesión del repositorio. **RF:** RF-6, RF-7.
  - Hecho cuando: el repositorio expone la sesión de Firebase y `signOut()` elimina la sesión activa sin guardar contraseñas.
- [x] 19. Mapear fallos de Firebase a categorías genéricas. **RF:** RF-4, RF-5.
  - Hecho cuando: credenciales inválidas y problemas de red/servicio no exponen códigos técnicos ni valores de entrada a la UI.
- [x] 20. Crear `ConnectivityMonitor.kt` usando `ConnectivityManager`. **RF:** RF-4, RF-5.
  - Hecho cuando: el adaptador puede informar cambios de conectividad y no declara que una red conectada garantiza Firebase disponible.
- [x] 21. Probar decisiones puras para los estados de conectividad. **RF:** RF-4, RF-5.
  - Hecho cuando: los casos online y offline mantienen la app disponible y bloquean el acceso protegido sin autenticación.

## Interfaz, permisos y flujo de sesión

- [x] 22. Añadir recursos en español para campos vacíos y errores de credenciales. **RF:** RF-3, RF-5.
  - Hecho cuando: los recursos son genéricos, objetivos y no identifican qué dato de credencial es incorrecto.
- [x] 23. Añadir recursos en español para permisos, conectividad y servicio no disponible. **RF:** RF-1, RF-2, RF-4, RF-5.
  - Hecho cuando: existe un mensaje distinto y comprensible para permiso aplicable denegado, ausencia de Internet y servicio inaccesible.
- [x] 24. Adaptar Login para capturar correo/contraseña y enviar la acción de ingreso. **RF:** RF-3, RF-5.
  - Hecho cuando: el formulario entrega valores solo al callback de ingreso y muestra estado de espera sin invocar Firebase desde el composable.
- [x] 25. Conectar la acción de Login al repositorio y al flujo puro. **RF:** RF-3, RF-4, RF-5.
  - Hecho cuando: una solicitud válida llama automáticamente al repositorio y muestra el estado/mensaje resultante.
- [x] 26. Limpiar la contraseña en memoria al finalizar o abandonar el ingreso. **RF:** RF-3, RF-5.
  - Hecho cuando: tras éxito, error o salida del formulario no queda contraseña en el estado visible del formulario ni en logs/almacenamiento.
- [x] 27. Integrar el estado de conectividad en el flujo de Login. **RF:** RF-4, RF-5.
  - Hecho cuando: sin conexión se explica que no es posible validar, la app sigue en ejecución y no se habilitan rutas protegidas.
- [x] 28. Implementar la solicitud de permisos runtime solo para permisos restringidos aplicables. **RF:** RF-1, RF-2.
  - Hecho cuando: los permisos normales no muestran prompt y los restringidos aplicables se solicitan al necesitarlos.
- [x] 29. Completar el manejo de denegación repetida/permanente de permisos aplicables. **RF:** RF-1, RF-2, RF-5.
  - Hecho cuando: el usuario recibe explicación y, si Android ya no permite otro diálogo, instrucciones para habilitar el permiso en Ajustes sin cerrar la app.
- [x] 30. Conectar restauración de sesión y guardián de navegación en `MainActivity.kt`. **RF:** RF-5, RF-6.
  - Hecho cuando: una sesión válida lleva a pantallas protegidas y cualquier sesión ausente/inválida mantiene Login.
- [x] 31. Añadir acción explícita de cerrar sesión en el flujo autenticado. **RF:** RF-6.
  - Hecho cuando: al activarla se invoca `signOut()` y la navegación vuelve a Login.
- [x] 32. Crear `SessionLifecycle.kt` para coordinar el límite de cinco minutos. **RF:** RF-6, RF-7.
  - Hecho cuando: la coordinación inicia/cancela el vencimiento según ciclo de vida e interacción, sin depender de un temporizador en memoria tras muerte del proceso.
- [x] 33. Comprobar el vencimiento al reanudar/reabrir la aplicación. **RF:** RF-7.
  - Hecho cuando: `checkExpirationOnLaunch()` evalúa el plazo persistido antes de restaurar el usuario y las pruebas puras verifican no vencido, vencimiento exacto y posterior; `onForeground()` conserva la sesión si vuelve antes del límite.

## Verificación integrada

- [x] 34. Ejecutar pruebas unitarias de autenticación, conectividad, permisos y sesión. **RF:** RF-1–RF-7.
  - Hecho cuando: todas las pruebas relevantes terminan en verde; no se continúa con fallos.
- [ ] 35. Verificar manualmente Login vacío e ingreso con credenciales inválidas. **RF:** RF-3, RF-5. **Pendiente manual:** no hay dispositivo/emulador accesible (`adb` no está instalado/disponible); la validación unitaria de campos y mapeo genérico sí pasó.
  - Hecho cuando: se solicitan nuevamente los campos y se muestra mensaje genérico en español sin revelar valores ni campo incorrecto.
- [ ] 36. Verificar manualmente funcionamiento offline y fallo de Firebase. **RF:** RF-4, RF-5. **Pendiente manual:** sin dispositivo/emulador; las pruebas de lógica offline y clasificación de fallos pasan.
  - Hecho cuando: ambos escenarios muestran mensajes apropiados, la app permanece abierta y no se accede a pantallas protegidas.
- [ ] 37. Verificar sesión autenticada, logout y expiración/reanudación de cinco minutos con cuenta de prueba. **RF:** RF-6, RF-7. **Pendiente manual:** requiere dispositivo/emulador y credenciales autorizadas de una cuenta de prueba, no disponibles aquí.
  - Hecho cuando: se confirma persistencia hasta logout, conservación al volver antes del límite y cierre tras vencer sin interacción.
- [x] 38. Revisar manifiesto, recursos y repositorio en busca de secretos o credenciales persistidas. **RF:** RF-3, RF-5.
  - Hecho cuando: auditoría de fuentes/recursos/manifiesto no encontró claves privadas, service accounts, tokens ni contraseñas persistidos o registrados. `google-services.json` contiene configuración de cliente (incluida su API key pública de Firebase), no una credencial de servicio.
