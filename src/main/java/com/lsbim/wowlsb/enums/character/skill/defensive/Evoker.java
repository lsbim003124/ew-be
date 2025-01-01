package com.lsbim.wowlsb.enums.character.skill.defensive;

import com.lsbim.wowlsb.enums.character.WowClass;
import lombok.Getter;

@Getter
public enum Evoker implements SkillInfo{
    OBSIDIAN_SCALES(WowClass.EVOKER, "ALL", "흑요석 비늘", 363916),
    ZEPHYR(WowClass.EVOKER, "ALL", "미풍", 374227),
    RESCUE(WowClass.EVOKER, "ALL", "구출", 370665),
    RENEWING_BLAZE(WowClass.EVOKER, "ALL", "소생의 불길", 374348),
    //    증강
//    PRISMATIC_BARRIER(WowClass.EVOKER, "Augmentation", "오색 방벽", 235450),
    //    황폐
//    ICE_BARRIER(WowClass.EVOKER, "Devastation", "얼음 보호막", 11426),
    //    보존
    STASIS_START(WowClass.EVOKER, "Preservation", "정지장", 370537), // 저장
    STASIS_END(WowClass.EVOKER, "Preservation", "정지장", 370564), // 배출
    TIME_DILATION(WowClass.EVOKER, "Preservation", "시간 팽창", 357170),
    REWIND(WowClass.EVOKER, "Preservation", "되돌리기", 363534);

    private final WowClass className;
    private final String spec;
    private final String skillName;
    private final int skillId;

    Evoker(WowClass className, String spec, String skillName, int skillId) {
        this.className = className;
        this.spec = spec;
        this.skillName = skillName;
        this.skillId = skillId;
    }
}
