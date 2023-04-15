package todo.list_service.service.posts;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import todo.list_service.domain.posts.Posts;
import todo.list_service.domain.posts.PostsRepository;
import todo.list_service.domain.uploadPosts.UploadPosts;
import todo.list_service.domain.user.User;
import todo.list_service.service.upload.UploadService;
import todo.list_service.service.user.UserService;
import todo.list_service.web.dto.posts.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostsService {

    private final PostsRepository postsRepository;
    private final UploadService uploadService;
    private final ReplyPostsService replyPostsService;
    private final UserService userService;

    // 저장
    @Transactional
    public Long save(PostsSaveRequestDto requestDto, List<MultipartFile> files) throws Exception {
        // User 불러오기
        User user = userService.findById(requestDto.getUserId());

        Posts posts = Posts.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .user(user)
                .build();

        // 저장할 Post 생성
        Posts savedPost = postsRepository.save(posts);

        // multipart 형식 저장하고 주소리스트 반환
        List<UploadPosts> uploadPostsList = uploadService.postsSave(files,savedPost);


        return savedPost.getId();
    }

    //업데이트
    @Transactional
    public Long update(PostsUpdateRequestDto requestDto, List<MultipartFile> files) throws Exception {
        //post 객체 불러오기
        Posts savedPost = postsRepository.findById(requestDto.getPostId())
                .orElseThrow(()-> new IllegalArgumentException("해당 게시글이 없습니다. id: "+requestDto.getPostId()));
        //post 정보 업데이트
        savedPost.update(requestDto.getTitle(), requestDto.getContent());

        if (files == null || files.isEmpty()) {
            // 요청 파일이 없을때 전부 삭제
            uploadService.postsDelete(savedPost.getId());
        }else {
            // 요청파일이 있을때 삭제
            uploadService.postsDelete(savedPost.getId());
            //새로운 요청으로 저장
            List<UploadPosts> uploadPostsList = uploadService.postsSave(files, savedPost);
        }
        return savedPost.getId();
    }

    // 상세정보 조회 및 조회수 갱신
    @Transactional(readOnly = true)
    public PostsResponseDto findByIdToShow(Long id){
        Posts post = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id: " + id));
        // 조회수 갱신
        post.updateViewCount();

        // 저장된 파일 리스트
        List<String> uploadStoredFiles = post.getUploadPosts().stream()
                .map(p -> p.getStoredFileName()).collect(Collectors.toList());

        // 댓글 관련 정보
        List<ReplyPostsResponseDto> replyDtos = replyPostsService.findByPostIdToShow(id);

        // responseDto 생성
        PostsResponseDto responseDto = new PostsResponseDto(post);
        responseDto.addStoredFileNames(uploadStoredFiles);
        responseDto.addReplyDto(replyDtos);


        return responseDto;
    }

    // 인덱스 페이지 목록 조회
    @Transactional(readOnly = true)
    public List<PostsListResponseDto> findAll(Integer sort){
        // id, title , userNickname , viewCount , createdDate 가져와야함
        List<PostsListResponseDto> listDto = new ArrayList<>();
        if (sort==null || sort==1) {
            listDto = postsRepository.findAllDesc().stream()
                    .map(posts -> new PostsListResponseDto(posts)).collect(Collectors.toList());
        }else if (sort==2){
            listDto = postsRepository.findAllLookDesc().stream()
                    .map(posts -> new PostsListResponseDto(posts)).collect(Collectors.toList());
        }else {
            listDto = postsRepository.findAllCommentDesc().stream()
                    .map(posts -> new PostsListResponseDto(posts)).collect(Collectors.toList());
        }

        for (PostsListResponseDto list : listDto) {
            String thumbnailStoredFileName = uploadService.FindThumbnailByPostsId(list.getId());

            if (thumbnailStoredFileName !=null){
                list.addStoredFileName(thumbnailStoredFileName);
            }
        }

        return listDto;
    }
    @Transactional(readOnly = true)
    public List<PostsListResponseDto> findBySearch(String search, Integer sort) {
        List<PostsListResponseDto> listDto = new ArrayList<>();
        if (sort==null || sort==1){
            listDto = postsRepository.findBySearchDesc(search).stream()
                    .map(posts -> new PostsListResponseDto(posts)).collect(Collectors.toList());
        }else if (sort==2){
            listDto = postsRepository.findBySearchAndLookDesc(search).stream()
                    .map(posts -> new PostsListResponseDto(posts)).collect(Collectors.toList());
        }else {
            listDto = postsRepository.findBySearchAndCommentDesc(search).stream()
                    .map(posts -> new PostsListResponseDto(posts)).collect(Collectors.toList());
        }


        for (PostsListResponseDto list : listDto) {
            String thumbnailStoredFileName = uploadService.FindThumbnailByPostsId(list.getId());

            if (thumbnailStoredFileName !=null){
                list.addStoredFileName(thumbnailStoredFileName);
            }
        }

        return listDto;
    }


    // 삭제
    @Transactional
    public Long delete(Long id){
        // post 객체 찾아오기
        Posts posts = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id: " + id));

        //삭제
        uploadService.postsDelete(posts.getId());
        replyPostsService.deleteAll(posts.getId());
        postsRepository.delete(posts);

        return id;
    }

    @Transactional
    public Long replySave(ReplyPostsSaveRequestDto requestDto, Long postId, Long userId){
        User user = userService.findById(userId);
        Posts posts = postsRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id: " + postId));
        return replyPostsService.save(requestDto,user,posts);
    }

    @Transactional
    public Long replyUpdate(ReplyPostsSaveRequestDto requestDto,Long postId ,Long replyId,Long userId){
        //posts 작성자인지
        User user = userService.findById(userId);
        Posts posts = postsRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id: " + postId));
        if (user.getId() != posts.getUser().getId()){
            //작성자가 아니라면 ?
        }


        return replyPostsService.update(requestDto, replyId, user);
    }

    @Transactional
    public Long replyDelete(Long postId, Long replyId, Long userId){
        Posts posts = postsRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id: " + postId));

        return replyPostsService.delete(replyId, posts);
    }


}
