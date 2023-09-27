package com.guo.mapper;

import com.guo.pojo.Trait;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TraitMapper {

    @Insert("insert into moleculardesign.table_trait(id,traitName ,trait) values(#{id},#{traitName},#{trait})")
    int insert(Trait trait);

    @Select("select * from moleculardesign.table_trait")
    List<Trait> selectAll();

    @Select("select * from table_trait where traitName=#{traitName}")
    Trait selectPart();

    @Select("select trait from moleculardesign.table_trait where traitName =#{traitName} ")
    String selectC(String traitName);

    @Select("select classify from moleculardesign.table_trait where trait =#{trait}")
    String selectClassify(String trait);
}

