package todo.list_service.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import todo.list_service.config.oauth.dto.SessionUser;
import todo.list_service.domain.posts.Posts;
import todo.list_service.domain.posts.PostsRepository;
import todo.list_service.domain.replyposts.ReplyPosts;
import todo.list_service.domain.replyposts.ReplyPostsRepository;
import todo.list_service.domain.uploadPosts.UploadPosts;
import todo.list_service.domain.uploadPosts.UploadPostsRepository;
import todo.list_service.domain.user.Role;
import todo.list_service.domain.user.User;
import todo.list_service.domain.user.UserRepository;
import todo.list_service.web.dto.posts.PostsSaveRequestDto;
import todo.list_service.web.dto.posts.ReplyPostsSaveRequestDto;

import java.io.FileInputStream;
import java.util.List;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


// TestRestTemplate 을 이용하여 실제로 Post신호를 줄건데 기존 포트가 아닌 다른 포트가 필요. 그래서 랜덤 포트로 실행
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PostsApiControllerTest {

    @LocalServerPort
    private int port;
    
    @Autowired
    private PostsRepository postsRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UploadPostsRepository uploadPostsRepository;
    @Autowired
    private ReplyPostsRepository replyPostsRepository;

    
    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;
    
    @BeforeEach
    void setup(){
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }
    
    @AfterEach
    void tearDown() {
        uploadPostsRepository.deleteAll();
        replyPostsRepository.deleteAll();
        postsRepository.deleteAll();
        userRepository.deleteAll();
    }
    
    @Test
    @WithMockUser(roles = "USER")
    void Posts_등록() throws Exception {
        //given
        // 유저 데이터 생성 및 세션에 저장
        User user = userRepository.save(User.builder()
                .name("testname")
                .picture("test")
                .email("testemail")
                .role(Role.USER).build());

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user",new SessionUser(user));

        // PostSaveRequestDto 생성
        String title = "test title";
        String content = "test content";
        Long userId = 1L;

        PostsSaveRequestDto requestDto = PostsSaveRequestDto.builder()
                .title(title).content(content).build();
        requestDto.setUserId(userId);

        // MockMultipartFile 생성
        MockMultipartFile file = new MockMultipartFile("file", "test.png", "image/png",
                new FileInputStream("/Users/jogyeongmin/Downloads/미모티콘.png"));
        MockMultipartFile mock = new MockMultipartFile("key", "key", "application/json", new ObjectMapper().writeValueAsString(requestDto).getBytes());
        // 보낼 url
        String url = "http://localhost:"+port+"/api/v1/posts";


        //when
        // url을 통해 post를 보내줌
        mvc.perform(MockMvcRequestBuilders.multipart(url)
                        .file(file)
                        .file(mock).session(session))
                .andExpect(status().isOk());


        //then
        // 바디에 들어있는지 확인
        List<Posts> all = postsRepository.findAll();

        Assertions.assertThat(all.get(0).getTitle()).isEqualTo(title);
        Assertions.assertThat(all.get(0).getContent()).isEqualTo(content);
        Assertions.assertThat(all.get(0).getViewCount()).isSameAs(0);

        // 업로드 파일 잘 들어갔는지 확인
        List<UploadPosts> allByPostId = uploadPostsRepository.findAllByPostId(all.get(0).getId());
        Assertions.assertThat(allByPostId.get(0).getOriginalFileName()).isEqualTo("test.png");
    }

    @Test
    @WithMockUser(roles = "USER")
    void Posts_수정() throws Exception {
        //given
        String title = "test title";
        String content = "test content";
        Long userId = 1L;

        // 유저 데이터 생성 및 세션에 저장
        User user = userRepository.save(User.builder()
                .name("testname")
                .picture("test")
                .email("testemail")
                .role(Role.USER).build());

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user",new SessionUser(user));

        // PostSaveRequestDto 생성
        PostsSaveRequestDto requestDto = PostsSaveRequestDto.builder()
                .title(title).content(content).build();
        requestDto.setUserId(userId);

        // MockMultipartFile 생성
        MockMultipartFile file = new MockMultipartFile("file", "test.png", "image/png",
                new FileInputStream("/Users/jogyeongmin/Downloads/미모티콘.png"));
        MockMultipartFile mock = new MockMultipartFile("key", "key", "application/json", new ObjectMapper().writeValueAsString(requestDto).getBytes());

        // 보낼 url
        String url = "http://localhost:"+port+"/api/v1/posts";

        // url을 통해 post요청 및 저장
        mvc.perform(MockMvcRequestBuilders.multipart(url)
                        .file(file)
                        .file(mock).session(session))
                .andExpect(status().isOk());


        //when
        List<Posts> all = postsRepository.findAll();
        Long savedPostsId = all.get(all.size() - 1).getId();

        // 수정 url
        url = "http://localhost:"+port+"/api/v1/posts/"+savedPostsId;

        // 업데이트 자료
        String updateTitle = "test title";
        String updateContent = "test content";


        // 업데이트 PostSaveRequestDto 생성
        PostsSaveRequestDto updateRequestDto = PostsSaveRequestDto.builder()
                .title(updateTitle).content(updateContent).build();
        requestDto.setUserId(userId);

        MockMultipartFile updateMock = new MockMultipartFile("key", "key", "application/json",
                new ObjectMapper().writeValueAsString(updateRequestDto).getBytes());

        //사진을 제외하고 post 요청
        mvc.perform(MockMvcRequestBuilders.multipart(url)
                        .file(updateMock).session(session))
                .andExpect(status().isOk());

        //then
        // 바디에 들어있는지 확인
        List<Posts> updateAll = postsRepository.findAll();

        Assertions.assertThat(updateAll.get(0).getTitle()).isEqualTo(updateTitle);
        Assertions.assertThat(updateAll.get(0).getContent()).isEqualTo(updateContent);
        Assertions.assertThat(updateAll.get(0).getViewCount()).isSameAs(0);

        // 업로드 파일 삭제 확인
        List<UploadPosts> allByPostId = uploadPostsRepository.findAllByPostId(all.get(0).getId());
        Assertions.assertThat(allByPostId).isEmpty();
    }

    @Test
    @WithMockUser
    void posts_삭제하기() throws Exception {
        //given
        String title = "test title";
        String content = "test content";

        // 유저 데이터 생성 및 세션에 저장
        User user = userRepository.save(User.builder()
                .name("testname")
                .picture("test")
                .email("testemail")
                .role(Role.USER).build());

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user",new SessionUser(user));

        // PostSaveRequestDto 생성
        PostsSaveRequestDto requestDto = PostsSaveRequestDto.builder()
                .title(title).content(content).build();
        requestDto.setUserId(user.getId());

        // MockMultipartFile 생성
        MockMultipartFile file = new MockMultipartFile("file", "test.png", "image/png",
                new FileInputStream("/Users/jogyeongmin/Downloads/미모티콘.png"));
        MockMultipartFile mock = new MockMultipartFile("key", "key", "application/json", new ObjectMapper().writeValueAsString(requestDto).getBytes());

        // 보낼 url
        String url = "http://localhost:"+port+"/api/v1/posts";

        // url을 통해 post요청 및 저장
        mvc.perform(MockMvcRequestBuilders.multipart(url)
                        .file(file)
                        .file(mock).session(session))
                .andExpect(status().isOk());

        // 저장된 postId
        List<Posts> all = postsRepository.findAll();
        Long savedPostsId = all.get(all.size() - 1).getId();


        // 댓글 저장 요청
        String replyPostsContent = "test reply";
        Integer replyLevel = 0;
        ReplyPostsSaveRequestDto replySaveRequestDto = ReplyPostsSaveRequestDto.builder()
                .replyLevel(0)
                .replyParent("haha")
                .content(replyPostsContent)
                .build();
        //댓글저장 url
        url = "http://localhost:"+port+"/api/v1/posts/"+savedPostsId+"/reply";


        mvc.perform(MockMvcRequestBuilders.post(url).contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(replySaveRequestDto))
                        .session(session))
                .andExpect(status().isOk());



        //when
        // 삭제요청 url
        url = "http://localhost:"+port+"/api/v1/posts/"+savedPostsId;

        // delete 요청
        mvc.perform(MockMvcRequestBuilders.delete(url)).andExpect(status().isOk());

        // post 게시글 삭제 확인
        List<Posts> deletedAll =  postsRepository.findAll();
        Assertions.assertThat(deletedAll.size()).isSameAs(0);
        // uploadPosts 게시글 삭제 확인
        List<UploadPosts> savedUploadPosts = uploadPostsRepository.findAllByPostId(savedPostsId);
        Assertions.assertThat(savedUploadPosts.size()).isSameAs(0);
        // 댓글 삭제 확인
        List<ReplyPosts> savedReplyPostsList = replyPostsRepository.findAllByPostId(savedPostsId);
        Assertions.assertThat(savedReplyPostsList.size()).isSameAs(0);

    }

    @Test
    @WithMockUser
    void ReplyPosts_저장하기() throws Exception {
        //given
        // 유저 데이터 생성 및 세션에 저장
        User user = userRepository.save(User.builder()
                .name("testname")
                .picture("test")
                .email("testemail")
                .role(Role.USER).build());

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user",new SessionUser(user));

        // PostSaveRequestDto 생성
        String title = "test title";
        String content = "test content";

        PostsSaveRequestDto requestDto = PostsSaveRequestDto.builder()
                .title(title).content(content).build();

        // MockMultipartFile 생성
        MockMultipartFile file = new MockMultipartFile("file", "test.png", "image/png",
                new FileInputStream("/Users/jogyeongmin/Downloads/미모티콘.png"));
        MockMultipartFile mock = new MockMultipartFile("key", "key", "application/json", new ObjectMapper().writeValueAsString(requestDto).getBytes());

        // 보낼 url
        String url = "http://localhost:"+port+"/api/v1/posts";

        // url을 통해 post요청 및 저장
        mvc.perform(MockMvcRequestBuilders.multipart(url)
                        .file(file)
                        .file(mock).session(session))
                .andExpect(status().isOk());

        // 저장된 postId
        List<Posts> all = postsRepository.findAll();
        Long savedPostsId = all.get(all.size() - 1).getId();

        // 댓글 저장 요청
        String replyPostsContent = "test reply";
        ReplyPostsSaveRequestDto replySaveRequestDto = ReplyPostsSaveRequestDto.builder()
                .replyLevel(0)
                .replyParent("haha")
                .content(replyPostsContent)
                .build();

        //댓글저장 url
        url = "http://localhost:"+port+"/api/v1/posts/"+savedPostsId+"/reply";

        mvc.perform(MockMvcRequestBuilders.post(url).contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(replySaveRequestDto))
                        .session(session))
                .andExpect(status().isOk());

        // 저장된 댓글 확인
        List<ReplyPosts> savedReplyPostsList = replyPostsRepository.findAllByPostId(savedPostsId);
        Assertions.assertThat(savedReplyPostsList.get(0).getReplyContent()).isEqualTo(replyPostsContent);
    }

    @Test
    @WithMockUser
    void ReplyPosts_수정하기() throws Exception {
        //given
        // 유저 데이터 생성 및 세션에 저장
        User user = userRepository.save(User.builder()
                .name("testname")
                .picture("test")
                .email("testemail")
                .role(Role.USER).build());

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user",new SessionUser(user));

        // PostSaveRequestDto 생성
        String title = "test title";
        String content = "test content";

        PostsSaveRequestDto requestDto = PostsSaveRequestDto.builder()
                .title(title).content(content).build();

        // MockMultipartFile 생성
        MockMultipartFile file = new MockMultipartFile("file", "test.png", "image/png",
                new FileInputStream("/Users/jogyeongmin/Downloads/미모티콘.png"));
        MockMultipartFile mock = new MockMultipartFile("key", "key", "application/json", new ObjectMapper().writeValueAsString(requestDto).getBytes());

        // 보낼 url
        String url = "http://localhost:"+port+"/api/v1/posts";

        // url을 통해 post요청 및 저장
        mvc.perform(MockMvcRequestBuilders.multipart(url)
                        .file(file)
                        .file(mock).session(session))
                .andExpect(status().isOk());

        // 저장된 postId
        List<Posts> all = postsRepository.findAll();
        Long savedPostsId = all.get(all.size() - 1).getId();

        // 댓글 저장 요청
        String replyPostsContent = "test reply";
        ReplyPostsSaveRequestDto replySaveRequestDto = ReplyPostsSaveRequestDto.builder()
                .replyLevel(0)
                .replyParent("haha")
                .content(replyPostsContent)
                .build();

        //댓글저장 url
        url = "http://localhost:"+port+"/api/v1/posts/"+savedPostsId+"/reply";

        mvc.perform(MockMvcRequestBuilders.post(url).contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(replySaveRequestDto))
                        .session(session))
                .andExpect(status().isOk());

        // 저장된 replyPostsId
        List<ReplyPosts> savedReplyPostsList = replyPostsRepository.findAllByPostId(savedPostsId);
        Long savedReplyPostsId = savedReplyPostsList.get(savedReplyPostsList.size() - 1).getId();

        // 댓글 수정 요청
        String updateReplyPostsContent = "update test reply";
        ReplyPostsSaveRequestDto updateReplySaveRequestDto = ReplyPostsSaveRequestDto.builder()
                .content(updateReplyPostsContent)
                .build();

        //댓글저장 url
        url = "http://localhost:"+port+"/api/v1/posts/"+savedPostsId+"/reply/"+savedReplyPostsId;

        mvc.perform(MockMvcRequestBuilders.post(url).contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(updateReplySaveRequestDto))
                        .session(session))
                .andExpect(status().isOk());

        // 저장된 댓글 확인
        List<ReplyPosts> updatedReplyPostsList = replyPostsRepository.findAll();
        Assertions.assertThat(updatedReplyPostsList.get(0).getReplyContent()).isEqualTo(updateReplyPostsContent);
    }



}