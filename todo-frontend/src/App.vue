<template>
  <div class="app">
    <header class="app-header">
      <h1>📝 To-Do List</h1>
      <button class="btn-primary" @click="openCreate">+ Nueva tarea</button>
    </header>

    <main class="app-main">
      <!-- Barra de filtros -->
      <TaskFilter
        :active-filter="activeFilter"
        :counts="counts"
        @filter-change="handleFilterChange"
      />

      <!-- Lista de tareas -->
      <TaskList
        :tasks="tasks"
        :loading="loading"
        :error="error"
        :active-filter="activeFilter"
        @edit="openEdit"
        @delete="handleDelete"
        @complete="handleComplete"
        @status-change="handleStatusChange"
      />
    </main>

    <!-- Modal crear / editar (solo se monta cuando showForm es true) -->
    <TaskForm
      v-if="showForm"
      :task-to-edit="taskToEdit"
      :loading="loading"
      @close="closeForm"
      @save="handleSave"
    />
  </div>
</template>

<script setup>
// ─────────────────────────────────────────────────────────────────────────────
// App.vue — el orquestador principal
//
// ¿Por qué App.vue concentra la lógica?
// Todos los componentes hijos (TaskFilter, TaskList, TaskForm) son "tontos":
// solo muestran datos y emiten eventos. App.vue es el único que sabe
// qué hacer cuando llega un evento: llama al composable y actualiza el estado.
//
// Este patrón se llama "lifting state up" (elevar el estado).
// El estado vive en el nivel más alto que lo necesite; los hijos lo reciben
// por props y se comunican hacia arriba por eventos.
//
// onMounted → lifecycle hook de Vue. Se ejecuta una vez, cuando el componente
//             ya está en el DOM. Perfecto para la carga inicial de datos.
// ─────────────────────────────────────────────────────────────────────────────
import { ref, onMounted } from 'vue'
import { useTasks }    from './composables/useTasks.js'
import TaskFilter      from './components/TaskFilter.vue'
import TaskList        from './components/TaskList.vue'
import TaskForm        from './components/TaskForm.vue'

const {
  tasks, loading, error, counts, activeFilter,
  loadTasks, createTask, updateTask, changeStatus, completeTask, deleteTask
} = useTasks()

// Estado local del modal
const showForm   = ref(false)
const taskToEdit = ref(null)

// Carga inicial al montar el componente
onMounted(() => loadTasks())

// ── Handlers del modal ─────────────────────────────────────────────────────
function openCreate() {
  taskToEdit.value = null
  showForm.value   = true
}

function openEdit(task) {
  taskToEdit.value = task
  showForm.value   = true
}

function closeForm() {
  showForm.value   = false
  taskToEdit.value = null
}

// ── Guardar (crear o editar según si hay taskToEdit) ───────────────────────
async function handleSave(formData) {
  if (taskToEdit.value) {
    await updateTask(taskToEdit.value.id, formData)
  } else {
    await createTask(formData)
  }
  if (!error.value) closeForm()
}

// ── Acciones desde TaskCard ────────────────────────────────────────────────
function handleFilterChange(status) { loadTasks(status)       }
function handleDelete(id)           { deleteTask(id)           }
function handleComplete(id)         { completeTask(id)         }
function handleStatusChange(id, s)  { changeStatus(id, s)      }
</script>
