package com.lsbim.wowlsb.enums.character.skill.defensive;

import com.lsbim.wowlsb.enums.character.WowClass;
import lombok.Getter;

@Getter
public enum Priest implements SkillInfo {
    DESPERATE_PRATER(WowClass.PRIEST, "ALL", "구원의 기도", 19236),
    //    수양
//    PAIN_SUPPRESSION(WowClass.PRIEST, "Discipline", "고통 억제", 33206),
    POWER_WORD_BARRIER(WowClass.PRIEST, "Discipline", "신의 권능: 방벽", 62618),
//    LUMINOUS_BARRIER(WowClass.PRIEST, "Discipline", "영롱한 방벽", 271466),
    RAPTURE(WowClass.PRIEST, "Discipline", "환희", 47536),
//    EVANGELISM(WowClass.PRIEST, "Discipline", "사도", 246287),
//    SHADOWFIEND(WowClass.PRIEST, "Discipline", "어둠의 마귀", 34433),
//    MINDBENDER(WowClass.PRIEST, "Discipline", "환각의 마귀", 123040),
    VOIDWRAITH(WowClass.PRIEST, "Discipline", "공허의 마귀", 451235),
//    ULTIMATE_PENITENCE(WowClass.PRIEST, "Discipline", "궁극의 참회", 421453),
    MIND_BLAST(WowClass.PRIEST, "Discipline", "정신 분열", 8092),
    //    신성
    GUARDIAN_SPIRIT(WowClass.PRIEST, "Holy", "신성 수호", 47788),
    APOTHEOSIS(WowClass.PRIEST, "Holy", "절정", 200183),
    HOLY_WORD_SALVATION(WowClass.PRIEST, "Holy", "빛의 권능: 구원", 265202),
    DIVINE_HYMN(WowClass.PRIEST, "Holy", "천상의 찬가", 64843),
    //    암흑
    DISPERSION(WowClass.PRIEST, "Shadow", "분산", 47585),
    VAMPIRIC_EMBRACE(WowClass.PRIEST, "Shadow", "흡혈의 선물", 15286);

    private final WowClass className;
    private final String spec;
    private final String skillName;
    private final int skillId;

    Priest(WowClass className, String spec, String skillName, int skillId) {
        this.className = className;
        this.spec = spec;
        this.skillName = skillName;
        this.skillId = skillId;
    }
}
