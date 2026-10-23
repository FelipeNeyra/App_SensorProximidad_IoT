# Spec 00-5 — Vinculación de Usuario de Seguridad y Registro de Alertas en Firestore

## Contexto y objetivo
Implementar la vinculación y desvinculación de un usuario de seguridad desde la interfaz (con validación de campos obligatorios de correo y teléfono, y verificación de existencia en Firebase Authentication). Asimismo, automatizar la generación de documentos en la colección `'Alerta'` en Cloud Firestore cuando un registro de `'Aparcamiento'` o `'Cambio_Brusco'` contenga un estado con el valor `'ADVERTENCIA'`, incluyendo el correo del usuario de seguridad vinculado, referencia al documento original y un mensaje de correo notificatorio modificable.

## Usuarios / Actores
- Usuario autenticado de la aplicación.
- Usuario de seguridad vinculado.
- Sistema externo: Firebase Authentication y Cloud Firestore (colección `'Alerta'`).

## Historias de usuario
- **HU-1**: Como usuario quiero poder vincular o desvincular un usuario de seguridad ingresando correo y teléfono.
- **HU-2**: Como usuario quiero que la aplicación verifique si el correo del usuario de seguridad existe en Firebase Authentication antes de permitir la vinculación.
- **HU-3**: Como usuario quiero que, cuando se registre un evento de `'Aparcamiento'` o `'Cambio_Brusco'` con estado `'ADVERTENCIA'`, se cree automáticamente un documento en la colección `'Alerta'` de Firestore.
- **HU-4**: Como usuario quiero que la alerta incluya el correo del usuario de seguridad asociado y la referencia al documento de origen.
- **HU-5**: Como usuario quiero que el mensaje de alerta vía correo contenga por defecto `"Prueba de mensaje de alerta - App Arduino."` y sea fácilmente modificable.

## Requisitos funcionales (EARS)
- **RF-1**: LA INTERFAZ PERMITIRÁ VINCULAR O DESVINCULAR UN USUARIO DE SEGURIDAD MEDIANTE LOS BOTONES "VINCULAR USUARIO" Y "DESVINCULAR USUARIO".
- **RF-2**: PARA VINCULAR UN USUARIO DE SEGURIDAD, LOS CAMPOS DE CORREO Y TELÉFONO DEBEN ESTAR OBLIGATORIAMENTE RELLENADOS (EL TELÉFONO NO SE ALMACENARÁ EN BASE DE DATOS EN ESTA ETAPA).
- **RF-3**: EL SISTEMA VERIFICARÁ EN FIREBASE AUTHENTICATION QUE EL CORREO A VINCULAR ESTÉ REGISTRADO; SI NO EXISTE, SE DENEGARÁ LA VINCULACIÓN CON UN MENSAJE DE ERROR.
- **RF-4**: SI SE GENERA UN DOCUMENTO EN `'Aparcamiento'` O `'Cambio_Brusco'` CON `estado == "ADVERTENCIA"`, EL SISTEMA CREARÁ UN NUEVO DOCUMENTO EN LA COLECCIÓN `'Alerta'`.
- **RF-5**: EL DOCUMENTO EN `'Alerta'` CONTENDRÁ: `id`, `userId`, `securityUserEmail`, `referenceDocumentId`, `referenceCollection`, `estado`, `mensaje` Y `timestamp` (`com.google.firebase.Timestamp`).
- **RF-6**: EL MENSAJE DE CORREO NOTIFICATORIO SERÁ CONFIGURABLE CON EL VALOR PREDETERMINADO `"Prueba de mensaje de alerta - App Arduino."`.

## Requisitos no funcionales
- **RNF-1 (Seguridad)**: Verificación en Firebase Auth antes de persistir vinculación. Reglas de Firestore configuradas para `/Alerta/{documentId}`.
- **RNF-2 (Usabilidad)**: Mensajes en español claros para el estado de vinculación y errores de validación.
