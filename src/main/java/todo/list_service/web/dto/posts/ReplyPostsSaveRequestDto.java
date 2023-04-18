package todo.list_service.web.dto.posts;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import todo.list_service.domain.replyposts.ReplyPosts;

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

    public ReplyPosts toEntity(){
            return ReplyPosts.builder()
                    .replyContent(this.content)
                    .replyLevel(this.replyLevel)
                    .replyParent(this.replyParent)
                    .replyGroup(this.replyGroup)
                    .build();
    }
}
