package todo.list_service.service.timer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import todo.list_service.domain.ranking.Ranking;
import todo.list_service.domain.timer.Timer;
import todo.list_service.domain.user.User;

import todo.list_service.domain.timer.TimerRepository;

import todo.list_service.service.ranking.RankingService;
import todo.list_service.service.user.UserService;

import todo.list_service.web.dto.timer.TimerListResponseDto;
import todo.list_service.web.dto.timer.TimerResponseDto;
import todo.list_service.web.dto.timer.TimerSaveRequestDto;
import todo.list_service.web.dto.timer.TimerUpdateRequestDto;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TimerService {
    private final TimerRepository timerRepository;
    private final UserService userService;
    private final RankingService rankingService;

    /**
     * 설명 : Timer 목록 조회
     * */
    @Transactional(readOnly = true)
    public List<TimerListResponseDto> findByUserIdAndTakeAllDesc(String date, Long id){

        // 날짜와 user id로 목록 조회
        List<TimerListResponseDto> listDto = timerRepository.findByUserIdAndAllDesc(date, id).stream()
                .map(timer -> new TimerListResponseDto(timer)).collect(Collectors.toList());

        return listDto;
    }

    /**
     * 설명 : 측정한 시간 저장
     * */
    @Transactional
    public Long save(TimerSaveRequestDto requestDto){
        // user 찾아오기
        User user = userService.findById(requestDto.getUserId());

        //Timer 객체 생성
        Timer timer = requestDto.toEntity(user);

        //Ranking 객체 찾아오거나 생성
        Ranking ranking;

        // Ranking 리스트 찾아오기
        List<Ranking> findRankingList = rankingService.findByUserIdAndDate(LocalDate.now(), requestDto.getUserId());

        // 찾아온 Ranking 리스트 비어있는지 판별
        if (findRankingList == null || findRankingList.isEmpty()){

            // 기존에 기간에 해당하는 객체가 없다면 새로 저장
            ranking = rankingService.save(LocalDate.now(), requestDto.getUserId(), requestDto.getDoingTime());

        }else {

            // 기존에 기간에 해당하는 객체가 있으면 업데이트
            ranking = findRankingList.get(0);
            rankingService.updateDoingTime(ranking, requestDto.getDoingTime());

        }

        //Timer에 Ranking 저장
        timer.setRanking(ranking);

        // Timer 저장
        return timerRepository.save(timer).getId();
    }

    /**
     * 설명 : Timer 상세 내용을 수정한다
     * */
    @Transactional
    public Long update(TimerUpdateRequestDto requestDto){

        // Timer 객체 찾아오기
        Timer timer = timerRepository.findById(requestDto.getId())
                .orElseThrow(() -> new IllegalStateException("해당 기록이 존재하지 않습니다."));

        // update
        timer.update(requestDto);

        return timer.getId();
    }

    /**
     * 설명 : Timer 상세 정보 조회
     * */
    @Transactional(readOnly = true)
    public TimerResponseDto findById(Long id){

        // Timer 상세조회를 위한 정보 반환
        Timer timer = timerRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("해당 기록이 존재하지 않습니다."));


        // Entity -> Dto 변환
        return new TimerResponseDto(timer);
    }

    /**
     * 설명 : Timer 삭제
     * */
    @Transactional
    public Long delete(Long id){

        // Timer 객체 찾아오기
        Timer timer = timerRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("해당 기록이 존재하지 않습니다."));

        // Ranking 객체 찾아오기
        Ranking ranking = timer.getRanking();

        // Ranking 객체 삭제여부 판단
        if (ranking.getTimer().size() == 1){

            // 기록이 하나뿐이라면 ranking 객체도 삭제
            timer.setRanking(null);
            timerRepository.delete(timer);
            rankingService.delete(ranking.getId());
        }else {

            // 기록이 더 있다면 doingTime만 수정
            timer.setRanking(null);
            rankingService.updateDoingTimeToDeleteTimer(ranking, timer.getDoingTime());
            timerRepository.delete(timer);
        }

        return id;
    }



}
