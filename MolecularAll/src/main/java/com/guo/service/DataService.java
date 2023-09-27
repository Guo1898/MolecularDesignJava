package com.guo.service;

import com.guo.mapper.DataMapper;
import com.guo.pojo.AdiposeTissues;
import com.guo.pojo.DataShare;
import com.guo.pojo.SNP;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class DataService {

    @Autowired
    private DataMapper dataMapper;

    public List<DataShare> queryData(){
        return  dataMapper.queryData();
    }

    public List<DataShare> queryDat81(){
        return  dataMapper.queryData81();
    }

    public List<AdiposeTissues> queryGene(){
        return  dataMapper.queryGene();
    }

    public List<DataShare> queryDataId(String data){
        return  dataMapper.queryId(data);
    }

    public List<AdiposeTissues> queryGeneId(String aa){
        return  dataMapper.queryGeneId(aa);
    }






}
