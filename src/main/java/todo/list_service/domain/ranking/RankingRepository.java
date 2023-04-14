package todo.list_service.domain.ranking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface RankingRepository extends JpaRepository<Ranking,Long> {

    @Query("SELECT p FROM Ranking p WHERE p.user.id = :id AND p.startDate <= :date AND p.endDate >= :date")
    List<Ranking> findByUserIdAndDate(@Param("date")LocalDate date, @Param("id") Long id);

    @Query("SELECT p FROM Ranking p WHERE p.week = :week ORDER BY p.totalDoingTime DESC ")
    List<Ranking> findByWeek(@Param("week")String week);

    @Query("SELECT p FROM Ranking p WHERE p.week = :week AND p.user.nickName LIKE %:nickName% ORDER BY p.totalDoingTime DESC")
    List<Ranking> findByWeekAndNickName(@Param("week")String week, @Param("nickName")String nickName);
}
