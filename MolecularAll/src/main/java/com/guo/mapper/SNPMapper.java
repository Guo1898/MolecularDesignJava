package com.guo.mapper;

import com.guo.pojo.SNP;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SNPMapper {

    @Select("select * from moleculardesign.table_snp")
    List<SNP> querySnp();
}
