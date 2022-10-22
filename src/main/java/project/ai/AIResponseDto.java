package project.ai;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AIResponseDto {

    private List<String> name; // 필적등록된사람 명단
    private List<Integer> probability; // 필적검정시 해당 필적의 주인일 확률
//    private List<String> acc;

//    @Data
//    public class InnerNameDto {
//        private List
//    }
}
