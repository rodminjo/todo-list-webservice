package todo.list_service.web.dto.timer;

import lombok.Getter;
import lombok.NoArgsConstructor;
import todo.list_service.domain.timer.Category;
import todo.list_service.domain.timer.Timer;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class TimerListResponseDto {
    private Long id;
    private String category;
    private String title;
    private Integer doingTime;
    private String convertingDoingTime;

    public TimerListResponseDto(Timer timer) {
        this.id = timer.getId();
        this.category = timer.getCategory().toString();
        this.title = timer.getTitle();
        this.doingTime = timer.getDoingTime();
        this.convertingDoingTime = String.format("%02d", (doingTime / 3600)) + ":"
                + String.format("%02d", ((doingTime % 3600) / 60)) + ":"
                + String.format("%02d", ((doingTime % 3600) % 60));
    }
}
