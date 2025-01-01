package com.lsbim.wowlsb.enums.character.skill.defensive;

import com.lsbim.wowlsb.enums.character.WowClass;
import lombok.Getter;

@Getter
public enum Warlock implements SkillInfo{
    UNENDING_RESOLVE(WowClass.WARLOCK, "ALL", "영원한 결의", 104773),
    DEMONIC_HEALTHSTONE(WowClass.WARLOCK, "ALL", "악마의 생명석", 452930),
    DARK_PACT(WowClass.WARLOCK, "ALL", "어둠의 서약", 108416);
    private final WowClass className;
    private final String spec;
    private final String skillName;
    private final int skillId;

    Warlock(WowClass className, String spec, String skillName, int skillId) {
        this.className = className;
        this.spec = spec;
        this.skillName = skillName;
        this.skillId = skillId;
    }
}
