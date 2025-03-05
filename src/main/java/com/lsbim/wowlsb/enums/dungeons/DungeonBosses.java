package com.lsbim.wowlsb.enums.dungeons;

import lombok.Getter;

@Getter
public enum DungeonBosses {
    //    THE_STONEVAULT
    E_D_N_A(Dungeons.THE_STONEVAULT, "E.D.N.A", 210108, "asd"),
    SKARMORAK(Dungeons.THE_STONEVAULT, "Skarmorak", 210156, "asd"),
    SPEAKER_BROKK(Dungeons.THE_STONEVAULT, "Speaker Brokk", 213217, "asd"),
    SPEAKER_DORLITA(Dungeons.THE_STONEVAULT, "Speaker Drolita", 213216, "asd"),
    VOID_SPEAKER_EIRICH(Dungeons.THE_STONEVAULT, "Void Speaker Eirich", 213119, "asd"),
    //    THE_NECROTIC_WAKE
    BLIGHTBONE(Dungeons.THE_NECROTIC_WAKE, "Blightbone", 162691, "asd"),
    AMARTH(Dungeons.THE_NECROTIC_WAKE, "Amarth", 163157, "asd"),
    SURGEON_STITCHFLESH(Dungeons.THE_NECROTIC_WAKE, "Surgeon Stitchflesh", 162689, "asd"),
    NALTHOR_THE_RIMEBINDER(Dungeons.THE_NECROTIC_WAKE, "Nalthor the Rimebinder", 162693, "asd"),
    //    THE_DAWNBREAKER
    SPEAKER_SHADOWCROWN(Dungeons.THE_DAWNBREAKER, "Speaker Shadowcrown", 211087, "asd"),
    ANUB_IKKAJ(Dungeons.THE_DAWNBREAKER, "Anub'ikkaj", 211089, "asd"),
    RASHA_NAN(Dungeons.THE_DAWNBREAKER, "Rasha'nan", 213937, "asd"),
    //    SIEGE_OF_BORALUS
    CHOPPER_REDHOOK(Dungeons.SIEGE_OF_BORALUS, "Chopper Redhook", 128650, "asd"),
    DREAD_CAPTAIN_LOCKWOOD(Dungeons.SIEGE_OF_BORALUS, "Dread Captain Lockwood", 129208, "asd"),
    HADAL_DARKFATHOM(Dungeons.SIEGE_OF_BORALUS, "Hadal Darkfathom", 128651, "asd"),
    VIQ_GOTH(Dungeons.SIEGE_OF_BORALUS, "Viq'Goth", 128652, "asd"),
    DEMOLISHING_TERROR_1(Dungeons.SIEGE_OF_BORALUS, "Demolishing Terror", 137614, "asd"), // 1단상 파괴
    DEMOLISHING_TERROR_2(Dungeons.SIEGE_OF_BORALUS, "Demolishing Terror", 137625, "asd"), // 2단상 파괴
    DEMOLISHING_TERROR_3(Dungeons.SIEGE_OF_BORALUS, "Demolishing Terror", 137626, "asd"), // 3단상 파괴
    //    MISTS_OF_TIRNA_SCITHE
    DROMAN_OULFARRAN(Dungeons.MISTS_OF_TIRNA_SCITHE, "Droman Oulfarran", 164804, "asd"),
    INGRA_MALOCH(Dungeons.MISTS_OF_TIRNA_SCITHE, "Ingra Maloch", 164567, "asd"),
    MISTCALLER(Dungeons.MISTS_OF_TIRNA_SCITHE, "Mistcaller", 164501, "asd"),
    TRED_OVA(Dungeons.MISTS_OF_TIRNA_SCITHE, "Tred'ova", 164517, "asd"),
    //    GRIM BATOL
    GENERAL_UMBRISS(Dungeons.GRIMBATOL, "General Umbriss", 39625, "asd"),
    FORGEMASTER_THRONGUS(Dungeons.GRIMBATOL, "Forgemaster Throngus", 40177, "asd"),
    DRAHGA_SHADOWBURNER(Dungeons.GRIMBATOL, "Drahga Shadowburner", 40319, "asd"),
    VALIONA(Dungeons.GRIMBATOL, "Valiona", 40320, "asd"),
    ERUDAX(Dungeons.GRIMBATOL, "Erudax", 40484, "asd"),
    //    ARA-KARA
    AVANOXX(Dungeons.ARAKARA, "Avanoxx", 213179, "asd"),
    ANUB_ZEKT(Dungeons.ARAKARA, "Anub'zekt", 215405, "asd"),
    KI_KATAL_THE_HARVESTER(Dungeons.ARAKARA, "Ki'katal the Harvester", 215407, "asd"),
    //    THREAD
    ORATOR_KRIX_VIZK(Dungeons.CITY_OF_THREADS, "Orator Krix'vizk", 216619, "asd"),
    FANGS_OF_THE_QUEEN_NX(Dungeons.CITY_OF_THREADS, "Fangs of the Queen Nx", 216648, "asd"),
    FANGS_OF_THE_QUEEN_VX(Dungeons.CITY_OF_THREADS, "Fangs of the Queen Vx", 216649, "asd"),
    FANGS_OF_THE_QUEEN(Dungeons.CITY_OF_THREADS, "Fangs of the Queen", 0, "여왕의 송곳니"),
    THE_COAGLAMATION(Dungeons.CITY_OF_THREADS, "The Coaglamation", 216320, "asd"),
    IZO_THE_GRAND_SPLICER(Dungeons.CITY_OF_THREADS, "Izo, the Grand Splicer", 216658, "asd"),

