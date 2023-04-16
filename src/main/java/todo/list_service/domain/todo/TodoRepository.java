package todo.list_service.domain.todo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, Long> {

    /**
     *  설명 : 회원별 할일 목록을 중요도에 따라 내림차순 정렬하여 반환
     * */
    @Query("SELECT p FROM Todo p WHERE p.todoDate = :date AND p.user.id = :id ORDER BY p.importance DESC")
    List<Todo> findByUserIdAndAllDesc(@Param("date") String date,@Param("id") Long id);
}
