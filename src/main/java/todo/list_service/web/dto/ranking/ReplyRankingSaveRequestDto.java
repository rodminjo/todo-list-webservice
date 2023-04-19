package todo.list_service.web.dto.ranking;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import todo.list_service.domain.ranking.Ranking;
import todo.list_service.domain.replyranking.ReplyRanking;
import todo.list_service.domain.user.User;

@Getter
@NoArgsConstructor
public class ReplyRankingSaveRequestDto {

    private String content;

    @Builder
    public ReplyRankingSaveRequestDto(String content) {
        this.content = content;
    }

    public ReplyRanking toEntity(User user){

        return ReplyRanking.builder()
                .user(user)
                .replyContent(this.content).build();
    }
}
