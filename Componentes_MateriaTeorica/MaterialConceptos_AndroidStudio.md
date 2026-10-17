# Material de conceptos: Android Studio, Kotlin y conexión BLE

## 1. Contexto de la unidad

Este material corresponde a la **UA2 — Aplicaciones Móviles Android** del curso TI3042. En la unidad anterior se desarrolló un prototipo Arduino con sensor, actuador e interconexión inalámbrica (Bluetooth o WiFi), además de definir un proyecto propio por dupla mediante la ficha de definición.

El objetivo de esta unidad es crear la aplicación Android que leerá e interactuará con ese prototipo. La evaluación de cierre consiste en una aplicación Android empaquetada como APK, de carácter individual, con una ponderación de 25%.

La secuencia de aprendizaje es:

1. Primer contacto con Android Studio y Kotlin.
2. Creación de varias pantallas y navegación con Jetpack Compose.
3. Declaración y solicitud de permisos Android.
4. Conexión entre la aplicación y Arduino mediante Bluetooth Low Energy (BLE).
5. Aplicación de lo aprendido al proyecto IoT propio.

---

## 2. Android y su arquitectura

Android es un sistema operativo basado en Linux. Su arquitectura se organiza en capas:

1. **Aplicaciones:** las aplicaciones desarrolladas y las aplicaciones del sistema, como Cámara o Mensajes.
2. **Framework de Android:** incluye componentes como `Activities`, `Services` y las API del sistema.
3. **Android Runtime (ART) y bibliotecas nativas:** el entorno de ejecución y las bibliotecas implementadas en C/C++.
4. **Capa de Abstracción de Hardware (HAL):** permite que el sistema interactúe con el hardware.
5. **Kernel de Linux:** administra controladores, memoria, energía y seguridad.

Esta base comparte conceptos con Arduino e IoT: procesos, controladores y permisos para acceder a hardware como Bluetooth, WiFi, cámara o GPS.

---

## 3. Android Studio

Android Studio es el IDE oficial para desarrollar aplicaciones Android. Está basado en IntelliJ IDEA, de JetBrains. En él se escribe, ejecuta y depura la aplicación.

Sus herramientas principales son:

- **Editor de código:** permite programar en Kotlin y ofrece autocompletado inteligente.
- **Preview de Compose (`@Preview`):** muestra la interfaz dibujada junto al código sin ejecutar la aplicación en el emulador.
- **Emulador o dispositivo físico:** permite probar la aplicación sin requerir necesariamente un teléfono físico.
- **Logcat:** muestra en vivo lo que hace la aplicación; es comparable al Monitor Serial de Arduino.
- **Gradle:** sistema de compilación que transforma el código en un APK.

La instalación ocupa aproximadamente 1,5 GB. Requiere espacio en disco y memoria RAM; se recomienda contar con 16 GB de RAM, especialmente porque el emulador es el componente más exigente.

Desde 2026 Android utiliza un enfoque **Compose-first**. El sistema tradicional de vistas y layouts XML se encuentra en mantenimiento, por lo que las plantillas estándar ya no generan archivos XML de layout.

### 3.1. Crear el primer proyecto

1. Instalar Android Studio o verificar que esté instalado.
2. Crear un proyecto de tipo **Empty Activity**.
3. Seleccionar **Kotlin** como lenguaje.
4. Utilizar la interfaz basada en **Compose**.
5. Ejecutar el proyecto en el emulador o en un teléfono físico.
6. Capturar la aplicación funcionando como evidencia.

---

## 4. Estructura de un proyecto Android

Las carpetas y archivos principales son:

- `manifests/AndroidManifest.xml`: declara las `Activities` y los permisos de la aplicación.
- `kotlin+java/`: contiene el código Kotlin de la aplicación, incluida la interfaz declarada con `@Composable`.
- `res/`: contiene recursos como `drawable`, `mipmap` (íconos), `values` y `xml`.
- `build.gradle`: configura el sistema de compilación y las dependencias.

En un proyecto Compose no se utiliza necesariamente una carpeta `layout/`, porque la interfaz se construye mediante funciones Kotlin.

