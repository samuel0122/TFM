# Aplicación móvil para el análisis de documentos musicales mediante redes neuronales

Este repositorio contiene el código fuente de la aplicación desarrollada como parte del Trabajo de Fin de Máster (TFM) del Máster Universitario en Desarrollo de Sistemas Software para Dispositivos Móviles de la Universidad de Alicante.

## Descripción

El objetivo principal de esta aplicación es facilitar el análisis de documentos musicales mediante el uso de redes neuronales. Sin embargo, está limitado a la extracción de pentagramas por las limitaciones del modelo de red neuronal en la API.

## Características
- 🌟 Clean Architecture con separación de capas: Data, Domain, y UI.
- 🖥️ Implementación del patrón de presentacion MVVM.
- 🗂️ Implementación del patrón de datos Repository.
- 🔄 Uso de Android Workers para tareas en segundo plano.
- 🌐 Integración con [API](https://github.com/samuel0122/TFM_Api) desplegada en [Heroku](https://www.heroku.com/home).

## Arquitectura

Este proyecto aplica los principios de Clean Architecture, y utiliza el patrón de presentaciçon MVVM (Model-View-ViewModel) para separar la lógica de presentación y las capas de dominio y datos, y el patrón de acceso a datos Repository para manejar distintas fuentes de datos, asegurando así un código más limpio, mantenible y escalable.

## Tecnologías

La aplicación hace uso de un conjunto de tecnologías y librerías modernas para Android que garantizan un desarrollo robusto y eficiente. Estas herramientas están descritas en la sección 3.1.2 del trabajo final.

- 🛜 Retrofit: Para realizar solicitudes a la API para procesar las imagenes de manera sencilla.
- 👷🏻 WorkManager: Para la gestión de tareas en segundo plano de manera persistente.
- 🧭 Navigation Components: Para gestionar la navegación dentro de la aplicación.
- 📖 ViewPager2: Para la implementación de vistas paginadas en la interfaz de usuario.
- 📷 CameraX: Para capturar la integración de la cámara en la aplicació para capturar imágenes.
- 💉 Dagger Hilt: Para la inyección de dependencias, facilitando un código modular y desacoplado.
- 💾 Room: Para la gestión de la base de datos local mediante una capa de abstracción sobre SQLite.
- 🖼️ Glide: Para la carga y el manejo eficiente de imágenes.
    
## Publicación del Trabajo

Puedes acceder a la publicación completa del TFM en [este enlace](http://hdl.handle.net/10045/147284), titulado "Aplicación móvil para el análisis de documentos musicales mediante redes neuronales".
