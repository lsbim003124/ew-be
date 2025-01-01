package com.lsbim.wowlsb.dto.mplus;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.lsbim.wowlsb.dto.PlayerSkillInfoDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)  // 매핑되지 않는 JSON 속성 무시
public class MplusRankingsDTO {

    private List<Ranking> rankings = new ArrayList<>();
    private List<PlayerSkillInfoDTO> playerSkillInfo = new ArrayList<>();
    private Set<Integer> bossSkillInfo = new HashSet<>();
    private Set<Integer> takenBuffInfo = new HashSet<>();

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Ranking { // static 내부클래스 = 외부(DTO)클래스와 독립적. 외부클래스의 인스턴스 없이 바로 생성 가능
        private String name;
        @JsonProperty("class") // class는 예약어라서 className으로 바꾸나, 필드명은 class로
        private String className;
        private String spec;
        private double amount; // 총딜
        private int hardModeLevel; // 단수
        private long duration; // 쐐기 진행 시간 (밀리초)
        private Report report;
        private List<Integer> affixes;
        private String medal;
        private double score;
        private Server server;
        @JsonProperty("fights")
        private MplusFightsDTO mplusFightsDTO;

        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Report {
            private String code;
            private int fightID;
        }

        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Server {
            private String name;
            private String region;
        }
    }

    //    객체 생성을 위한 팩토리 메서드 패턴
    public static MplusRankingsDTO fromArrayNode(ArrayNode node) {
        MplusRankingsDTO dto = new MplusRankingsDTO();
        // 최대 10개
        int max = node.size() > 10 ? 10 : node.size();

        for (int i = 0; i < max; i++) {
            JsonNode mini = node.get(i);
            Ranking ranking = new Ranking();

            ranking.setName(mini.path("name").asText());
            ranking.setClassName(mini.path("class").asText());
            ranking.setSpec(mini.path("spec").asText());
            ranking.setAmount(mini.path("amount").asDouble());
            ranking.setHardModeLevel(mini.path("hardModeLevel").asInt());
            ranking.setDuration(mini.path("duration").asInt());
            ranking.setMedal(mini.path("medal").asText());
            ranking.setScore(mini.path("score").asDouble());

            JsonNode reportNode = mini.path("report");
            if (reportNode.isObject()) {
                Ranking.Report report = new Ranking.Report();
                report.setCode(reportNode.path("code").asText());
                report.setFightID(reportNode.path("fightID").asInt());
                ranking.setReport(report);
            }

            ArrayNode affixesNode = (ArrayNode) mini.path("affixes");
            if (affixesNode != null) {
                List<Integer> affixes = new ArrayList<>();
                for (JsonNode affixNode : affixesNode) {
                    affixes.add(affixNode.asInt());
                }
                ranking.setAffixes(affixes);
            }

            JsonNode serverNode = mini.path("server");
            if (serverNode != null) {
                Ranking.Server server = new Ranking.Server();
                server.setName(serverNode.path("name").asText());
                server.setRegion(serverNode.path("region").asText());
                ranking.setServer(server);
            }

            dto.getRankings().add(ranking);
        }
        return dto;
    }

}