    //    S2
    //    THE_MOTHERLODE
    COIN_OPERATED_CROWD_PUMMELER(Dungeons.THE_MOTHERLODE, "Coin-Operated Crowd Pummeler"
            , 129214, "동전 투입식 군중 난타기"),
    AZEROKK(Dungeons.THE_MOTHERLODE, "Azerokk", 129227, "아제로크"),
    RIXXA_FLUXFLAME(Dungeons.THE_MOTHERLODE, "Rixxa Fluxfume", 129231, "릭사 플럭스플레임"),
    MOGUL_RAZDUNK(Dungeons.THE_MOTHERLODE, "Mogul Razdunk", 129232, "모굴 라즈덩크"),
    //      OPERATION_MECHAGON_WORKSHOP
    GNOMERCY_4_U(Dungeons.OPERATION_MECHAGON_WORKSHOP, "Gnomercy 4.U.", 145185, "무자. B. 노움전차"),
    THE_PLATINUM_PUMMELER(Dungeons.OPERATION_MECHAGON_WORKSHOP, "The Platinum Pummeler"
            , 144244, "백금 난타로봇"),
    TUSSLE_TONKS(Dungeons.THE_MOTHERLODE, "Tussle Tonks", 0, "통통 격투"),
    K_U_J_0(Dungeons.OPERATION_MECHAGON_WORKSHOP, "K.U.-J.0.", 144246, "쿠.조."),
    HEAD_MACHINIST_SPARKFLUX(Dungeons.OPERATION_MECHAGON_WORKSHOP, "Machinist's Garden", 144248, "기계공의 정원"),
    KING_MECHAGON_1(Dungeons.OPERATION_MECHAGON_WORKSHOP, "King Mechagon", 150396, "왕 메카곤"), // 1페
    KING_MECHAGON_2(Dungeons.OPERATION_MECHAGON_WORKSHOP, "King Mechagon", 144249, "왕 메카곤"), // 2페
    //    THEATER_OF_PAIN
    DESSIA_THE_DECAPITATOR(Dungeons.THEATER_OF_PAIN, "Dessia the Decapitator", 164451, "참수자 데시아"),
    PACERAN_THE_VIRULENT(Dungeons.THEATER_OF_PAIN, "Paceran the Virulent", 164463, "맹독의 파세란"),
    SATHEL_THE_ACCURSED(Dungeons.THEATER_OF_PAIN, "Sathel the Accursed", 164461, "저주받은 자 사델"),
    AN_AFFRONT_OF_CHALLENGERS(Dungeons.THEATER_OF_PAIN, "An Affront of Challengers", 0, "오만불손한 도전자"),
    GORECHOP(Dungeons.THEATER_OF_PAIN, "Gorechop", 162317, "선혈토막"),
    KUL_THAROK(Dungeons.THEATER_OF_PAIN, "Kul'tharok", 162309, "쿨타로크"),
    XAV_THE_UNFALLEN(Dungeons.THEATER_OF_PAIN, "Xav the Unfallen", 162329, "몰락하지 않은 자 자브"),
    MORDRETHA(Dungeons.THEATER_OF_PAIN, "Mordretha", 165946, "무한의 여제 모르드레타"),
    //    THE_ROOKERY
    KYRIOSS(Dungeons.THE_ROOKERY, "Kyrioss", 209230, "키리오스"),
    STORMGUARD_GORREN(Dungeons.THE_ROOKERY, "Stormguard Gorren", 207205, "폭풍수호병 고렌"),
    VOIDSTONE_MONSTROSITY(Dungeons.THE_ROOKERY, "Skardyn Monstrosity", 207207, "공허석 괴수"),
    //    PRIORY_OF_THE_SACRED_FLAME
    CAPTAIN_DAILCRY(Dungeons.PRIORY_OF_THE_SACRED_FLAME, "Captain Dailcry", 207946, "대장 데일크라이"),
    //    1넴 부관
    ELEAENA_EMBERLANZ(Dungeons.PRIORY_OF_THE_SACRED_FLAME, "Elaena Emberlanz", 211290, "엘레나 엠버랜즈"),
    BARON_BRAUNPYKE(Dungeons.PRIORY_OF_THE_SACRED_FLAME, "Baron Braunpyke", 207939, "남작 브라운파이크"),
    PRIORESS_MURRPRAY(Dungeons.PRIORY_OF_THE_SACRED_FLAME, "Prioress Murrpray", 207940, "수도원장 머프라이"),
    //    DARKFLAME_CLEFT
    OL_WAXBEARD(Dungeons.DARKFLAME_CLEFT, "Ol' Waxbeard", 210153, "밀랍수염 영감"),
    BLAZIKON(Dungeons.DARKFLAME_CLEFT, "Blazikon", 208743, "블레지콘"),
    THE_CANDLE_KING(Dungeons.DARKFLAME_CLEFT, "The Candle King", 208745, "양초왕"),
    THE_DARKNESS(Dungeons.DARKFLAME_CLEFT, "The Darkness", 208747, "어둠의 존재"),
    //    CINDERBREW_MEADERY
    BREW_MASTER_ALDRYR(Dungeons.CINDERBREW_MEADERY, "Brewmaster Aldryr", 210271, "양조장인 알드리르"),
    I_PA(Dungeons.CINDERBREW_MEADERY, "I'pa", 210267, "이파"),
    BENK_BUZZBEE(Dungeons.CINDERBREW_MEADERY, "Benk Buzzbee", 218002, "벤크 버즈비"),
    GOLDIE_BARONBOTTOM(Dungeons.CINDERBREW_MEADERY, "Goldie Baronbottom", 214661, "골디 바론바텀"),
    //      OPERATION_FLOODGATE
    BIG_MOMMA(Dungeons.OPERATION_FLOODGATE, "Big M.O.M.M.A.", 226398, "큰.대.모."),
    BRONT(Dungeons.OPERATION_FLOODGATE, "Bront", 226402, "브론트"),
    KEEZA_QUICKFUSE(Dungeons.OPERATION_FLOODGATE, "Keeza Quickfuse", 226403, "키자"),
    DEMOLITION_DUO(Dungeons.OPERATION_FLOODGATE, "Demolition Duo", 0, "박살 2인조"),
    SWAMPFACE(Dungeons.OPERATION_FLOODGATE, "Swampface", 226396, "늪지면상"),
    GEEZLE_GIGAZAP(Dungeons.OPERATION_FLOODGATE, "Geezle Gigazap", 226404, "기즐 기가잽"),



    //      영문명 -> 한글명으로 바꾸기 때문에, 네임드가 여럿인 경우 대표 더미데이터 필요
    FANGS_OF_THE_QUEEN_DUMMY(Dungeons.CITY_OF_THREADS, "Fangs of the Queen", 0, "여왕의 송곳니");

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
