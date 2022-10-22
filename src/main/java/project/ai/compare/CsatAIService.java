package project.ai.compare;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import project.ai.AIResponseDto;
import project.file.DeleteFile;
import project.repository.target.CsatRepository;
import project.target.student.csat.Csat;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CsatAIService implements AIService {

    private final CsatRepository csatRepository;

    // API서버와 통신을 위해 필요한 객체 생성
    RestTemplate restTemplate = new RestTemplate();

    @Value("${flask.csat.url}")
    private String url;

    // 서버에 저장될 비교 이미지 경로.
    @Value("${delete.dir}")
    private String dir;

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

        JsonObject jsonObject = null;
        try {
            AIResponseDto dto = objectMapper.readValue(response.getBody(), AIResponseDto.class);
            List<Map.Entry<String, Integer>> entryList = showResult(dto);
            // Json 직접 생성하여 전달
            Map.Entry<String, Integer> first = entryList.get(0);
            // Map.Entry<String, Integer> second = entryList.get(1);

            // json에서 뽑아낸 값은 idCode값이므로 idCode값을 key로 name 받아옴
            String idCode = first.getKey();
            Optional<Csat> Object = csatRepository.findByIdCode(idCode);
            Csat target = Object.get();

            jsonObject.addProperty("firstName", target.getCsatName());
            jsonObject.addProperty("firstProbability", first.getValue());

            jsonObject.addProperty("castIdCode", target.getCsatIdCode());
            jsonObject.addProperty("csatName", target.getCsatName());
            jsonObject.addProperty("csatSsn", target.getCsatSsn());
            jsonObject.addProperty("csatAddress", target.getCsatAddress());
            jsonObject.addProperty("csatExamDate", target.getCsatExamDate());
            jsonObject.addProperty("csatExamLoc", target.getCsatExamDate());
            jsonObject.addProperty("csatRgstDate", target.getCsatRgstDate());
            jsonObject.addProperty("csatUpdateDate", target.getCsatUpdateDate());

            // 가장높은 확률이 40미만이라면 필적등록이 안 된 사람임을 알림
            if (first.getValue() < 40) {
                jsonObject.addProperty("matchable", false);
            } else {
                jsonObject.addProperty("matchable", true);
            }

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } finally {
            // 사용후파일삭제
            DeleteFile deleteFile = new DeleteFile();
            deleteFile.deleteFile(dir);
        }
        return jsonObject;
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
