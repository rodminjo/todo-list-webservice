package todo.list_service.config.oauth.dto;

import lombok.Getter;
import todo.list_service.domain.user.User;

import java.io.Serializable;

@Getter
public class SessionUser implements Serializable {
    private Long id;
    private String name;
    private String email;
    private String picture;
    private String nickName;

    public SessionUser(User user){
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.picture = user.getPicture();
        this.nickName = user.getNickName();
    }




}
