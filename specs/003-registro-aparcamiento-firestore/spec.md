# Spec 00-3 — Registro de Seguimiento de Aparcamiento en Firestore

## Contexto y objetivo
Establecer el flujo de seguimiento de la variación de valores del sensor de proximidad en la pantalla de simulación de monitoreo y activador (`ValuesScreen`), de acuerdo al punto 2.1.1.2 de la rúbrica (elementos interactivos como Sliders, Switches, RadioButtons, OutlinedTextFields). Cuando el usuario interactúa con la barra de cambio de valores (Slider), la aplicación mantiene un estado visual de seguimiento activo. Al finalizar el gesto de variación, se registra automáticamente una medición de aparcamiento en la colección `'Aparcamiento'` de Cloud Firestore, respetando una estructura compuesta de identificador (ID) del documento.

## Usuarios / Actores
- Usuario autenticado en la aplicación.
- Sistema externo: Cloud Firestore (Base de datos NoSQL en la nube).

## Historias de usuario
- **HU-1**: Como usuario quiero interactuar con el Slider de proximidad y ver en tiempo real el estado de seguimiento de la medición.
- **HU-2**: Como usuario quiero que, al soltar la barra de valores, la medición de aparcamiento se guarde automáticamente en la colección `'Aparcamiento'` de Firestore.
- **HU-3**: Como usuario quiero contar con elementos interactivos de Material Design (Switch para activar guardado, RadioButtons para seleccionar ubicación del sensor y EditText para ingresar notas) según el punto 2.1.1.2 de la rúbrica.
- **HU-4**: Como usuario quiero ver un mensaje de confirmación cuando el registro se haya guardado exitosamente en Firestore.

## Requisitos funcionales (EARS)
- **RF-1**: MIENTRAS EL USUARIO MUEVA EL SLIDER DE VALORES, LA APLICACIÓN MOSTRARÁ UN ESTADO DE SEGUIMIENTO ACTIVO EN LA INTERFAZ.
- **RF-2**: CUANDO EL USUARIO FINALICE LA VARIACIÓN DEL SLIDER, EL SISTEMA REGISTRARÁ UN DOCUMENTO EN LA COLECCIÓN `'Aparcamiento'` EN FIRESTORE.
- **RF-3**: EL SISTEMA COMPONDRÁ EL ID DEL DOCUMENTO DE APARCAMIENTO SIGUIENDO EL PATRÓN ESTRUCTURADO `aparcamiento_<userId>_<timestamp>`.
- **RF-4**: EL DOCUMENTO REGISTRADO EN FIRESTORE CONTENDRÁ LOS CAMPOS: `id`, `userId`, `userEmail`, `distancia`, `estado` (SEGURO, ADVERTENCIA, PELIGRO), `ubicacionModo`, `nota` Y `timestamp`.
- **RF-5**: EL SISTEMA PERMITIRÁ ACTIVAR O DESACTIVAR EL MODO DE GUARDADO AUTOMÁTICO DE SEGUIMIENTO MEDIANTE UN SWITCH DE MATERIAL DESIGN.
- **RF-6**: EL SISTEMA PERMITIRÁ SELECCIONAR LA UBICACIÓN DEL SENSOR (FRONTAL O TRASERO) MEDIANTE RADIOBUTTONS O CHIPS INTERACTIVOS.
- **RF-7**: EL SISTEMA PERMITIRÁ INGRESAR UNA NOTA DE UBICACIÓN O PUNTO DE APARCAMIENTO MEDIANTE UN CAMPO DE TEXTO (OUTLINEDTEXTFIELD / EDITTEXT).

## Requisitos no funcionales
- **RNF-1 (Usabilidad / Material Design)**: Uso de componentes interactivos estándar de Jetpack Compose / Material Design 3 en cumplimiento con el punto 2.1.1.2 de la rúbrica.
- **RNF-2 (Arquitectura)**: Lógica de negocio y construcción de documentos aisladas de composables mediante repositorio/clase pura (`ParkingRepository` / `ParkingFlow`).
- **RNF-3 (Seguridad)**: Reglas de seguridad en Firestore configuradas para requerir autenticación para escribir en la colección `'Aparcamiento'`.
- **RNF-4 (Idioma)**: Todos los mensajes y etiquetas visibles estarán en idioma español.

## Criterios de finalización
- Adición del SDK `firebase-firestore` a la configuración de Gradle.
- Actualización de `firestore.rules` permitiendo acceso de lectura/escritura en `Aparcamiento`.
- Implementación del estado de seguimiento y guardado en Firestore en `ValuesScreen.kt`.
- Pruebas unitarias de la lógica pura de composición de documentos e identificadores.
