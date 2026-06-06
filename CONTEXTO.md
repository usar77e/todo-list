# Contexto del Proyecto: To-Do List

## 1. ¿Qué hace este proyecto?
Es una aplicación de gestión de tareas que permite a los usuarios crear, modificar, filtrar y eliminar tareas. El sistema mantiene un registro del estado de cada tarea (PENDIENTE, EN_PROGRESO, COMPLETADA) y persiste los datos en una base de datos H2 (en archivo), lo que permite que la información no se pierda al reiniciar el servidor.

## 2. Arquitectura General
El proyecto sigue un modelo de **cliente-servidor desacoplado**:

*   **Backend (Java / Spring Boot 3):**
    *   Actúa como una API REST.
    *   **Capa de Datos:** Usa Spring Data JPA y H2 Database.
    *   **Lógica de Negocio:** Implementada en servicios (`TaskService`) que median entre el repositorio y los controladores.
    *   **Comunicación:** Expone endpoints JSON y maneja la seguridad de origen mediante CORS para permitir peticiones desde el frontend.
    *   **DTOs:** Utiliza Objetos de Transferencia de Datos (`TaskRequestDTO`, `TaskResponseDTO`) para separar la entidad de base de datos del contrato de la API.

*   **Frontend (JavaScript / Vue 3 + Vite):**
    *   Es una Single Page Application (SPA).
    *   **Estado:** Gestionado mediante un *composable* (`useTasks.js`) que centraliza la reactividad y las llamadas a la API.
    *   **Componentes:** Arquitectura modular basada en componentes especializados (filtros, lista, tarjetas y formularios).
    *   **Comunicación:** Utiliza `axios` para consumir los endpoints del backend.

## 3. Puntos Clave para entender el código
Para navegar por el proyecto, es fundamental comprender:
1.  **Flujo de Datos en Vue:** Los datos bajan mediante *props* y los eventos suben mediante *emits*. El estado reside en el nivel más alto necesario (`App.vue` / `useTasks.js`).
2.  **El rol de los DTOs:** No se devuelve la entidad `Task` directamente al cliente; se mapea a un `TaskResponseDTO` para evitar exponer detalles internos de la base de datos.
3.  **Reactividad en Vue 3:** El uso de `ref()` y `computed()` es central. Los `computed` se usan para filtrar tareas o contar estados sin modificar el array original.
4.  **Manejo de Errores Global:** En el backend, el `GlobalExceptionHandler` captura excepciones y las transforma en respuestas JSON estandarizadas (HTTP 400, 404, etc.).

## 4. Partes Complejas o Menos Obvias
*   **Proxy de Vite:** El archivo `vite.config.js` configura un proxy para redirigir `/api/*` al puerto 8080. Esto es crucial porque evita errores de CORS en desarrollo haciendo que el navegador crea que todo está en el mismo dominio.
*   **Sincronización del Estado (Frontend):** Cuando se actualiza una tarea o cambia su estado, el frontend no vuelve a pedir toda la lista; actualiza el elemento específico en el array reactivo `tasks` usando el índice encontrado por ID, optimizando la renderización.
*   **Ciclo de Vida y Watchers:** El uso de `watch` con `immediate: true` en el formulario de tareas es un detalle técnico importante para precargar datos cuando se entra en modo edición.

## 5. Bugs Silenciosos Identificados
*   **Persistencia del estado de error en Modal:** En `todo-frontend/src/App.vue` (función `handleSave`), la condición `if (!error.value) closeForm()` podría fallar si el estado de `error` no se limpia correctamente entre intentos, manteniendo el modal abierto o cerrado erróneamente.
*   **Inconsistencia visual con filtros activos:** En `todo-frontend/src/composables/useTasks.js` (función `createTask`), el uso de `tasks.value.unshift(response.data)` agrega la tarea al estado global; si hay un filtro activo, esto podría generar confusión visual si la nueva tarea no cumple con el filtro pero se inserta en el array base.
*   **Race Conditions por clics múltiples:** En `todo-frontend/src/composables/useTasks.js` (funciones `createTask` y `updateTask`), si el botón de envío en el componente `TaskForm.vue` no deshabilita la interacción basándose en el estado `loading`, se pueden disparar múltiples peticiones HTTP idénticas.
*   **Fallo de actualización quirúrgica por tipo de dato:** En `todo-frontend/src/composables/useTasks.js` (función `updateTask`), la línea `tasks.value.findIndex(t => t.id === id)` es sensible al tipo de dato. Si el ID llega como string en lugar de number, la tarea se actualizará en la BD pero no en la pantalla.
