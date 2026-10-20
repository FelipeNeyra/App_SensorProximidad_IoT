# Constitución — Protottipo de Aplicación de Sensores
Principios innegociables. Toda spec, plan y tarea debe cumplirlos.
1. **Funcionalidades clave**: Manejo entre distintas pantallas y conexión de medios externos
    establecidos en la ruta /Componentes_MateriaTeorica son la base de la aplicacion.
2. **Materiales ya conocidos**: Todo componente o función necesaria a implementar debe haber
    correspondido a los conceptos vistos en el documento DefinicionComponentes_AppPrototipo.md.
    De ser necesaria otra incorporation, debe ser solicitada, explicar su proposito y documentarla en
    la spec correspondiente.
2. **La spec manda**: Nada se implementa si no está en la spec activa. Si falta una
   decisión, se para y se pregunta. Si falta un componente se solicita y se ingresa en la spec.
3. **Lógica separada de la interfaz**: La recepción y envío de datos son funciones puras, 
   sin DOM, que deben conectarse mediante una API externa y/o credenciales de servicio.
4. **Tests como puerta**: la lógica se prueba con un(os) MCP conectados y/o habilitados en el agente y
   cuyo uso corresponda en el contexto.
   Prohibido avanzar con tests en rojo.
5. **Los datos del usuario son sagrados**: Firebase Authenticacion con conexion directa desde la pantalla principal 
   y solicitud de permisos necesarios que no se encuentran libres a la app. 
6. **Funcionamiento pleno**: La aplicación debe mantenerse en funcionamiento incluso si el usuario
    deniega los permisos necesarios. Se le debe hacer saber el porque de la situación y de la
    importancia de los permisos.
6. **Idioma**: código en inglés, a excepción de variables; interfaz y documentación en español. 