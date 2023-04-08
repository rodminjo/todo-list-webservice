package todo.list_service.web.dto.mypage;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MypageIdCheckRequestDto {

    private String nickName;

    @Builder
    public MypageIdCheckRequestDto(String nickName) {
        this.nickName = nickName;
    }

}
