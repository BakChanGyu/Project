package project.ai.learn;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class MissingLearnAIService implements LearnAIService {

    // API서버와 통신을 위해 필요한 객체 생성
    RestTemplate restTemplate = new RestTemplate();

    @Value("${flask.missing.learn.url}")
    private String url;

    // 인공지능 모델에 들어갈 이미지 받아오기
    @Override
    public void requestToFlask() {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<?> requestMessage = new HttpEntity<>(null, httpHeaders);

        // 해당 url로 request요청 전송
        restTemplate.postForEntity(url, requestMessage, String.class);
    }
}
