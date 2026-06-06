<template>
  <!-- Tarjeta individual de tarea -->
  <div :class="['task-card', statusClass]">
    <div class="card-header">
      <span :class="['status-badge', statusClass]">{{ statusLabel }}</span>
      <div class="card-actions">
        <!-- Botón completar: solo visible si la tarea no está ya completada -->
        <button
          v-if="task.status !== 'COMPLETADA'"
          class="btn-icon btn-complete"
          title="Marcar como completada"
          @click="$emit('complete', task.id)"
        >✓</button>

        <button
          class="btn-icon btn-edit"
          title="Editar"
          @click="$emit('edit', task)"
        >✎</button>

        <button
          class="btn-icon btn-delete"
          title="Eliminar"
          @click="$emit('delete', task.id)"
        >✕</button>
      </div>
    </div>

    <h3 class="card-title">{{ task.title }}</h3>
    <p v-if="task.description" class="card-desc">{{ task.description }}</p>

    <!-- Selector de estado rápido -->
    <select
      class="status-select"
      :value="task.status"
      @change="$emit('status-change', task.id, $event.target.value)"
    >
      <option value="PENDIENTE">Pendiente</option>
      <option value="EN_PROGRESO">En progreso</option>
      <option value="COMPLETADA">Completada</option>
    </select>

    <p class="card-date">Creada: {{ formatDate(task.createdAt) }}</p>
  </div>
</template>

<script setup>
// ─────────────────────────────────────────────────────────────────────────────
// TaskCard.vue — ¿por qué existe?
//
// Renderiza UNA tarea. Separarlo en su propio componente permite:
// - Reutilizarlo en distintas vistas (lista, búsqueda, etc.)
// - Que TaskList.vue sea solo una iteración de tarjetas, sin HTML repetido
//
// computed → valor derivado del prop `task`. Se recalcula solo si task cambia.
//            Úsalo cuando el valor depende de datos reactivos y es costoso
//            de calcular, o simplemente para que el template sea más legible.
//
// Este componente NO sabe nada de la API ni del estado global.
// Solo muestra datos y emite eventos. La lógica real la maneja el padre.
// ─────────────────────────────────────────────────────────────────────────────
import { computed } from 'vue'

const props = defineProps({
  task: { type: Object, required: true }
})

defineEmits(['edit', 'delete', 'complete', 'status-change'])

const STATUS_MAP = {
  PENDIENTE:   { label: 'Pendiente',   cls: 'pendiente'  },
  EN_PROGRESO: { label: 'En progreso', cls: 'en-progreso'},
  COMPLETADA:  { label: 'Completada',  cls: 'completada' },
}

const statusLabel = computed(() => STATUS_MAP[props.task.status]?.label ?? props.task.status)
const statusClass = computed(() => STATUS_MAP[props.task.status]?.cls  ?? '')

function formatDate(dateStr) {
  if (!dateStr) return ''
  return new Date(dateStr).toLocaleDateString('es-ES', {
    day: '2-digit', month: 'short', year: 'numeric'
  })
}
</script>
