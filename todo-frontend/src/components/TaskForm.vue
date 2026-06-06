<template>
  <!-- Overlay oscuro detrás del modal -->
  <div class="modal-overlay" @click.self="$emit('close')">
    <div class="modal">
      <h2>{{ isEditing ? 'Editar tarea' : 'Nueva tarea' }}</h2>

      <form @submit.prevent="handleSubmit">
        <div class="field">
          <label for="title">Título *</label>
          <input
            id="title"
            v-model="form.title"
            type="text"
            maxlength="100"
            placeholder="Escribe el título..."
            required
          />
        </div>

        <div class="field">
          <label for="description">Descripción</label>
          <textarea
            id="description"
            v-model="form.description"
            maxlength="500"
            rows="3"
            placeholder="Descripción opcional..."
          ></textarea>
        </div>

        <p v-if="localError" class="form-error">{{ localError }}</p>

        <div class="form-actions">
          <button type="button" class="btn-secondary" @click="$emit('close')">
            Cancelar
          </button>
          <button type="submit" class="btn-primary" :disabled="loading">
            {{ loading ? 'Guardando...' : (isEditing ? 'Guardar cambios' : 'Crear tarea') }}
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
// ─────────────────────────────────────────────────────────────────────────────
// TaskForm.vue — ¿por qué existe?
//
// Es el modal de creación y edición. Unificamos ambos casos en un solo
// componente porque comparten la misma estructura de formulario.
//
// v-model   → enlace bidireccional: si el usuario escribe en el input,
//             form.title se actualiza automáticamente. Es azúcar sintáctico
//             sobre :value + @input.
//
// @submit.prevent → intercepta el submit del formulario y evita que el
//                   navegador recargue la página (comportamiento HTML por defecto).
//
// watch(taskToEdit) → observa el prop y precarga el formulario cuando el padre
//                     abre el modal para editar una tarea existente.
//
// La prop `taskToEdit` llega como null cuando es creación nueva,
// o como el objeto Task cuando es edición.
// ─────────────────────────────────────────────────────────────────────────────
import { ref, watch, computed } from 'vue'

const props = defineProps({
  taskToEdit: { type: Object, default: null },
  loading:    { type: Boolean, default: false }
})

const emit = defineEmits(['close', 'save'])

const form = ref({ title: '', description: '' })
const localError = ref(null)

// Si recibimos una tarea para editar, precargamos el formulario
watch(() => props.taskToEdit, (task) => {
  if (task) {
    form.value = { title: task.title, description: task.description ?? '' }
  } else {
    form.value = { title: '', description: '' }
  }
}, { immediate: true })

const isEditing = computed(() => props.taskToEdit !== null)

async function handleSubmit() {
  localError.value = null
  if (!form.value.title.trim()) {
    localError.value = 'El título no puede estar vacío'
    return
  }
  try {
    // Emite el evento 'save' con los datos. El padre (App.vue) decide si
    // llamar a createTask o updateTask según taskToEdit.
    await emit('save', { ...form.value })
  } catch {
    localError.value = 'Ocurrió un error al guardar la tarea'
  }
}
</script>
