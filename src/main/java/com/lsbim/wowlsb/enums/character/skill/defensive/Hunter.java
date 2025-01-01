package com.lsbim.wowlsb.enums.character.skill.defensive;

import com.lsbim.wowlsb.enums.character.WowClass;
import lombok.Getter;

@Getter
public enum Hunter implements SkillInfo {
    ASPECT_OF_THE_TURTLE(WowClass.HUNTER, "ALL", "거북의 상", 186265),
    EXHILARATION(WowClass.HUNTER, "ALL", "활기", 109304),
    SURVIVAL_OF_THE_FITTEST(WowClass.HUNTER, "ALL", "적자생존", 264735);

    private final WowClass className;
    private final String spec;
    private final String skillName;
    private final int skillId;

    Hunter(WowClass className, String spec, String skillName, int skillId) {
        this.className = className;
        this.spec = spec;
        this.skillName = skillName;
        this.skillId = skillId;
    }
}
