package todo.list_service.web.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import todo.list_service.domain.posts.Posts;

import java.time.LocalDateTime;

@Getter
public class PostsListResponseDto {
    private Long id;
    private String title;
    private String author;

    private LocalDateTime modifiedDate;
    private Boolean checked;

    public PostsListResponseDto(Posts posts) {
        this.id = posts.getId();
        this.title = posts.getTitle();
        this.author = posts.getAuthor();
        this.modifiedDate = posts.getModifiedDate();
        this.checked = posts.getChecked();
    }
}
