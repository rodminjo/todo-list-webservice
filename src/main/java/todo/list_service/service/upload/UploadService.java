package todo.list_service.service.upload;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import todo.list_service.domain.posts.Posts;
import todo.list_service.domain.todo.Todo;
import todo.list_service.domain.uploadPosts.UploadPosts;
import todo.list_service.domain.uploadPosts.UploadPostsRepository;
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

    //공통 메서드
    // multiPartFile 업로드
    public List<UploadPosts> convertingFiles(List<MultipartFile> files) throws Exception {
        return uploadPostsHandler.parseFileInfo(files);
    }
    // posts 메서드
    // uploadPosts save
    @Transactional
    public List<UploadPosts> postsSave(List<MultipartFile> files, Posts posts) throws Exception {
        List<UploadPosts> expertSaveUploadList = new ArrayList<>();
        List<UploadPosts> savedUploadList = new ArrayList<>();
        // null인지 확인하고 비었는지 확인
        if (files != null&&!files.isEmpty()){
            expertSaveUploadList = uploadPostsHandler.parseFileInfo(files);

            for (UploadPosts uploadPost : expertSaveUploadList){
                uploadPost.setPosts(posts);
                savedUploadList.add(uploadPostsRepository.save(uploadPost));
            }
        }
        // 파일 저장
        return savedUploadList;

    }

    @Transactional(readOnly = true)
    public String FindThumbnailByPostsId(Long id) {
        List<UploadPosts> findAllById = new ArrayList<>();
        findAllById = uploadPostsRepository.findAllByPostId(id);

        if (findAllById == null || findAllById.isEmpty()) {
            return null;
        }

        return findAllById.get(0).getStoredFileName();
    }

    @Transactional
    public void postsDelete(Long id) {
        List<UploadPosts> savedUploadList = uploadPostsRepository.findAllByPostId(id);
        if (savedUploadList != null && !savedUploadList.isEmpty()){
            for (UploadPosts uploadPost : savedUploadList){
                uploadPost.setPosts(null);
                uploadPostsHandler.delete(uploadPost.getStoredFileName());
                uploadPostsRepository.delete(uploadPost);
            }
        }
    }

    // todo 메서드
    // todo save
    @Transactional
    public List<UploadTodo> todoSave(List<MultipartFile> files, Todo todo) throws Exception {
        List<UploadTodo> expectUploadList = new ArrayList<>();
        List<UploadTodo> savedUploadList = new ArrayList<>();

        if (files != null && !files.isEmpty()){
            expectUploadList = uploadTodoHandler.parseFileInfo(files);
            for (UploadTodo uploadTodo : expectUploadList){
                uploadTodo.setTodo(todo);
               savedUploadList.add(uploadTodoRepository.save(uploadTodo));
            }
        }
        // 파일 저장
        return savedUploadList;

    }

    @Transactional(readOnly = true)
    public String FindThumbnailByTodoId(Long id) {
        List<UploadTodo> findAllById = new ArrayList<>();
        findAllById = uploadTodoRepository.findAllByTodoId(id);

        if (findAllById == null || findAllById.isEmpty()) {
            return null;
        }

        return findAllById.get(0).getStoredFileName();
    }

    @Transactional
    public void todoDelete(Long id) {
        List<UploadTodo> savedUploadList = uploadTodoRepository.findAllByTodoId(id);
        if (savedUploadList != null && !savedUploadList.isEmpty()) {
            for (UploadTodo uploadTodo : savedUploadList) {
                uploadTodo.setTodo(null);
                uploadPostsHandler.delete(uploadTodo.getStoredFileName());
                uploadTodoRepository.delete(uploadTodo);
            }
        }
    }


}
