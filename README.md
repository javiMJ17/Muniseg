
# MuniSeg - Aplicación de Seguridad Municipal 🛡️

MuniSeg es una aplicación móvil desarrollada para Android, orientada a la seguridad ciudadana y la participación de los vecinos de la comuna de San Joaquín.

Permite registrar incidencias, consultar reportes ciudadanos, visualizar alertas, administrar perfiles de usuario y realizar un seguimiento de los casos registrados.

Proyecto académico desarrollado para la asignatura de Desarrollo de Aplicaciones Móviles de Duoc UC.

---

## Integrantes

| Nombre | Rol |
|---|---|
| Isaac Manzor | Estudiante |
| Javier Castro | Estudiante |
| Benjamin Labrin | Estudiante |

---

## Tecnologías utilizadas

- Kotlin.
- Android Studio.
- Jetpack Compose.
- Material 3.
- Navigation Compose.
- Arquitectura MVVM.
- Retrofit 2.11.0.
- Gson.
- Xano como backend externo.
- Room Database.
- SharedPreferences.
- Kotlin Coroutines.
- Google Maps API.
- Google Play Services Location.
- JUnit.
- MockWebServer.
- Git y GitHub.
- Trello.

---

## Funcionalidades principales

- Registro e inicio de sesión de usuarios mediante almacenamiento local.
- Visualización y edición del perfil de usuario.
- Registro de incidencias ciudadanas mediante una API REST.
- Selección de categorías y prioridades obtenidas desde Xano.
- Consulta de incidencias almacenadas en el servidor.
- Consulta del detalle de una incidencia mediante su ID.
- Filtrado de incidencias según su estado.
- Visualización de alertas activas y reportes registrados.
- Seguimiento local de casos.
- Captura de evidencia fotográfica mediante la cámara.
- Visualización de mapas y ubicación del dispositivo.
- Bandeja de notificaciones de demostración.
- Ícono personalizado de la aplicación.

---

## Arquitectura MVVM

La integración con la API REST utiliza la arquitectura Model-View-ViewModel, separando la interfaz gráfica, la lógica de presentación y el acceso a los datos.

### Model

Representa los datos de las incidencias mediante las clases IncidenciaApi y NuevaIncidenciaApi.

### View

Corresponde a las pantallas desarrolladas con Jetpack Compose, encargadas de mostrar información y recibir las acciones del usuario.

### ViewModel

IncidenciasViewModel administra los estados de carga, éxito y error de las solicitudes realizadas a Xano.

### Repository

IncidenciasRepository permite separar la lógica de acceso a los datos de la interfaz gráfica.

### Flujo de comunicación

```text
Pantallas Jetpack Compose
          |
          v
IncidenciasViewModel
          |
          v
IncidenciasRepository
          |
          v
Retrofit / MunisegApi
          |
          v
Backend Xano
          |
          v
Base de datos
```

---

## Integración con API REST

Para la Evaluación 3 se implementó el consumo de siete endpoints mediante Retrofit, utilizando Xano como backend externo.

### URL base

```text
https://x8ki-letl-twmt.n7.xano.io/api:6yaLESbh/
```

### Endpoints implementados

| Método | Endpoint | Descripción |
|---|---|---|
| GET | /incidencias | Obtener todas las incidencias |
| POST | /incidencias | Registrar una nueva incidencia |
| GET | /incidencias/{incidencias_id} | Consultar una incidencia por ID |
| GET | /categorias | Obtener categorías de reportes |
| GET | /prioridades | Obtener prioridades disponibles |
| GET | /estados | Obtener estados de incidencias |
| GET | /alertas | Consultar incidencias activas |

Los siete endpoints fueron integrados y comprobados desde la aplicación Android.

Las operaciones PATCH y DELETE mantienen deshabilitado su acceso externo, debido a que no son necesarias para las funcionalidades actuales del usuario.

---

## Recursos nativos del dispositivo

### Cámara

