package project.AI;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import project.file.DeleteFile;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("/api")
public class AIController {

    // API서버와 통신을 위해 필요한 객체 생성
    RestTemplate restTemplate = new RestTemplate();

    @Value("${flask.url}")
    private String url;
    @Value("${delete.dir}")
    private String dir;

    // 인공지능 모델에 들어갈 이미지 받아오기
    @GetMapping("/loadAI")
    public JsonObject requestToFlask () throws JsonProcessingException {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<?> requestMessage = new HttpEntity<>(null, httpHeaders);

        HttpEntity<String> response = restTemplate.postForEntity(url, requestMessage, String.class);

        log.info("response ={}", response);

        // response 파싱
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT, true);

        JsonObject jsonObject = null;
        try {
            AIResponseDto dto = objectMapper.readValue(response.getBody(), AIResponseDto.class);
            log.info("dto={}", dto.toString());

            List<Map.Entry<String, Integer>> entryList = showResult(dto);

            // Json 직접 생성하여 전달
            Map.Entry<String, Integer> first = entryList.get(0);
            Map.Entry<String, Integer> second = entryList.get(1);

            jsonObject = new JsonObject();

            jsonObject.addProperty("firstName", first.getKey());
            jsonObject.addProperty("firstProbability", first.getValue());
            jsonObject.addProperty("secondName", second.getKey());
            jsonObject.addProperty("secondProbability", second.getValue());

            // 가장높은 확률이 40미만이라면 필적등록이 안 된 사람임을 알림
            if (first.getValue() < 40) {
                jsonObject.addProperty("matchable", false);
            } else {
                jsonObject.addProperty("matchable", true);
            }

            log.info("jsonObject={} ", jsonObject);

            /**
             * TODO list
             * 1. json형태로 결과 가져온 후 가장높은확률값 혹은 두번쨰까지만 출력해주기
             * 2. 필적과 일치하지 않는다 (ex 김흥돌과 일치할확률 40% 미만일경우 필적정보가 없음을 출력 등)
             * 3. 한번 필적감정 후 target 폴더에 올렸던 이미지 파일 삭제
             *
             * 수정사항 : 파일명이 제각각임 bsh_cqz_xeh_ 등.. 그래서 파싱힘듬 이 파일들에는
             * MissingMember(추후명칭변경예정)의 필적이 들어가므로 MissingMember의 이름이 ??
             * >>> json 직접 생성해서 해결
             */
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

        log.info("가져온 이름 ={}", name);
        log.info("가져온 확률 ={}", probability);

        Map<String, Integer> map = new HashMap<>();

        for (int i = 0; i < name.size(); i++) {
            map.put(name.get(i), probability.get(i));
        }
        log.info("map ={}", map);

        List<Map.Entry<String, Integer>> entryList = new ArrayList<>(map.entrySet());
        entryList.sort((o1, o2) -> o2.getValue() - o1.getValue());

        log.info("entryList ={}", entryList);

        return entryList;
    }
}
