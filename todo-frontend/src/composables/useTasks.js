import { ref, computed } from 'vue'
import * as taskApi from '../api/taskApi.js'

// ─────────────────────────────────────────────────────────────────────────────
// ¿Qué es un composable y por qué existe useTasks.js?
//
// Un composable es una función que encapsula estado reactivo y lógica reutilizable.
// Es el equivalente Vue 3 de los "custom hooks" de React.
//
// Sin este archivo, cada componente tendría que:
//   - Declarar su propio array de tareas
//   - Repetir la lógica de carga, creación, edición y borrado
//   - Sincronizar manualmente el estado entre componentes
//
// Con useTasks.js, toda esa lógica vive en un solo lugar.
// Los componentes solo llaman: const { tasks, createTask, ... } = useTasks()
//
// ref()      → hace que una variable sea "reactiva" (Vue detecta cambios y
//              re-renderiza los componentes que la usan automáticamente)
// computed() → valor derivado que se recalcula solo cuando cambia su dependencia
// ─────────────────────────────────────────────────────────────────────────────

export function useTasks() {
  const tasks       = ref([])          // lista de tareas cargadas
  const loading     = ref(false)       // true mientras hay una petición activa
  const error       = ref(null)        // mensaje de error si algo falla
  const activeFilter = ref(null)       // 'PENDIENTE' | 'EN_PROGRESO' | 'COMPLETADA' | null

  // Cuenta de tareas por estado — se recalcula solo cuando cambia `tasks`
  const counts = computed(() => ({
    total:      tasks.value.length,
    pendiente:  tasks.value.filter(t => t.status === 'PENDIENTE').length,
    enProgreso: tasks.value.filter(t => t.status === 'EN_PROGRESO').length,
    completada: tasks.value.filter(t => t.status === 'COMPLETADA').length,
  }))

  // ── Cargar tareas ──────────────────────────────────────────────────────────
  async function loadTasks(status = null) {
    loading.value = true
    error.value   = null
    try {
      const response  = await taskApi.getTasks(status)
      tasks.value     = response.data
      activeFilter.value = status
    } catch (e) {
      error.value = e.response?.data?.mensaje || 'Error al cargar las tareas'
    } finally {
      loading.value = false
    }
  }

  // ── Crear tarea ────────────────────────────────────────────────────────────
  async function createTask(formData) {
    loading.value = true
    error.value   = null
    try {
      const response = await taskApi.createTask(formData)
      tasks.value.unshift(response.data)   // añadir al inicio de la lista
    } catch (e) {
      error.value = e.response?.data?.mensaje || 'Error al crear la tarea'
      throw e   // re-lanzar para que el formulario sepa que falló
    } finally {
      loading.value = false
    }
  }

  // ── Editar tarea ───────────────────────────────────────────────────────────
  async function updateTask(id, formData) {
    loading.value = true
    error.value   = null
    try {
      const response = await taskApi.updateTask(id, formData)
      // Reemplazar solo la tarea modificada en el array (evita recargar todo)
      const index = tasks.value.findIndex(t => t.id === id)
      if (index !== -1) tasks.value[index] = response.data
    } catch (e) {
      error.value = e.response?.data?.mensaje || 'Error al actualizar la tarea'
      throw e
    } finally {
      loading.value = false
    }
  }

  // ── Cambiar estado ─────────────────────────────────────────────────────────
  async function changeStatus(id, newStatus) {
    try {
      const response = await taskApi.changeStatus(id, newStatus)
      const index = tasks.value.findIndex(t => t.id === id)
      if (index !== -1) tasks.value[index] = response.data
    } catch (e) {
      error.value = e.response?.data?.mensaje || 'Error al cambiar el estado'
    }
  }

  // ── Marcar como completada ─────────────────────────────────────────────────
  async function completeTask(id) {
    try {
      const response = await taskApi.completeTask(id)
      const index = tasks.value.findIndex(t => t.id === id)
      if (index !== -1) tasks.value[index] = response.data
    } catch (e) {
      error.value = e.response?.data?.mensaje || 'Error al completar la tarea'
    }
  }

  // ── Eliminar tarea ─────────────────────────────────────────────────────────
  async function deleteTask(id) {
    try {
      await taskApi.deleteTask(id)
      tasks.value = tasks.value.filter(t => t.id !== id)
    } catch (e) {
      error.value = e.response?.data?.mensaje || 'Error al eliminar la tarea'
    }
  }

  return {
    tasks,
    loading,
    error,
    counts,
    activeFilter,
    loadTasks,
    createTask,
    updateTask,
    changeStatus,
    completeTask,
    deleteTask
  }
}
