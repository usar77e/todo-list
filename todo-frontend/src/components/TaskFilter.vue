<template>
  <!-- Barra de filtros por estado -->
  <div class="filter-bar">
    <button
      v-for="filter in filters"
      :key="filter.value"
      :class="['filter-btn', filter.value === activeFilter ? 'active' : '', filter.cls]"
      @click="$emit('filter-change', filter.value)"
    >
      {{ filter.label }}
      <span class="badge">{{ counts[filter.countKey] }}</span>
    </button>
  </div>
</template>

<script setup>
// ─────────────────────────────────────────────────────────────────────────────
// TaskFilter.vue — ¿por qué existe?
//
// Separa la barra de filtros del resto de la pantalla para que sea fácil
// de modificar (añadir un filtro nuevo, cambiar el estilo) sin tocar
// la lógica principal de la lista.
//
// defineProps  → declara los datos que este componente RECIBE del padre.
//               No los modifica; solo los usa para renderizar.
// defineEmits  → declara los eventos que este componente ENVÍA al padre.
//               El padre escucha @filter-change y reacciona cargando las tareas.
//
// Este patrón (props abajo, eventos arriba) es la forma estándar de
// comunicación padre→hijo en Vue.
// ─────────────────────────────────────────────────────────────────────────────
defineProps({
  activeFilter: { type: String, default: null },
  counts:       { type: Object, required: true }
})

defineEmits(['filter-change'])

const filters = [
  { value: null,          label: 'Todas',       countKey: 'total',      cls: 'all'        },
  { value: 'PENDIENTE',   label: 'Pendientes',  countKey: 'pendiente',  cls: 'pendiente'  },
  { value: 'EN_PROGRESO', label: 'En progreso', countKey: 'enProgreso', cls: 'en-progreso'},
  { value: 'COMPLETADA',  label: 'Completadas', countKey: 'completada', cls: 'completada' },
]
</script>
