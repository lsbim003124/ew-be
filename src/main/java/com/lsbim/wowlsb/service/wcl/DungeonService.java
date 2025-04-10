package com.lsbim.wowlsb.service.wcl;

import com.lsbim.wowlsb.dto.mplus.MplusFightsDTO;
import com.lsbim.wowlsb.enums.dungeons.DungeonBosses;
import com.lsbim.wowlsb.enums.dungeons.Dungeons;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class DungeonService {

    //    보스의 npcId(ActorId)만 리턴
    public List<Integer> findBossIdByList(List<MplusFightsDTO.Pull.EnemyNpc> list, int dungeonId) {

        List<Integer> arr = new ArrayList<>();

//        던전Id가 일치하는 Enum 던전정보 가져오기
        Dungeons dungeon = Arrays.stream(Dungeons.values())
                .filter(d -> d.getId() == dungeonId)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Invalid dungeon id: " + dungeonId
                ));

        for (MplusFightsDTO.Pull.EnemyNpc npc : list) {
            int gameId = npc.getGameId();

            // 조건에 맞는 보스를 찾기
            // pull 안에 있는 npcId 목록을 전부 조회하여 일치하는 보스 정보 전부 가져오기
            Optional<DungeonBosses> optionalBoss = Arrays.stream(DungeonBosses.values())
                    .filter(b -> b.getDungeons().equals(dungeon))
                    .filter(b -> b.getGameId() == gameId)
                    .findFirst();

            // 보스를 찾으면 gameId를 사용, isPresent -> true = 옵셔널 안에 값이 존재 / false = 옵셔널이 비어있음
            if (optionalBoss.isPresent()) {
                arr.add(npc.getId());
            }
        }

        return arr;
    }
}
