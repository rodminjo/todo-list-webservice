package todo.list_service.domain.todo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, Long> {

    @Query("SELECT p FROM Todo p WHERE p.todoDate = :date AND p.user.id = :id ORDER BY p.importance DESC")
    List<Todo> findByUserIdAndAllDesc(@Param("date") String date,@Param("id") Long id);
}
