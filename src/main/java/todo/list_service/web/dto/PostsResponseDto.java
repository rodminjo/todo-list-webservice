package todo.list_service.web.dto;

import lombok.Getter;
import todo.list_service.domain.posts.Posts;

@Getter
public class PostsResponseDto {
    private Long id;
    private String title;
    private String author;
    private String content;
    private Boolean checked;

    public PostsResponseDto(Posts post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.author = post.getAuthor();
        this.content = post.getContent();
        this.checked = post.getChecked();
    }
}
