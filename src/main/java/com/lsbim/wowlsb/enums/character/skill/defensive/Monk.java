package com.lsbim.wowlsb.enums.character.skill.defensive;

import com.lsbim.wowlsb.enums.character.WowClass;
import lombok.Getter;

@Getter
public enum Monk implements SkillInfo{
    FORTIFYING_BREW(WowClass.MONK, "ALL", "강화주", 115203),
    DIFFUSE_MAGIC(WowClass.MONK, "ALL", "마법 해소", 122783),
    //  풍운
    TOUCH_OF_KARMA(WowClass.MONK, "Windwalker", "업보의 손아귀", 122470),
    //  양조
    DAMPEN_HARM(WowClass.MONK, "Brewmaster", "해악 감퇴", 122278),
//    INVOKE_NIUZAO(WowClass.MONK, "Brewmaster", "흑우 니우짜오의 원령", 132578),
    ZEN_MEDITATION(WowClass.MONK, "Brewmaster", "명상", 115176),
//    CELESTIAL_BREW(WowClass.MONK, "Brewmaster", "천신주", 322507),
    //  운무
//    INVOKE_CHI_JI(WowClass.MONK, "Mistweaver", "주학 츠지의 원령", 325197),
//    INVOKE_YU_LON(WowClass.MONK, "Mistweaver", "옥룡 위론의 원령", 322118),
    RESTORAL(WowClass.MONK, "Mistweaver", "회복", 388615),
    REVIVAL(WowClass.MONK, "Mistweaver", "재활", 115310),
    LIFE_COCOON(WowClass.MONK, "Mistweaver", "기의 고치", 116849);

    private final WowClass className;
    private final String spec;
    private final String skillName;
    private final int skillId;

    Monk(WowClass className, String spec, String skillName, int skillId) {
        this.className = className;
        this.spec = spec;
        this.skillName = skillName;
        this.skillId = skillId;
    }
}
