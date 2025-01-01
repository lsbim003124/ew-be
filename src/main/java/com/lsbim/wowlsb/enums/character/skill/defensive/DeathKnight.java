package com.lsbim.wowlsb.enums.character.skill.defensive;

import com.lsbim.wowlsb.enums.character.WowClass;
import lombok.Getter;

@Getter
public enum DeathKnight implements SkillInfo{
    ANTI_MAGIC_SHELL(WowClass.DEATH_KNIGHT, "ALL", "대마법 보호막", 48707),
    ICEBOUND_FORTITUDE(WowClass.DEATH_KNIGHT, "ALL", "얼음같은 인내력", 48792),
    ANTI_MAGIC_ZONE(WowClass.DEATH_KNIGHT, "ALL", "대마법 지대", 51052),
    LICHBORNE(WowClass.DEATH_KNIGHT, "ALL", "리치의 혼", 49039),
    //  혈기
    DANCING_RUNE_WEAPON(WowClass.DEATH_KNIGHT, "Blood", "춤추는 룬 무기", 49028),
    VAMPIRIC_BLOOD(WowClass.DEATH_KNIGHT, "Blood", "흡혈", 55233),
    RUNE_TAP(WowClass.DEATH_KNIGHT, "Blood", "룬 전환", 194679);

    private final WowClass className;
    private final String spec;
    private final String skillName;
    private final int skillId;

    DeathKnight(WowClass className, String spec, String skillName, int skillId) {
        this.className = className;
        this.spec = spec;
        this.skillName = skillName;
        this.skillId = skillId;
    }
}
