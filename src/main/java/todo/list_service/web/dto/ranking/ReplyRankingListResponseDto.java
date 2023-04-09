package todo.list_service.web.dto.ranking;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import todo.list_service.domain.replyranking.ReplyRanking;

@Getter
@NoArgsConstructor
public class ReplyRankingListResponseDto {
    private Long id;
    private Long userId;
    private String nickName;
    private String picture;
    private String replyContent;

    @Builder
    public ReplyRankingListResponseDto(ReplyRanking replyRanking) {
        this.id = replyRanking.getId();
        this.userId = replyRanking.getUser().getId();
        this.nickName = replyRanking.getUser().getNickName();
        this.picture = replyRanking.getUser().getPicture();
        this.replyContent = replyRanking.getReplyContent();
    }
}
