package com.lsbim.wowlsb.util.init;

import com.lsbim.wowlsb.entity.WowClass;
import com.lsbim.wowlsb.entity.WowSpec;
import com.lsbim.wowlsb.repository.WowClassRepository;
import com.lsbim.wowlsb.repository.WowSpecRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@Log4j2
public class WowClassInitData {
    private final WowClassRepository wowClassRepository;
    private final WowSpecRepository wowSpecRepository;

    @Transactional
    public void initWowClassSpec() {
        List<WowClass> wowClasses = wowClassRepository.findAll();
        List<WowSpec> wowSpecs = wowSpecRepository.findAll();

        if(wowClasses.size() < 13 || wowSpecs.size() < 39){
            log.info("wowClasses.size(): {}", wowClasses.size());
            log.info("wowSpecs.size(): {}", wowSpecs.size());

            final int WARRIOR_ID = 1;
            final int HUNTER_ID = 2;
            final int SHAMAN_ID = 3;
            final int MONK_ID = 4;
            final int ROGUE_ID = 5;
            final int DEATH_KNIGHT_ID = 6;
            final int MAGE_ID = 7;
            final int DRUID_ID = 8;
            final int PALADIN_ID = 9;
            final int PRIEST_ID = 10;
            final int WARLOCK_ID = 11;
            final int DEMON_HUNTER_ID = 12;
            final int EVOKER_ID = 13;

            WowClass warrior = WowClass.builder()
                    .classId(WARRIOR_ID)
                    .className("Warrior")
                    .build();
            WowClass hunter = WowClass.builder()
                    .classId(HUNTER_ID)
                    .className("Hunter")
                    .build();
            WowClass shaman = WowClass.builder()
                    .classId(SHAMAN_ID)
                    .className("Shaman")
                    .build();
            WowClass monk = WowClass.builder()
                    .classId(MONK_ID)
                    .className("Monk")
                    .build();
            WowClass rogue = WowClass.builder()
                    .classId(ROGUE_ID)
                    .className("Rogue")
                    .build();
            WowClass deathKnight = WowClass.builder()
                    .classId(DEATH_KNIGHT_ID)
                    .className("DeathKnight")
                    .build();
            WowClass mage = WowClass.builder()
                    .classId(MAGE_ID)
                    .className("Mage")
                    .build();
            WowClass druid = WowClass.builder()
                    .classId(DRUID_ID)
                    .className("Druid")
                    .build();
            WowClass paladin = WowClass.builder()
                    .classId(PALADIN_ID)
                    .className("Paladin")
                    .build();
            WowClass priest = WowClass.builder()
                    .classId(PRIEST_ID)
                    .className("Priest")
                    .build();
            WowClass warlock = WowClass.builder()
                    .classId(WARLOCK_ID)
                    .className("Warlock")
                    .build();
            WowClass demonHunter = WowClass.builder()
                    .classId(DEMON_HUNTER_ID)
                    .className("DemonHunter")
                    .build();
            WowClass evoker = WowClass.builder()
                    .classId(EVOKER_ID)
                    .className("Evoker")
                    .build();

            wowClassRepository.saveAll(Arrays.asList(
                    warrior, hunter, shaman, monk
                    , rogue, deathKnight, mage, druid
                    , paladin, priest, warlock, demonHunter
                    , evoker));

            String dpsRole = "dps";
            String tanksRole = "tanks";
            String healersRole = "healers";

// Warrior (1) - Arms (101), Fury (102), Protection (103)
            WowSpec warriorArms = WowSpec.builder().specId(101).specName("Arms").wowClass(warrior).role(dpsRole).build();
            WowSpec warriorFury = WowSpec.builder().specId(102).specName("Fury").wowClass(warrior).role(dpsRole).build();
            WowSpec warriorProt = WowSpec.builder().specId(103).specName("Protection").wowClass(warrior).role(tanksRole).build();

// Hunter (2) - All DPS
            WowSpec hunterBM = WowSpec.builder().specId(201).specName("BeastMastery").wowClass(hunter).role(dpsRole).build();
            WowSpec hunterMM = WowSpec.builder().specId(202).specName("Marksmanship").wowClass(hunter).role(dpsRole).build();
            WowSpec hunterSV = WowSpec.builder().specId(203).specName("Survival").wowClass(hunter).role(dpsRole).build();

// Shaman (3) - Elemental (301), Enhancement (302), Restoration (303)
            WowSpec shamanEle = WowSpec.builder().specId(301).specName("Elemental").wowClass(shaman).role(dpsRole).build();
            WowSpec shamanEnh = WowSpec.builder().specId(302).specName("Enhancement").wowClass(shaman).role(dpsRole).build();
            WowSpec shamanRest = WowSpec.builder().specId(303).specName("Restoration").wowClass(shaman).role(healersRole).build();

// Monk (4) - Brewmaster (401), Mistweaver (402), Windwalker (403)
            WowSpec monkBrew = WowSpec.builder().specId(401).specName("Brewmaster").wowClass(monk).role(tanksRole).build();
            WowSpec monkMist = WowSpec.builder().specId(402).specName("Mistweaver").wowClass(monk).role(healersRole).build();
            WowSpec monkWind = WowSpec.builder().specId(403).specName("Windwalker").wowClass(monk).role(dpsRole).build();

// Rogue (5) - All DPS
            WowSpec rogueAssn = WowSpec.builder().specId(501).specName("Assassination").wowClass(rogue).role(dpsRole).build();
            WowSpec rogueOut = WowSpec.builder().specId(502).specName("Outlaw").wowClass(rogue).role(dpsRole).build();
            WowSpec rogueSub = WowSpec.builder().specId(503).specName("Subtlety").wowClass(rogue).role(dpsRole).build();

// Death Knight (6) - Blood (601), Frost (602), Unholy (603)
            WowSpec dkBlood = WowSpec.builder().specId(601).specName("Blood").wowClass(deathKnight).role(tanksRole).build();
            WowSpec dkFrost = WowSpec.builder().specId(602).specName("Frost").wowClass(deathKnight).role(dpsRole).build();
            WowSpec dkUnholy = WowSpec.builder().specId(603).specName("Unholy").wowClass(deathKnight).role(dpsRole).build();

// Mage (7) - All DPS
            WowSpec mageArcane = WowSpec.builder().specId(701).specName("Arcane").wowClass(mage).role(dpsRole).build();
            WowSpec mageFire = WowSpec.builder().specId(702).specName("Fire").wowClass(mage).role(dpsRole).build();
            WowSpec mageFrost = WowSpec.builder().specId(703).specName("Frost").wowClass(mage).role(dpsRole).build();

// Druid (8) - Balance (801), Feral (802), Guardian (803), Restoration (804)
            WowSpec druidBalance = WowSpec.builder().specId(801).specName("Balance").wowClass(druid).role(dpsRole).build();
            WowSpec druidFeral = WowSpec.builder().specId(802).specName("Feral").wowClass(druid).role(dpsRole).build();
            WowSpec druidGuardian = WowSpec.builder().specId(803).specName("Guardian").wowClass(druid).role(tanksRole).build();
            WowSpec druidResto = WowSpec.builder().specId(804).specName("Restoration").wowClass(druid).role(healersRole).build();

// Paladin (9) - Holy (901), Protection (902), Retribution (903)
            WowSpec palaHoly = WowSpec.builder().specId(901).specName("Holy").wowClass(paladin).role(healersRole).build();
            WowSpec palaProt = WowSpec.builder().specId(902).specName("Protection").wowClass(paladin).role(tanksRole).build();
            WowSpec palaRet = WowSpec.builder().specId(903).specName("Retribution").wowClass(paladin).role(dpsRole).build();

// Priest (10) - Discipline (1001), Holy (1002), Shadow (1003)
            WowSpec priestDisc = WowSpec.builder().specId(1001).specName("Discipline").wowClass(priest).role(healersRole).build();
            WowSpec priestHoly = WowSpec.builder().specId(1002).specName("Holy").wowClass(priest).role(healersRole).build();
            WowSpec priestShadow = WowSpec.builder().specId(1003).specName("Shadow").wowClass(priest).role(dpsRole).build();

// Warlock (11) - All DPS
            WowSpec warlockAff = WowSpec.builder().specId(1101).specName("Affliction").wowClass(warlock).role(dpsRole).build();
            WowSpec warlockDemo = WowSpec.builder().specId(1102).specName("Demonology").wowClass(warlock).role(dpsRole).build();
            WowSpec warlockDest = WowSpec.builder().specId(1103).specName("Destruction").wowClass(warlock).role(dpsRole).build();

// Demon Hunter (12) - Havoc (1201), Vengeance (1202)
            WowSpec dhHavoc = WowSpec.builder().specId(1201).specName("Havoc").wowClass(demonHunter).role(dpsRole).build();
            WowSpec dhVeng = WowSpec.builder().specId(1202).specName("Vengeance").wowClass(demonHunter).role(tanksRole).build();

// Evoker (13) - Devastation (1301), Preservation (1302), Augmentation (1303)
            WowSpec evokerDev = WowSpec.builder().specId(1301).specName("Devastation").wowClass(evoker).role(dpsRole).build();
            WowSpec evokerPres = WowSpec.builder().specId(1302).specName("Preservation").wowClass(evoker).role(healersRole).build();
            WowSpec evokerAug = WowSpec.builder().specId(1303).specName("Augmentation").wowClass(evoker).role(dpsRole).build();

            wowSpecRepository.saveAll(Arrays.asList(
                    warriorArms, warriorFury, warriorProt,
                    hunterBM, hunterMM, hunterSV,
                    shamanEle, shamanEnh, shamanRest,
                    monkBrew, monkMist, monkWind,
                    rogueAssn, rogueOut, rogueSub,
                    dkBlood, dkFrost, dkUnholy,
                    mageArcane, mageFire, mageFrost,
                    druidBalance, druidFeral, druidGuardian, druidResto,
                    palaHoly, palaProt, palaRet,
                    priestDisc, priestHoly, priestShadow,
                    warlockAff, warlockDemo, warlockDest,
                    dhHavoc, dhVeng,
                    evokerDev, evokerPres, evokerAug
            ));
        } else {
            log.info("has init wow class & spec data");
        }
    }
}
