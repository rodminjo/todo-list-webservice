package todo.list_service.web.dto.timer;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TimerUpdateRequestDto {

    private Long id;
    private String title;
    private String content;
    private String category;

    @Builder
    public TimerUpdateRequestDto(String title, String content, String category) {
        this.title = title;
        this.content = content;
        this.category = category;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
