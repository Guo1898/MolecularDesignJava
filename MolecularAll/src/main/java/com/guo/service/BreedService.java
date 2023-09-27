package com.guo.service;

import com.guo.mapper.BreedMapper;
import com.guo.pojo.Breed;
import com.guo.pojo.Trait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BreedService {

    @Autowired
    private BreedMapper breedMapper;


    public String queryBreed(String breed){
        return  breedMapper.query(breed);
    }
}
