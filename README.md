# 📍 LocationSaver — Aplicación Android en Kotlin

**LocationSaver** es una aplicación móvil moderna desarrollada en **Kotlin**, diseñada para guardar rápidamente la ubicación actual del usuario. Cada registro incluye:

- Latitud  
- Longitud  
- Link directo a Google Maps  
- Fecha y hora exactas del guardado  

La app también ofrece una pantalla donde puedes consultar, administrar y eliminar todas las ubicaciones almacenadas.

---

## ✨ Características principales

### 🟥 Guardar ubicación actual
Al presionar un botón central con el ícono 📍, la aplicación:

1. Solicita permisos de ubicación si aún no se han otorgado  
2. Obtiene latitud y longitud mediante `FusedLocationProviderClient`  
3. Genera un enlace de Google Maps  
4. Registra fecha y hora  
5. Guarda la información en la base de datos local

---

### 🟨 Ver ubicaciones guardadas
En una segunda pantalla podrás:

- Ver todas las ubicaciones registradas  
- Consultar coordenadas, fecha/hora y link  
- Eliminar registros individualmente
- Asignar un nombre a cada ubicación  

---

## 🛠️ Tecnologías y arquitectura

### 🔷 Lenguaje
- **Kotlin 100%**

### 🔷 Arquitectura
- **MVVM (Model-View-ViewModel)**  
- Patrón Repository  
- StateFlow / LiveData  
- Navegación moderna

### 🔷 Librerías principales
- **Room** (almacenamiento local)  
- **Hilt** (inyección de dependencias)  
- **Google Play Services Location**  
- **Jetpack Compose**
- **XML** 
- **Material 3**  
- **Navigation Compose**

---

## 🎨 Diseño y UI

La app utiliza un diseño minimalista basado en **Material 3**.

### 🎨 Colores principales
- **Primario:** `#AB2439` (rojo vino)  
- **Secundario:** `#FDC57E` (dorado)

### 🖼 Pantallas
#### Pantalla principal
- Botón grande con el ícono 📍 centrado  
- Botón “Ver guardados” debajo

#### Pantalla de ubicaciones guardadas
- Lista de ubicaciones con:
  - Latitud  
  - Longitud  
  - Link de Google Maps  
  - Fecha y hora  
- Botón / icono para eliminar elementos

---

## 🔒 Permisos utilizados
La aplicación requiere:

- `ACCESS_FINE_LOCATION`  
- `ACCESS_COARSE_LOCATION`

Manejo moderno mediante `ActivityResultLauncher`.

---

## 🚀 Objetivo del proyecto

El proyecto tiene fines personales y educativos, permitiendo:

- Practicar Kotlin moderno  
- Implementar Room, Hilt, MVVM y Navigation  
- Trabajar con APIs de ubicación  
- Aplicar buenas prácticas y arquitectura limpia

---

## 📂 Estado del proyecto

🔧 Terminado, funcional. Con posibilidad de implementar mejoras.

---

## 🤝 Contribuciones

¡Las contribuciones, ideas y mejoras son bienvenidas!  
Puedes abrir issues o enviar pull requests.

---


