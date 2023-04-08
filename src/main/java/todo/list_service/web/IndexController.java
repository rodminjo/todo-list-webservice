package todo.list_service.web;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import todo.list_service.config.auth.LoginUser;
import todo.list_service.config.auth.dto.SessionUser;
import todo.list_service.service.user.UserService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@Controller
@RequiredArgsConstructor
public class IndexController {

    private final UserService userService;

    @GetMapping("/")
    public String index(Model model, @LoginUser SessionUser user) {
        if (user!= null) {
            model.addAttribute("userName", user.getName());
            return "main";
        }
        return "index";
    }

    @GetMapping("/main")
    public String main(Model model, @LoginUser SessionUser user) {
        if (user!= null) {
            model.addAttribute("userName", user.getName());
        }
        return "main";
    }

    @GetMapping("/mypage")
    public String myPage(Model model, @LoginUser SessionUser user) {
        if (user!= null) {
            model.addAttribute("userName", user.getName());
            model.addAttribute("userEmail", user.getEmail());
            model.addAttribute("userPicture", user.getPicture());
            model.addAttribute("userNickName", userService.findById(user.getId()).getNickName());
        }
        return "mypage";
    }


    @GetMapping("/display")
    public ResponseEntity<Resource> GetProfileImage(@RequestParam("file") String fileName) {
        String absolutePath = new File("").getAbsolutePath() + File.separator+ File.separator;
        Resource resource = new FileSystemResource(absolutePath + fileName);
        if(!resource.exists())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        HttpHeaders header = new HttpHeaders();
        try{
            Path filePath = Paths.get(absolutePath + fileName);
            header.add("Content-type", Files.probeContentType(filePath));
        }catch(IOException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(resource, header, HttpStatus.OK);
    }




}
