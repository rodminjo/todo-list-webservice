package todo.list_service.web.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostsUpdateRequestDto {
    private String title;
    private String content;
    private Boolean checked;

    @Builder
    public PostsUpdateRequestDto(String title, String content, Boolean checked) {
        this.title = title;
        this.content = content;
        this.checked = checked;
    }
}
