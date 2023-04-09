package todo.list_service.web.dto.timer;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class TimerSaveRequestDto {
    private Long userId;
    private String title;
    private String content;
    //enum 형태는 바로 받을 수 없음. sevice에서 처리
    private String category;
    private String todoDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime startTime;
    // 받아올때 초단위로 받아오기 때문
    private Integer doingTime;

    @Builder
    public TimerSaveRequestDto(String title, String content, String category, String todoDate, LocalDateTime startTime, Integer doingTime) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.todoDate = todoDate;
        this.startTime = startTime;
        this.doingTime = doingTime;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setTodoDate(String todoDate) {
        this.todoDate = todoDate;
    }


}
