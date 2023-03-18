package todo.list_service.config.auth;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


// Target(ElementType.PARAMETER) : 이 어노테이션이 생성될 수 있는 위치 지정. 여기선 파라미터로 지정
// @interface : 이 파일을 어노테이션 클래스로 지정함. 즉 LoginUser이라는 어노테이션 생성
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginUser {


}
