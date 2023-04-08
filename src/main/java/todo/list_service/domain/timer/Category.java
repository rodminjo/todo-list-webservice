package todo.list_service.domain.timer;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Category {

    // enum : 연관시킬 문자를 ()로 선언했을 경우 세미콜론을 끝에 붙이고 아래 변수를 선언해줌

    STUDY,
    WORK,
    EXERCISE,
    REST,
    ETC;

    @JsonCreator
    public static Category category(String s) {
        return Category.valueOf(s.toUpperCase());
    }
}
