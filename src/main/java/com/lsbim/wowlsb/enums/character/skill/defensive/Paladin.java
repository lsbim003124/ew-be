package com.lsbim.wowlsb.enums.character.skill.defensive;

import com.lsbim.wowlsb.enums.character.WowClass;
import lombok.Getter;

@Getter
public enum Paladin implements SkillInfo{
    DIVINE_SHIELD(WowClass.PALADIN, "ALL", "천상의 보호막", 642),
//    BLESSING_OF_PROTECTION(WowClass.PALADIN, "ALL", "보호의 축복", 1022),
//    BLESSING_OF_SACRIFICE(WowClass.PALADIN, "ALL", "희생의 축복", 6940),
//    BLESSING_OF_SPELLWARDING(WowClass.PALADIN, "ALL", "주문 수호의 축복", 204018),
//    LAY_ON_HANDS(WowClass.PALADIN, "ALL", "신의 축복", 471195),
    //  징벌
    SHIELD_OF_VENGEANCE(WowClass.PALADIN, "Retribution", "복수의 방패", 184662),
    DIVINE_PROTECTION_RETRIBUTION(WowClass.PALADIN, "Retribution", "신의 가호", 403876),
    //  신성
    AURA_MASTERY(WowClass.PALADIN, "Holy", "오라 숙련", 31821),
    AVENGING_WRATH(WowClass.PALADIN, "Holy", "응징의 격노", 31884),
    AVENGING_CRUSADER(WowClass.PALADIN, "Holy", "응징의 성전사", 394088),
    BEACON_OF_VIRTUE(WowClass.PALADIN, "Holy", "고결의 봉화", 200025),
    DIVINE_PROTECTION_HOLY(WowClass.PALADIN, "Holy", "신의 가호", 498),
    //  보호
    ARDENT_DEFENDER(WowClass.PALADIN, "Protection", "헌신적인 수호자", 31850),
    EYE_OF_TYR(WowClass.PALADIN, "Protection", "티르의 눈", 387174),
    GUARDIAN_OF_ANCIENT_KINGS(WowClass.PALADIN, "Protection", "고대 왕의 수호자", 86659);

    private final WowClass className;
    private final String spec;
    private final String skillName;
    private final int skillId;

    Paladin(WowClass className, String spec, String skillName, int skillId) {
        this.className = className;
        this.spec = spec;
        this.skillName = skillName;
        this.skillId = skillId;
    }
}
