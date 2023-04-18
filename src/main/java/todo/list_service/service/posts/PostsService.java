package todo.list_service.service.posts;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import todo.list_service.domain.posts.Posts;
import todo.list_service.domain.user.User;

import todo.list_service.domain.posts.PostsRepository;
import todo.list_service.service.upload.UploadService;
import todo.list_service.service.user.UserService;

import todo.list_service.web.dto.posts.*;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostsService {

    private final PostsRepository postsRepository;
    private final UploadService uploadService;
    private final ReplyPostsService replyPostsService;
    private final UserService userService;

    /**
     * 설명 : 글 등록 화면에서 게시글을 저장한다.
     * */
    @Transactional
    public Long save(PostsSaveRequestDto requestDto, List<MultipartFile> files) throws Exception {
        // User 불러오기
        User user = userService.findById(requestDto.getUserId());

        // 저장할 Posts 생성(title, content 존재)
        Posts posts = requestDto.toEntity();

        // posts 에 user 저장
        posts.addUser(user);

        // 저장된 Posts
        Posts savedPost = postsRepository.save(posts);

        // multipart 형식 저장하고 주소리스트 반환
        uploadService.postsSave(files,savedPost);


        return savedPost.getId();
    }

    /**
     * 설명 : 글 상세보기 화면에서 게시글을 수정한다
    * */
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
            uploadService.postsSave(files, savedPost);
        }
        return savedPost.getId();
    }

    /**
     * 설명 : 상세정보 조회 및 조회수 갱신
     */
    @Transactional(readOnly = true)
    public PostsResponseDto findByIdToShow(Long id){

        // Posts 찾아오기
        Posts posts = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id: " + id));

        // 조회수 갱신
        posts.updateViewCount();

        // 저장된 파일 리스트
        List<String> uploadStoredFiles = posts.getUploadPosts().stream()
                .map(p -> p.getStoredFileName()).collect(Collectors.toList());

        // 댓글 관련 정보
        List<ReplyPostsResponseDto> replyDtos = replyPostsService.findByPostIdToShow(id);

        // responseDto 생성
        PostsResponseDto responseDto = new PostsResponseDto(posts);

        // 댓글, 사진 Dto에 저장
        responseDto.addStoredFileNames(uploadStoredFiles);
        responseDto.addReplyDto(replyDtos);


        return responseDto;
    }

    /**
     *  설명 : 인덱스 페이지 목록 조회
     *      반환값 : id, title , userNickname , viewCount , createdDate
     *  */
    @Transactional(readOnly = true)
    public List<PostsListResponseDto> findAll(Integer sort){

        // 반환할 Dto 변수 생성
        List<PostsListResponseDto> listDto;

        // 정렬에 따른 목록 조회
        if (sort==null || sort==1) {

            // sort = 1 최신순
            listDto = postsRepository.findAllDesc().stream()
                    .map(posts -> new PostsListResponseDto(posts)).collect(Collectors.toList());
        }else if (sort==2){

            // sort = 2 조회순
            listDto = postsRepository.findAllLookDesc().stream()
                    .map(posts -> new PostsListResponseDto(posts)).collect(Collectors.toList());
        }else {

            // sort = 3 댓글순
            listDto = postsRepository.findAllCommentDesc().stream()
                    .map(posts -> new PostsListResponseDto(posts)).collect(Collectors.toList());
        }

        // 섬네일 사진 조회
        for (PostsListResponseDto list : listDto) {
            String thumbnailStoredFileName = uploadService.FindThumbnailByPostsId(list.getId());

            if (thumbnailStoredFileName !=null){
                list.addStoredFileName(thumbnailStoredFileName);
            }
        }

        return listDto;
    }

    /**
     * 설명 : Board 인덱스 페이지에서 검색 결과 조회
     * */
    @Transactional(readOnly = true)
    public List<PostsListResponseDto> findBySearch(String search, Integer sort) {

        // 검색결과 반환 Dto 변수 선언
        List<PostsListResponseDto> listDto;

        // 정렬에 따른 검색결과 조회
        if (sort==null || sort==1){

            // 최신순
            listDto = postsRepository.findBySearchDesc(search).stream()
                    .map(posts -> new PostsListResponseDto(posts)).collect(Collectors.toList());

        }else if (sort==2){

            // 조회순
            listDto = postsRepository.findBySearchAndLookDesc(search).stream()
                    .map(posts -> new PostsListResponseDto(posts)).collect(Collectors.toList());

        }else {

            // 댓글순
            listDto = postsRepository.findBySearchAndCommentDesc(search).stream()
                    .map(posts -> new PostsListResponseDto(posts)).collect(Collectors.toList());
        }

        // 섬네일 사진 조회
        for (PostsListResponseDto list : listDto) {
            String thumbnailStoredFileName = uploadService.FindThumbnailByPostsId(list.getId());

            if (thumbnailStoredFileName !=null){
                list.addStoredFileName(thumbnailStoredFileName);
            }
        }

        return listDto;
    }


    /**
     * 설명 : 게시글을 삭제한다
     * */
    @Transactional
    public Long delete(Long id){
        // post 객체 찾아오기
        Posts posts = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id: " + id));

        // 게시글 사진 삭제
        uploadService.postsDelete(posts.getId());

        // 게시글 댓글 삭제
        replyPostsService.deleteAll(posts.getId());

        // 게시글 삭제
        postsRepository.delete(posts);

        return id;
    }


    /**
     * 설명 :  댓글을 저장한다
     * */
    @Transactional
    public Long replySave(ReplyPostsSaveRequestDto requestDto, Long postId, Long userId){

        // User 조회
        User user = userService.findById(userId);

        // Posts 조회
        Posts posts = postsRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id: " + postId));

        // 댓글 정보 넘겨주기
        return replyPostsService.save(requestDto,user,posts);
    }

    /**
     * 설명 : 댓글을 수정한다
     * */
    @Transactional
    public Long replyUpdate(ReplyPostsSaveRequestDto requestDto,Long postId ,Long replyId,Long userId){
        // User 조회
        User user = userService.findById(userId);

        // Posts 조회
        Posts posts = postsRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id: " + postId));

        // 댓글 정보 넘겨주기
        return replyPostsService.update(requestDto, replyId, user);
    }

    /**
     * 설명 : 댓글을 삭제한다
     * */
    @Transactional
    public Long replyDelete(Long postId, Long replyId, Long userId){

        // Posts 조회
        Posts posts = postsRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id: " + postId));

        // 댓글 정보 넘겨주기
        return replyPostsService.delete(replyId, posts);
    }


}
