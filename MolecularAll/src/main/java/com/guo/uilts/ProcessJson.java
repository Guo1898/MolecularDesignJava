package com.guo.uilts;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProcessJson {

    public static String JsonBreed(JSONArray BreedArray) throws JSONException {
        // 合并reBreed数据 考虑猪种
        StringBuilder reBreedBuilder = new StringBuilder();
        for (int i = 0; i < BreedArray.length(); i++) {
            reBreedBuilder.append(BreedArray.getString(i));
        }
        String Breed = reBreedBuilder.toString();
        return Breed;
    }

    public static String[] JsonTrait(String requestData,String trait) throws JsonProcessingException, JSONException {
        //考虑性状
        JSONObject dataTrait = new JSONObject(requestData);
        JSONArray Trait = dataTrait.getJSONArray(trait);
        ObjectMapper mapper = new ObjectMapper();
        String[] TraitArray = mapper.readValue(Trait.toString(), String[].class);
        return TraitArray;

    }
}
