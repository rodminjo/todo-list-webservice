package todo.list_service.service.timer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import todo.list_service.domain.ranking.Ranking;
import todo.list_service.domain.timer.Category;
import todo.list_service.domain.timer.Timer;
import todo.list_service.domain.timer.TimerRepository;
import todo.list_service.domain.user.User;
import todo.list_service.service.ranking.RankingService;
import todo.list_service.service.user.UserService;
import todo.list_service.web.dto.timer.TimerListResponseDto;
import todo.list_service.web.dto.timer.TimerResponseDto;
import todo.list_service.web.dto.timer.TimerSaveRequestDto;
import todo.list_service.web.dto.timer.TimerUpdateRequestDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TimerService {
    private final TimerRepository timerRepository;
    private final UserService userService;
    private final RankingService rankingService;

    @Transactional(readOnly = true)
    public List<TimerListResponseDto> findByUserIdAndTakeAllDesc(String date, Long id){
        List<TimerListResponseDto> listDto = timerRepository.findByUserIdAndAllDesc(date, id).stream()
                .map(timer -> new TimerListResponseDto(timer)).collect(Collectors.toList());

        return listDto;
    }


    @Transactional
    public Long save(TimerSaveRequestDto requestDto){
        // user 찾아오기
        User user = userService.findById(requestDto.getUserId());

        //category 처리
        Category category = Category.valueOf(requestDto.getCategory().toUpperCase());

        //Timer 객체 생성
        Timer timer = Timer.builder().user(user).title(requestDto.getTitle()).content(requestDto.getContent())
                .todoDate(LocalDate.now().toString()).category(category)
                .startTime(requestDto.getStartTime()).doingTime(requestDto.getDoingTime()).build();

        //Ranking 객체 찾아오거나 생성
        Ranking ranking;
        List<Ranking> findRankingList = rankingService.findByUserIdAndDate(LocalDate.now(), requestDto.getUserId());
        if (findRankingList == null || findRankingList.isEmpty()){
            // 기존에 기간에 해당하는 객체가 없다면 새로 저장
            ranking = rankingService.save(LocalDate.now(), requestDto.getUserId(), requestDto.getDoingTime());
        }else {
            // 기존에 기간에 해당하는 객체가 있으면 업데이트
            ranking = findRankingList.get(0);
            rankingService.updateDoingTime(ranking, requestDto.getDoingTime());
        }

        timer.setRanking(ranking);

        return timerRepository.save(timer).getId();
    }
    @Transactional
    public Long update(TimerUpdateRequestDto requestDto){
        Timer timer = timerRepository.findById(requestDto.getId())
                .orElseThrow(() -> new IllegalStateException("해당 기록이 존재하지 않습니다."));

        //category string to Category 처리
        Category category = Category.valueOf(requestDto.getCategory().toUpperCase());

        // update
        timer.update(requestDto,category);

        return timer.getId();
    }
    @Transactional(readOnly = true)
    public TimerResponseDto findById(Long id){
        Timer timer = timerRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("해당 기록이 존재하지 않습니다."));

        // category enum to String 변환
        String category = timer.getCategory().toString();

        TimerResponseDto responseDto = TimerResponseDto.builder().title(timer.getTitle()).content(timer.getContent())
                .category(category).startTime(timer.getStartTime()).doingTime(timer.getDoingTime()).build();


        return responseDto;
    }

    @Transactional
    public Long delete(Long id){
        Timer timer = timerRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("해당 기록이 존재하지 않습니다."));

        Ranking ranking = timer.getRanking();
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
