package todoapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

// DTO de entrada: los datos que el cliente envía en el body del request
// Separar este DTO de la entidad evita exponer campos internos (id, fechas)
@Data
public class TaskRequestDTO {

    @NotBlank(message = "El título no puede estar vacío")
    @Size(max = 100, message = "El título no puede superar los 100 caracteres")
    private String title;

    @Size(max = 500, message = "La descripción no puede superar los 500 caracteres")
    private String description;
}
