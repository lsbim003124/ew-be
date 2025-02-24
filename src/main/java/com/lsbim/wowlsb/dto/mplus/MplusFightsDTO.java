package com.lsbim.wowlsb.dto.mplus;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.lsbim.wowlsb.enums.dungeons.DungeonBosses;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MplusFightsDTO {

    private List<Pull> pulls = new ArrayList<>();
    private long pullsStartTime;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Pull {

//        private String name;
        private String krBossName;
        private int bossGameId;
//        private Boolean kill;
        private long startTime;
        private long endTime;
        private long combatTime;
        private List<EnemyNpc> enemyNpcs;
        private Events events;

        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class EnemyNpc {
            private int id;
            private int gameId;
        }

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Events {
            @JsonProperty("enemyCasts")
            private List<MplusEnemyCastsDTO> enemyCastsDTO;
            @JsonProperty("playerCasts")
            private List<List<MplusPlayerCastsDTO>> playerDefensiveCastsDTO;
            @JsonProperty("playerTakenDef")
            private List<List<MplusPlayerCastsDTO>> playerTakenDefensiveDTO;
        }
    }

    public static MplusFightsDTO fromArrayNode(ArrayNode node, long startTime) {
        MplusFightsDTO dto = new MplusFightsDTO();
        dto.setPullsStartTime(startTime);

        for (int i = 0; i < node.size(); i++) {
            MplusFightsDTO.Pull pull = new MplusFightsDTO.Pull();
            JsonNode mini = node.get(i);

//            pull.setName(mini.path("name").asText());
//            pull.setKill(mini.path("kill").asBoolean());
            pull.setStartTime(mini.path("startTime").asLong());
            pull.setEndTime(mini.path("endTime").asLong());
            pull.setCombatTime(pull.getEndTime() - pull.getStartTime());

            if (i == 0) {
//                리액트에서 첫번째 유저의 보스이름만 출력하기 때문에 0번 인덱스만 한글 보스명 가져오기
                String krBossName = findKrBossNameByEnum(mini.path("name").asText());
                pull.setKrBossName(krBossName);
            }


            ArrayNode enemyNpcsNodes = (ArrayNode) mini.path("enemyNPCs");
            if (enemyNpcsNodes != null && enemyNpcsNodes.size() > 0) {
                List<Pull.EnemyNpc> enemyNpcs = new ArrayList<>();
                for (JsonNode enemyNpcsNode : enemyNpcsNodes) {
                    MplusFightsDTO.Pull.EnemyNpc enemyNpc = new MplusFightsDTO.Pull.EnemyNpc();
                    enemyNpc.setId(enemyNpcsNode.path("id").asInt());
                    enemyNpc.setGameId(enemyNpcsNode.path("gameID").asInt());
                    enemyNpcs.add(enemyNpc);
                }
                pull.setEnemyNpcs(enemyNpcs);
            }

            dto.getPulls().add(pull);
        }

        return dto;
    }

    //    다른 곳에서도 쓰이면 dungeonService로 옮기기
    private static String findKrBossNameByEnum(String bossName) {


        // 조건에 맞는 보스를 찾기
        return Arrays.stream(DungeonBosses.values())
                .filter(b -> b.getBossName().equals(bossName))
                .findFirst()
                .map(DungeonBosses::getKrBossName) // 변환한다는 의미 맵핑
                .orElse("보스");

    }
}
