package todo.list_service.domain.ranking;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import todo.list_service.domain.BaseTimeEntity;
import todo.list_service.domain.replyranking.ReplyRanking;
import todo.list_service.domain.timer.Timer;
import todo.list_service.domain.user.User;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class Ranking extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @OneToMany(mappedBy = "ranking", cascade = CascadeType.ALL)
    private List<Timer> timer = new ArrayList<>();

    @OneToMany(mappedBy = "ranking")
    private List<ReplyRanking> replyRanking = new ArrayList<>();

    @Column(nullable = false)
    private String week;

    @Column(length = 10, nullable = false)
    private LocalDate startDate;

    @Column(length = 10, nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    private Integer totalDoingTime;

    @Builder
    public Ranking(User user, String week, LocalDate startDate, LocalDate endDate,Integer totalDoingTime) {
        this.user = user;
        this.week = week;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalDoingTime = totalDoingTime;
    }

    public void updateDoingTime(Integer plusDoingTime) {
        this.totalDoingTime += plusDoingTime;
    }

    public void updateTotalDoingTimeToDeleteTimer(Integer minusTotalDoingTime) {
        this.totalDoingTime -= minusTotalDoingTime;
    }


}

