package project.ai.learn;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;

@Service
@Slf4j
@RequiredArgsConstructor
public class ToeicLearnAIService implements LearnAIService {

    // API서버와 통신을 위해 필요한 객체 생성
    RestTemplate restTemplate = new RestTemplate();

    @Value("${flask.toeic.learn.url}")
    private String url;

    @Value("${toeic.learn.new.dir}")
    private String path;

    @Value("${toeic.imgCap.url}")
    private String imgCapUrl;

    // 인공지능 모델에 들어갈 이미지 받아오기
    @Override
    public String requestToFlask() {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<?> requestMessage = new HttpEntity<>(null, httpHeaders);

        // 서버내부에서 학습시작
        // 이때 내부 폴더 갯수가 10이상 아니면 학습불가
        long count = countFolder();
        log.info("폴더 갯수 ={}", count);
        if (count < 10) {
            String error_code = "10인분의 이상의 필적을 넣어주세요. 현재: " + count + "인분 입니다.";
            return error_code;
        }

        // 해당 url로 request요청 전송
        ResponseEntity<String> stringResponseEntity = restTemplate.postForEntity(url, requestMessage, String.class);
        String body = stringResponseEntity.getBody();
        return body;
    }

    @Override
    public void imgCap() {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<?> requestMessage = new HttpEntity<>(null, httpHeaders);

        // 해당 url로 request요청 전송
        restTemplate.postForEntity(imgCapUrl, requestMessage, String.class);
    }

    private long countFolder() {
        long length = 0;

        for (File info : new File(path).listFiles()) {
            if (info.isDirectory()) {
                length++;
            }
        }
        return length;
    }
}
