package com.lsbim.wowlsb.enums.dungeons;

import lombok.Getter;

@Getter
public enum DungeonBosses {
//    THE_STONEVAULT
    E_D_N_A(Dungeons.THE_STONEVAULT, "E.D.N.A", 210108),
    SKARMORAK(Dungeons.THE_STONEVAULT, "Skarmorak", 210156),
    SPEAKER_BROKK(Dungeons.THE_STONEVAULT, "Speaker Brokk", 213217),
    SPEAKER_DORLITA(Dungeons.THE_STONEVAULT, "Speaker Drolita", 213216),
    VOID_SPEAKER_EIRICH(Dungeons.THE_STONEVAULT, "Void Speaker Eirich", 213119),
//    THE_NECROTIC_WAKE
    BLIGHTBONE(Dungeons.THE_NECROTIC_WAKE, "Blightbone", 162691),
    AMARTH(Dungeons.THE_NECROTIC_WAKE, "Amarth", 163157),
    SURGEON_STITCHFLESH(Dungeons.THE_NECROTIC_WAKE, "Surgeon Stitchflesh", 162689),
    NALTHOR_THE_RIMEBINDER(Dungeons.THE_NECROTIC_WAKE, "Nalthor the Rimebinder", 162693),
//    THE_DAWNBREAKER
    SPEAKER_SHADOWCROWN(Dungeons.THE_DAWNBREAKER, "Speaker Shadowcrown", 211087),
    ANUB_IKKAJ(Dungeons.THE_DAWNBREAKER, "Anub'ikkaj", 211089),
    RASHA_NAN(Dungeons.THE_DAWNBREAKER, "Rasha'nan", 213937),
    //    SIEGE_OF_BORALUS
    CHOPPER_REDHOOK(Dungeons.SIEGE_OF_BORALUS, "Chopper Redhook", 128650),
    DREAD_CAPTAIN_LOCKWOOD(Dungeons.SIEGE_OF_BORALUS, "Dread Captain Lockwood", 129208),
    HADAL_DARKFATHOM(Dungeons.SIEGE_OF_BORALUS, "Hadal Darkfathom", 128651),
    VIQ_GOTH(Dungeons.SIEGE_OF_BORALUS, "Viq'Goth", 128652),
    DEMOLISHING_TERROR_1(Dungeons.SIEGE_OF_BORALUS, "Demolishing Terror", 137614), // 1단상 파괴
    DEMOLISHING_TERROR_2(Dungeons.SIEGE_OF_BORALUS, "Demolishing Terror", 137625), // 2단상 파괴
    DEMOLISHING_TERROR_3(Dungeons.SIEGE_OF_BORALUS, "Demolishing Terror", 137626), // 3단상 파괴
    //    MISTS_OF_TIRNA_SCITHE
    DROMAN_OULFARRAN(Dungeons.MISTS_OF_TIRNA_SCITHE, "Droman Oulfarran", 164804),
    INGRA_MALOCH(Dungeons.MISTS_OF_TIRNA_SCITHE, "Ingra Maloch", 164567),
    MISTCALLER(Dungeons.MISTS_OF_TIRNA_SCITHE, "Mistcaller", 164501),
    TRED_OVA(Dungeons.MISTS_OF_TIRNA_SCITHE, "Tred'ova", 164517),
    //    GRIM BATOL
    GENERAL_UMBRISS(Dungeons.GRIMBATOL, "General Umbriss", 39625),
    FORGEMASTER_THRONGUS(Dungeons.GRIMBATOL, "Forgemaster Throngus", 40177),
    DRAHGA_SHADOWBURNER(Dungeons.GRIMBATOL, "Drahga Shadowburner", 40319),
    VALIONA(Dungeons.GRIMBATOL, "Valiona", 40320),
    ERUDAX(Dungeons.GRIMBATOL, "Erudax", 40484),
    //    ARA-KARA
    AVANOXX(Dungeons.ARAKARA, "Avanoxx", 213179),
    ANUB_ZEKT(Dungeons.ARAKARA, "Anub'zekt", 215405),
    KI_KATAL_THE_HARVESTER(Dungeons.ARAKARA, "Ki'katal the Harvester", 215407),
    //    THREAD
    ORATOR_KRIX_VIZK(Dungeons.CITY_OF_THREADS, "Orator Krix'vizk", 216619),
    FANGS_OF_THE_QUEEN_NX(Dungeons.CITY_OF_THREADS, " Fangs of the Queen Nx", 216648),
    FANGS_OF_THE_QUEEN_VX(Dungeons.CITY_OF_THREADS, " Fangs of the Queen Vx", 216649),
    THE_COAGLAMATION(Dungeons.CITY_OF_THREADS, "The Coaglamation", 216320),
    IZO_THE_GRAND_SPLICER(Dungeons.CITY_OF_THREADS, "Izo, the Grand Splicer", 216658);

    private final Dungeons dungeons;
    private final String bossName;
    private final int gameId;

    DungeonBosses(Dungeons dungeons, String bossName, int gameId) {
        this.dungeons = dungeons;
        this.bossName = bossName;
        this.gameId = gameId;
    }

}
