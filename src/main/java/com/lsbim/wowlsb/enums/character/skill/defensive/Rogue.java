package com.lsbim.wowlsb.enums.character.skill.defensive;

import com.lsbim.wowlsb.enums.character.WowClass;
import lombok.Getter;

@Getter
public enum Rogue implements SkillInfo{
    FEINT(WowClass.ROGUE, "ALL", "교란", 1966),
    CLOAK_OF_SHADOWS(WowClass.ROGUE, "ALL", "그림자 망토", 31224),
    EVASION(WowClass.ROGUE, "ALL", "회피", 5277),
    CRIMSON_VIAL(WowClass.ROGUE, "ALL", "진홍색 약병", 185311);
    private final WowClass className;
    private final String spec;
    private final String skillName;
    private final int skillId;

    Rogue(WowClass className, String spec, String skillName, int skillId) {
        this.className = className;
        this.spec = spec;
        this.skillName = skillName;
        this.skillId = skillId;
    }
}
