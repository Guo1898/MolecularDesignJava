package com.guo.service;

import com.guo.mapper.SNPMapper;
import com.guo.pojo.SNP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SnpService {

    @Autowired
    private SNPMapper snpMapper;

    public List<SNP> querySnp(){
        return  snpMapper.querySnp();
    }
}
