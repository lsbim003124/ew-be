package com.lsbim.wowlsb.enums.dungeons;

import lombok.Getter;

@Getter
public enum Dungeons {

//    CATA
    GRIMBATOL("Grim Batol",60670),
//    BFA
    SIEGE_OF_BORALUS("Siege of Boralus",61822),
//    SL
    THE_NECROTIC_WAKE("The Necrotic Wake",62286),
    MISTS_OF_TIRNA_SCITHE("Mists of Tirna Scithe",62290),
//    TWW
    THE_STONEVAULT("The Stonevault", 12652),
    ARAKARA("Ara-Kara, City of Echoes", 12660),
    THE_DAWNBREAKER("The Dawnbreaker", 12662),
    CITY_OF_THREADS("City of Threads", 12669);

    private final String name;
    private final int id;

    Dungeons(String name, int id) {
        this.name = name;
        this.id = id;
    }
}
