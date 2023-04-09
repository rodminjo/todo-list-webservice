package todo.list_service.web.dto.timer;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Builder
    public TimerResponseDto(String title, String content, String category, LocalDateTime startTime, Integer doingTime) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.startTime = startTime;
        this.doingTime = doingTime;
    }

    public void setId(Long id) {
        this.id = id;
    }




}
