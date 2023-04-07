package todo.list_service.web.dto.posts;

import lombok.Getter;
import lombok.NoArgsConstructor;
import todo.list_service.domain.posts.Posts;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class PostsResponseDto {
    private Long id;
    private Long userId;
    private String nickName;
    private String picture;
    private String title;
    private String content;
    private Integer viewCount;
    private Integer replyCount;
    private LocalDateTime createdDate;
    private List<String> storedFileNames;
    private List<ReplyPostsResponseDto> replyDtos;

    public PostsResponseDto(Posts post) {
        this.id = post.getId();
        this.userId = post.getUser().getId();
        this.picture = post.getUser().getPicture();
        this.nickName = post.getUser().getNickName();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.viewCount = post.getViewCount();
        this.replyCount = post.getReplyPosts().size();
        this.createdDate = post.getCreatedDate();

    }

    public void addStoredFileNames(List<String> storedFileNames) {
        this.storedFileNames = storedFileNames;
    }

    public void addReplyDto(List<ReplyPostsResponseDto> replyDtos) {
        this.replyDtos = replyDtos;
    }
}
