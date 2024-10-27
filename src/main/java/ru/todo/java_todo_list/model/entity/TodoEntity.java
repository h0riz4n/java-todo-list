package ru.todo.java_todo_list.model.entity;

import java.time.LocalDateTime;
import java.util.Objects;

import org.hibernate.annotations.Comment;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.proxy.HibernateProxy;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder.Default;
import ru.todo.java_todo_list.model.type.EPriorityType;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "todo")
public class TodoEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("ID записи")
    private Integer id;

    @NotNull
    @Column(name = "title")
    @Comment("Название TODO")
    private String title;

    @Column(name = "description", columnDefinition = "text")
    @Comment("Описание TODO")
    private String description;

    @CreationTimestamp
    @Column(name = "creation_date_time", nullable = false, updatable = false)
    @Comment("Дата и время создания TODO")
    private LocalDateTime creationDateTime;

    @UpdateTimestamp
    @Column(name = "update_date_time", nullable = false)
    @Comment("Дата и время обновления TODO")
    private LocalDateTime updateDateTime;

    @Default
    @NotNull
    @Column(name = "is_completed")
    @Comment("Статус завершения TODO")
    private Boolean isCompleted = false;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "priority")
    @Comment("Приоритет TODO")
    private EPriorityType priority;

    @Override 
    public final boolean equals(Object o) { 
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass(); 
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass(); 
        if (thisEffectiveClass != oEffectiveClass) return false; 
        TodoEntity todo = (TodoEntity) o; 
        return getId() != null && Objects.equals(getId(), todo.getId()); 
    } 
    
    @Override 
    public final int hashCode() { 
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode(); 
    }

}
