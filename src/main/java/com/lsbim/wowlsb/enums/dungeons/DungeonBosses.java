package com.lsbim.wowlsb.enums.dungeons;

import lombok.Getter;

@Getter
public enum DungeonBosses {
//    THE_STONEVAULT
    E_D_N_A(Dungeons.THE_STONEVAULT, "E.D.N.A", 210108,"asd"),
    SKARMORAK(Dungeons.THE_STONEVAULT, "Skarmorak", 210156,"asd"),
    SPEAKER_BROKK(Dungeons.THE_STONEVAULT, "Speaker Brokk", 213217,"asd"),
    SPEAKER_DORLITA(Dungeons.THE_STONEVAULT, "Speaker Drolita", 213216,"asd"),
    VOID_SPEAKER_EIRICH(Dungeons.THE_STONEVAULT, "Void Speaker Eirich", 213119,"asd"),
//    THE_NECROTIC_WAKE
    BLIGHTBONE(Dungeons.THE_NECROTIC_WAKE, "Blightbone", 162691,"asd"),
    AMARTH(Dungeons.THE_NECROTIC_WAKE, "Amarth", 163157,"asd"),
    SURGEON_STITCHFLESH(Dungeons.THE_NECROTIC_WAKE, "Surgeon Stitchflesh", 162689,"asd"),
    NALTHOR_THE_RIMEBINDER(Dungeons.THE_NECROTIC_WAKE, "Nalthor the Rimebinder", 162693,"asd"),
//    THE_DAWNBREAKER
    SPEAKER_SHADOWCROWN(Dungeons.THE_DAWNBREAKER, "Speaker Shadowcrown", 211087,"asd"),
    ANUB_IKKAJ(Dungeons.THE_DAWNBREAKER, "Anub'ikkaj",211089 ,"asd"),
    RASHA_NAN(Dungeons.THE_DAWNBREAKER, "Rasha'nan", 213937,"asd"),
    //    SIEGE_OF_BORALUS
    CHOPPER_REDHOOK(Dungeons.SIEGE_OF_BORALUS, "Chopper Redhook", 128650,"asd"),
    DREAD_CAPTAIN_LOCKWOOD(Dungeons.SIEGE_OF_BORALUS, "Dread Captain Lockwood", 129208,"asd"),
    HADAL_DARKFATHOM(Dungeons.SIEGE_OF_BORALUS, "Hadal Darkfathom", 128651,"asd"),
    VIQ_GOTH(Dungeons.SIEGE_OF_BORALUS, "Viq'Goth", 128652,"asd"),
    DEMOLISHING_TERROR_1(Dungeons.SIEGE_OF_BORALUS, "Demolishing Terror", 137614,"asd"), // 1단상 파괴
    DEMOLISHING_TERROR_2(Dungeons.SIEGE_OF_BORALUS, "Demolishing Terror", 137625,"asd"), // 2단상 파괴
    DEMOLISHING_TERROR_3(Dungeons.SIEGE_OF_BORALUS, "Demolishing Terror", 137626,"asd"), // 3단상 파괴
    //    MISTS_OF_TIRNA_SCITHE
    DROMAN_OULFARRAN(Dungeons.MISTS_OF_TIRNA_SCITHE, "Droman Oulfarran", 164804,"asd"),
    INGRA_MALOCH(Dungeons.MISTS_OF_TIRNA_SCITHE, "Ingra Maloch", 164567,"asd"),
    MISTCALLER(Dungeons.MISTS_OF_TIRNA_SCITHE, "Mistcaller", 164501,"asd"),
    TRED_OVA(Dungeons.MISTS_OF_TIRNA_SCITHE, "Tred'ova", 164517,"asd"),
    //    GRIM BATOL
    GENERAL_UMBRISS(Dungeons.GRIMBATOL, "General Umbriss", 39625,"asd"),
    FORGEMASTER_THRONGUS(Dungeons.GRIMBATOL, "Forgemaster Throngus", 40177,"asd"),
    DRAHGA_SHADOWBURNER(Dungeons.GRIMBATOL, "Drahga Shadowburner", 40319,"asd"),
    VALIONA(Dungeons.GRIMBATOL, "Valiona", 40320,"asd"),
    ERUDAX(Dungeons.GRIMBATOL, "Erudax", 40484,"asd"),
    //    ARA-KARA
    AVANOXX(Dungeons.ARAKARA, "Avanoxx", 213179,"asd"),
    ANUB_ZEKT(Dungeons.ARAKARA, "Anub'zekt", 215405,"asd"),
    KI_KATAL_THE_HARVESTER(Dungeons.ARAKARA, "Ki'katal the Harvester", 215407,"asd"),
    //    THREAD
    ORATOR_KRIX_VIZK(Dungeons.CITY_OF_THREADS, "Orator Krix'vizk", 216619,"asd"),
    FANGS_OF_THE_QUEEN_NX(Dungeons.CITY_OF_THREADS, " Fangs of the Queen Nx", 216648,"asd"),
    FANGS_OF_THE_QUEEN_VX(Dungeons.CITY_OF_THREADS, " Fangs of the Queen Vx", 216649,"asd"),
    FANGS_OF_THE_QUEEN(Dungeons.CITY_OF_THREADS, " Fangs of the Queen", 0,"여왕의 송곳니"),
    THE_COAGLAMATION(Dungeons.CITY_OF_THREADS, "The Coaglamation", 216320,"asd"),
    IZO_THE_GRAND_SPLICER(Dungeons.CITY_OF_THREADS, "Izo, the Grand Splicer", 216658,"asd");

//    !!!!!!!!!!!!!! 보스가 여럿일 때, 이름이 통일되지 않으면 보스 도감명의 더미보스를 만들어 한국명을 줘야함.
    /*
    * FANGS_OF_THE_QUEEN_VX(Dungeons.CITY_OF_THREADS, " Fangs of the Queen", 0, "여왕의 송곳니"),
    */

    private final Dungeons dungeons;
    private final String bossName;
    private final int gameId;
    private final String krBossName;

    DungeonBosses(Dungeons dungeons, String bossName, int gameId, String krBossName) {
        this.dungeons = dungeons;
        this.bossName = bossName;
        this.gameId = gameId;
        this.krBossName = krBossName;
    }

}
