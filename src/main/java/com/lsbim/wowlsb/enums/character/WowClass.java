package com.lsbim.wowlsb.enums.character;

import lombok.Getter;

@Getter
public enum WowClass {
    WARRIOR("Warrior"),
    HUNTER("Hunter"),
    SHAMAN("Shaman"),
    MONK("Monk"),

    ROGUE("Rogue"),
    DEATH_KNIGHT("DeathKnight"),
    MAGE("Mage"),
    DRUID("Druid"),

    PALADIN("Paladin"),
    PRIEST("Priest"),
    WARLOCK("Warlock"),
    DEMON_HUNTER("DemonHunter"),

    EVOKER("Evoker");

    private final String displayName;

    WowClass(String displayName) {
        this.displayName = displayName;
    }


//    public List<Spec> getSpecs() {
//        return Arrays.stream(Spec.values())
//                .filter(spec -> spec.getWowClass() == this)
//                .collect(Collectors.toList());
//    }
}
