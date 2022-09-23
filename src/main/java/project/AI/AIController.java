package project.AI;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AIController {

    // 인공지능 모델에 들어갈 이미지 받아오기
    @PostMapping("/search")
    public String search () {
        return null;
    }
}
