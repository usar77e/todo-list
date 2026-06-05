package todoapp.exception;

// Excepción personalizada para cuando no existe la tarea solicitada
// Al extender RuntimeException no obliga a declarar "throws" en los métodos
public class TaskNotFoundException extends RuntimeException {

    public TaskNotFoundException(Long id) {
        super("Tarea no encontrada con id: " + id);
    }
}
