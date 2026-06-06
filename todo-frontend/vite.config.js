import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// ─────────────────────────────────────────────────────────────────────────────
// ¿Por qué existe vite.config.js?
//
// Vite es la herramienta que compila y sirve el frontend en desarrollo.
// Este archivo configura dos cosas clave:
//
// 1. plugin-vue: le enseña a Vite a entender los archivos .vue
//    (un formato especial que mezcla HTML, JS y CSS en un solo archivo).
//
// 2. server.proxy: durante desarrollo, redirige las llamadas a /api/*
//    al backend Spring (puerto 8080). Esto evita problemas de CORS en dev
//    y hace que el frontend no necesite saber la URL exacta del backend.
//    En producción esta redirección no existe; el servidor real la gestiona.
// ─────────────────────────────────────────────────────────────────────────────
export default defineConfig({
  plugins: [vue()],
  server: {
    port: 5173,
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true
      }
    }
  }
})
