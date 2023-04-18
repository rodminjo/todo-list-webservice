package todo.list_service.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import todo.list_service.domain.user.User;
import todo.list_service.domain.user.UserRepository;
import todo.list_service.web.dto.mypage.MypageIdCheckRequestDto;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    /**
     * 설명 : User Id를 이용하여 User 를 반환
     * */
    @Transactional(readOnly = true)
    public User findById(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다" ));

        return user;
    }

    /**
     * 설명 : 닉네임 중복여부를 판별한다.
     * */
    @Transactional(readOnly = true)
    public Boolean checkedId(MypageIdCheckRequestDto requestDto){

        // 해당 닉네임을 가지고 있는 모든 유저를 반환
        List<User> findByNickName = userRepository.findByNickName(requestDto.getNickName());

        // Nick name으로 찾아서 값이 없으면 아이디설정가능 true, 있으면 false
        if (findByNickName ==null || findByNickName.isEmpty()){
            return true;
        }

        return false;
    }

    /**
     * 설명 : 닉네임을 변경한다.
     * */
    @Transactional
    public Long changeNickName(MypageIdCheckRequestDto requestDto, Long id){

        // user id를 이용하여 user 찾아오기
        User user = userRepository.findById(id).orElseThrow(()->new IllegalArgumentException("해당 유저가 없습니다."));

        // user nickname 변경
        user.nickNameSetting(requestDto.getNickName());

        return user.getId();
    }



}