Una forma de relacionar esta estructura con Arduino IDE es considerar que el código Kotlin cumple el papel del programa principal, los recursos contienen elementos auxiliares de la aplicación y Gradle se encarga de preparar el resultado ejecutable.

---

## 5. Fundamentos mínimos de Kotlin

La lógica de variables, funciones y condicionales es similar a la utilizada en C++ para Arduino; cambia principalmente la sintaxis.

```kotlin
val nombre = "Sensor 1"  // val no cambia
var lectura = 0          // var puede cambiar

fun leerSensor(pin: Int): Int {
    return pin * 2       // valor de ejemplo
}

@Composable
fun Saludo(nombre: String) {
    Text(text = "Hola, $nombre")
}
```

- `val` declara un valor que no se reasigna.
- `var` declara una variable cuyo valor puede cambiar.
- `fun` declara una función.
- `@Composable` identifica una función que construye parte de la interfaz con Jetpack Compose.
- La interpolación `$nombre` inserta el valor de una variable dentro de un texto.

---

## 6. Activities y pantallas

Una **Activity** representa una pantalla de la aplicación, por ejemplo, una pantalla de inicio de sesión o una pantalla con la lista de sensores. Toda aplicación Android comienza al menos con una Activity.

### 6.1. Ciclo de vida simplificado

El ciclo de vida de una Activity incluye:

1. `onCreate()`
2. `onStart()`
3. `onResume()`
4. `onPause()`
5. `onStop()`
6. `onDestroy()`

`onCreate()` se ejecuta una vez al crear la pantalla y puede compararse conceptualmente con `setup()` en Arduino. `onResume()` indica que la pantalla está visible e interactiva. `onPause()` y `onStop()` ocurren cuando el usuario deja la pantalla, por ejemplo, al recibir una llamada o cerrar la aplicación.

### 6.2. Activities clásicas y Compose

El término Activity continúa siendo parte del modelo Android. Sin embargo, en Compose la navegación normalmente se realiza dentro de una sola Activity, utilizando destinos o pantallas en vez de crear una Activity separada para cada pantalla.

Una aplicación IoT suele necesitar, como mínimo:

- Una pantalla de inicio para mostrar el dato del sensor.
- Una pantalla de configuración para preparar la conexión Bluetooth o WiFi.

---

## 7. Navegación con Jetpack Compose

La navegación se construye con tres piezas:

- **`NavController`:** sabe en qué pantalla se encuentra la aplicación y ordena los desplazamientos.
- **`NavHost`:** contenedor que dibuja la pantalla activa según la ruta actual.
- **`composable("ruta")`:** registra cada pantalla y la identifica con un texto único.

La analogía con un navegador web es útil: el `NavController` funciona como el historial de navegación, cada ruta es similar a una URL y el `NavHost` es el espacio donde se dibuja la página actual.

### 7.1. Declarar las rutas

```kotlin
val navController = rememberNavController()

NavHost(
    navController = navController,
    startDestination = "inicio"
) {
    composable("inicio") {
        PantallaInicio(navController)
    }
    composable("configuracion") {
        PantallaConfiguracion(navController)
    }
}
```

### 7.2. Navegar hacia otra pantalla

La acción que debe cambiar de pantalla llama a `navigate()` sobre el mismo `NavController` que recibe la pantalla:

```kotlin
@Composable
fun PantallaInicio(navController: NavController) {
    Column {
        Text("Dato del sensor: 24.5 °C")

        Button(
            onClick = {
                navController.navigate("configuracion")
            }
        ) {
            Text("Ir a Configuración")
        }
    }
}
```

### 7.3. Volver atrás y back stack

Cada llamada a `navigate()` apila una nueva pantalla. El botón Atrás del sistema puede regresar automáticamente, pero también es posible controlar el regreso desde el código:

```kotlin
Button(
    onClick = {
        navController.popBackStack()
    }
) {
    Text("Volver")
}
```

Desde la pantalla de configuración se debe preferir `popBackStack()` para volver a la pantalla anterior. Si se llama nuevamente a `navigate("inicio")`, se apila una instancia nueva de Inicio y el botón Atrás puede dejar de comportarse como se espera.

