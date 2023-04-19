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


    /**
     * 설명 : userId와 현재 날짜를 이용하여 랭킹 목록을 불러옴
     * */
    @Transactional(readOnly = true)
    public List<Ranking> findByUserIdAndDate(LocalDate date, Long userId) {

        return rankingRepository.findByUserIdAndDate(date, userId);
    }

    /**
     * 설명 : 주차를 구하고 해당 데이터를 저장함
     * */
    @Transactional
    public Ranking save(LocalDate date, Long userId, Integer doingTime) {
        User user = userService.findById(userId);

        // 주차 구하기(월요일 ~ 일요일)
        // 주에 년을 넘어가는 날이 존재하면 바로 1로 바뀜.그래서 53주차는 1주로 바꿔주기. 즉 한주에 다음년도날짜가 속해있으면 다음년도로 판단
        int weekOfYear = date.get(WeekFields.ISO.weekOfYear()) <= 52 ? date.get(WeekFields.ISO.weekOfYear()) : 1;

        // 시작 일 구하기
        LocalDate startDate = date.minusDays(date.get(WeekFields.ISO.dayOfWeek()) - 1);

        // 종료일 구하기
        LocalDate endDate = date.plusDays(7 - date.get(WeekFields.ISO.dayOfWeek()));

        // 주차
        String week = endDate.getYear() + "-" + weekOfYear;

        return rankingRepository.save(Ranking.builder()
                .user(user)
                .week(week)
                .startDate(startDate)
                .endDate(endDate)
                .totalDoingTime(doingTime).build());
    }


    /**
     * 설명 : doingTime을 업데이트함
     * */
    @Transactional
    public void updateDoingTime(Ranking ranking, Integer doingTime) {

        // 추가 DoingTime 저장
        ranking.updateDoingTime(doingTime);

    }

    /**
     * 설명 : 기록이 하나뿐인 Timer 가 삭제된다면 랭킹 전부 삭제
     * */

    @Transactional
    public void delete(Long rankingId) {

        // Ranking 객체 찾아오기
        Ranking ranking = rankingRepository.findById(rankingId)
                .orElseThrow(() -> new IllegalStateException("해당 순위정보를 찾을 수 없습니다. id : " + rankingId));

        // 해당하는 랭킹 댓글 삭제
        replyRankingService.deleteAll(rankingId);

        // 해당하는 랭킹 삭제
        rankingRepository.delete(ranking);
    }

    /**
     * 설명 : 두개 이상 Timer가 존재할 때 Timer가 삭제되면 시간도 차감
     * */
    @Transactional
    public void updateDoingTimeToDeleteTimer(Ranking ranking, Integer doingTime) {

        // DoingTime 빼기
        ranking.updateTotalDoingTimeToDeleteTimer(doingTime);

    }


    /**
     * 설명 : Ranking 인덱스페이지 목록 조회
     * */
    @Transactional(readOnly = true)
    public List<RankingListResponseDto> findByWeek(String week) {

        // 필요한 데이터 매핑 후 반환
        List<RankingListResponseDto> responseDtoList = rankingRepository.findByWeek(week).stream()
                .map(ranking -> new RankingListResponseDto(ranking)).collect(Collectors.toList());

        return responseDtoList;
    }

    /**
     * 설명 : Ranking 상세조회 화면 정보 조회
     * */
    @Transactional(readOnly = true)
    public RankingResponseDto findById(Long id) {

        // Ranking 객체 찾아오기
        Ranking ranking = rankingRepository.findById(id).orElseThrow(() -> new IllegalStateException("해당 순위정보를 찾을 수 없습니다."));

        return new RankingResponseDto(ranking);
    }

    /**
     * 설명 : Ranking 댓글 저장
     * */
    public Long replySave(ReplyRankingSaveRequestDto requestDto, Long id, Long userId) {

        // User 객체 찾아오기
        User user = userService.findById(userId);

        // Ranking 객체 찾아오기
        Ranking ranking = rankingRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("해당 순위정보를 찾을 수 없습니다."));

        // 댓글 Service에 넘겨주기
        return replyRankingService.save(requestDto, user, ranking);
    }

    /**
     * 설명 : Ranking 댓글 수정
     * */
    public Long replyUpdate(ReplyRankingSaveRequestDto requestDto, Long id, Long replyId, Long userId) {
        // User 객체 찾아오기
        User user = userService.findById(userId);

        // 댓글 Service에 넘겨주기
        return replyRankingService.update(requestDto, replyId, user);
    }


    /**
     * 설명 : Ranking 댓글 삭제
     * */
    public Long replyDelete(Long id, Long replyId, Long userId) {
        // User 객체 찾아오기
        User user = userService.findById(userId);

        // Ranking 객체 찾아오기
        Ranking ranking = rankingRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("해당 순위정보를 찾을 수 없습니다."));

        // 댓글 Service에 넘겨주기
        return replyRankingService.delete(replyId, ranking);
    }

    /**
     * 설명 : Ranking페이지 닉네임 검색 결과 반환
     * */
    public List<RankingListResponseDto> findByWeekAndNickName(String week, String search) {

        // 주차, 검색어로 조회후 결과 반환
        List<RankingListResponseDto> responseDtoList = rankingRepository.findByWeekAndNickName(week,search).stream()
                .map(ranking -> new RankingListResponseDto(ranking)).collect(Collectors.toList());

        return responseDtoList;
    }
}
