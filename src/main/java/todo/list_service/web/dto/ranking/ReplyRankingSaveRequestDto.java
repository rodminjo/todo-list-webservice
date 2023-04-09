package todo.list_service.web.dto.ranking;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReplyRankingSaveRequestDto {

    private String content;

    public ReplyRankingSaveRequestDto(String content) {
        this.content = content;
    }
}