### 7.4. Pasar datos mediante argumentos de ruta

Una ruta puede contener un valor, de forma similar a un parámetro de una URL:

```kotlin
// Declarar la ruta con un argumento
composable("detalleSensor/{valor}") { backStackEntry ->
    val valor = backStackEntry.arguments?.getString("valor")
    PantallaDetalle(valor)
}

// Navegar enviando el dato real
navController.navigate("detalleSensor/24.5")
```

### 7.5. Actividad sugerida

En el proyecto de la ficha de definición de UA1:

1. Crear una pantalla **Inicio** para mostrar el dato del sensor.
2. Crear una pantalla **Configuración** para la futura conexión Bluetooth o WiFi.
3. Agregar un botón en cada pantalla para ir a la otra.
4. Usar textos de relleno si todavía no existe la lógica real del sensor.

El entregable es un proyecto con las dos pantallas navegables, listo para mostrar al inicio de la clase siguiente.

---

## 8. Permisos en Android

Para comunicarse con Arduino, la aplicación necesita acceso a Bluetooth. Android protege los recursos sensibles mediante un **sandbox**: cada aplicación se ejecuta aislada y no puede acceder libremente a recursos como Bluetooth, cámara o ubicación.

El sistema verifica:

1. Que el permiso esté declarado en el manifiesto.
2. Que el usuario lo haya autorizado cuando se trate de un permiso sensible.

El usuario puede revocar la autorización desde Ajustes. Por ello, la aplicación nunca debe asumir que un permiso sigue concedido.

Un permiso adicional es una puerta de acceso adicional y puede representar una brecha potencial. Debe aplicarse el principio de **mínimo privilegio**:

- Pedir solo lo que la función necesita.
- Pedirlo cuando se utiliza la función.
- Explicar al usuario por qué se necesita.
- No declarar un permiso cuya finalidad no se pueda explicar en una frase.

### 8.1. Flujo de un permiso en tiempo de ejecución

1. Declarar el permiso en `AndroidManifest.xml`.
2. Verificarlo con `checkSelfPermission`.
3. Solicitarlo mediante `launcher.launch`.
4. Procesar el resultado: concedido o denegado.

Si el permiso ya estaba concedido, se omite el tercer paso. La aplicación debe continuar funcionando, aunque con la función correspondiente deshabilitada, si el usuario responde que no.

La solicitud debería ocurrir en contexto, por ejemplo, al pulsar el botón de conectar, y no automáticamente al abrir la aplicación. El resultado llega mediante un callback y el estado de la interfaz debe reaccionar a ese resultado.

### 8.2. Solicitar un permiso desde Compose

```kotlin
var concedido by remember {
    mutableStateOf(tienePermiso())
}

val launcher = rememberLauncherForActivityResult(
    ActivityResultContracts.RequestPermission()
) { resultado ->
    concedido = resultado
}

Button(
    onClick = {
        launcher.launch(Manifest.permission.BLUETOOTH_CONNECT)
    }
) {
    Text("Pedir permiso Bluetooth")
}
```

En este ejemplo:

- `launcher` abre el diálogo del sistema.
- `{ resultado -> ... }` recibe `true` o `false`.
- `mutableStateOf` mantiene el estado y hace que Compose redibuje la interfaz cuando cambia.
- `launch(...)` inicia la solicitud.

La aplicación debe contemplar los estados concedido, denegado y denegado permanentemente. Si el usuario rechaza el permiso, debe explicarse qué función se deshabilita, ofrecer una alternativa cuando corresponda y respetar la decisión sin insistir indefinidamente.

### 8.3. Bluetooth en Android 11 y Android 12+

Android 12 separó los permisos de Bluetooth porque el escaneo podía permitir deducir la ubicación del usuario. La aplicación debe solicitar solo los permisos correspondientes a su versión y función.

Declaración de permisos:

