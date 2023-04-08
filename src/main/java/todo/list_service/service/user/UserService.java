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

    @Transactional(readOnly = true)
    public User findById(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다" ));

        return user;
    }

    @Transactional(readOnly = true)
    public Boolean checkedId(MypageIdCheckRequestDto requestDto){
        List<User> findByNickName = userRepository.findByNickName(requestDto.getNickName());

        // Nick name으로 찾아서 값이 없으면 아이디설정가능 true, 있으면 false
        if (findByNickName ==null || findByNickName.isEmpty()){
            return true;
        }
        return false;
    }

    @Transactional
    public Long changeNickName(MypageIdCheckRequestDto requestDto, Long id){
        User user = userRepository.findById(id).orElseThrow(()->new IllegalArgumentException("해당 유저가 없습니다."));

        user.nickNameSetting(requestDto.getNickName());

        return user.getId();
    }



}
