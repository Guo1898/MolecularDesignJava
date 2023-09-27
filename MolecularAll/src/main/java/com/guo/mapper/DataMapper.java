package com.guo.mapper;

import com.guo.pojo.AdiposeTissues;
import com.guo.pojo.DataShare;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DataMapper {

    @Select("select * from moleculardesign.differencedisplay")
    List<DataShare> queryData();


    @Select("select * from moleculardesign.difference81")
    List<DataShare> queryData81();


    @Select("select * from moleculardesign.geneexpression")
    List<AdiposeTissues> queryGene();

    @Select("select * from moleculardesign.differencedisplay where AlignmentID = #{AlignmentID}")
    List<DataShare> queryId(String AlignmentID);

    @Select("select * from moleculardesign.geneexpression where Genesprediction = #{Genesprediction}")
    List<AdiposeTissues> queryGeneId(String Genesprediction);


}
