package com.lsbim.wowlsb.service.repository;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.lsbim.wowlsb.entity.WowSpec;
import com.lsbim.wowlsb.repository.WowClassRepository;
import com.lsbim.wowlsb.repository.WowSpecRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WowClassService {
//  wowClass와 wowSpec 모두 여기서 관리
    private final WowClassRepository wowClassRepository;
    private final WowSpecRepository wowSpecRepository;

    public String getRoleByClassNameAndSpecName(String className, String specName){

        String role = wowSpecRepository.findRoleByClassNameAndSpecName(className,specName);

        return role;
    }

    public List<WowSpec> getSpecAll(){
        List<WowSpec> arr = wowSpecRepository.findAll();

        return arr;
    }
}
