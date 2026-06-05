package todoapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import todoapp.model.Task;
import todoapp.model.TaskStatus;

// Spring Data JPA genera automáticamente la implementación de esta interfaz
// JpaRepository<Task, Long> → entidad Task con clave primaria de tipo Long
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    // Spring traduce el nombre del método a SQL: SELECT * FROM tasks WHERE status = ?
    List<Task> findByStatus(TaskStatus status);

    // SELECT * FROM tasks WHERE title LIKE %keyword%  (case insensitive)
    List<Task> findByTitleContainingIgnoreCase(String keyword);
}
