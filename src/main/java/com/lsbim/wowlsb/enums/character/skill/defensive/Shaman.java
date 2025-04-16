package com.lsbim.wowlsb.enums.character.skill.defensive;

import com.lsbim.wowlsb.enums.character.WowClass;
import lombok.Getter;

@Getter
public enum Shaman implements SkillInfo{
    STONE_BULWARK_TOTEM(WowClass.SHAMAN, "ALL", "돌의 보루 토템", 108270),
    ASTRAL_SHIFT(WowClass.SHAMAN, "ALL", "영혼 이동", 108271),
//    ANCESTRAL_GUIDANCE(WowClass.SHAMAN, "ALL", "고대의 인도", 108281),
    EARTH_ELEMENTAL(WowClass.SHAMAN, "ALL", "대지의 정령", 198103),
    //  복원
    ASCENDANCE(WowClass.SHAMAN, "Restoration", "승천", 114052),
    SPIRIT_LINK_TOTEM(WowClass.SHAMAN, "Restoration", "정신의 고리 토템", 98008),
    HEALING_TIDE_TOTEM(WowClass.SHAMAN, "Restoration", "치유의 해일 토템", 108280);

    private final WowClass className;
    private final String spec;
    private final String skillName;
    private final int skillId;

    Shaman(WowClass className, String spec, String skillName, int skillId) {
        this.className = className;
        this.spec = spec;
        this.skillName = skillName;
        this.skillId = skillId;
    }
}
