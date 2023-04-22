package todo.list_service.domain.ranking;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import todo.list_service.domain.user.Role;
import todo.list_service.domain.user.User;
import todo.list_service.domain.user.UserRepository;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class RankingRepositoryTest {

    @Autowired
    RankingRepository rankingRepository;

    @Autowired
    UserRepository userRepository;

    @Test
    void 유저아이디와_현재날짜로_가져온다() {
        User user = User.builder().name("testname").picture("test").email("testemail").role(Role.USER).build();

        User saveUser = userRepository.save(user);

        rankingRepository.save(Ranking.builder().user(saveUser).week("2023-14").startDate(LocalDate.now()).endDate(LocalDate.now()).totalDoingTime(100).build());

        List<Ranking> byUserIdAndDate = rankingRepository.findByUserIdAndDate(LocalDate.now(), saveUser.getId());

        Assertions.assertThat(byUserIdAndDate.get(byUserIdAndDate.size() - 1).getUser().getId()).isEqualTo(saveUser.getId());
    }

    @Test
    void 주차별로_가져온다() {
        User user = User.builder().name("testname").picture("test").email("testemail").role(Role.USER).build();

        User saveUser = userRepository.save(user);

        rankingRepository.save(Ranking.builder().user(saveUser).week("2023-14").startDate(LocalDate.now()).endDate(LocalDate.now()).totalDoingTime(100).build());

        List<Ranking> byWeek = rankingRepository.findByWeek("2023-14");

        Assertions.assertThat(byWeek.get(byWeek.size()-1).getWeek()).isEqualTo("2023-14");
    }

    @Test
    void 이름으로_검색해서_가져온다() {

        User user = User.builder().name("testname").picture("test").email("testemail").role(Role.USER).build();
        user.nickNameSetting("testNickname");

        User saveUser = userRepository.save(user);

        rankingRepository.save(Ranking.builder().user(saveUser).week("2023-14").startDate(LocalDate.now()).endDate(LocalDate.now()).totalDoingTime(100).build());

        List<Ranking> byWeek = rankingRepository.findByWeekAndNickName("2023-14", saveUser.getNickName());
        

        Assertions.assertThat(byWeek.get(byWeek.size()-1).getWeek()).isEqualTo("2023-14");
        Assertions.assertThat(byWeek.get(byWeek.size()-1).getUser().getNickName()).isEqualTo(saveUser.getNickName());
        

    }
}