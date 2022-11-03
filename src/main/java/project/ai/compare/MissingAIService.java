package project.ai.compare;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import project.ai.AIResponseDto;
import project.repository.target.MissingRepository;
import project.target.missing.Missing;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class MissingAIService implements AIService {

    private final MissingRepository missingRepository;

    // API서버와 통신을 위해 필요한 객체 생성
    RestTemplate restTemplate = new RestTemplate();

    // 이미지 캡쳐 후 필적 비교하는 api 호출
    @Value("${flask.missing.url}")
    private String url;

    // 엑셀파일에서 데이터 추출하는 api 호출
    @Value("${flask.missing.excel.url}")
    private String excelUrl;

    // 이미지캡쳐 api 호출 (비교)
    @Value("${imgCap.url}")
    private String imgCapUrl;

    // 인공지능 모델에 들어갈 이미지 받아오기
    @Override
    public JsonObject requestToFlask() {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<?> requestMessage = new HttpEntity<>(null, httpHeaders);

        // 해당 url로 request요청 전송
        HttpEntity<String> response = restTemplate.postForEntity(url, requestMessage, String.class);

        // response 파싱
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT, true);

        JsonObject jsonObject = new JsonObject();
        try {
            AIResponseDto dto = objectMapper.readValue(response.getBody(), AIResponseDto.class);
            List<Map.Entry<String, Integer>> entryList = showResult(dto);
            log.info("dto ={}", dto);

            // Json 직접 생성하여 전달
            Map.Entry<String, Integer> first = entryList.get(0);
            // Map.Entry<String, Integer> second = entryList.get(1);
            // json에서 뽑아낸 값은 idCode값이므로 idCode값을 key로 name 받아옴
            String idCode = first.getKey();
            log.info("idCode={}", idCode);
            Optional<Missing> Object = missingRepository.findByIdCode(idCode);
            log.info("idCode를 key로 받아온 target ={}", Object);
            Missing target = Object.get();

            jsonObject.addProperty("firstName", target.getMissingName());
            jsonObject.addProperty("firstProbability", first.getValue());

            jsonObject.addProperty("missingIdCode", target.getMissingIdCode());
            jsonObject.addProperty("missingName", target.getMissingName());
            jsonObject.addProperty("missingSsn", target.getMissingSsn());
            jsonObject.addProperty("missingAddress", target.getMissingAddress());
            jsonObject.addProperty("missingDate", target.getMissingDate());
            jsonObject.addProperty("protectorName", target.getProtectorName());
            jsonObject.addProperty("protectorTel", target.getProtectorTel());
            jsonObject.addProperty("missingRgstDate", target.getMissingRgstDate());
            jsonObject.addProperty("missingUpdateDate", target.getMissingUpdateDate());

            // 가장높은 확률이 80미만이라면 필적등록이 안 된 사람임을 알림
            if (first.getValue() < 80) {
                jsonObject.addProperty("matchable", "false");
            } else {
                jsonObject.addProperty("matchable", "true");
            }

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    @Override
    public void imgCap() {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<?> requestMessage = new HttpEntity<>(null, httpHeaders);

        // 해당 url로 request요청 전송
        restTemplate.postForEntity(imgCapUrl, requestMessage, String.class);
    }

    @Override
    public void saveExcel() {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<?> requestMessage = new HttpEntity<>(null, httpHeaders);

        // 해당 url로 request요청 전송
        restTemplate.postForEntity(excelUrl, requestMessage, String.class);
    }

    private List<Map.Entry<String, Integer>> showResult(AIResponseDto dto) {
        List<String> name = dto.getName();
        List<Integer> probability = dto.getProbability();

        Map<String, Integer> map = new HashMap<>();

        for (int i = 0; i < name.size(); i++) {
            map.put(name.get(i), probability.get(i));
        }

        List<Map.Entry<String, Integer>> entryList = new ArrayList<>(map.entrySet());
        entryList.sort((o1, o2) -> o2.getValue() - o1.getValue());

        return entryList;
    }
}
