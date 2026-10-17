# Spec 00-1 — Generación de Pantallas Conceptualmente
## Contexto y objetivo
Establecer las tres pantallas de uso de la aplicación definidas para el prototipo de esta.
Cumplen con la función de dividir las funciones solicitadas por la rúbrica y aspectos considerados para la presente evaluación. 
Deben ser compuestas con base en las especificaciones definidas en el documento DefinicionComponentes_AppPrototipo.md.
No se hará conexión con la base de datos ni se verificarán las credenciales en esta spec.- Personas que esten haciendo uso del sistema de medición por sensores.
## Historias de usuario
- H1: Como desarrollador quiero que se soliciten todos los permisos necesarios.
- N2: Como desarrollador quiero que la aplicación alerte sobre los permisos necesarios que hayan sido denegados.
- H3: Como usuario quiero ingresar mis credenciales para acceder a la aplicación.
- H4: Como usuario quiero poder navegar entre pantallas.
## Requisitos funcionales (criterios de aceptación en EARS)
- RF-1: CUANDO SE REQUIERA UN PERMISO, LA APLICACIÓN MOSTRARA UN MENSAJE DE SOLICITUD DEL MISMO.
- RF-2: SI UN(OS) PERMISO(S) SON DENEGADOS, ENTONCES LA APLICACIÓN IMPEDIRA LA NAVEGACIÓN POR EL SISTEMA.
- RF-3: MIENTRAS LA APLICACIÓN ESTÉ BLOQUEADA POR PERMISOS, EL SISTEMA DEBERA VOLVER A SOLICITAR LOS PERMISOS.
- RF-4: SI LOS PERMISOS VUELVEN A SER DENEGADOS, LA APLICACIÓN SE DEBE BLOQUEAR INDEFINIDAMENTE.
- RF-5: MIENTRAS LA APLICACIÓN ESTÉ INDEFINIDAMENTE BLOQUEADA, EL SISTEMA DEBERA MOSTRAR UN MENSAJE DE BLOQUEO.
- RF-6: EL SISTEMA DEBE PERMITIR LA NAVEGACIÓN ENTRE PANTALLAS Y LA EJECUCIÓN DE FUNCIONES OFRECIDAS.

## Aclaración de alcance aprobada para T14–T18
- Wi-Fi: se solicitará `NEARBY_WIFI_DEVICES` en Android 13 (API 33) y posteriores para habilitar conceptualmente el acceso futuro a dispositivos Wi-Fi cercanos. No se descubre ni conecta a redes/dispositivos en esta spec.
- En versiones anteriores a API 33, donde esta capacidad conceptual no requiere un permiso runtime en el prototipo actual, no se declara un permiso alternativo ni se solicita ubicación.
- Bluetooth permanece conceptual y no inicia una conexión ni solicita permisos Bluetooth en esta spec.
- La acción Wi-Fi informa que el permiso fue concedido y que la conexión real queda para una etapa posterior.
## Requisitos no funcionales
- Idioma: Se debe establecer el idioma en español para la interfaz.
- Usabilidad: Incorporación de líbrerias y permisos acorde a lo estipulado.
- Seguridad: Solicitud de permisos en tiempo real.
- Ejecución: La aplicación debe mantenerse ejecutada sin importar el estado de los permisos. Esto para   respetar el principio de disponibilidad total.
## Casos límite
- Errores de ejecución (por versiones de software, librerías, etc.))
## Fuera de alcance
- Conectividad con la base de datos Firebase.
- Verificación de credenciales reales de usuario.
- Simulación de valores de sensores.
## Criterios de finalización
- Separación de pantallas en distintos archivos (Activitys)
- Todas las HU y RF cubiertos.
## Dudas abiertas
- Uso de dependencias externas no vistas con anterioridad: no se requieren ni se incorporan en esta spec. Si una implementación futura las requiere, solicitar aprobación y documentarla antes de usarlas.

## Decisiones confirmadas para la planificación
- Android: `minSdk 24`, `targetSdk 37` (configuración existente del proyecto).
- Permisos: `NEARBY_WIFI_DEVICES` se solicita en API 33+ para dispositivos Wi-Fi cercanos; se declara con `neverForLocation`. No hay permiso runtime equivalente solicitado para esta capacidad conceptual en API inferiores. Bluetooth continúa conceptual.
- Disposición: Login centrado; Configuración en columna; Valores con área identificada y regreso; bloqueo explicativo con reintento solo mientras sea recuperable. No se agregan dependencias.
