package todo.list_service.web.dto.posts;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReplyPostsSaveRequestDto {
    private String content;
    private Integer replyLevel;
    private Long replyGroup;
    private String replyParent;

    @Builder
    public ReplyPostsSaveRequestDto(String content, Integer replyLevel, Long replyGroup, String replyParent) {
        this.content = content;
        this.replyLevel = replyLevel;
        this.replyGroup = replyGroup;
        this.replyParent = replyParent;
    }
}
