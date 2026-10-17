# Definición del proyecto y dise?o prototipado

> Contenido convertido desde `Definición de Componentes_APP.docx`. Se conserva el orden y la informaci?n del documento original.

Definición del proyecto y diseño prototipado

Este proyecto abarca de las Unidades N°2 a N°4 de la asignatura ‘Aplicaciones Móviles para IoT’. La unidad presente esta enmarcada para la evaluación N°2 de la asignatura. Se establece de forma conceptual una decisión de proyecto impuesta por las instrucciones de la evaluación, pero el proyecto mismo debe funcionar como un prototipo delimitado solo al desarrollo y prueba de una aplicación mediante Android Studio, dejando para futuras evaluaciones la incorporación de una solución tecnológica en base a un Arduino.

### Identificación de la problemática:

Piensa en la problemática que trataste en la unidad anterior. ¿Qué dispositivos o sistemas necesitas conectar? ¿Cuál es el objetivo de la comunicación entre estos dispositivos? A partir de esta reflexión, define las funcionalidades clave que debe tener tu aplicación para resolver esta problemática. Piensa en aspectos como qué tipo de interacción quieres lograr (enviar o recibir datos, controlar dispositivos, etc.) y qué características debería tener la aplicación para ser efectiva.

Nuestra problemática abarca la identificación y prevención de riesgos asociados a la proximidad de vehículos a distintas estructuras o puntos de riego, esencialmente al efectuar un aparcamiento o proximidad en una carretera. Para esto se implementarán sensores de proximidad que alerten sobre la cercanía a distintos objetos, y en caso extremos de peligro respecto a la distancia (tanto por una colisión posible o un cambio muy brusco de distancia), enviar una advertencia por correo y número de teléfono a una persona de seguridad asociada al usuario mediante la aplicación.

### Dispositivos necesarios para conectar:

- Dos o más teléfonos/celulares: Permitirán la gestión de la aplicación al ofrecer la capacidad de enviar datos para activar el sistema y ver los datos en tiempo real que este proporcione, esto entre múltiples dispositivos móviles.
- Wifi: Los servicios de Wifi permiten (y son necesarios) para conectar con una base de datos alojada en la nube (Firebase) al ser requeridos el envío y recepción de datos, tanto para el ingreso de usuario como para la recuperación de datos almacenados.
- Bluetooth: Sus servicios permiten conectar con el Arduino y llevar a cabo el envío y recepción de datos capturados por los sensores de proximidad.

### Tipo de interacción esperada:

Por una parte, se espera el envío de datos de usuario para su verificación mediante la base de datos (Firebase) y acceso seguro en la aplicación, luego se espera el solicitar la conexión con servicios Bluetooth y Wifi para poder acceder a servicios externos (base de datos y Arduino); La respuesta de la aplicación dependerá si el usuario otorga permisos o no a los servicios.

Los sensores de proximidad medirán la distancia del usuario (simulando que esté en un vehículo) de cualquier objeto cercano, mostrando una señal en forma gráfica si se encuentra a una distancia segura, media o peligrosa del mismo.
En caso de encontrarse muy cerca, se le notificara al usuario de que la distancia es riesgosa para él, y en caso de haber una colisión o un cambio muy brusco de distancia, se le notificara al usuario de seguridad asociado como forma de respuesta segura.

### Selección de herramientas de desarrollo:

La aplicación será desarrollada en Android Studio con Kotlin. Especifica qué herramientas y bibliotecas adicionales utilizarás para integrar la conectividad con dispositivos IoT y la autenticación de usuarios. Las opciones para el almacenamiento y gestión de datos incluyen SQLite, Firebase, MySQL o APIs externas. Justifica tu elección con base en estándares de desarrollo de aplicaciones Android y en la interoperabilidad de los datos capturados por los sensores.

- Base de datos (Firebase): Permitirá almacenar datos en la nube, agilizando el proceso de creación y conexión a comparación como ocurriría con una base de datos local. A su vez, permite gestionar la creación y acceso de usuarios de forma directa y con un sistema de autenticación ya integrado (Firebase Authentication).

### Diseñar la interfaz de usuario (UI):

Elabora un mockup de la aplicación, detallando las pantallas principales, la navegación y la interacción con los dispositivos IoT. Incluye cómo se visualizarán los datos capturados y cómo el usuario podrá interactuar con los dispositivos conectados.

Se espera contar con las siguientes tres pantallas de interacción en la aplicación. Cada una cumplirá con las funciones y contendrá los componentes definidos a continuación:

- Pantalla de Login: Contendrá marcos de ingreso de texto (Correo electrónico y contraseña) para poder iniciar sesión. Debe validar el ingreso de usuarios mediante su autenticación. Por su funcionamiento, debe poder establecerse acceso a Wifi desde la apertura de la aplicación, y así que esta pueda enviar y recibir datos.

### Pantalla de Configuración: Contendrá tres botones principales:

- “Bluetooth”: Permitirá conceder o denegar los permisos Bluetooth necesarios para la conexión próxima con el Arduino de operaciones.
- “Wifi”: Permitirá conceder o denegar los permisos Wifi requeridos para la conexión con la base de datos y la recuperación de datos.
- Valores: Establecerá el vinculo con la tercera pantalla principal de la aplicación.
- Vinculación de usuario de seguridad (Tendrá un icono gráfico): Permitirá establecer una conexión con un usuario externo a elección, esta vinculación se podrá hacer vía correo electrónico o mediante número de teléfono.
- Pantalla de Valores: Mostrar los valores en tiempo real de los sensores de proximidad del Arduino. Para esta evaluación, deben incorporarse una serie de botones interactivos para manipular dichos valores, dada la ausencia del sistema vía Arduino.

## Recursos gráficos incrustados

Los siguientes recursos estaban incrustados en el documento y se conservan en la carpeta de recursos asociada:

- [image1.gif](DefinicionComponentes_AppPrototipo_assets/image1.gif)
