package com.lsbim.wowlsb.enums.character.skill.defensive;

import com.lsbim.wowlsb.enums.character.WowClass;

public interface SkillInfo {
    WowClass getClassName();
    String getSpec();
    String getSkillName();
    int getSkillId();
}
