package todo.list_service.service.todo;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import todo.list_service.domain.todo.Todo;
import todo.list_service.domain.todo.TodoRepository;
import todo.list_service.domain.uploadTodo.UploadTodo;
import todo.list_service.domain.user.User;
import todo.list_service.service.upload.UploadService;
import todo.list_service.service.user.UserService;
import todo.list_service.web.dto.todo.*;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TodoService {
    private final TodoRepository todoRepository;
    private final UserService userService;
    private final UploadService uploadService;


    @Transactional
    public Long save(TodoSaveRequestDto requestDto, List<MultipartFile> files) throws Exception {
        // User 불러오기
        User user = userService.findById(requestDto.getUserId());

        // 저장할 Todo 생성 후 저장
        Todo todo = Todo.builder().user(user).title(requestDto.getTitle()).content(requestDto.getContent()).todoDate(requestDto.getTodoDate()).build();
        Todo savedTodo = todoRepository.save(todo);

        //file uploadTodo에 저장
        List<UploadTodo> uploadTodoList = uploadService.todoSave(files,savedTodo);


        return savedTodo.getId();
    }

    @Transactional
    public Long update(TodoUpdateRequestDto requestDto, List<MultipartFile> files) throws Exception {
        // todo 객체 불러오기
        Todo savedTodo = todoRepository.findById(requestDto.getTodoId())
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물이 없습니다"));
        //todo 정보 업데이트
        savedTodo.update(requestDto.getTitle(),requestDto.getContent(), requestDto.getTodoDate());


        if (files == null || files.isEmpty()) {
            // 요청 파일이 없을때 전부 삭제
            uploadService.todoDelete(savedTodo.getId());
        }else {
            // 요청파일이 있을때 삭제
            uploadService.todoDelete(savedTodo.getId());

            //새로운 요청으로 저장
            List<UploadTodo> uploadTodoList = uploadService.todoSave(files, savedTodo);
        }

        return savedTodo.getId();
    }


    @Transactional(readOnly = true)
    public List<TodosListResponseDto> findByUserIdAndTakeAllDesc(String date, Long id){
        List<TodosListResponseDto> listDto = todoRepository.findByUserIdAndAllDesc(date, id).stream()
                .map(todos -> new TodosListResponseDto(todos)).collect(Collectors.toList());


        for (TodosListResponseDto list : listDto) {
            String thumbnailStoredFileName = uploadService.FindThumbnailByTodoId(list.getId());

            if (thumbnailStoredFileName !=null){
                list.addStoredFileName(thumbnailStoredFileName);
            }
        }

        return listDto;
    }


    @Transactional(readOnly = true)
    public TodoResponseDto findByIdToShow(Long id){
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id: " + id));

        return new TodoResponseDto(todo);
    }



    @Transactional
    public String delete(Long id) {
        //Todo 객체 찾아오기
        Todo todo = todoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시물이 없습니다"));
        // 날짜 데이터 반환을 위한 저장
        String deleteTodoDate = todo.getTodoDate();

        // 삭제
        uploadService.todoDelete(todo.getId());
        todoRepository.delete(todo);

        return deleteTodoDate;
    }

    public Long importanceChange(TodoRequestimportanceCheckedDto requestImportanceDto) {
        Todo todo = todoRepository.findById(requestImportanceDto.getId()).orElseThrow(() -> new IllegalArgumentException("해당 게시물이 없습니다"));
        todo.importanceChange(requestImportanceDto.getImportance());

        return todo.getId();
    }
    public Long checkedChange(TodoRequestimportanceCheckedDto requestImportanceDto) {
        Todo todo = todoRepository.findById(requestImportanceDto.getId()).orElseThrow(() -> new IllegalArgumentException("해당 게시물이 없습니다"));
        todo.checkedChange(requestImportanceDto.getChecked());

        return todo.getId();
    }


}
