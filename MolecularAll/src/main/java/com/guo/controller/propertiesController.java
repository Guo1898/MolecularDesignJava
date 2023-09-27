package com.guo.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.guo.pojo.Trait;
import com.guo.service.BreedService;
import com.guo.service.TraitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class propertiesController {

    @Autowired
    private TraitService traitService;
    @Autowired
    private BreedService breedService;

    @PostMapping("/SelectReproduction")
    public void handlePostReproduction(@RequestBody String data)  {
        System.out.println(data);
    }

    @PostMapping("/addTrait")
    public String addTrait(@RequestBody Trait trait){
        traitService.addTrait(trait);
        return "success";
    }

    @GetMapping("/getAllTrait")
    public List<Trait> getALLTrait(){
        return traitService.getALLtrait();
    }

    @GetMapping("/getPartTrait")
    public Trait selectParttrait(){
        return traitService.getParttrait();
    }

    @PostMapping("/TraitSelection")
    public void processTrait(@RequestBody String data){

    }

    @GetMapping("/breedtest")
    public String breedtest(@RequestParam("breed") String breed){
        String s = breedService.queryBreed(breed);
        return s;
    }
}
