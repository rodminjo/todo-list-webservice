package todo.list_service.domain.replyranking;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import todo.list_service.domain.BaseTimeEntity;
import todo.list_service.domain.ranking.Ranking;
import todo.list_service.domain.user.User;
import todo.list_service.web.dto.ranking.ReplyRankingSaveRequestDto;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class ReplyRanking extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "REPLYRANKING_ID")
    private Long id;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne(targetEntity = Ranking.class)
    @JoinColumn(name = "RANKING_ID")
    private Ranking ranking;

    @Column(columnDefinition = "TEXT", length = 500)
    private String replyContent;



    @Builder
    public ReplyRanking(User user, String replyContent) {
        this.user = user;
        this.replyContent = replyContent;
    }

    public void setRanking(Ranking ranking) {
        if (this.ranking != null) {
            this.ranking.getReplyRanking().remove(this);
        }

        if (ranking != null) {
            this.ranking = ranking;
            ranking.getReplyRanking().add(this);
        }else{
            this.ranking = null;
        }
    }

    public void update(ReplyRankingSaveRequestDto requestDto){
        this.replyContent = requestDto.getContent();
    }
}
