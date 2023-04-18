package todo.list_service.web.dto.posts;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import todo.list_service.domain.posts.Posts;

@Getter
@ToString
@NoArgsConstructor
public class PostsSaveRequestDto {
    private String title;
    private String content;
    private Long userId;


    @Builder
    public PostsSaveRequestDto(String title, String content, Long userId) {
        this.title = title;
        this.content = content;
        this.userId = userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Posts toEntity(){
        return Posts.builder()
                .title(this.title)
                .content(this.content)
                .build();
    }
}
