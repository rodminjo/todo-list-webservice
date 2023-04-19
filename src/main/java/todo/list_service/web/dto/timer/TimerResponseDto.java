package todo.list_service.web.dto.timer;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import todo.list_service.domain.timer.Timer;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class TimerResponseDto {

    private Long id;
    private String title;
    private String content;
    private String category;
    private LocalDateTime startTime;
    private Integer doingTime;


    public TimerResponseDto(Timer timer) {
        this.id = timer.getId();
        this.title = timer.getTitle();
        this.content = timer.getContent();
        this.category = timer.getCategory().toString();
        this.startTime = timer.getStartTime();
        this.doingTime = timer.getDoingTime();

    }



}
