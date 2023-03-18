package todo.list_service.web.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import todo.list_service.domain.posts.Posts;

@Getter
@NoArgsConstructor
public class PostsSaveRequestDto {
    private String title;
    private String content;
    private Boolean checked;
    private String author;

    @Builder
    public PostsSaveRequestDto(String title, String content, Boolean checked, String author) {
        this.title = title;
        this.content = content;
        this.checked = checked;
        this.author = author;
    }

    public Posts toEntity() {
        return Posts.builder().title(title).content(content).checked(checked).author(author).build();
    }
}