```xml
<uses-permission
    android:name="android.permission.BLUETOOTH"
    android:maxSdkVersion="30" />

<uses-permission
    android:name="android.permission.BLUETOOTH_ADMIN"
    android:maxSdkVersion="30" />

<uses-permission
    android:name="android.permission.ACCESS_FINE_LOCATION"
    android:maxSdkVersion="30" />

<uses-permission
    android:name="android.permission.BLUETOOTH_SCAN"
    android:usesPermissionFlags="neverForLocation" />

<uses-permission
    android:name="android.permission.BLUETOOTH_CONNECT" />
```

- `maxSdkVersion="30"` limita el permiso a Android 11 y versiones anteriores.
- `neverForLocation` declara que el escaneo no se utilizará para ubicar al usuario y evita pedir ubicación en Android 12 o superior.
- Si el módulo ya está emparejado, como puede ocurrir con un HC-05, probablemente solo se necesite `BLUETOOTH_CONNECT`.

Actividad práctica:

1. Declarar los permisos en el manifiesto.
2. Solicitar un permiso en tiempo de ejecución con Compose.
3. Solicitar `SCAN` y `CONNECT` según la versión de Android.
4. Manejar concedido, denegado y denegado permanentemente.
5. Crear una tabla de permisos justificados para el proyecto propio, aplicando mínimo privilegio.

---

## 9. Bluetooth Low Energy y Arduino

En esta actividad se utiliza **Bluetooth Low Energy (BLE)**, integrado en el Arduino UNO R4 WiFi, sin módulo ni cables adicionales.

### 9.1. Central y periférico

- **Arduino:** actúa como periférico; se anuncia y espera conexiones.
- **Celular:** actúa como central; busca dispositivos y se conecta.

### 9.2. Modelo GATT

El modelo GATT puede entenderse mediante esta analogía:

- **Dispositivo:** edificio.
- **Servicio:** oficina que agrupa datos relacionados.
- **Característica:** buzón donde se lee o escribe un dato.
- **UUID:** identificador único del servicio o la característica.

En el ejemplo:

- Servicio: `19b10000-e8f2-537e-4f6c-d104768a1214`
- Característica: `19b10001-e8f2-537e-4f6c-d104768a1214`

La aplicación escribe `1` o `0` en la característica y Arduino reacciona encendiendo o apagando el LED.

### 9.3. Código del periférico Arduino

```cpp
#include <ArduinoBLE.h>

BLEService servicioAccion(
    "19b10000-e8f2-537e-4f6c-d104768a1214"
);

BLEByteCharacteristic caracteristicaComando(
    "19b10001-e8f2-537e-4f6c-d104768a1214",
    BLERead | BLEWrite
);

void setup() {
    BLE.begin();
    BLE.setLocalName("IoT-DuplaN");  // nombre único por dupla
    BLE.setAdvertisedService(servicioAccion);
    servicioAccion.addCharacteristic(caracteristicaComando);
    BLE.addService(servicioAccion);
    BLE.advertise();                  // "estoy aquí"
}

void loop() {
    BLEDevice central = BLE.central();

    while (central && central.connected()) {
        if (caracteristicaComando.written()) {
            byte v = caracteristicaComando.value();

            if (v == 1) {
                digitalWrite(LED_BUILTIN, HIGH);
            } else if (v == 0) {
                digitalWrite(LED_BUILTIN, LOW);
            }
            // Los demás valores se ignoran.
        }
    }
}
```

Antes de probar la aplicación, se recomienda probar el Arduino con **nRF Connect**. Si funciona desde nRF Connect, el problema probablemente se encuentra en la aplicación Android y no en el periférico.

---

## 10. Flujo de conexión BLE en Android

El flujo completo es:

1. Verificar y obtener permisos.
2. Escanear utilizando `ScanFilter`.
3. Conectar mediante `connectGatt`.
4. Descubrir servicios con `discoverServices`.
5. Escribir en la característica con `writeCharacteristic`.

Los callbacks principales son:

- `onScanResult`: informa que se detectó un dispositivo.
- `onConnectionStateChange`: informa que cambió el estado de la conexión.
- `onServicesDiscovered`: informa que ya se pueden consultar los servicios y características.

