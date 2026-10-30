# MuniSeg - Aplicación de Seguridad Municipal 🛡️

Aplicación móvil desarrollada para Android orientada a la seguridad ciudadana y la participación colaborativa en la comuna de San Joaquín. Permite a los vecinos reportar incidencias de forma anónima o registrada, gestionar cuadrantes referenciales, administrar perfiles de usuario locales y realizar un seguimiento transparente de los casos reportados.

---

## Integrantes

| Nombre | Rol / Usuario |
| :--- | :--- |
| **Isaac Manzor**    | Estudiante |
| **Javier Castro**   | Estudiante |
| **Benjamin Labrin** | Estudiante |

---

## Tecnologías

* **UI / Framework:** Jetpack Compose, Material 3, Material Icons.
* **Navegación:** Navigation Compose con gestión de estados de pila (`launchSingleTop = true`).
* **Persistencia Local:** Base de datos **Room** (con entidades, DAOs y flujos asíncronos mediante `Flow`) y almacenamiento de sesión con **SharedPreferences** (`SessionManager`).
* **Concurrencia:** Kotlin Coroutines y `viewModelScope` para operaciones asíncronas seguras.
* **APIs y Componentes:** Google Maps API para geolocalización y mapas referenciales de cuadrantes.

---

## Funcionalidades

* **Autenticación y Registro Local:** Registro de nuevos usuarios y validación de credenciales almacenadas de manera local mediante Room y gestión de sesiones activas con `SessionManager`.
* **Gestión de Perfil de Usuario:** Visualización dinámica de identidad y correo electrónico en `PerfilScreen`, con una vista de edición (`EditarPerfilScreen`) conectada a base de datos y validaciones de formularios en tiempo real (correos válidos, longitud y coincidencia de contraseñas).
* **Módulo de Reporte Ciudadano:** Creación de incidencias seleccionando categorías (luminaria pública dañada, ruidos molestos, vehículos abandonados, microbasurales, situaciones sospechosas) y sectores referenciales geolocalizados.
* **Trazabilidad de Casos:** Seguimiento del estado de las incidencias a través de un ciclo de vida definido (*Ingresado*, *Derivado*, *En Terreno*, *Cerrado*) con registro histórico de eventos por rol (*Vecino*, *Operador*, *Inspector*).
* **Identidad Anónima para Reportes:** Generación automática de alias protegidos (ej. *Vecino Anónimo #104*) para resguardar la privacidad del usuario al emitir alertas públicas.

---

## Cómo ejecutar el proyecto

1. **Clonar el repositorio:**
   ```bash
   git clone <url-del-repositorio>
2. Abrir en Android Studio:

    Abre Android Studio, selecciona Open y busca la carpeta raíz del proyecto MuniSeg.

3. Sincronizar Gradle:

    Espera a que el entorno sincronice automáticamente las dependencias del proyecto.

4. Configurar dispositivo:

    Selecciona un emulador Android configurado (recomendado API 26 o superior) o conecta un dispositivo físico mediante depuración USB.

5. Ejecutar la app:

    Haz clic en el botón Run (▶) en la barra superior de Android Studio.

---

## Estructura del proyecto

```text
com.example.muniseg/
├── data/               # Base de datos Room (AppDatabase, UsuarioDao, UsuarioEntity, SessionManager)
├── model/              # Modelos de datos (Incidencia, RolUsuario, SectorReferencial, etc.)
├── repository/         # Repositorio central de estados compartidos (MunisegRepository)
├── ui/                 # Pantallas y componentes en Jetpack Compose
│   ├── alertas/        # Pantalla de alertas ciudadanas
│   ├── home/           # Pantalla principal del sistema
│   ├── login/          # Autenticación y creación de cuentas
│   ├── navigation/     # Configuración de rutas y NavHost
│   ├── notificaciones/ # Bandeja de notificaciones y avisos
│   ├── perfil/         # PerfilScreen y EditarPerfilScreen
│   └── report/         # Formulario de creación de incidencias
└── MainActivity.kt     # Punto de entrada principal de la aplicación
```

---

## Límites y supuestos

    La persistencia de datos está diseñada para operar de forma local en el dispositivo del usuario utilizando Room y SharedPreferences, sin depender de un servidor backend externo en esta fase académica.

    Las coordenadas de los sectores de San Joaquín utilizan puntos referenciales predefinidos para asegurar una simulación fluida y controlada en entornos de emulación móvil.

---

## Mejoras futuras

    Integración completa con servicios backend en la nube mediante Retrofit y Ktor.

    Notificaciones push en tiempo real ante cambios de estado en los reportes ciudadanos.

    Carga de imágenes reales mediante la cámara del dispositivo adjuntas al reporte geolocalizado.

---

## Gestión del proyecto

    Tablero Trello: https://trello.com/b/Ja2Vj1zs

    Video de demostración:
