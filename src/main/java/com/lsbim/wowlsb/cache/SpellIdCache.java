package com.lsbim.wowlsb.cache;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.lsbim.wowlsb.dto.mplus.MplusTimelineDataDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

@Component
@Log4j2
public class SpellIdCache {
    /*
    주문 이미지를 가져오기 위한 Set.
    기존 Set에 포함되지 않아 새로 추가된 spellId을 이용해
    WCL로부터 이미지 생성 -> GCS 버킷에 삽입
    */

    private final Set<Integer> spellIdSet = new HashSet<>();

    public Set<Integer> addMissingIds(Set<Integer> ids) {

        // spellIdSet이 ids를 전부 포함하지 않는가? or 비어있으면 초기데이터 전부 삽입
        if (spellIdSet.isEmpty()) {
            // 비어있으면 초기데이터 전부 삽입
            spellIdSet.addAll(ids);
            log.info("add init spell ids: {}", ids.size());

            return Collections.emptySet();

        } else {
            // 기존 데이터가 있을 때, 새로 추가될 ID만 모아서 반환
            Set<Integer> newIds = new HashSet<>();
            for (Integer id : ids) {
                if (!spellIdSet.contains(id)) {
                    newIds.add(id);
                }
            }
            spellIdSet.addAll(newIds);
            log.info("add spell ids: {}", newIds.size());

            return newIds;
        }
    }

}
