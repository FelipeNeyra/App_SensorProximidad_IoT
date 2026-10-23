# Spec 00-4 — Detección de Cambio Brusco de Distancia en Firestore

## Contexto y objetivo
Implementar la detección automática de reducciones abruptas de distancia (caídas de distancia producidas en un intervalo menor a 1 segundo) dentro de la pantalla de simulación de valores (`ValuesScreen.kt`). Ante la presencia de un cambio brusco, la aplicación registrará un nuevo documento en la colección `'Cambio_Brusco'` de Cloud Firestore, respetando la estructura de campos y tipos del sistema (ID del documento compuesto, usuario, distancias inicial y final, variación, tiempo transcurrido, estado de peligro/advertencia, ubicación del sensor, nota y Timestamp de fecha/hora).

## Usuarios / Actores
- Usuario autenticado o simulador en la aplicación.
- Sistema externo: Cloud Firestore (colección `'Cambio_Brusco'`).

## Historias de usuario
- **HU-1**: Como usuario quiero que la aplicación detecte cuando reduzco abruptamente la distancia en el simulador en menos de 1 segundo.
- **HU-2**: Como usuario quiero que, al producirse un cambio brusco, se cree automáticamente un registro en la colección `'Cambio_Brusco'` de Firestore.
- **HU-3**: Como usuario quiero ver un aviso visual en pantalla notificando la detección del cambio brusco y la confirmación del registro guardado.

## Requisitos funcionales (EARS)
- **RF-1**: MIENTRAS EL USUARIO VARÍE EL SLIDER DE DISTANCIA, EL SISTEMA MONITOREARÁ EL TIEMPO Y LA MAGNITUD DE REDUCCIÓN DE DISTANCIA.
- **RF-2**: SI SE REGISTRA UNA DISMINUCIÓN DE DISTANCIA (EJ. DE 20 CM O MÁS) EN UN TIEMPO MENOR A 1 SEGUNDO (1000 MS), EL SISTEMA GENERARÁ UN REGISTRO EN LA COLECCIÓN `'Cambio_Brusco'`.
- **RF-3**: EL ID DEL DOCUMENTO EN `'Cambio_Brusco'` SE COMPONDRÁ MEDIANTE EL PATRÓN `cambio_brusco_<userId>_<timestampMs>`.
- **RF-4**: EL DOCUMENTO REGISTRARÁ LOS CAMPOS: `id`, `userId`, `userEmail`, `distanciaInicial`, `distanciaFinal`, `variacionDistancia`, `tiempoMs`, `estado`, `ubicacionModo`, `nota` Y `timestamp` (TIPO `com.google.firebase.Timestamp`).
- **RF-5**: EL SISTEMA NOTIFICARÁ AL USUARIO EN LA INTERFAZ SOBRE LA DETECCIÓN DEL CAMBIO BRUSCO Y EL RESULTADO DE LA INSERCIÓN EN FIRESTORE.

## Requisitos no funcionales
- **RNF-1 (Rendimiento)**: Evaluación eficiente en memoria del tiempo y la variación de distancia sin bloquear la interfaz.
- **RNF-2 (Arquitectura)**: Lógica pura de detección y generación de modelo aislada en `SuddenChangeRecord` y `ParkingRepository`.
- **RNF-3 (Seguridad)**: Configuración en `firestore.rules` permitiendo acceso a la colección `'Cambio_Brusco'` a usuarios autenticados.
