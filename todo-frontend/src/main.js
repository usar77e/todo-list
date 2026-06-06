import { createApp } from 'vue'
import App from './App.vue'
import './styles/main.css'

// ─────────────────────────────────────────────────────────────────────────────
// ¿Qué hace main.js?
//
// Es el punto de entrada de la aplicación Vue. Solo hace una cosa:
// toma el componente raíz (App.vue) y lo "monta" dentro del div#app
// que está en index.html. A partir de ahí Vue toma el control del DOM.
//
// createApp(App)  → crea la instancia de la aplicación
// .mount('#app')  → la conecta al elemento HTML con id="app"
// ─────────────────────────────────────────────────────────────────────────────
createApp(App).mount('#app')
