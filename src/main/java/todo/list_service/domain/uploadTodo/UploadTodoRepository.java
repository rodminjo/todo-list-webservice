package todo.list_service.domain.uploadTodo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UploadTodoRepository extends JpaRepository<UploadTodo,Long> {
    @Query("SELECT p FROM UploadTodo p WHERE p.todo.id = :id")
    List<UploadTodo> findAllByTodoId(@Param("id") Long id);

}