La aplicación permite capturar fotografías mediante la cámara del dispositivo utilizando ActivityResultContracts.TakePicturePreview.

La fotografía se visualiza dentro del formulario de registro de incidencias y se conserva localmente como evidencia.

### Geolocalización

Se utiliza FusedLocationProviderClient para obtener la ubicación del dispositivo.

La pantalla principal permite visualizar la ubicación mediante Google Maps, utilizando los permisos correspondientes de Android.

### Permisos utilizados

- INTERNET.
- CAMERA.
- ACCESS_FINE_LOCATION.
- ACCESS_COARSE_LOCATION.

---

## Pruebas unitarias

Se implementaron dos pruebas utilizando JUnit, Kotlin Coroutines Test y MockWebServer.

| Archivo | Objetivo | Resultado |
|---|---|---|
| IncidenciasRepositoryTest.kt | Comprobar una solicitud GET y la conversión de la respuesta JSON | Aprobada |
| IncidenciasViewModelTest.kt | Comprobar el manejo de errores de conexión | Aprobada |

Las pruebas utilizan un servidor simulado y un repositorio falso, evitando modificar los datos reales de Xano.

### Ejecutar las pruebas

Desde la terminal de Android Studio en Windows:

```powershell
.\gradlew.bat :app:testDebugUnitTest
```

Resultado obtenido:

```text
BUILD SUCCESSFUL
```

---

## Estructura del proyecto

```text
com.example.muniseg/
├── data/
├── model/
├── remote/
│   ├── MunisegApi.kt
│   └── RetrofitClient.kt
├── repository/
│   ├── MunisegRepository.kt
│   └── IncidenciasRepository.kt
├── viewmodel/
│   └── IncidenciasViewModel.kt
├── ui/
│   ├── alertas/
│   ├── home/
│   ├── incidencias/
│   ├── login/
│   ├── navigation/
│   ├── notificaciones/
│   ├── perfil/
│   ├── report/
│   └── theme/
└── MainActivity.kt
```

### Estructura de pruebas

```text
app/src/test/java/com/example/muniseg/
├── repository/
│   └── IncidenciasRepositoryTest.kt
└── viewmodel/
    └── IncidenciasViewModelTest.kt
```

---

## Cómo ejecutar el proyecto

1. Clonar el repositorio:

```bash
git clone https://github.com/javiMJ17/Muniseg.git
```

2. Abrir el proyecto en Android Studio.
3. Sincronizar las dependencias de Gradle.
4. Configurar un emulador Android o conectar un dispositivo físico.
5. Configurar una clave válida de Google Maps si corresponde.
6. Ejecutar la aplicación mediante el botón Run.
7. Aceptar los permisos necesarios para utilizar la cámara y ubicación.

---

## Límites y supuestos

- La aplicación corresponde a un prototipo académico.
- Los usuarios y sesiones se administran mediante almacenamiento local.
- Las incidencias se registran y consultan mediante Xano.
- La evidencia fotográfica se conserva localmente.
- Las ubicaciones de los reportes son referenciales.
- Las notificaciones corresponden a datos de demostración.
- Los endpoints públicos deben utilizar únicamente información ficticia durante las pruebas.
- Para una implementación productiva se requiere incorporar autenticación y autorización en el backend.

---

## Mejoras futuras

- Autenticación de usuarios mediante el backend.
- Control de acceso según roles y permisos.
- Almacenamiento de fotografías en la nube.
- Notificaciones push en tiempo real.
- Geolocalización automática de los reportes.
- Sincronización completa de los casos locales y remotos.
- Panel de administración para funcionarios municipales.

---

## Gestión del proyecto

- GitHub: https://github.com/javiMJ17/Muniseg
- Trello: https://trello.com/b/Ja2Vj1zs
- Video de demostración: https://drive.google.com/drive/folders/1n7hgri7_G03wzfbO7vg0wuTukv0_ZhA1?usp=sharing

---

