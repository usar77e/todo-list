package todoapp.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import todoapp.dto.TaskRequestDTO;
import todoapp.dto.TaskResponseDTO;
import todoapp.model.TaskStatus;
import todoapp.service.TaskService;

// @RestController = @Controller + @ResponseBody (devuelve JSON automáticamente)
// @RequestMapping define el prefijo de ruta para todos los endpoints de esta clase
@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    // GET /api/tasks          → todas las tareas
    // GET /api/tasks?status=PENDIENTE → filtrar por estado
    @GetMapping
    public ResponseEntity<List<TaskResponseDTO>> getAllTasks(
            @RequestParam(required = false) TaskStatus status) {

        List<TaskResponseDTO> tasks = (status != null)
                ? taskService.getTasksByStatus(status)
                : taskService.getAllTasks();

        return ResponseEntity.ok(tasks);
    }

    // GET /api/tasks/1  → tarea con id=1
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> getTaskById(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    // POST /api/tasks  → crear nueva tarea
    // @Valid activa las validaciones declaradas en TaskRequestDTO (@NotBlank, @Size)
    @PostMapping
    public ResponseEntity<TaskResponseDTO> createTask(
            @Valid @RequestBody TaskRequestDTO request) {

        TaskResponseDTO created = taskService.createTask(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created); // HTTP 201
    }

    // PUT /api/tasks/1  → actualizar título y descripción
    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> updateTask(
            @PathVariable Long id,
            @Valid @RequestBody TaskRequestDTO request) {

        return ResponseEntity.ok(taskService.updateTask(id, request));
    }

    // PATCH /api/tasks/1/status?value=EN_PROGRESO  → cambiar solo el estado
    @PatchMapping("/{id}/status")
    public ResponseEntity<TaskResponseDTO> changeStatus(
            @PathVariable Long id,
            @RequestParam TaskStatus value) {

        return ResponseEntity.ok(taskService.changeStatus(id, value));
    }

    // PATCH /api/tasks/1/complete  → marcar como completada (atajo)
    @PatchMapping("/{id}/complete")
    public ResponseEntity<TaskResponseDTO> completeTask(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.completeTask(id));
    }

    // DELETE /api/tasks/1  → eliminar tarea
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build(); // HTTP 204
    }
}
