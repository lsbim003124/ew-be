package com.lsbim.wowlsb.enums.character.skill.defensive;

import com.lsbim.wowlsb.enums.character.WowClass;
import lombok.Getter;

@Getter
public enum Mage implements SkillInfo {
    ICE_COLD(WowClass.MAGE, "ALL", "얼음장", 414658),
    ICE_BLOCK(WowClass.MAGE, "ALL", "얼음 방패", 45438),
    MIRROR_IMAGE(WowClass.MAGE, "ALL", "환영 복제", 55342),
    GREATER_INVISIBILITY(WowClass.MAGE, "ALL", "상급 투명화", 110959),
    MASS_BARRIER(WowClass.MAGE, "ALL", "대규모 방벽", 414660),
    ALTER_TIME(WowClass.MAGE, "ALL", "시간 돌리기", 342245),
//    비전
    PRISMATIC_BARRIER(WowClass.MAGE, "Arcane", "오색 방벽", 235450),
//    냉기
    ICE_BARRIER(WowClass.MAGE, "Frost", "얼음 보호막", 11426),
//    화염
    BLAZING_BARRIER(WowClass.MAGE, "Fire", "이글거리는 방벽", 235313);

    private final WowClass className;
    private final String spec;
    private final String skillName;
    private final int skillId;

    Mage(WowClass className, String spec, String skillName, int skillId) {
        this.className = className;
        this.spec = spec;
        this.skillName = skillName;
        this.skillId = skillId;
    }
}
