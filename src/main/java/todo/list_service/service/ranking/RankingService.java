package todo.list_service.service.ranking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import todo.list_service.domain.ranking.Ranking;
import todo.list_service.domain.ranking.RankingRepository;
import todo.list_service.domain.user.User;
import todo.list_service.service.user.UserService;
import todo.list_service.web.dto.ranking.RankingResponseDto;
import todo.list_service.web.dto.ranking.RankingListResponseDto;
import todo.list_service.web.dto.ranking.ReplyRankingSaveRequestDto;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class RankingService {
    private final RankingRepository rankingRepository;
    private final ReplyRankingService replyRankingService;
    private final UserService userService;



    @Transactional(readOnly = true)
    public List<Ranking> findByUserIdAndDate(LocalDate date, Long userId) {

        return rankingRepository.findByUserIdAndDate(date, userId);
    }

    @Transactional
    public Ranking save(LocalDate date, Long userId, Integer doingTime) {
        User user = userService.findById(userId);

        // 주차 구하기(월요일 ~ 일요일)
        // 주에 년을 넘어가는 날이 존재하면 바로 1로 바뀜.그래서 53주차는 1주로 바꿔주기. 즉 한주에 다음년도날짜가 속해있으면 다음년도로 판단
        int weekOfYear = date.get(WeekFields.ISO.weekOfYear()) <= 52 ? date.get(WeekFields.ISO.weekOfYear()) : 1;
        LocalDate startDate = date.minusDays(date.get(WeekFields.ISO.dayOfWeek()) - 1);
        LocalDate endDate = date.plusDays(7 - date.get(WeekFields.ISO.dayOfWeek()));

        String week = endDate.getYear() + "-" + weekOfYear;

        return rankingRepository.save(Ranking.builder()
                .user(user)
                .week(week)
                .startDate(startDate)
                .endDate(endDate)
                .totalDoingTime(doingTime).build());
    }

    @Transactional
    public void updateDoingTime(Ranking ranking, Integer doingTime) {
        ranking.updateDoingTime(doingTime);

    }

    @Transactional
    public void delete(Long rankingId) {
        Ranking ranking = rankingRepository.findById(rankingId)
                .orElseThrow(() -> new IllegalStateException("해당 순위정보를 찾을 수 없습니다. id : " + rankingId));

        replyRankingService.deleteAll(rankingId);
        rankingRepository.delete(ranking);
    }

    @Transactional
    public void updateDoingTimeToDeleteTimer(Ranking ranking, Integer doingTime) {
        ranking.updateTotalDoingTimeToDeleteTimer(doingTime);

    }

    @Transactional(readOnly = true)
    public List<RankingListResponseDto> findByWeek(String week) {
        List<RankingListResponseDto> responseDtoList = rankingRepository.findByWeek(week).stream()
                .map(ranking -> new RankingListResponseDto(ranking)).collect(Collectors.toList());

        return responseDtoList;
    }

    @Transactional(readOnly = true)
    public RankingResponseDto findById(Long id) {
        Ranking ranking = rankingRepository.findById(id).orElseThrow(() -> new IllegalStateException("해당 순위정보를 찾을 수 없습니다."));

        return new RankingResponseDto(ranking);
    }

    public Long replySave(ReplyRankingSaveRequestDto requestDto, Long id, Long userId) {
        User user = userService.findById(userId);

        Ranking ranking = rankingRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("해당 순위정보를 찾을 수 없습니다."));

        return replyRankingService.save(requestDto, user, ranking);
    }

    public Long replyUpdate(ReplyRankingSaveRequestDto requestDto, Long id, Long replyId, Long userId) {
        // 작성자 확인
        User user = userService.findById(userId);
        Ranking ranking = rankingRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("해당 순위정보를 찾을 수 없습니다."));
        return replyRankingService.update(requestDto, replyId, user);
    }

    public Long replyDelete(Long id, Long replyId, Long userId) {
        Ranking ranking = rankingRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("해당 순위정보를 찾을 수 없습니다."));
        return replyRankingService.delete(replyId, ranking);
    }


    public List<RankingListResponseDto> findByWeekAndNickName(String week, String search) {

        List<RankingListResponseDto> responseDtoList = rankingRepository.findByWeekAndNickName(week,search).stream()
                .map(ranking -> new RankingListResponseDto(ranking)).collect(Collectors.toList());

        return responseDtoList;
    }
}