BLE es asíncrono: la aplicación solicita una operación y el sistema avisa después mediante un callback. Por esta razón no se puede escribir inmediatamente después de `connectGatt()`. El botón de envío debe habilitarse solamente cuando el estado sea `LISTO`, después de descubrir los servicios.

### 10.1. Escanear con filtro

```kotlin
val filtro = ScanFilter.Builder()
    .setServiceUuid(ParcelUuid(SERVICIO))
    .build()

adapter.bluetoothLeScanner.startScan(
    listOf(filtro),
    ajustes,
    scanCallback
)

private val scanCallback = object : ScanCallback() {
    override fun onScanResult(tipo: Int, r: ScanResult) {
        if (encontrados.none { it.device.address == r.device.address }) {
            encontrados.add(r)
            // La lista redibuja Compose.
        }
    }
}

handler.postDelayed(
    { detenerEscaneo() },
    10_000
)
```

`ScanFilter` limita la búsqueda a dispositivos que anuncian el UUID del servicio propio, en vez de mostrar todos los equipos Bluetooth del aula. El escaneo se detiene después de 10 segundos para evitar un consumo innecesario de batería.

### 10.2. Conectar, descubrir servicios y escribir

```kotlin
gatt = dispositivo.connectGatt(
    context,
    false,
    gattCallback,
    BluetoothDevice.TRANSPORT_LE
)

override fun onConnectionStateChange(
    g: BluetoothGatt,
    status: Int,
    newState: Int
) {
    if (newState == BluetoothProfile.STATE_CONNECTED) {
        g.discoverServices()
    }
}

override fun onServicesDiscovered(
    g: BluetoothGatt,
    status: Int
) {
    caracteristica = g
        .getService(SERVICIO)
        ?.getCharacteristic(COMANDO)

    estado = EstadoBle.LISTO
}

fun enviar(valor: Int) {
    val datos = byteArrayOf(valor.toByte())

    if (SDK_INT >= TIRAMISU) {
        // Android 13+: API nueva
        g.writeCharacteristic(
            c,
            datos,
            BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT
        )
    } else {
        // API antigua
        c.value = datos
        g.writeCharacteristic(c)
    }
}
```

La conexión debe cerrarse siempre con `gatt.close()` al desconectarse y en `onDispose` de la pantalla, para liberar correctamente los recursos.

### 10.3. Actividad de integración

La guía de laboratorio propone los siguientes checkpoints:

1. Dejar Arduino listo y probarlo con nRF Connect.
2. Escanear y listar los dispositivos.
3. Conectar y descubrir los servicios.
4. Enviar una acción y observar el LED.
5. Aplicar el flujo al proyecto propio.

Se utiliza una placa por dupla y un teléfono físico por persona. Cada parte debe explicarse en voz alta antes de avanzar.

---

## 11. Explicación de un diagnóstico BLE en Compose

El documento de código complementario analiza una función `DiagnosticoBle`, cuyo propósito es comprobar si el teléfono puede encontrar el Arduino mediante BLE.

### 11.1. Dependencias, manifiesto y librerías

Las dependencias se declaran en `AndroidManifest.xml`. Permiten obtener y utilizar permisos del dispositivo dentro de los límites establecidos, como el acceso a ubicación en las versiones que lo requieren.

Las librerías utilizadas son necesarias para:

- Mostrar componentes gráficos.
- Utilizar las funciones de Android.
- Establecer la conexión con el Arduino.

### 11.2. Anotaciones importantes

- `@SuppressLint("MissingPermission")`: suprime las advertencias del analizador relacionadas con la verificación explícita de permisos en tiempo de ejecución. Permite invocar las API de Bluetooth dentro de un bloque `try-catch`. Esta supresión no reemplaza la solicitud ni la comprobación real de permisos.
- `@Composable`: indica que `DiagnosticoBle` es una función reactiva de interfaz construida con Jetpack Compose.

### 11.3. Estado y contexto

