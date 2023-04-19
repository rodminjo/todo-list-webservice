package todo.list_service.service.user;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.test.context.support.WithMockUser;
import todo.list_service.domain.user.Role;
import todo.list_service.domain.user.User;
import todo.list_service.domain.user.UserRepository;
import todo.list_service.web.dto.mypage.MypageIdCheckRequestDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {


    @Mock
    UserRepository userRepository;


    /**
     * 설명 : User Id를 이용하여 User 를 반환
     * */
    @WithMockUser
    @Test
    void 유저아이디를_받아_유저를_반환한다(){

        User user = User.builder()
                .name("testname")
                .email("testemail")
                .picture("testpicture")
                .role(Role.USER)
                .build();

        UserService userService = new UserService(userRepository);

        given(userRepository.findById(any(Long.class))).willReturn(Optional.ofNullable(user));

        Assertions.assertThat(userService.findById(1L))
                .isEqualTo(user);

    }

    /**
     * 설명 : 닉네임이 같은 객체 불러오기
     * */
    @WithMockUser
    @Test
    void 닉네임_중복여부(){

        User user = User.builder()
                .name("testname")
                .email("testemail")
                .picture("testpicture")
                .role(Role.USER)
                .build();

        List<User> list = new ArrayList<>();
        list.add(user);

        UserService userService = new UserService(userRepository);

        given(userRepository.findByNickName(any(String.class))).willReturn(list);

        Assertions.assertThat(userService.checkedId(new MypageIdCheckRequestDto("aa")))
                .isEqualTo(false);


    }

    /**
     * 설명 : 닉네임 변경
     * */
    @WithMockUser
    @Test
    void 닉네임_변경하기(){

        User user = User.builder()
                .name("testname")
                .email("testemail")
                .picture("testpicture")
                .role(Role.USER)
                .build();

        List<User> list = new ArrayList<>();
        list.add(user);

        UserService userService = new UserService(userRepository);

        given(userRepository.findById(any(Long.class))).willReturn(Optional.ofNullable(user));

        // 위에서 선언한 user 객체 닉네임 변경
        userService.changeNickName(new MypageIdCheckRequestDto("aa"), 1L);

        // 변경된 닉네임 확인
        Assertions.assertThat(user.getNickName())
                .isEqualTo("aa");

    }






}