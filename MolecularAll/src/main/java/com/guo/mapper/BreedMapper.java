package com.guo.mapper;

import com.guo.pojo.Breed;
import com.guo.pojo.Trait;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface BreedMapper {

    @Select("select location from moleculardesign.table_breed where breedNaEn = #{breedNaEn}")
    String query(String breed);


}
