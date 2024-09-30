# Aplicación móvil para el análisis de documentos musicales mediante redes neuronales

Este repositorio contiene el código fuente de la aplicación desarrollada como parte del Trabajo de Fin de Máster (TFM) del Máster Universitario en Desarrollo de Sistemas Software para Dispositivos Móviles de la Universidad de Alicante.

## Descripción del Proyecto

El objetivo principal de esta aplicación es facilitar el análisis de documentos musicales mediante el uso de redes neuronales, proporcionando una solución innovadora y eficiente para el reconocimiento y procesamiento de partituras musicales y otros documentos relacionados.

## Arquitectura

Este proyecto aplica los principios de Clean Architecture, y utiliza el patrón MVVM (Model-View-ViewModel) para separar la lógica de presentación y las capas de dominio y datos, asegurando así un código más limpio, mantenible y escalable.

## Tecnologías y Librerías Utilizadas

La aplicación hace uso de un conjunto de tecnologías y librerías modernas para Android que garantizan un desarrollo robusto y eficiente. Estas herramientas están descritas en la sección 3.1.2 del trabajo final.

* Retrofit: Para realizar solicitudes a la API para procesar las imagenes de manera sencilla.
* WorkManager: Para la gestión de tareas en segundo plano de manera persistente.
* Navigation Components: Para gestionar la navegación dentro de la aplicación.
* ViewPager2: Para la implementación de vistas paginadas en la interfaz de usuario.
* CameraX: Para capturar la integración de la cámara en la aplicació para capturar imágenes.
* Dagger Hilt: Para la inyección de dependencias, facilitando un código modular y desacoplado.
* Room: Para la gestión de la base de datos local mediante una capa de abstracción sobre SQLite.
* Glide: Para la carga y el manejo eficiente de imágenes.
    
## Publicación del Trabajo

Puedes acceder a la publicación completa del TFM en [este enlace](http://hdl.handle.net/10045/147284), titulado "Aplicación móvil para el análisis de documentos musicales mediante redes neuronales".
