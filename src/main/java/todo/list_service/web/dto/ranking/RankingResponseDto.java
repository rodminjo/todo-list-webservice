package todo.list_service.web.dto.ranking;

import lombok.Getter;
import lombok.NoArgsConstructor;
import todo.list_service.domain.ranking.Ranking;
import todo.list_service.domain.replyranking.ReplyRanking;
import todo.list_service.web.dto.timer.TimerListResponseDto;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class RankingResponseDto {
    private Long id;
    private String picture;
    private String nickName;
    private String week;
    private Integer totalDoingTime;
    private List<TimerListResponseDto> timerListResponseDto;
    private List<ReplyRankingListResponseDto> replyRankingListResponseDto;

    public RankingResponseDto(Ranking ranking) {
        this.id = ranking.getId();
        this.picture = ranking.getUser().getPicture();
        this.nickName = ranking.getUser().getNickName();
        this.week = ranking.getWeek();
        this.totalDoingTime = ranking.getTotalDoingTime();
        this.timerListResponseDto = ranking.getTimer()
                .stream().map(timer -> new TimerListResponseDto(timer)).collect(Collectors.toList());
        this.replyRankingListResponseDto = ranking.getReplyRanking()
                .stream().map(reply -> new ReplyRankingListResponseDto(reply)).collect(Collectors.toList());
    }
}
