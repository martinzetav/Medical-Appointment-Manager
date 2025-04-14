# 🏥 Gestor de Turnos Médicos 🏥

Les presento un proyecto en el que he estado trabajando: **Gestor de Turnos Médicos**, un sistema diseñado para optimizar la administración de citas médicas y mejorar la experiencia de los usuarios. Este sistema fue desarrollado utilizando tecnologías modernas y siguiendo buenas prácticas de desarrollo de software.

## 🛠 Tecnologías utilizadas
- **Backend:** Java, Spring Boot
- **Seguridad:** Spring Security, JWT, OAuth2
- **Base de datos:** MySQL, Flyway (para inicialización automática)
- **Herramientas adicionales:** Lombok, Postman
- **Documentación de API:** Swagger
- **Proveedores de autenticación OAuth2:** Google 🌐 y GitHub 🐱

## 🔐 Control de roles y permisos
El sistema cuenta con gestión avanzada de roles predefinidos:
- **Admin:** Gestión completa, incluyendo eliminación de turnos, pacientes y doctores.
- **Secretary:** Creación de turnos, consulta de agendas y gestión eficiente de citas.
- **Doctor:** Visualización de turnos asociados, acceso a información de pacientes con privacidad garantizada.
- **User (Paciente):** Acceso exclusivo a su información de turnos y búsqueda de médicos por especialidad.

## 🖌️ Diseño y arquitectura
- **Patrón Builder:** Simplificación en la creación de objetos complejos.
- **DTOs:** Transferencia eficiente de datos entre capas.
- **Manejo de excepciones globales:** Respuestas consistentes y controladas.
- **Arquitectura Multicapa:** Separación de responsabilidades para mayor mantenibilidad.
- **APIs REST:** Interacción eficiente entre servicios y clientes.

## ⚙️ Funcionalidades clave
### 👨‍⚕️ Doctores
- Visualización de turnos asociados.
- Acceso a información clave de pacientes (nombre, apellido, DNI y correo).

### 🤦🏼 Pacientes
- Acceso exclusivo a sus turnos y detalles de médicos tratantes.
- Búsqueda de doctores por especialidad.

### 📋 Secretary
- Creación de turnos sin superposición de horarios.
- Consulta de turnos y médicos por especialidad.
- Gestión de pacientes por DNI.

### 🔑 Administrador
- Todas las funcionalidades mencionadas.
- Capacidad para eliminar turnos, pacientes y doctores.

## 📄 Documentación interactiva
Explora fácilmente los endpoints de la API gracias a **Swagger**, diseñado para hacer la integración con servicios externos más sencilla y clara.
