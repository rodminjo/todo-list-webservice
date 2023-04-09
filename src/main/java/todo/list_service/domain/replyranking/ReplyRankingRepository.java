package todo.list_service.domain.replyranking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReplyRankingRepository extends JpaRepository<ReplyRanking, Long> {

    @Query("SELECT p FROM ReplyRanking p WHERE p.ranking.id = :id")
    List<ReplyRanking> findAllByRankingId(@Param("id") Long id);

}
