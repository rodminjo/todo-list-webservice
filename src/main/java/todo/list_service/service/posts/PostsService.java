package todo.list_service.service.posts;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import todo.list_service.domain.posts.Posts;
import todo.list_service.domain.posts.PostsRepository;
import todo.list_service.web.dto.PostsListResponseDto;
import todo.list_service.web.dto.PostsResponseDto;
import todo.list_service.web.dto.PostsSaveRequestDto;
import todo.list_service.web.dto.PostsUpdateRequestDto;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostsService {

    private final PostsRepository postsRepository;

    @Transactional
    public Long save(PostsSaveRequestDto requestDto){
        Posts savedPost = postsRepository.save(requestDto.toEntity());
        return savedPost.getId();
    }

    @Transactional
    public Long update(Long id, PostsUpdateRequestDto requestDto){
        Posts post = postsRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("해당 게시글이 없습니다. id: "+id));

        post.update(requestDto.getTitle(), requestDto.getContent(), requestDto.getChecked());

        return id;
    }

    @Transactional(readOnly = true)
    public PostsResponseDto findById(Long id){
        Posts post = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id: " + id));

        return new PostsResponseDto(post);
    }

    @Transactional(readOnly = true)
    public List<PostsListResponseDto> findAllDesc(){
        List<PostsListResponseDto> listDto = postsRepository.findAllDesc().stream()
                .map(posts -> new PostsListResponseDto(posts)).collect(Collectors.toList());

        return listDto;
    }

    @Transactional
    public void delete(Long id){
        postsRepository.delete(postsRepository.findById(id).get());
    }

}
