package com.lsbim.wowlsb.enums.character.skill.defensive;

import com.lsbim.wowlsb.enums.character.WowClass;
import lombok.Getter;

@Getter
public enum Druid implements SkillInfo{
    BARKSKIN(WowClass.DRUID, "ALL", "나무 껍질", 22812),
//    FRENZIED_REGENERATION(WowClass.DRUID, "ALL", "광포한 재생력", 22842),
    RENEWAL(WowClass.DRUID, "ALL", "소생", 108238),
    //  회복
    IRON_BARK(WowClass.DRUID, "Restoration", "무쇠 껍질", 102342),
    CONVOKE_THE_SPIRITS(WowClass.DRUID, "Restoration", "영혼 소집", 391528),
    //  야성
    SURVIVAL_INSTINCTS_FERAL(WowClass.DRUID, "Feral", "생존 본능", 61336),
    //  수호
    SURVIVAL_INSTINCTS_GUARDIAN(WowClass.DRUID, "Guardian", "생존 본능", 61336),
    INCARNATION_URSOC(WowClass.DRUID, "Guardian", "화신: 우르속의 수호자", 102558),
    RAGE_OF_THE_SLEEPER(WowClass.DRUID, "Guardian", "잠자는 자의 분노", 200851),
    // 조화
    BEAR_FORM_BOOMY(WowClass.DRUID, "Balance", "곰 변신", 5487);

    private final WowClass className;
    private final String spec;
    private final String skillName;
    private final int skillId;

    Druid(WowClass className, String spec, String skillName, int skillId) {
        this.className = className;
        this.spec = spec;
        this.skillName = skillName;
        this.skillId = skillId;
    }
}
