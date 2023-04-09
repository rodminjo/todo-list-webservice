package todo.list_service.domain.timer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface TimerRepository extends JpaRepository<Timer, Long>{

    @Query("SELECT p FROM Timer p WHERE p.todoDate = :date AND p.user.id = :id ORDER BY p.doingTime DESC")
    List<Timer> findByUserIdAndAllDesc(@Param("date") String date, @Param("id") Long id);

}
