package todoapp.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import todoapp.model.TaskStatus;

// DTO de salida: los datos que la API devuelve al cliente
// Controlamos exactamente qué información exponemos (no exponer internos de la BD)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponseDTO {

    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
