package todo.list_service.web.dto.posts;

import lombok.Getter;
import lombok.NoArgsConstructor;
import todo.list_service.domain.replyposts.ReplyPosts;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class ReplyPostsResponseDto {
    private Long id;
    private Long userId;
    private String nickName;
    private String picture;
    private String content;
    private LocalDateTime createdTime;
    private Integer replyLevel;
    private String replyParent;
    private Long replyGroup;

    public ReplyPostsResponseDto(ReplyPosts replyPosts) {
        this.id = replyPosts.getId();
        this.userId = replyPosts.getUser().getId();
        this.nickName = replyPosts.getUser().getNickName();
        this.picture = replyPosts.getUser().getPicture();
        this.content = replyPosts.getReplyContent();
        this.createdTime = replyPosts.getCreatedDate();
        this.replyLevel = replyPosts.getReplyLevel();
        this.replyParent = replyPosts.getReplyParent();
        this.replyGroup = replyPosts.getReplyGroup();
    }
}
