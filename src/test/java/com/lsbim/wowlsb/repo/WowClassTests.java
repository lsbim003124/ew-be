package com.lsbim.wowlsb.repo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.lsbim.wowlsb.entity.WowSpec;
import com.lsbim.wowlsb.enums.character.Spec;
import com.lsbim.wowlsb.repository.WowClassRepository;
import com.lsbim.wowlsb.repository.WowSpecRepository;
import com.lsbim.wowlsb.service.repository.WowClassService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@Log4j2
public class WowClassTests {

/*    @Autowired
    private WowClassRepository wowClassRepository;

    @Autowired
    private WowSpecRepository wowSpecRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WowClassService wowClassService;

    @Test
    public void wowClassTest1() {
//        직업과 전문화로 역할 호출
        String myClass = "Warrior";
        String mySpec = "Arms";

        String role = wowSpecRepository.findRoleByClassNameAndSpecName(myClass,mySpec);

        log.info("my role: {}",role);
    }
    
    @Test
    @Transactional
    public void wowClassTest2(){
        List<WowSpec> asd = wowSpecRepository.findAll();

        ArrayNode node = objectMapper.valueToTree(asd);

        for(JsonNode spec : node){

//            log.info("class: " + spec.path("wowClass").path("className").asText() + ", spec: " + spec.path("specName").asText());
            log.info(spec);
        }

        log.info("=========================================");

        for(WowSpec spec : asd){
            log.info(spec);
        }
    }

    @Test
    public void wowClassTest3(){
        List<WowSpec> arr = wowClassService.getSpecAll();

        for (WowSpec spec : arr) {
            log.info(spec);
        }
    }*/
}
