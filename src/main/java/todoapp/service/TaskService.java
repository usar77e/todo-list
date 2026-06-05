package todoapp.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import todoapp.dto.TaskRequestDTO;
import todoapp.dto.TaskResponseDTO;
import todoapp.exception.TaskNotFoundException;
import todoapp.model.Task;
import todoapp.model.TaskStatus;
import todoapp.repository.TaskRepository;

// @Service marca esta clase como componente de lógica de negocio
// @RequiredArgsConstructor (Lombok) genera el constructor con los campos "final"
// Spring usa ese constructor para inyectar TaskRepository automáticamente
@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    // ─── Listar todas las tareas ───────────────────────────────────────────────

    public List<TaskResponseDTO> getAllTasks() {
        return taskRepository.findAll()
                .stream()
                .map(this::toResponseDTO) // convierte cada Task en TaskResponseDTO
                .collect(Collectors.toList());
    }

    public List<TaskResponseDTO> getTasksByStatus(TaskStatus status) {
        return taskRepository.findByStatus(status)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    // ─── Obtener una tarea por ID ──────────────────────────────────────────────

    public TaskResponseDTO getTaskById(Long id) {
        Task task = findTaskOrThrow(id);
        return toResponseDTO(task);
    }

    // ─── Crear tarea ───────────────────────────────────────────────────────────

    public TaskResponseDTO createTask(TaskRequestDTO request) {
        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(TaskStatus.PENDIENTE); // toda tarea nueva empieza en PENDIENTE

        Task saved = taskRepository.save(task);
        return toResponseDTO(saved);
    }

    // ─── Actualizar título y descripción ──────────────────────────────────────

    public TaskResponseDTO updateTask(Long id, TaskRequestDTO request) {
        Task task = findTaskOrThrow(id);
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());

        Task saved = taskRepository.save(task);
        return toResponseDTO(saved);
    }

    // ─── Cambiar estado ────────────────────────────────────────────────────────

    public TaskResponseDTO changeStatus(Long id, TaskStatus newStatus) {
        Task task = findTaskOrThrow(id);
        task.setStatus(newStatus);

        Task saved = taskRepository.save(task);
        return toResponseDTO(saved);
    }

    // ─── Marcar como completada (atajo conveniente) ────────────────────────────

    public TaskResponseDTO completeTask(Long id) {
        return changeStatus(id, TaskStatus.COMPLETADA);
    }

    // ─── Eliminar ──────────────────────────────────────────────────────────────

    public void deleteTask(Long id) {
        findTaskOrThrow(id); // si no existe lanza excepción antes de intentar borrar
        taskRepository.deleteById(id);
    }

    // ─── Helpers privados ──────────────────────────────────────────────────────

    // Busca la tarea o lanza TaskNotFoundException si no existe
    private Task findTaskOrThrow(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }

    // Convierte la entidad Task al DTO de respuesta
    private TaskResponseDTO toResponseDTO(Task task) {
        return new TaskResponseDTO(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getCreatedAt(),
                task.getUpdatedAt()
        );
    }
}
