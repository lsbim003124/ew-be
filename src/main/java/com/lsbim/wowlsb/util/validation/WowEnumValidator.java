package com.lsbim.wowlsb.util.validation;

import com.lsbim.wowlsb.enums.character.Spec;
import com.lsbim.wowlsb.enums.character.WowClass;
import com.lsbim.wowlsb.enums.dungeons.Dungeons;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class WowEnumValidator {

    private static final Set<String> VALID_CLASS_NAMES = Arrays.stream(WowClass.values())
            .map(WowClass::getDisplayName)
            .collect(Collectors.toSet());
    private static final Set<String> VALID_SPEC_NAMES = Arrays.stream(Spec.values())
            .map(Spec::getSpecName) // spec -> spec.getSpecName과 같다
            .collect(Collectors.toSet());

    private static final Set<Integer> VALID_DUNGEON_IDS = Arrays.stream(Dungeons.values())
            .map(Dungeons::getId)
            .collect(Collectors.toSet());


    public static boolean isValidClassName(String className) {return VALID_CLASS_NAMES.contains(className);}

    public static boolean isValidSpecName(String specName) {
        return VALID_SPEC_NAMES.contains(specName);
    }

    public static boolean isValidDungeonId(int dungeonId) {
        return VALID_DUNGEON_IDS.contains(dungeonId);
    }
}
