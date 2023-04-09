package todo.list_service.web.dto.ranking;

import lombok.Getter;
import lombok.NoArgsConstructor;
import todo.list_service.domain.ranking.Ranking;

@Getter
@NoArgsConstructor
public class RankingListResponseDto {
    private Long id;
    private String week;
    private String picture;
    private String nickName;
    private String convertTotalDoingTime;

    public RankingListResponseDto(Ranking ranking){
        this.id = ranking.getId();
        this.week = ranking.getWeek();
        this.picture = ranking.getUser().getPicture();
        this.nickName = ranking.getUser().getNickName();
        this.convertTotalDoingTime = String.format("%02d", (ranking.getTotalDoingTime() / 3600)) + ":"
                + String.format("%02d", ((ranking.getTotalDoingTime() % 3600) / 60)) + ":"
                + String.format("%02d", ((ranking.getTotalDoingTime() % 3600) % 60));
    }
}
