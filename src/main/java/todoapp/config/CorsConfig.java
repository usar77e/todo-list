package todoapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// ─────────────────────────────────────────────────────────────────────────────
// ¿Por qué existe este archivo?
//
// El navegador bloquea por seguridad las peticiones HTTP que vienen de un
// origen distinto al del servidor (política Same-Origin).
// Vue corre en localhost:5173 y la API en localhost:8080 → orígenes distintos.
// Sin esta configuración, el navegador rechaza TODAS las respuestas de la API.
//
// Esta clase le dice a Spring: "acepta peticiones de localhost:5173".
// ─────────────────────────────────────────────────────────────────────────────
@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")          // aplica solo a rutas /api/...
                        .allowedOrigins("http://localhost:5173") // origen de Vite (Vue)
                        .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE")
                        .allowedHeaders("*");
            }
        };
    }
}
