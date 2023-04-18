package todo.list_service.domain.user;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import todo.list_service.domain.BaseTimeEntity;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private Long id;
    @Column(nullable = false, length = 50)
    private String name;
    @Column(nullable = false)
    private String email;
    @Column
    private String picture;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;
    @Column(length = 50)
    private String nickName;


    @Builder
    public User(String name, String email, String picture, Role role) {
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.role = role;
    }

    public User update(String name, String picture) {
        this.name = name;
        this.picture = picture;

        return this;
    }

    public String getRoleKey(){
        return this.role.getKey();
    }

    /**
     * 설명 : 닉네임을 변경한다
     * */
    public void nickNameSetting(String nickName){
        this.nickName = nickName;
    }
}
