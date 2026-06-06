import axios from 'axios'

// ─────────────────────────────────────────────────────────────────────────────
// ¿Por qué existe esta capa de API separada?
//
// Centralizar todas las llamadas HTTP en un solo archivo tiene tres ventajas:
// 1. Si la URL base cambia, solo se cambia aquí.
// 2. Los componentes Vue no necesitan saber nada de HTTP; solo llaman funciones.
// 3. Es fácil añadir headers globales (autenticación, tokens) en un solo lugar.
//
// axios es la librería que usamos para hacer peticiones HTTP.
// Es más cómoda que fetch nativo porque parsea JSON automáticamente
// y maneja errores con try/catch de forma más limpia.
// ─────────────────────────────────────────────────────────────────────────────

const api = axios.create({
  baseURL: '/api',          // el proxy de vite.config.js redirige esto a :8080/api
  headers: {
    'Content-Type': 'application/json'
  }
})

// Obtener todas las tareas, opcionalmente filtradas por estado
// status puede ser: 'PENDIENTE' | 'EN_PROGRESO' | 'COMPLETADA' | null
export const getTasks = (status = null) => {
  const params = status ? { status } : {}
  return api.get('/tasks', { params })
}

// Obtener una tarea por su id
export const getTaskById = (id) => api.get(`/tasks/${id}`)

// Crear tarea nueva. body = { title, description }
export const createTask = (body) => api.post('/tasks', body)

// Actualizar título y descripción de una tarea existente
export const updateTask = (id, body) => api.put(`/tasks/${id}`, body)

// Cambiar el estado a cualquier valor del enum TaskStatus
export const changeStatus = (id, value) =>
  api.patch(`/tasks/${id}/status`, null, { params: { value } })

// Atajo para marcar como COMPLETADA directamente
export const completeTask = (id) => api.patch(`/tasks/${id}/complete`)

// Eliminar tarea permanentemente
export const deleteTask = (id) => api.delete(`/tasks/${id}`)
