package com.guo.service;

import com.guo.mapper.TraitMapper;
import com.guo.pojo.Trait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TraitService {
    @Autowired
    private TraitMapper traitMapper;

    public void addTrait(Trait trait){
        traitMapper.insert(trait);
    }

    public List<Trait> getALLtrait(){
        return traitMapper.selectAll();
    }

    public Trait getParttrait(){
        return traitMapper.selectPart();
    }

    public String selectTrait(String traitName){
        return traitMapper.selectC(traitName);
    }


    public String selectClassify(String trait){
        return traitMapper.selectClassify(trait);
    }
}
