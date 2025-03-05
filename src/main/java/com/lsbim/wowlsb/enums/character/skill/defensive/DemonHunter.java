package com.lsbim.wowlsb.enums.character.skill.defensive;

import com.lsbim.wowlsb.enums.character.WowClass;
import lombok.Getter;

@Getter
public enum DemonHunter implements SkillInfo{
    DARKNESS(WowClass.DEMON_HUNTER, "ALL", "어둠", 196718),
    //  파멸
    NETHERWALK(WowClass.DEMON_HUNTER, "Havoc", "황천걸음", 196555),
    BLUR(WowClass.DEMON_HUNTER, "Havoc", "흐릿해지기", 198589),
    //  복수
//    DEMON_SPIKES(WowClass.DEMON_HUNTER, "Vengeance", "악마 쐐기", 203720), 아이콘이 너무 많아 임시로 제거
    METAMORPHOSIS(WowClass.DEMON_HUNTER, "Vengeance", "탈태", 187827),
    FIERY_BRAND(WowClass.DEMON_HUNTER, "Vengeance", "불타는 낙인", 204021),
    FEL_DEVASTATION(WowClass.DEMON_HUNTER, "Vengeance", "지옥 황폐", 212084);

    private final WowClass className;
    private final String spec;
    private final String skillName;
    private final int skillId;

    DemonHunter(WowClass className, String spec, String skillName, int skillId) {
        this.className = className;
        this.spec = spec;
        this.skillName = skillName;
        this.skillId = skillId;
    }
}