- `LocalContext.current`: obtiene el `Context` de Android necesario para acceder a servicios del sistema, como `BluetoothManager`, y a las características del paquete mediante `PackageManager`.
- `mutableStateOf` y `remember`: definen y conservan la variable de estado `resultado` durante las recomposiciones. Cuando cambia `resultado`, Compose actualiza automáticamente el texto visible.

### 11.4. Elementos de la interfaz

- `Column`: organiza verticalmente los componentes hijos y aplica un `padding` interno de 24 dp.
- `Text(resultado)`: muestra el diagnóstico actual. Inicialmente muestra `"Toca <<Diagnosticar>>"` y luego el resultado del escaneo.
- `Button`: ejecuta el bloque de `onClick` cuando el usuario lo presiona y contiene el texto `"Diagnosticar"`.

### 11.5. Verificar el hardware BLE

`hasSystemFeature(...)` consulta si el teléfono cuenta con un chip compatible con Bluetooth Low Energy.

`BluetoothManager` y `adapter` permiten obtener el adaptador Bluetooth predeterminado del sistema operativo. El diagnóstico debe comprobar:

- Si el teléfono no posee soporte BLE.
- Si la antena Bluetooth está apagada.

En cualquiera de esos casos se informa al usuario la causa del problema.

### 11.6. Filtro y callback de escaneo

El `ScanFilter` limita la búsqueda a dispositivos BLE que anuncien el servicio del Arduino:

`19b10000-e8f2-537e-4f6c-d104768a1214`

`ScanCallback` es una clase abstracta nativa de Android (`android.bluetooth.le.ScanCallback`). `onScanResult(...)` es un callback: el código de la aplicación no lo llama manualmente; Android lo invoca en segundo plano cada vez que la antena captura un paquete de advertising del Arduino.

Parámetros entregados por Android:

- `tipo`: valor `Int` (`callbackType`) que indica la modalidad del evento, por ejemplo, si es la primera detección o un paquete repetido.
- `r`: objeto `ScanResult` que contiene la información técnica transmitida por el dispositivo Bluetooth remoto.

### 11.7. Operadores seguros de Kotlin

El operador `?.` evita un `NullPointerException`. Por ejemplo, si `scanRecord` existe, se obtiene `deviceName`; si es `null`, la expresión devuelve `null` sin provocar el cierre de la aplicación.

El operador Elvis (`?:`) proporciona un valor alternativo cuando la expresión de la izquierda es `null`:

- Si `r.scanRecord?.deviceName` devuelve un nombre, como `"Arduino_UNO"`, se utiliza ese nombre.
- Si no devuelve nombre porque Arduino no configuró uno visible, se utiliza `r.device.address`, por ejemplo `98:D3:31:FD:01:2C`.

### 11.8. Temporización del diagnóstico

`bluetoothLeScanner.startScan(...)` inicia el escaneo en segundo plano utilizando el filtro y la configuración definidos.

`Handler` junto con `postDelayed(...)` programa una tarea en el hilo principal (`Looper.getMainLooper()`) después de 8.000 milisegundos:

1. Detener el escaneo mediante `stopScan(cb)` para ahorrar batería.
2. Comprobar si `resultado` fue actualizado por el callback.
3. Si no se actualizó, informar `"No se encontro el Arduino"` porque el dispositivo no está al alcance.

---

## 12. Seguridad y continuidad del proyecto

Una prueba de clase muestra que, mediante nRF Connect, es posible escribir `01` en el Arduino de otra dupla. El hecho de que funcione demuestra que el dispositivo acepta la escritura, pero también evidencia un riesgo: si el sistema controlara una bomba, una cerradura u otro actuador crítico, un tercero podría ejecutar una acción no autorizada.

La seguridad no termina al obtener la conexión. Debe considerarse:

- Qué dispositivos pueden conectarse.
- Qué características permiten escritura.
- Qué acciones se habilitan con cada valor recibido.
- Qué ocurre cuando se pierde la conexión o se deniega un permiso.
- Qué permisos son realmente necesarios.

La sesión siguiente aborda ISO 27400 y la seguridad de aplicaciones IoT, además del avance de la APK individual. Más adelante se trabajará la recepción de datos desde Arduino en la unidad siguiente.

