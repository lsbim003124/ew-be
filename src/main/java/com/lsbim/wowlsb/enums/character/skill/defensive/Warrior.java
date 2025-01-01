package com.lsbim.wowlsb.enums.character.skill.defensive;

import com.lsbim.wowlsb.enums.character.WowClass;
import lombok.Getter;

@Getter
public enum Warrior implements SkillInfo{
    RALLYING_CRY(WowClass.WARRIOR, "ALL", "재집결의 함성", 97462),
    IMPENDING_VICTORY(WowClass.WARRIOR, "ALL", "예견된 승리", 202168),
    //  무기
    DIE_BY_THE_SWORD(WowClass.WARRIOR, "Arms", "투사의 혼", 118038),
    //  분노
    ENRAGED_REGENERATION(WowClass.WARRIOR, "Fury", "격노의 재생력", 184364),
    //  방어
    DEMORALIZING_SHOUT(WowClass.WARRIOR, "Protection", "사기의 외침", 1160),
    SHIELD_WALL(WowClass.WARRIOR, "Protection", "방패의 벽", 871),
    LAST_STAND(WowClass.WARRIOR, "Protection", "최후의 저항", 12975);

    private final WowClass className;
    private final String spec;
    private final String skillName;
    private final int skillId;

    Warrior(WowClass className, String spec, String skillName, int skillId) {
        this.className = className;
        this.spec = spec;
        this.skillName = skillName;
        this.skillId = skillId;
    }
}
