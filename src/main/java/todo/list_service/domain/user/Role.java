package todo.list_service.domain.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

    // enum : 연관시킬 문자를 ()로 선언했을 경우 세미콜론을 끝에 붙이고 아래 변수를 선언해줌
    // 아래의 경우 ROLE_GUEST가 키, 손님이 타이틀
    USER("ROLE_USER", "일반 사용자"),
    ADMIN("ROLE_ADMIN", "관리자"),
    BAN("ROLE_BAN", "금지 사용자");

    private final String key;
    private final String title;

}
