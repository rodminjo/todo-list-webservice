package todo.list_service.domain.ranking;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import todo.list_service.domain.user.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class RankingTest {


    @Test
    void 총시간_업데이트(){

        Ranking ranking = new Ranking(new User(), "2023-01-01", LocalDate.now(), LocalDate.now(), 100);

        ranking.updateDoingTime(10);

        Assertions.assertThat(ranking.getTotalDoingTime()).isEqualTo(100+10);
    }

    @Test
    void 총시간_빼기_업데이트(){

        Ranking ranking = new Ranking(new User(), "2023-01-01", LocalDate.now(), LocalDate.now(), 100);

        ranking.updateTotalDoingTimeToDeleteTimer(10);

        Assertions.assertThat(ranking.getTotalDoingTime()).isEqualTo(100-10);
    }
}