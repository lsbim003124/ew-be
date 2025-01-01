package com.lsbim.wowlsb.enums.character;

import lombok.Getter;

@Getter
public enum Spec {

    // 전사 - WARRIOR
    ARMS(WowClass.WARRIOR, "Arms", Role.DPS),
    FURY(WowClass.WARRIOR, "Fury", Role.DPS),
    PROTECTION_WARRIOR(WowClass.WARRIOR, "Protection", Role.TANKS),

    // 사냥꾼 - HUNTER
    BEAST_MASTERY(WowClass.HUNTER, "Beast Mastery", Role.DPS),
    MARKSMANSHIP(WowClass.HUNTER, "Marksmanship", Role.DPS),
    SURVIVAL(WowClass.HUNTER, "Survival", Role.DPS),

    // 주술사 - SHAMAN
    ELEMENTAL(WowClass.SHAMAN, "Elemental", Role.DPS),
    ENHANCEMENT(WowClass.SHAMAN, "Enhancement", Role.DPS),
    RESTORATION_SHAMAN(WowClass.SHAMAN, "Restoration", Role.HEALERS),

    // 수도사 - MONK
    BREWMASTER(WowClass.MONK, "Brewmaster", Role.TANKS),
    MISTWEAVER(WowClass.MONK, "Mistweaver", Role.HEALERS),
    WINDWALKER(WowClass.MONK, "Windwalker", Role.DPS),

    // 도적 - ROGUE
    ASSASSINATION(WowClass.ROGUE, "Assassination", Role.DPS),
    OUTLAW(WowClass.ROGUE, "Outlaw", Role.DPS),
    SUBTLETY(WowClass.ROGUE, "Subtlety", Role.DPS),

    // 죽음의 기사 - DEATH KNIGHT
    BLOOD(WowClass.DEATH_KNIGHT, "Blood", Role.TANKS),
    DK_FROST(WowClass.DEATH_KNIGHT, "Frost", Role.DPS),
    UNHOLY(WowClass.DEATH_KNIGHT, "Unholy", Role.DPS),

    // 마법사 - MAGE
    ARCANE(WowClass.MAGE, "Arcane", Role.DPS),
    FIRE(WowClass.MAGE, "Fire", Role.DPS),
    MAGE_FROST(WowClass.MAGE, "Frost", Role.DPS),

    // 드루이드 - DRUID
    BALANCE(WowClass.DRUID, "Balance", Role.DPS),
    FERAL(WowClass.DRUID, "Feral", Role.DPS),
    GUARDIAN(WowClass.DRUID, "Guardian", Role.TANKS),
    RESTORATION_DRUID(WowClass.DRUID, "Restoration", Role.HEALERS),

    // 성기사 - PALADIN
    HOLY_PALADIN(WowClass.PALADIN, "Holy", Role.HEALERS),
    PROTECTION_PALADIN(WowClass.PALADIN, "Protection", Role.TANKS),
    RETRIBUTION(WowClass.PALADIN, "Retribution", Role.DPS),

    // 사제 - PRIEST
    DISCIPLINE(WowClass.PRIEST, "Discipline", Role.HEALERS),
    HOLY_PRIEST(WowClass.PRIEST, "Holy", Role.HEALERS),
    SHADOW(WowClass.PRIEST, "Shadow", Role.DPS),

    // 흑마법사 - WARLOCK
    AFFLICTION(WowClass.WARLOCK, "Affliction", Role.DPS),
    DEMONOLOGY(WowClass.WARLOCK, "Demonology", Role.DPS),
    DESTRUCTION(WowClass.WARLOCK, "Destruction", Role.DPS),

    // 악마사냥꾼 - DEMON HUNTER
    HAVOC(WowClass.DEMON_HUNTER, "Havoc", Role.DPS),
    VENGEANCE(WowClass.DEMON_HUNTER, "Vengeance", Role.TANKS),

    // 기원사 - EVOKER
    DEVASTATION(WowClass.EVOKER, "Devastation", Role.DPS),
    PRESERVATION(WowClass.EVOKER, "Preservation", Role.HEALERS),
    AUGMENTATION(WowClass.EVOKER, "Augmentation", Role.DPS);

//    final이지만 인스턴스 변수 -> 소문자 작명
    private final WowClass wowClass;
    private final String specName;
    private final Role role;

    Spec(WowClass wowClass, String specName, Role role) {
        this.wowClass = wowClass;
        this.specName = specName;
        this.role = role;
    }

}
