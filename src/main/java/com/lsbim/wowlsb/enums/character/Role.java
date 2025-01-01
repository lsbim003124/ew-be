package com.lsbim.wowlsb.enums.character;

import lombok.Getter;

@Getter
public enum Role {
    DPS("dps"),
    HEALERS("healers"),
    TANKS("tanks");

    private final String value;

    Role(String value) {
        this.value = value;
    }

}
