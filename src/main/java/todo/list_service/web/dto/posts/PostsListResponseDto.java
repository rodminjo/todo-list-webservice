package todo.list_service.web.dto.posts;

import lombok.Getter;
import lombok.NoArgsConstructor;
import todo.list_service.domain.posts.Posts;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class PostsListResponseDto {
    private Long id;
    private String title;
    private String author;
    private Integer viewCount;
    private Integer replyCount;
    private LocalDateTime createdDate;
    private String storedFileName;


    public PostsListResponseDto(Posts posts) {
        this.id = posts.getId();
        this.title = posts.getTitle();
        this.author = posts.getUser().getNickName();
        this.viewCount = posts.getViewCount();
        this.createdDate = posts.getCreatedDate();
        this.replyCount = posts.getReplyPosts().size();
    }

    public void addStoredFileName(String storedFileName){
        this.storedFileName = storedFileName;
    }
}
