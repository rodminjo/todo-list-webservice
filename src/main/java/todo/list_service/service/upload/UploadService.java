package todo.list_service.service.upload;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

// Posts
import todo.list_service.domain.posts.Posts;
import todo.list_service.domain.uploadPosts.UploadPosts;
import todo.list_service.domain.uploadPosts.UploadPostsRepository;

// Todo
import todo.list_service.domain.todo.Todo;
import todo.list_service.domain.uploadTodo.UploadTodo;
import todo.list_service.domain.uploadTodo.UploadTodoRepository;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UploadService {

    private final UploadPostsRepository uploadPostsRepository;
    private final UploadPostsHandler uploadPostsHandler;
    private final UploadTodoRepository uploadTodoRepository;
    private final UploadTodoHandler uploadTodoHandler;



    /**
     *  posts 메서드
     * */


    /**
     * 설명 : posts 에 따른 이미지 저장
     * */
    @Transactional
    public List<UploadPosts> postsSave(List<MultipartFile> files, Posts posts) throws Exception {

        // 변환한 사진 리스트를 담을 변수 선언
        List<UploadPosts> expertSaveUploadList;

        // 저장된 리스트 담을 변수 선언
        List<UploadPosts> savedUploadList = new ArrayList<>();

        // 파라미터로 받은 files 가 null 인지, 비었는지 확인
        if (files != null && !files.isEmpty()){

            // 사진 변환 및 저장
            expertSaveUploadList = uploadPostsHandler.parseFileInfo(files);

            // 변환한 사진 정보 posts에 저장
            for (UploadPosts uploadPost : expertSaveUploadList){

                uploadPost.setPosts(posts);
                savedUploadList.add(uploadPostsRepository.save(uploadPost));
            }
        }


        return savedUploadList;

    }

    /**
     * 설명 : posts 목록에 사용할 섬네일 이미지 가져오기
     * */

    @Transactional(readOnly = true)
    public String FindThumbnailByPostsId(Long id) {

        // repository에서 post id에 해당하는 목록 반환
        List<UploadPosts> findAllById = uploadPostsRepository.findAllByPostId(id);

        // 반환값이 null이거나 비었으면 null 반환
        if (findAllById == null || findAllById.isEmpty()) {
            return null;
        }

        // 첫번째 이미지 반환
        return findAllById.get(0).getStoredFileName();
    }

    /**
     * 설명 : Posts를 삭제할때 이에 따른 사진도 전부 삭제
     * */
    @Transactional
    public void postsDelete(Long id) {

        // posts id에 맞는 사진 리스트 반환
        List<UploadPosts> savedUploadList = uploadPostsRepository.findAllByPostId(id);

        // 비었는지 확인
        if (savedUploadList != null && !savedUploadList.isEmpty()){

            // 반복문을 통해 리스트에 들어있는 목록 삭제
            for (UploadPosts uploadPost : savedUploadList){

                // posts와 uploadPost의 관계를 끊어주기 위해 null값을 입력
                uploadPost.setPosts(null);

                // upload posts 실제로 저장된 사진 삭제
                uploadPostsHandler.delete(uploadPost.getStoredFileName());

                // DB에서 삭제
                uploadPostsRepository.delete(uploadPost);
            }
        }
    }

    /**
     *  todo 메서드
     * */

    /**
     *  설명 : todo 이미지를 저장한다.
     * */
    @Transactional
    public List<UploadTodo> todoSave(List<MultipartFile> files, Todo todo) throws Exception {

        // 변환될 file이 담길 변수 선언
        List<UploadTodo> expectUploadList;

        // DB에 저장되고 반환될 변수 선언
        List<UploadTodo> savedUploadList = new ArrayList<>();


        // files 가 비어있는지 확인
        if (files != null && !files.isEmpty()){

            // 파일 변환 및 저장소 저장
            expectUploadList = uploadTodoHandler.parseFileInfo(files);

            // 반복문을 통해 세팅 및 DB저장
            for (UploadTodo uploadTodo : expectUploadList){

                // 저장될 uploadTodo에 todo 값 입력
                uploadTodo.setTodo(todo);

                // DB저장
               savedUploadList.add(uploadTodoRepository.save(uploadTodo));
            }
        }
        // 파일 저장
        return savedUploadList;

    }

    /**
     *  설명 : todo id를 받아 섬네일용 이미지를 반환한다
     * */
    @Transactional(readOnly = true)
    public String FindThumbnailByTodoId(Long id) {

        // todo id를 받아 해당하는 이미지 리스트 반환
        List<UploadTodo> findAllById = uploadTodoRepository.findAllByTodoId(id);

        // 이미지가 존재하지 않을 경우 null값 출력, 존재할 경우 섬네일용 첫번째 이미지 반환
        if (findAllById == null || findAllById.isEmpty()) {
            return null;
        }

        return findAllById.get(0).getStoredFileName();
    }


    /**
     * 설명 : todo 가 삭제될 경우 저장된 사진파일 삭제
     * */
    @Transactional
    public void todoDelete(Long id) {

        // todoid에 맞는 게시글 불러오기
        List<UploadTodo> savedUploadList = uploadTodoRepository.findAllByTodoId(id);

        // 비어있는지 확인
        if (savedUploadList != null && !savedUploadList.isEmpty()) {

            // 반복문을 통해 삭제
            for (UploadTodo uploadTodo : savedUploadList) {

                // uploadTodo와 todo의 관계를 끊어주기 위해 null값 입력
                uploadTodo.setTodo(null);

                // 로컬저장소에서 파일 삭제
                uploadTodoHandler.delete(uploadTodo.getStoredFileName());

                // DB에서 삭제
                uploadTodoRepository.delete(uploadTodo);
            }
        }
    }


}
