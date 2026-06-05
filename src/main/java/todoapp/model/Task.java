package todoapp.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Representa una fila en la tabla "tasks" de la base de datos
@Entity
@Table(name = "tasks")
@Data               // Lombok: genera getters, setters, equals, hashCode y toString
@NoArgsConstructor  // Lombok: constructor vacío requerido por JPA
@AllArgsConstructor // Lombok: constructor con todos los campos
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-incremento en la BD
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(length = 500)
    private String description;

    @Enumerated(EnumType.STRING) // Guarda el nombre del enum como texto, no número
    @Column(nullable = false)
    private TaskStatus status;

    @CreationTimestamp // Hibernate asigna automáticamente la fecha de creación
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp // Hibernate actualiza automáticamente en cada cambio
    private LocalDateTime updatedAt;
}
