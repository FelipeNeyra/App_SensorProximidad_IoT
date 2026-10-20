# Spec N00-2 — Integración de Autenticación de Usuarios

## Contexto y objetivo
Se busca conectar la base de datos de Firebase, precisamente Authentication para permitir
la validación de credenciales de usuario en la pantalla de login inicial. El prototipo debe
hacer saber al usuario si las credenciales son válidas o no, y si dispone de conectividad a
internet para poder llevar a cabo la petición.

## Usuarios / Actores
Usuario principal: Usuario que desea ingresar.
Sistema externo (si aplica): Firebase (Authentication)

## Historias de usuario
- HU-1: Como usuario quiero ingresar mi correo y contraseña para acceder a la aplicación.
- HU-2: Como usuario quiero que se me haga saber si las credenciales son válidas o no.
- HU-3: Como usuario quiero saber si hay conexión a internet para poder acceder a la aplicación.
- HU-4: Como usuario quiero que se me haga solicite el acceso a internet a la aplicación para poder 
acceder con mis credenciales.

## Requisitos Funcionales  
- RF-1: Si el sistema no tiene permisos para conectarse a internet, debe solicitarlos.
- RF-2: Si los permisos son denegados más de dos veces, el sistema debe informar al usuario que debe
  habilitarlos manualmente desde la configuración del dispositivo.
- RF-3: El sistema debe hacer conexión con el servicio Authentication de Firebase para validar las credenciales
- RF-4: Si el sistema no logra conectarse con el servicio de Firebase, deberá informarlo al
  usuario. 
- RF-5: La aplicación debe mantenerse en funcionamiento sin importar el estado de la conexión a 
  internet y a los servicios de Firebase.
- RF-6: Después de que el usuario logre ingresar, la aplicación debe mantener su sesión abierta hasta
  que el usuario decida cerrarla.
- RF-7: Si el usuario cierre la aplicación con su sesión abierta, la aplicación debe cerrar su sesión
  después de un límite de 5 minutos desde el cierre la aplicación (Solo si el usuario no interactúa con
  esta después dentro del tiempo límite)

## Requisitos No Funcionales
Rendimiento
- RNF-1: El sistema debe hacer una solicitud a Firebase automáticamente cuando el usuario haga una 
petición de ingreso.
- RNF-2: El sistema debe solicitar los permisos de conexión de forma automática al usuario cuando los
necesite. Esto solo aplica a permisos con restricciones de aplicación.

Seguridad
- RNF-3: El sistema no debe almacenar las credenciales de usuario en el dispositivo.
- RNF-4: El sistema no debe especificar valores erróneos en las credenciales (esto en caso de haber 
credenciales equivocadas en la pantalla de login).

Usabilidad
- RNF-5: Deben mostrarse mensajes genéricos pero objetivos acerca de permisos faltantes, errores de
conexión y credenciales erróneas.
- RNF-6: La aplicación debe mantenerse en ejecución sin importar el estado de los permisos. El ingreso
a las pantallas siguientes debe ser denegado al no poder validar credenciales.

Idioma
- RNF-7: Se debe establecer el idioma en español para todos los mensajes de muestra.

## Casos límite
Datos vacíos
- Caso 1: El usuario no ingresa ningún dato, se le solicitan nuevamente.

Datos inválidos
- Caso 2: El usuario ingresa datos inválidos, se le hacen saber y se le solicitan nuevamente.

Escenarios excepcionales
- Caso 2: No hay permiso de conexión. El sistema debe informar al usuario, explicar su necesidad y solicitarlos de nuevo.
- Caso 3: No hay conexión a internet. El sistema debe informar al usuario.
- Caso 4: No hay conexión a Firebase. El sistema debe informar al usuario.

## Fuera de alcance
- Envío, recepción y solicitud desde base de datos (Firestore).
- Registro de nuevos usuarios.
- Registro de usuarios de apoyo.
- Autenticación mediante otros factores (ej: correo)

## Criterios de finalización
La especificación se considerará completada cuando:
- Todos los requerimientos estén implementados.
- Las pruebas locales ejecutadas no presenten problemas.
- Las conexiones necesarias logren ejecutarse.
- No existan dudas abiertas.

## Dudas abiertas
- **Integración en Firebase**: ¿Cómo funciona la conexión con el proyecto creado en Firebase?
- **Validación de credenciales**: ¿Cómo funciona la obtención y validación de credenciales desde Firebase Authentication?
- **Vínculo mediante MCP**: ¿Es posible utilizar el MCP de Firebase configurado en este entorno para validar información del proyecto creado?
¿De ser así, como funciona ese proceso?