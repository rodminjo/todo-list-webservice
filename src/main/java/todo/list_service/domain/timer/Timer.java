package todo.list_service.domain.timer;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import todo.list_service.domain.BaseTimeEntity;
import todo.list_service.domain.ranking.Ranking;
import todo.list_service.domain.user.User;
import todo.list_service.web.dto.timer.TimerUpdateRequestDto;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
public class Timer extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TIMER_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne(targetEntity = Ranking.class)
    @JoinColumn(name="RANKING_ID")
    private Ranking ranking;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", length = 500)
    private String content;

    @Column(length = 10, nullable = false)
    private String todoDate;

    @Column
    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(columnDefinition = "TIMESTAMP", nullable = false)
    private LocalDateTime startTime;
    @Column(nullable = false)
    private Integer doingTime;

    @Builder
    public Timer(User user, String title, String content, String todoDate, Category category, LocalDateTime startTime, Integer doingTime) {
        this.user = user;
        this.title = title;
        this.content = content;
        this.todoDate = todoDate;
        this.category = category;
        this.startTime = startTime;
        this.doingTime = doingTime;
    }

    public void setRanking(Ranking ranking){
        if (this.ranking != null) {
            this.ranking.getTimer().remove(this);
        }

        if (ranking != null) {
            this.ranking = ranking;
            ranking.getTimer().add(this);
        }else{
            this.ranking = null;
        }
    }

    public void update(TimerUpdateRequestDto requestDto, Category category){
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.category = category;
    }


}
