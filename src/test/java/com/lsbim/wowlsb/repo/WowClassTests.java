package com.lsbim.wowlsb.repo;

import com.lsbim.wowlsb.repository.WowClassRepository;
import com.lsbim.wowlsb.repository.WowSpecRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Log4j2
public class WowClassTests {

    @Autowired
    private WowClassRepository wowClassRepository;

    @Autowired
    private WowSpecRepository wowSpecRepository;

    @Test
    public void wowClassTest1() {
        String myClass = "Warrior";
        String mySpec = "Arms";

        String role = wowSpecRepository.findRoleByClassNameAndSpecName(myClass,mySpec);

        log.info("my role: {}",role);
    }
}
