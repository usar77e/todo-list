<template>
  <div class="task-list">
    <!-- Estado: cargando -->
    <div v-if="loading" class="state-msg loading">
      <span class="spinner"></span> Cargando tareas...
    </div>

    <!-- Estado: error -->
    <div v-else-if="error" class="state-msg error">
      ⚠ {{ error }}
    </div>

    <!-- Estado: lista vacía -->
    <div v-else-if="tasks.length === 0" class="state-msg empty">
      No hay tareas{{ activeFilter ? ' con este estado' : '' }}. ¡Crea la primera!
    </div>

    <!-- Lista de tarjetas -->
    <TransitionGroup v-else name="task" tag="div" class="cards-grid">
      <TaskCard
        v-for="task in tasks"
        :key="task.id"
        :task="task"
        @edit="$emit('edit', $event)"
        @delete="$emit('delete', $event)"
        @complete="$emit('complete', $event)"
        @status-change="$emit('status-change', $event, arguments[1])"
      />
    </TransitionGroup>
  </div>
</template>

<script setup>
// ─────────────────────────────────────────────────────────────────────────────
// TaskList.vue — ¿por qué existe?
//
// Es el contenedor de la lista. Su responsabilidad es:
// 1. Mostrar los estados de UI (cargando / error / vacío / lista)
// 2. Iterar sobre el array de tareas y renderizar un TaskCard por cada una
// 3. Reenviar los eventos de cada TaskCard hacia arriba (App.vue)
//
// v-if / v-else-if / v-else → renderizado condicional. Vue elimina/añade
//                             los elementos del DOM según la condición.
//
// v-for con :key → Vue necesita un identificador único por elemento para
//                  poder actualizar solo el nodo que cambió (eficiencia).
//                  Siempre usa el id real del dato, nunca el índice del array.
//
// TransitionGroup → componente de Vue que añade animaciones CSS cuando los
//                   elementos entran o salen de la lista.
// ─────────────────────────────────────────────────────────────────────────────
import TaskCard from './TaskCard.vue'

defineProps({
  tasks:        { type: Array,   required: true },
  loading:      { type: Boolean, default: false },
  error:        { type: String,  default: null  },
  activeFilter: { type: String,  default: null  }
})

defineEmits(['edit', 'delete', 'complete', 'status-change'])
</script>
