package project.AI;

import lombok.Data;

@Data
public class AIResultJson {

    private String firstName; // 필적의 주인일 확률이 가장 높은 사람의 이름
    private String secondName; // 두번째로 높은 사람의 이름
    private Integer firstProbability; // 필적의 주인일 확률이 가장높을때, 그 확률치
    private Integer secondProbability; // 두번째로 높은 확률
    private Boolean matchable; // 필적이 등록된 사람이라면 true / 아니라면 false
}
