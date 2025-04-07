package com.lsbim.wowlsb.util.init;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
@RequiredArgsConstructor
public class WowInitRunner implements CommandLineRunner {

    private final WowClassInitData initData;
    private final WowSpellIdInit initSpell;

    @Override
    public void run(String... args) {
        initData.initWowClassSpec();
        initSpell.initWowSpellIdSet();
    }
}
